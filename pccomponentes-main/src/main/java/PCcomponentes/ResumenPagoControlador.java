package PCcomponentes;

import PCcomponentes.Login.Cookie;
import PCcomponentes.Pedido;
import PCcomponentes.Productos.MYSQL;
import PCcomponentes.Profile.LoginEditarOCerrar;
import PCcomponentes.Usuario;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ResumenPagoControlador {

    @FXML
    private TextField paganombre;
    @FXML
    private TextField pagaapellido;
    @FXML
    private TextField provincia;
    @FXML
    private TextField localidad;
    @FXML
    private TextField direccion;
    @FXML
    private TextField codigopostal;
    @FXML
    private TextField cuentabancaria;

    @FXML
    private TableView<Pedido> tablaPedido;
    @FXML
    private TableColumn<Pedido, String> NombreProducto;
    @FXML
    private TableColumn<Pedido, Integer> Cantidad;
    @FXML
    private ImageView Paypal;
    @FXML
    private Button comprar;

    @FXML
    private Label totalpedido;

    public void initialize() {
        // Configurar tabla de pedidos
        configurarTablaPedidos();
        // Mostrar datos del usuario
        mostrarDatosUsuario();

        String imagePath = "/img/PayPal-Logo.png";

        // Cargar la imagen
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        Paypal.setImage(image);

        // Manejar evento de clic en la imagen de PayPal
        Paypal.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.paypal.com/es/home"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    private void mostrarDatosUsuario() {
        Usuario usuario = Cookie.getInstance().getUsuario();
        if (usuario != null) {
            paganombre.setText(usuario.getPagoNombre());
            pagaapellido.setText(usuario.getPagoApellido());
            provincia.setText(usuario.getProvincia());
            localidad.setText(usuario.getLocalidad());
            direccion.setText(usuario.getDireccion());
            codigopostal.setText(String.valueOf(usuario.getCodigoPostal()));
            cuentabancaria.setText(usuario.getCuentaBanco());
        }
    }

    private void configurarTablaPedidos() {
        NombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        Cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }

    public void setPedidos(ObservableList<Pedido> pedidos) {
        tablaPedido.setItems(pedidos);
        actualizarTotalPedido(); // Asegúrate de actualizar el total después de establecer los pedidos
    }

    public void actualizarTotalPedido() {
        double total = 0;
        for (Pedido pedido : tablaPedido.getItems()) {
            Producto producto = MYSQL.obtenerProductoPorNombre(pedido.getNombre());
            if (producto != null) {
                total += pedido.getCantidad() * producto.getPRECIO();
            }
        }
        totalpedido.setText("Total del pedido: €" + String.format("%.2f", total));
    }



    @FXML
    public void terminarpago(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        mostrarAlerta("Pago realizado", "Pedido realizado!", "El pedido estará en tu casa en unos días");
    }



    private void mostrarAlerta(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }




}