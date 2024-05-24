package PCcomponentes;

import PCcomponentes.Login.Cookie;
import PCcomponentes.Pedido;
import PCcomponentes.Usuario;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
    }
}
