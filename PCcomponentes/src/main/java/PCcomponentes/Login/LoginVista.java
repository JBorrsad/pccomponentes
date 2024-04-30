package PCcomponentes.Login;


import PCcomponentes.MYSQL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginVista extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        MYSQL.getUsuariosSQL();
        MYSQL.getDispositivosSQL();
        try {
            // Loading the FXML file and displaying the JavaFX scene
            FXMLLoader fxmlLoader = new FXMLLoader(LoginVista.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            stage.setResizable(false);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }
}
