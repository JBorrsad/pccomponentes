package PCcomponentes.Productos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ProductoControlador {

    @FXML private TableColumn IDTABLA;
    @FXML private TableColumn NOMBRETABLA;
    @FXML private TableColumn PRECIOTABLA;
    @FXML private TableColumn STOCKTABLA;
    @FXML
    private TableView tablaproductos;
    private Button añadir;
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
    }


    @FXML
    void anadirproducto(ActionEvent actionEvent) {
        String nombre= null;
        int stock= 0;
        int precio= 0;
        MYSQL.añadirProducto(nombre, stock, precio);
        refrescar(); // Actualizar la tabla después de añadir un producto
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
    private void abrirVentanaModificar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modificar.fxml"));
            Parent root = loader.load();
            ModificarControlador controller = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
