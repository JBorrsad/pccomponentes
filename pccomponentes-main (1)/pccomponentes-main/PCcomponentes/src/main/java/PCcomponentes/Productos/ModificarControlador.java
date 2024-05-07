package PCcomponentes.Productos;

import PCcomponentes.Productos.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ModificarControlador {

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

    public void setProducto(Producto producto) {
        this.producto = producto;
        mostrarProducto();
    }

    private void mostrarProducto() {
        nombreField.setText(producto.getNOMBRE());
        stockField.setText(Integer.toString(producto.getSTOCK()));
        precioField.setText(Double.toString(producto.getSTOCK()));
    }

    @FXML
    private void guardarCambios() {
        // Aquí debes implementar la lógica para guardar los cambios en la base de datos
        // Recupera los nuevos valores de los campos y actualiza el producto en la base de datos
        String nuevoNombre = nombreField.getText();
        int nuevoStock = Integer.parseInt(stockField.getText());
        int nuevoPrecio = Integer.parseInt(precioField.getText());

        // Actualizar el producto en la base de datos
        producto.setNOMBRE(nuevoNombre);
        producto.setSTOCK(nuevoStock);
        producto.setPRECIO(nuevoPrecio);

        // Lógica para guardar los cambios y cerrar la ventana
        // Esto puede incluir llamar a un método en la clase que gestiona la base de datos
        // y luego cerrar la ventana

        // Por ahora, simplemente imprimimos los nuevos valores del producto
        System.out.println("Los cambios se han guardado correctamente:");
        System.out.println(producto);
    }

    @FXML
    private void cancelar() {
        // Aquí puedes implementar la lógica para cerrar la ventana sin guardar cambios
        // Esto puede incluir simplemente cerrar la ventana o mostrar un mensaje de confirmación antes de cerrar

        // Por ahora, simplemente cerramos la ventana
        cancelarButton.getScene().getWindow().hide();
    }
}

