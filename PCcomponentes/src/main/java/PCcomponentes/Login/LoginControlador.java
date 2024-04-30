package PCcomponentes.Login;



import PCcomponentes.MYSQL;
import PCcomponentes.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class LoginControlador {
    @FXML
    PasswordField loginContraseña;
    @FXML
    TextField loginNombre;
    @FXML
    Button botonLogin;
    @FXML
    CheckBox checkBoxUsuario;



    @FXML
    public void login() {
        ArrayList<Usuario> usuarios= MYSQL.crearUsuarios();
        if (MYSQL.checkusuario(usuarios,loginNombre.toString(),loginContraseña.toString())){


        }else{

        }

    }
}
