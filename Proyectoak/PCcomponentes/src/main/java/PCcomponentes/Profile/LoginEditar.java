package PCcomponentes.Profile;

import PCcomponentes.Productos.MYSQL;
import PCcomponentes.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class LoginEditar {

    @FXML
    private TextField nombre;

    @FXML
    private PasswordField contrasena;

    @FXML
    private TextField rol;

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
    private Button Guardar;
    @FXML
    private Button Cancelar;

    private String username;

    public void initialize() {
        this.username = MYSQL.getUsername();
        mostrarDatosUsuario();
    }


    public void mostrarDatosUsuario() {
        Usuario usuario = MYSQL.getUsuariosSQL(username);
        if (usuario != null) {
            nombre.setText(usuario.getUsername());
            contrasena.setText(usuario.getContrasena());
            rol.setText(usuario.getRol());
            paganombre.setText(usuario.getPagoNombre());
            pagaapellido.setText(usuario.getPagoApellido());
            provincia.setText(usuario.getProvincia());
            localidad.setText(usuario.getLocalidad());
            direccion.setText(usuario.getDireccion());
            codigopostal.setText(String.valueOf(usuario.getCodigoPostal()));
            cuentabancaria.setText(usuario.getCuentaBanco());
        }
    }

    @FXML
    public void fileChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println("File selected: " + file.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
            profile.setImage(image);

        } else {
            System.out.println("No file selected.");
        }
    }

    @FXML
    void guardarCambios() {
        // Obtener la ruta relativa de la imagen
        String imagePath = "src/main/resources/img/" + new File(profile.getImage().getUrl()).getName();

        // Crear un nuevo objeto Usuario con los parámetros correctos
        Usuario usuario = new Usuario(
                username, // username
                contrasena.getText(), // contrasena
                rol.getText(), // rol
                paganombre.getText(), // pagoNombre
                pagaapellido.getText(), // pagoApellido
                Integer.parseInt(codigopostal.getText()), // codigoPostal
                provincia.getText(), // provincia
                localidad.getText(), // localidad
                direccion.getText(), // direccion
                cuentabancaria.getText(), // cuentaBanco
                imagePath // foto
        );

        // Actualizar los datos del usuario en la base de datos
        MYSQL.actualizarUsuario(usuario);
        Stage stage = (Stage) Guardar.getScene().getWindow();

        // Cerrar la pantalla de inicio de sesión
        stage.close();
    }


}
