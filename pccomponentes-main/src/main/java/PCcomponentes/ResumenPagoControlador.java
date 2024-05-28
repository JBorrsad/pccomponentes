package PCcomponentes;

import PCcomponentes.Login.Cookie;
import PCcomponentes.Pedido;
import PCcomponentes.Productos.MYSQL;
import PCcomponentes.Usuario;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
    @FXML
    private Label totalpedido;

    public void initialize() {
        cuentabancaria.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (!newValue.startsWith("ES")) {
                cuentabancaria.setText("ES ");
            } else {
                String formattedText = formatCuentaBancaria(newValue);
                if (!newValue.equals(formattedText)) {
                    cuentabancaria.setText(formattedText);
                    cuentabancaria.positionCaret(formattedText.length());
                }
            }
        });

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
            String cuentaBanco = usuario.getCuentaBanco();
            if (cuentaBanco != null) {
                cuentabancaria.setText(cuentaBanco);
            } else {
                cuentabancaria.setText("ES ");
            }
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
    private void handleComprar() {
        if (isFormValid()) {
            // Handle the payment processing logic here
            // For example, save the order, show confirmation, etc.
            mostrarAlerta("Éxito", "Pago realizado", "Su pago se ha realizado con éxito.", Alert.AlertType.INFORMATION);
            vaciarPedido(); // Clear the order after successful payment
            volverAClienteProductoControlador();
        } else {
            mostrarAlerta("Error", "Formulario inválido", "Por favor, complete todos los campos obligatorios con los datos correctos.", Alert.AlertType.ERROR);
        }
    }

    private boolean isFormValid() {
        boolean valid = true;

        valid &= validateField(paganombre);
        valid &= validateField(pagaapellido);
        valid &= validateField(provincia);
        valid &= validateField(localidad);
        valid &= validateField(direccion);
        valid &= validateField(codigopostal, "\\d{5}");
        valid &= validateField(cuentabancaria, "ES \\d{4} \\d{4} \\d{4} \\d{4}");

        return valid;
    }

    private boolean validateField(TextField field) {
        return validateField(field, ".+");
    }

    private boolean validateField(TextField field, String regex) {
        String text = field.getText();
        if (text != null && text.matches(regex)) {
            field.setStyle("-fx-border-color: green;");
            return true;
        } else {
            field.setStyle("-fx-border-color: red;");
            return false;
        }
    }

    private void mostrarAlerta(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String formatCuentaBancaria(String text) {
        text = text.replaceAll("\\s", "");  // Remove all spaces
        if (text.length() > 22) {  // Limit input length to 22 (2 for "ES" + 20 digits)
            text = text.substring(0, 22);
        }
        StringBuilder formatted = new StringBuilder("ES ");
        for (int i = 2; i < text.length(); i++) {
            if ((i - 2) % 4 == 0 && i > 2) {
                formatted.append(" ");
            }
            formatted.append(text.charAt(i));
        }
        return formatted.toString();
    }

    @FXML
    public void terminarpago(ActionEvent actionEvent) {
        if (isFormValid()) {
            vaciarPedido(); // Clear the order after successful payment
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            mostrarAlerta("Pago realizado", "Pedido realizado!", "El pedido estará en tu casa en unos días", Alert.AlertType.INFORMATION);
            volverAClienteProductoControlador();
        } else {
            mostrarAlerta("Error", "Formulario inválido", "Por favor, complete todos los campos obligatorios con los datos correctos.", Alert.AlertType.ERROR);
        }
    }

    private void vaciarPedido() {
        tablaPedido.getItems().clear();
        totalpedido.setText("Total del pedido: €0.00");
    }

    private void volverAClienteProductoControlador() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ClienteProducto.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
