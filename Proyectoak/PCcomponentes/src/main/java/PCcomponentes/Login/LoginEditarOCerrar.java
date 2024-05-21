package PCcomponentes.Login;

import PCcomponentes.Productos.MYSQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static PCcomponentes.Login.LoginControlador.closeAllStagesExcept;


public class LoginEditarOCerrar {

    private Stage productosStage;

    @FXML
    private void handleEditar(ActionEvent event) {

        try {
            // Load the new FXML for the editing scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginEditar.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML// En LoginEditarOCerrar.java

    private void handleCerrar(ActionEvent event) {
        // Eliminar cualquier información de usuario almacenada
        // En este caso, suponemos que tienes una clase Cookie para almacenar la sesión del usuario


        // Verificar si la instancia productosStage está abierta y cerrarla si es necesario
        if (productosStage != null && productosStage.isShowing()) {
            productosStage.close();
        }

        try {
            // Obtener el escenario actual y cerrarlo
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Cargar la ventana de inicio de sesión
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();

            // Crear un nuevo escenario para la ventana de inicio de sesión
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();
            closeAllStagesExcept(loginStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public void setProductosStage(Stage productosStage) {
        this.productosStage = productosStage;
    }

}
