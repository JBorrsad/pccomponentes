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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProductoControlador {

    @FXML private TableColumn IDTABLA;
    @FXML private TableColumn NOMBRETABLA;
    @FXML private TableColumn PRECIOTABLA;
    @FXML private TableColumn STOCKTABLA;
    @FXML
    private TableView tablaproductos;
    @FXML
    private Button nuevoproducto;



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
        private Label welcome;

        @FXML
        private Label IDPedido;

        @FXML
        private Label idusuario;

        @FXML
        public void initialize() {
            System.out.println("Inicializando ProductoClienteControlador");

            Usuario usuario = Cookie.getInstance().getUsuario();
            if (usuario != null) {
                System.out.println("Bienvenido, " + usuario.getUsername());
                idusuario.setText(String.valueOf(usuario.getId()));
            } else {
                System.out.println("No se encontró ningún usuario en la sesión.");
            }
            refrescar();
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

            NombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            Cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

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
                }

                if (selectedPedido.getCantidad() == 0) {
                    tablaPedido.getItems().remove(selectedPedido);
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
    }

    private Button eliminar;
    @FXML
    private Button refrescar;
    @FXML
    private ImageView img_id;
    @FXML
    private Label profile;
    private String username;

    @FXML
    void initialize() {
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
    }

    //aqui cargamos todos los productos desde la base de datos
    @FXML
    void refrescar() {
        tablaproductos.getItems().clear(); // Limpiar la tabla por si tenía algo dentro
        ArrayList<Producto> misproductos = MYSQL.crearProducto();
        for (Producto p : misproductos) {
            // Crear una nueva fila para la tabla y asignar los valores de Producto a cada celda
            TableColumn<Producto, String> idColumna = new TableColumn<>("ID");
            idColumna.setCellValueFactory(new PropertyValueFactory<>("ID_DISPOSITIVO"));

            TableColumn<Producto, String> nombreColumna = new TableColumn<>("Nombre");
            nombreColumna.setCellValueFactory(new PropertyValueFactory<>("NOMBRE"));

            TableColumn<Producto, Integer> stockColumna = new TableColumn<>("Stock");
            stockColumna.setCellValueFactory(new PropertyValueFactory<>("STOCK"));
           amarillo(stockColumna);

           TableColumn<Producto, Double> precioColumna = new TableColumn<>("Precio");
            precioColumna.setCellValueFactory(new PropertyValueFactory<>("PRECIO"));

            tablaproductos.getColumns().setAll(idColumna, nombreColumna, stockColumna, precioColumna);
            tablaproductos.getItems().add(p);
            System.out.println(p.getNOMBRE() + "  Añadido a la tabla");



        }
    }


    @FXML
    void eliminar() {
        Producto selectedProducto = (Producto) tablaproductos.getSelectionModel().getSelectedItem();
        if (selectedProducto == null) {
            // Show an error message if no producto is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No producto seleccionado");
            alert.setContentText("Seleccione un producto para eliminar");
            alert.showAndWait();
            return;
        }

        // Show a confirmation dialog to confirm the deletion of the selected product
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmar eliminación");
        confirmationAlert.setHeaderText("¿Estás seguro de que quieres eliminar el siguiente producto?");
        confirmationAlert.setContentText(selectedProducto.getNOMBRE());

        confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // Delete the selected product from the database
                MYSQL.eliminarProducto(selectedProducto.getID_DISPOSITIVO());
                // para Reoganizar las IDs despues eliminar el producto.
                MYSQL.reorganizarIDs();

                // Refresh the table to show the updated list of products
                refrescar();

            }
        });
    }


    public static void amarillo(TableColumn<Producto, Integer> stockColumna) {
        stockColumna.setCellFactory(column -> new TableCell<Producto, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                    getTableRow().setStyle(null); // Eliminar cualquier estilo de fila previamente aplicado
                } else {
                    setText(item.toString());
                    if (item==0) {
                        getTableRow().setStyle("-fx-background-color: red;");
                    }
                    else if (item<5) {
                        getTableRow().setStyle("-fx-background-color: yellow;");
                    }else{
                            getTableRow().setStyle(null);}

                    }


                }

        });
    }



    @FXML
    void anadirNuevoProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/añadirProductoNuevo.fxml"));
            Parent root = loader.load();

            // Crea un nuevo escenario para la ventana de añadir nuevo producto
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Nuevo Producto");
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refrescar();
    }

    // Método para invocar anadirproducto con parámetros específicos


    @FXML
    void comprarproducto(Producto p, int n) {
        MYSQL.restarstock(p, n);
        refrescar(); // Actualizar la tabla después de añadir un producto
    }

    public void comprarproducto(ActionEvent actionEvent) {
    }

    @FXML
    private void Modificar() {
        try {
            Producto selectedProducto = (Producto) tablaproductos.getSelectionModel().getSelectedItem();
            if (selectedProducto!= null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modificar.fxml"));
                Parent root = loader.load();
                ModificarControlador controller = loader.getController();
                controller.setProducto(selectedProducto); // Pass the selected producto to the modifier controller
                controller.setTableView(tablaproductos); // Pass the TableView object to the modifier controller

                Stage stage = new Stage();
                stage.setTitle("Modificar Producto");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } else {
                // Show an error message if no producto is selected
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No producto seleccionado");
                alert.setContentText("Seleccione un producto para modificar");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}