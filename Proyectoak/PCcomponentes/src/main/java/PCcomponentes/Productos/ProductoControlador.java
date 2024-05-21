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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

public class ProductoControlador {

    @FXML
    private TableColumn IDTABLA;

    @FXML
    private TableColumn NOMBRETABLA;

    @FXML
    private TableColumn PRECIOTABLA;

    @FXML
    private TableColumn STOCKTABLA;

    @FXML
    private TableView tablaproductos;

    @FXML
    private Button nuevoproducto;

    private Button eliminar;

    @FXML
    private Button refrescar;

    @FXML
    private ImageView img_id;

    @FXML
    private Label profile;

    private String username;

    @FXML
    private Label idusuario;

    @FXML
    void initialize() {
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
    }



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
                    setStyle(""); // Reset cell style
                } else {
                    setText(item.toString());
                    if (item == 0) {
                        setStyle("-fx-background-color: #F1948A;"); // Red background
                    } else if (item < 5) {
                        setStyle("-fx-background-color: #F7DC6F;"); // Yellow background
                    } else {
                        setStyle(""); // Default background
                    }
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
    void comprarproducto(ActionEvent actionEvent) {
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

    private void normal(MouseEvent mouseEvent) {
        profile.setStyle("-fx-background-color: transparent;");
    }

    private void azul(MouseEvent mouseEvent) {
        profile.setStyle("-fx-text-fill: blue; -fx-underline: true;");
    }

}
