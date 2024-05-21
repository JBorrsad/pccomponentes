package PCcomponentes.Productos;

import PCcomponentes.Login.Cookie;
import PCcomponentes.Pedido;
import PCcomponentes.Producto;
import PCcomponentes.ResumenPagoControlador;
import PCcomponentes.Usuario;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

public class ProductoClienteControlador {

    @FXML
    private TableView<Producto> tablaproductos;
    @FXML
    private TableColumn<Producto, Integer> IDTABLA;
    @FXML
    private TableColumn<Producto, String> NOMBRETABLA;
    @FXML
    private TableColumn<Producto, Integer> STOCKTABLA;
    @FXML
    private TableColumn<Producto, Double> PRECIOTABLA;
    @FXML
    private Button añadirapedido;
    @FXML
    private TableView<Pedido> tablaPedido;
    @FXML
    private TableColumn<Pedido, String> NombreProducto;
    @FXML
    private TableColumn<Pedido, Integer> Cantidad;
    @FXML
    private TableColumn<Pedido, Double> Precio;
    @FXML
    private TableColumn<Pedido, Void> Acciones;
    @FXML
    private Label idusuario;
    @FXML
    private Label totalpedido;
    @FXML
    private Button miPerfil;

    @FXML
    public void initialize() {
        Usuario usuario = Cookie.getInstance().getUsuario();
        if (usuario != null) {
            idusuario.setText("Hola " + usuario.getUsername());
        }
        refrescar();
        initializeAccionesColumn();
        actualizarTotalPedido();
    }

    @FXML
    private void handleProfileButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginEditarOCerrar.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeAccionesColumn() {
        Acciones.setCellFactory(param -> new TableCell<>() {
            private final HBox pane = new HBox(new Label("+"), new Label("-"), new Label("X"));

            {
                pane.setSpacing(15);
                pane.getChildren().get(0).setOnMouseClicked(event -> modificarCantidad(1));
                pane.getChildren().get(1).setOnMouseClicked(event -> modificarCantidad(-1));
                pane.getChildren().get(2).setOnMouseClicked(event -> eliminarPedido());
            }

            private void modificarCantidad(int delta) {
                Pedido pedido = getTableView().getItems().get(getIndex());
                if (pedido != null) {
                    Producto producto = MYSQL.obtenerProductoPorNombre(pedido.getNombre());
                    if (producto != null && producto.getSTOCK() + delta >= 0) {
                        pedido.setCantidad(pedido.getCantidad() + delta);
                        actualizarStock(producto.getNOMBRE(), -delta);
                        if (pedido.getCantidad() <= 0) {
                            getTableView().getItems().remove(pedido);
                        }
                        refrescar();
                        actualizarTotalPedido();
                    } else {
                        mostrarAlerta("Error", "Stock insuficiente", "No hay suficientes productos en stock.");
                    }
                }
            }

            private void eliminarPedido() {
                Pedido pedido = getTableView().getItems().get(getIndex());
                if (pedido != null) {
                    getTableView().getItems().remove(pedido);
                    actualizarStock(pedido.getNombre(), pedido.getCantidad());
                    refrescar();
                    actualizarTotalPedido();
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    void refrescar() {
        ArrayList<Producto> misproductos = MYSQL.crearProducto();
        ObservableList<Producto> listaObservable = FXCollections.observableArrayList(misproductos);

        IDTABLA.setCellValueFactory(new PropertyValueFactory<>("ID_DISPOSITIVO"));
        NOMBRETABLA.setCellValueFactory(new PropertyValueFactory<>("NOMBRE"));
        STOCKTABLA.setCellValueFactory(new PropertyValueFactory<>("STOCK"));
        PRECIOTABLA.setCellValueFactory(new PropertyValueFactory<>("PRECIO"));

        // Aplicar el método amarillo para cambiar el color de la celda de stock
        ProductoControlador.amarillo(STOCKTABLA);

        PRECIOTABLA.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", item));
                }
            }
        });

        tablaproductos.setItems(listaObservable);
        refrescarTablaPedido();
    }

    private void refrescarTablaPedido() {
        NombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        Cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        Precio.setCellValueFactory(param -> {
            Pedido pedido = param.getValue();
            Producto producto = MYSQL.obtenerProductoPorNombre(pedido.getNombre());
            return producto != null ? new ReadOnlyObjectWrapper<>(producto.getPRECIO()) : null;
        });

        Precio.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", item));
                }
            }
        });

        tablaPedido.refresh();
    }

    @FXML
    void añadirAPedido() {
        Producto selectedProduct = tablaproductos.getSelectionModel().getSelectedItem();
        if (selectedProduct != null && selectedProduct.getSTOCK() > 0) {
            Pedido nuevoPedido = new Pedido(selectedProduct.getNOMBRE(), 1);
            tablaPedido.getItems().add(nuevoPedido);
            actualizarStock(selectedProduct.getNOMBRE(), -1);
            refrescarTablaPedido();
            actualizarTotalPedido();
        } else {
            mostrarAlerta("Error", "Producto agotado", "No quedan productos en stock de este producto.");
        }
    }

    private void actualizarStock(String nombreProducto, int cantidad) {
        Producto producto = MYSQL.obtenerProductoPorNombre(nombreProducto);
        if (producto != null) {
            int nuevoStock = producto.getSTOCK() + cantidad;
            if (nuevoStock >= 0) {
                MYSQL.actualizarStockProducto(producto.getID_DISPOSITIVO(), nuevoStock);
            } else {
                mostrarAlerta("Error", "Stock insuficiente", "No hay suficientes productos en stock de este producto.");
            }
        } else {
            mostrarAlerta("Error", "Producto no encontrado", "El producto seleccionado no se encontró en la base de datos.");
        }
    }

    private void actualizarTotalPedido() {
        double total = 0;
        for (Pedido pedido : tablaPedido.getItems()) {
            Producto producto = MYSQL.obtenerProductoPorNombre(pedido.getNombre());
            if (producto != null) {
                total += pedido.getCantidad() * producto.getPRECIO();
            }
        }
        totalpedido.setText("Total del pedido: €" + String.format("%.2f", total));
    }

    private void mostrarAlerta(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleTramitarPedido(ActionEvent event) {
        if (tablaPedido.getItems().isEmpty()) {
            mostrarAlerta("Error", "Cesta vacía", "No hay productos en la cesta. Añade productos antes de tramitar el pedido.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/resumenpago.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle los datos necesarios
            ResumenPagoControlador controlador = loader.getController();
            controlador.setPedidos(tablaPedido.getItems());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la pantalla de resumen de pago", "Intente nuevamente.");
        }
    }
}
