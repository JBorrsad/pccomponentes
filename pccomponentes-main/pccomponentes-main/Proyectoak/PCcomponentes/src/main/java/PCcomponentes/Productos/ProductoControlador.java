package PCcomponentes.Productos;

import PCcomponentes.Producto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
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
    @FXML
    private Button eliminar;
    @FXML
    private Button refrescar;

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