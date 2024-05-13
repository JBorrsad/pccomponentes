package PCcomponentes.Productos;

import PCcomponentes.Pedido;
import PCcomponentes.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Label welcome;

    @FXML
    private Label IDPedido;


    @FXML
    void refrescar() {

        tablaproductos.getItems().clear();
        tablaproductos.getColumns().clear();
        // Obtener la lista de productos desde la base de datos
        ArrayList<PCcomponentes.Producto> misproductos = MYSQL.crearProducto();

        // Convertir el ArrayList en una lista observable
        ObservableList<PCcomponentes.Producto> listaObservable = FXCollections.observableArrayList(misproductos);

        // Asignar las columnas y agregar los productos a la tabla
        TableColumn<PCcomponentes.Producto, Integer> idColumna = new TableColumn<>("ID");
        idColumna.setCellValueFactory(new PropertyValueFactory<>("ID_DISPOSITIVO"));

        TableColumn<PCcomponentes.Producto, String> nombreColumna = new TableColumn<>("Nombre");
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("NOMBRE"));

        TableColumn<PCcomponentes.Producto, Integer> stockColumna = new TableColumn<>("Stock");
        stockColumna.setCellValueFactory(new PropertyValueFactory<>("STOCK"));

        TableColumn<PCcomponentes.Producto, Double> precioColumna = new TableColumn<>("Precio");
        precioColumna.setCellValueFactory(new PropertyValueFactory<>("PRECIO"));

        NombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        Cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        tablaproductos.getColumns().addAll(idColumna, nombreColumna, stockColumna, precioColumna);
        tablaproductos.setItems(listaObservable);
    }



    // Métodos para manejar los eventos de los botones
    @FXML
    void añadirAPedido() {
        Producto selectedProduct = tablaproductos.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            if (selectedProduct.getSTOCK() > 0) {
                Pedido nuevoPedido = new Pedido(selectedProduct.getNOMBRE(), 1); // Nuevo pedido con cantidad 1
                tablaPedido.getItems().add(nuevoPedido);
                actualizarStock(selectedProduct.getNOMBRE(), -1); // Restar 1 al stock del producto
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
                actualizarStock(selectedPedido.getNombre(), -1); // Restar 1 al stock del producto
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
                actualizarStock(selectedPedido.getNombre(), 1); // Agregar 1 al stock del producto
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
        // Obtener el producto de la base de datos usando el nombre
        PCcomponentes.Producto producto = MYSQL.obtenerProductoPorNombre(nombreProducto);

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
        // Lógica para realizar la compra
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
