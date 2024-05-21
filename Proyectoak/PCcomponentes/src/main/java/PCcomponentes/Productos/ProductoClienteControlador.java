package PCcomponentes.Productos;

import PCcomponentes.Login.Cookie;
import PCcomponentes.Pedido;
import PCcomponentes.Producto;
import PCcomponentes.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    private Button mas;

    @FXML
    private Button menos;

    @FXML
    private TableView<Pedido> tablaPedido;

    @FXML
    private TableColumn<Pedido, String> NombreProducto;

    @FXML
    private TableColumn<Pedido, Integer> Cantidad;

    @FXML
    private TableColumn<Pedido, Double> Precio;  // Agregando la columna de precio aquí

    @FXML
    private TableColumn<Pedido, Void> Acciones;

    @FXML
    private Label welcome;

    @FXML
    private Label IDPedido;

    @FXML
    private Label idusuario;

    @FXML
    private Label profile;

    @FXML
    private Label totalpedido;

    @FXML
    public void initialize() {
        System.out.println("Inicializando ProductoClienteControlador");

        Usuario usuario = Cookie.getInstance().getUsuario();
        if (usuario != null) {
            System.out.println("Bienvenido, " + usuario.getUsername());
            idusuario.setText("Logueado como:" + usuario.getUsername());
        } else {
            System.out.println("No se encontró ningún usuario en la sesión.");
        }
        refrescar();

        profile.setOnMouseEntered(this::azul);
        profile.setOnMouseExited(this::normal);
        profile.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/LoginEditarOCerrar.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        initializeAccionesColumn();
        actualizarTotalPedido();
    }

    private void initializeAccionesColumn() {
        Acciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Pedido, Void> call(final TableColumn<Pedido, Void> param) {
                return new TableCell<>() {

                    private final Label iconAumentar = new Label("+");
                    private final Label iconDisminuir = new Label("-");
                    private final Label iconEliminar = new Label("X");
                    private final HBox pane = new HBox(iconAumentar, iconDisminuir, iconEliminar);

                    {
                        iconAumentar.setOnMouseClicked(event -> {
                            Pedido pedido = getTableView().getItems().get(getIndex());
                            if (pedido != null) {
                                aumentarCantidad();
                            }
                        });

                        iconDisminuir.setOnMouseClicked(event -> {
                            Pedido pedido = getTableView().getItems().get(getIndex());
                            if (pedido != null) {
                                disminuirCantidad();
                            }
                        });

                        iconEliminar.setOnMouseClicked(event -> {
                            Pedido pedido = getTableView().getItems().get(getIndex());
                            if (pedido != null) {
                                int cantidad = pedido.getCantidad();
                                tablaPedido.getItems().remove(pedido);
                                actualizarStock(pedido.getNombre(), cantidad);
                                refrescar();
                                refrescarTablaPedido();
                                actualizarTotalPedido();
                            }
                        });

                        pane.setSpacing(15);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
            }
        });
    }

    @FXML
    void refrescar() {
        tablaproductos.getItems().clear();
        tablaproductos.getColumns().clear();
        ArrayList<Producto> misproductos = MYSQL.crearProducto();
        ObservableList<Producto> listaObservable = FXCollections.observableArrayList(misproductos);

        TableColumn<Producto, Integer> idColumna = new TableColumn<>("ID");
        idColumna.setCellValueFactory(new PropertyValueFactory<>("ID_DISPOSITIVO"));

        TableColumn<Producto, String> nombreColumna = new TableColumn<>("Nombre");
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("NOMBRE"));

        TableColumn<Producto, Integer> stockColumnaC = new TableColumn<>("Stock");
        stockColumnaC.setCellValueFactory(new PropertyValueFactory<>("STOCK"));
        ProductoControlador.amarillo(stockColumnaC);

        TableColumn<Producto, Double> precioColumna = new TableColumn<>("Precio");
        precioColumna.setCellValueFactory(new PropertyValueFactory<>("PRECIO"));
        precioColumna.setCellFactory(column -> new TableCell<Producto, Double>() {
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

        NombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        Cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        // Configurando la columna de precio para tablaPedido
        Precio.setCellFactory(column -> new TableCell<Pedido, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Producto producto = MYSQL.obtenerProductoPorNombre(getTableView().getItems().get(getIndex()).getNombre());
                    if (producto != null) {
                        setText(String.format("%.2f €", producto.getPRECIO()));
                    }
                }
            }
        });

        tablaproductos.getColumns().addAll(idColumna, nombreColumna, stockColumnaC, precioColumna);
        tablaproductos.setItems(listaObservable);
    }

    @FXML
    void añadirAPedido() {
        Producto selectedProduct = tablaproductos.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            if (selectedProduct.getSTOCK() > 0) {
                Pedido nuevoPedido = new Pedido(selectedProduct.getNOMBRE(), 1);
                tablaPedido.getItems().add(nuevoPedido);
                actualizarStock(selectedProduct.getNOMBRE(), -1);
                actualizarTotalPedido();
            } else {
                mostrarAlerta("Error", "Producto agotado", "No quedan productos en stock de este producto.");
            }
        } else {
            mostrarAlerta("Error", "Ningún producto seleccionado", "Seleccione un producto para añadir al pedido.");
        }
    }

    @FXML
    void aumentarCantidad() {
        Pedido selectedPedido = tablaPedido.getSelectionModel().getSelectedItem();
        if (selectedPedido != null) {
            Producto producto = MYSQL.obtenerProductoPorNombre(selectedPedido.getNombre());
            if (producto != null && producto.getSTOCK() > 0) {
                selectedPedido.setCantidad(selectedPedido.getCantidad() + 1);
                actualizarStock(selectedPedido.getNombre(), -1);
                refrescarTablaPedido();
                refrescar();
                actualizarTotalPedido();
            } else {
                mostrarAlerta("Error", "Stock insuficiente", "No hay suficientes productos en stock.");
            }
        } else {
            mostrarAlerta("Error", "Ningún pedido seleccionado", "Seleccione un pedido para aumentar la cantidad.");
        }
    }

    @FXML
    void disminuirCantidad() {
        Pedido selectedPedido = tablaPedido.getSelectionModel().getSelectedItem();
        if (selectedPedido != null) {
            if (selectedPedido.getCantidad() > 0) {
                selectedPedido.setCantidad(selectedPedido.getCantidad() - 1);
                actualizarStock(selectedPedido.getNombre(), 1);
                refrescarTablaPedido();
                refrescar();
                actualizarTotalPedido();
            }

            if (selectedPedido.getCantidad() == 0) {
                tablaPedido.getItems().remove(selectedPedido);
                actualizarTotalPedido();
            }
        } else {
            mostrarAlerta("Error", "Ningún pedido seleccionado", "Seleccione un pedido para disminuir la cantidad.");
        }
    }

    private void refrescarTablaPedido() {
        tablaPedido.refresh();
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

    @FXML
    void comprar() {
        // Implementa esta lógica aquí
    }

    private void mostrarAlerta(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void normal(MouseEvent mouseEvent) {
        profile.setStyle("-fx-background-color: transparent;");
    }

    private void azul(MouseEvent mouseEvent) {
        profile.setStyle("-fx-text-fill: blue; -fx-underline: true;");
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
}
