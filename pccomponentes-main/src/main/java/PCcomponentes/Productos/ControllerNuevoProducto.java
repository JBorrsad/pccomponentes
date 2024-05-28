package PCcomponentes.Productos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ControllerNuevoProducto {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField stockField;

    @FXML
    private TextField precioField;

    @FXML
    private Button anyadir;

    @FXML
    private Button cancelarButton;

    @FXML
    void initialize() {
        anyadir.setOnAction(event -> {
            añadirProducto();
        });
        cancelarButton.setOnAction(event -> {
            cancelar();
        });
    }

    private void añadirProducto() {
        String nombre = nombreField.getText();
        int stock = Integer.parseInt(stockField.getText());
        int precio = Integer.parseInt(precioField.getText());

        MYSQL.añadirProducto(nombre, stock, precio);
        System.out.println("Producto añadido: " + nombre);
        cancelar();
    }

    private void cancelar() {
        cancelarButton.getScene().getWindow().hide();
    }
}
