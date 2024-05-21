package PCcomponentes.Login;

import PCcomponentes.Productos.MYSQL;
import PCcomponentes.Productos.ProductoClienteControlador;
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
import javafx.stage.Window;

import java.io.IOException;

public class LoginControlador {
    @FXML
    PasswordField loginContraseña;
    @FXML
    TextField loginNombre;
    @FXML
    Button botonLogin;

    public void login(ActionEvent event) throws PermisosInsuficientesException {
        String username = loginNombre.getText().trim();
        String password = loginContraseña.getText().trim();

        if (MYSQL.checkusuario(username, password)) {
            MYSQL.setUsername(username);
            Usuario usuario = MYSQL.getUsuariosSQL(username);
            String rol = usuario.getRol();
            Cookie.getInstance().setUsuario(usuario);
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

    @FXML
    private void changeScene(String rol) {
        try {
            // Obtener el escenario actual
            Stage stage = (Stage) botonLogin.getScene().getWindow();

            // Cargar la ventana correspondiente según el rol del usuario
            FXMLLoader loader = new FXMLLoader();
            if (rol.equals("CLIENTE")) {
                loader.setLocation(getClass().getResource("/fxml/ProductosCliente.fxml"));
            } else if (rol.equals("PROVEEDOR")) {
                loader.setLocation(getClass().getResource("/fxml/productos.fxml"));
            } else {
                showAlert("Error", "Rol de usuario no válido.");
                return;
            }

            // Cargar el archivo FXML y obtener su controlador
            Parent root = loader.load();
            if (rol.equals("CLIENTE")) {
                ProductoClienteControlador controller = loader.getController();
                // Establecer el usuario en el controlador de ProductosCliente.fxml
                String username = loginNombre.getText().trim();
                Usuario usuario = MYSQL.getUsuariosSQL(username);

            }

            // Configurar la nueva escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void closeAllStagesExcept(Stage stageToKeep) {
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage) {
                Stage stage = (Stage) window;
                if (stage != stageToKeep) {
                    stage.close();
                    Cookie.getInstance().setUsuario(null);

                }
            }
        }
    }

}
