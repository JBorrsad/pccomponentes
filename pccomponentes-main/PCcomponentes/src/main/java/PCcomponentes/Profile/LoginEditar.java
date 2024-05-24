package PCcomponentes.Profile;

import PCcomponentes.Productos.MYSQL;
import PCcomponentes.Productos.ProductoClienteControlador;
import PCcomponentes.Productos.ProductoControlador;
import PCcomponentes.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
    private int id;
    private String selectedImagePath;
    private String currentImagePath;
    private ProductoControlador productoControlador;
    private ProductoClienteControlador productoClienteControlador;

    public void initialize() {
        this.username = MYSQL.getUsername();
        mostrarDatosUsuario();
        Cancelar.setOnAction(this::cerrarVentana);
    }

    public void setProductoControlador(ProductoControlador productoControlador) {
        this.productoControlador = productoControlador;
    }

    public void setProductoClienteControlador(ProductoClienteControlador productoClienteControlador) {
        this.productoClienteControlador = productoClienteControlador;
    }

    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) Cancelar.getScene().getWindow();
        stage.close();
    }

    public void mostrarDatosUsuario() {
        Usuario usuario = MYSQL.getUsuariosSQL(username);
        if (usuario != null) {
            id = usuario.getId();
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
            currentImagePath = usuario.getFoto();
            if (currentImagePath != null && !currentImagePath.isEmpty()) {
                profile.setImage(new Image(new File(currentImagePath).toURI().toString()));
            }
        }
    }

    @FXML
    public void fileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString(), 100, 150, true, true);
            profile.setImage(image);
        } else {
            System.out.println("No file selected.");
        }
    }

    @FXML
    void guardarCambios() {
        String imagePath = currentImagePath;
        if (selectedImagePath != null) {
            try {
                Path destinationDirectory = Paths.get("img");
                if (!Files.exists(destinationDirectory)) {
                    Files.createDirectories(destinationDirectory);
                }

                Path destinationPath = destinationDirectory.resolve(new File(selectedImagePath).getName());
                Files.copy(Paths.get(selectedImagePath), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = destinationPath.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        Usuario usuario = new Usuario(
                id,
                username,
                contrasena.getText(),
                rol.getText(),
                paganombre.getText(),
                pagaapellido.getText(),
                Integer.parseInt(codigopostal.getText()),
                provincia.getText(),
                localidad.getText(),
                direccion.getText(),
                cuentabancaria.getText(),
                imagePath
        );

        MYSQL.actualizarUsuario(usuario);

        if (productoControlador != null) {
            productoControlador.updateProfileImage(imagePath);
        } else if (productoClienteControlador != null) {
            productoClienteControlador.updateProfileImage(imagePath);
        }

        Stage stage = (Stage) Guardar.getScene().getWindow();
        stage.close();
    }
}
