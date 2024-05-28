package PCcomponentes.Productos;

import PCcomponentes.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class ModificarControlador  {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField stockField;

    @FXML
    private TextField precioField;

    @FXML
    private Button guardarButton;

    @FXML
    private Button cancelarButton;

    private Producto producto;

    private TableView<Producto> tableView;

    public void setProducto(Producto producto) {
        this.producto = producto;
        mostrarProducto();
    }

    public void setTableView(TableView<Producto> tableView) {
        this.tableView = tableView;
    }

    private void mostrarProducto() {
        nombreField.setText(producto.getNOMBRE());
        stockField.setText(Integer.toString(producto.getSTOCK()));
        precioField.setText(Double.toString(producto.getPRECIO()));
    }

    @FXML
    private void guardarCambios() {
        String nuevoNombre = nombreField.getText();
        int nuevoStock = Integer.parseInt(stockField.getText());
        double nuevoPrecio = Double.parseDouble(precioField.getText());

        producto.setNOMBRE(nuevoNombre);
        producto.setSTOCK(nuevoStock);
        producto.setPRECIO(nuevoPrecio);

        // Update the producto in the database
        MYSQL.actualizarProducto(producto);

        // Update the TableView
        tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(), producto);

        // Close the window
        cancelar();
    }

    @FXML
    private void cancelar() {
        // Close the window without saving changes
        cancelarButton.getScene().getWindow().hide();
    }

    @FXML
    public void initialize() {
        // Establece el evento de clic para el botón de guardarButton utilizando una expresión lambda
        guardarButton.setOnAction(event -> guardarCambios());
    }

}
