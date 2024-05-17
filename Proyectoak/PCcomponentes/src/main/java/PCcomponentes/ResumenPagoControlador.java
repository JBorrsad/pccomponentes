package PCcomponentes;

import PCcomponentes.Pedido;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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
    private ImageView profile;

    @FXML
    private TableView<Pedido> tablaPedido;

    @FXML
    private TableColumn<Pedido, String> NombreProducto;

    @FXML
    private TableColumn<Pedido, Integer> Cantidad;

    @FXML
    private TextField paypal;

    @FXML
    private TextField comprar;
}
