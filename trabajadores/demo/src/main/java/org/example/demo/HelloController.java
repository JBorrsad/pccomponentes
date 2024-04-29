package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    @FXML
    PasswordField loginContraseña;
    @FXML
    TextField loginNombre;
    @FXML
    Button botonLogin;
    @FXML
    CheckBox checkBoxUsuario;

    // Simulación de una base de datos

    public HelloController() {

    }

    @FXML
    public void initialize() {
        // Aquí puedes realizar inicializaciones adicionales
        // Por ejemplo, podrías deseleccionar el CheckBox
        checkBoxUsuario.setSelected(false);

        // También podrías establecer un evento de acción para el botón de inicio de sesión
        botonLogin.setOnAction(event -> login());

        // Si deseas cargar datos adicionales o realizar otras tareas de inicialización,
        // este es el lugar adecuado para hacerlo
    }

    @FXML
    public void login() {
        ArrayList<Usuario> usuarios= MYSQL.crearUsuarios();
        if (MYSQL.checkusuario(usuarios,loginNombre.toString(),loginContraseña.toString())){


        }else{

        }

    }
}
