package PCcomponentes.Login;

import PCcomponentes.Productos.MYSQL;
import PCcomponentes.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LoginControlador {
    @FXML
    PasswordField loginContraseña;
    @FXML
    TextField loginNombre;
    @FXML
    Button botonLogin;
    ArrayList<Usuario> usuarios= new ArrayList<>();

    public void login(ActionEvent event) throws PermisosInsuficientesException {
        String username = loginNombre.getText().trim();
        String password = loginContraseña.getText().trim();

        MYSQL mysql = new MYSQL();
        if (MYSQL.checkusuario(username, password)) {
            Usuario usuario = MYSQL.getUsuariosSQL(username,password);
            String rol = usuario.getROL();
            if (!rol.equals("CLIENTE") && !rol.equals("PROVEEDOR")) {
                throw new PermisosInsuficientesException("El usuario no tiene permisos para acceder a la aplicación.");
            }
            // Lógica para cambiar de escena según el rol
            changeScene(rol);
        } else {
            showAlert("Error", "Nombre de usuario o contraseña incorrectos.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void initialize() {
        botonLogin.setOnAction(event -> {
            try {
                login(event);
            } catch (PermisosInsuficientesException e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    private void changeScene(String rol) {
        // Obtener el escenario actual
        Stage stage = (Stage) botonLogin.getScene().getWindow();

        // Cerrar la pantalla de inicio de sesión
        stage.close();

        // Abrir la pantalla de productos
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/productos.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
