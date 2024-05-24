package PCcomponentes.Productos;

import PCcomponentes.Producto;
import PCcomponentes.Usuario;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;

public class MYSQL {

    private static String username;

    private static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/tiendaelectronica";
        String user = "root";
        String password = "root";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE USUARIOS SET CONTRASEÑA = ?, ROL = ?,NOMBRE = ?, APELLIDO = ?, PROVINCIA = ?, LOCALIDAD = ?, DIRECCION = ?, CP = ?, CUENTABANCO = ?, FOTO = ? WHERE USERNAME = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, usuario.getContrasena());
            pstmt.setString(2, usuario.getRol());
            pstmt.setString(3, usuario.getPagoNombre());
            pstmt.setString(4, usuario.getPagoApellido());
            pstmt.setString(5, usuario.getProvincia());
            pstmt.setString(6, usuario.getLocalidad());
            pstmt.setString(7, usuario.getDireccion());
            pstmt.setInt(8, usuario.getCodigoPostal());
            pstmt.setString(9, usuario.getCuentaBanco());
            pstmt.setString(10, usuario.getFoto());
            pstmt.setString(11, usuario.getUsername());


            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    public static Usuario getUsuariosSQL(String username) {
        Usuario usuario = null;
        String query = "SELECT * FROM USUARIOS WHERE USERNAME = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                Integer id = resultSet.getInt("ID");
                String nombre = resultSet.getString("USERNAME");
                String contraseña = resultSet.getString("CONTRASEÑA");
                String rol = resultSet.getString("ROL");
                String paganombre = resultSet.getString("NOMBRE");
                String pagaapellido = resultSet.getString("APELLIDO");
                Integer codigopostal = resultSet.getInt("CP");
                String provincia = resultSet.getString("PROVINCIA");
                String localidad = resultSet.getString("LOCALIDAD");
                String direccion = resultSet.getString("DIRECCION");
                String cuentabanco = resultSet.getString("CUENTABANCO");
                String foto = resultSet.getString("FOTO");
                usuario = new Usuario(id,username,contraseña, rol, paganombre, pagaapellido, codigopostal, provincia, localidad, direccion, cuentabanco, foto);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        return usuario;
    }

    public static boolean checkusuario(String username, String contraseña) {
        String query = "SELECT * FROM USUARIOS WHERE USERNAME = ? AND CONTRASEÑA = ?";
        boolean resultado = false;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, contraseña);
            ResultSet resultSet = pstmt.executeQuery();

            resultado = resultSet.next();

        } catch (SQLException e) {
            System.err.println("Error al verificar usuario: " + e.getMessage());
        }
        return resultado;
    }

//    public static ArrayList<Usuario> crearUsuarios() {
//        ArrayList<Usuario> usuarios = new ArrayList<>();
//        String query = "SELECT * FROM USUARIOS";
//
//        try (Connection conn = getConnection();
//             Statement statement = conn.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//
//            while (resultSet.next()) {
//                String username = resultSet.getString("USERNAME");
//                String contraseña = resultSet.getString("CONTRASEÑA");
//                String rol = resultSet.getString("ROL");
//                String paganombre = resultSet.getString("PAGONOMBRE");
//                String pagaapellido = resultSet.getString("PAGOAPELLIDO");
//                Integer codigopostal = resultSet.getInt("CP");
//                String provincia = resultSet.getString("PROVINCIA");
//                String localidad = resultSet.getString("LOCALIDAD");
//                String direccion = resultSet.getString("DIRECCION");
//                String cuentabanco = resultSet.getString("CUENTABANCO");
//                String foto = resultSet.getString("FOTO");
//
//                Usuario usuario = new Usuario(username,contraseña, rol, paganombre, pagaapellido, codigopostal, provincia, localidad, direccion, cuentabanco, foto);
//                usuarios.add(usuario);
//            }
//        } catch (SQLException e) {
//            System.err.println("Error al obtener usuarios: " + e.getMessage());
//        }
//        return usuarios;
//    }

    public static ArrayList<Producto> crearProducto() {
        ArrayList<Producto> resultado = new ArrayList<>();

        try (Connection C = getConnection();
             Statement statement = C.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCTOS")) {

            while (resultSet.next()) {
                int ID_DISPOSITIVO = resultSet.getInt("ID");
                String NOMBRE = resultSet.getString("NOMBRE");
                int STOCK = resultSet.getInt("STOCK");
                double PRECIO = resultSet.getInt("PRECIO");

                Producto prod = new Producto(ID_DISPOSITIVO, NOMBRE, STOCK, PRECIO);
                resultado.add(prod);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
        }
        return resultado;
    }

    public static void añadirProducto(String nombre, int stock, double precio) {
        String sql = "INSERT INTO productos (nombre, stock, precio) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setInt(2, stock);
            pstmt.setDouble(3, precio);
            pstmt.executeUpdate();

            System.out.println("Producto añadido exitosamente a la base de datos.");

        } catch (SQLException e) {
            System.err.println("Error al añadir el producto a la base de datos: " + e.getMessage());
        }
    }

    public static void eliminarProducto(int idProducto) {
        String sql = "DELETE FROM productos WHERE ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            pstmt.executeUpdate();

            System.out.println("Producto eliminado exitosamente.");
            reorganizarIDs();

        } catch (SQLException e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
        }
    }


    //eliminar producto pero sin sout's, para utilizarlo en otros metodos

    public static void eliminarProductosilencio(int idProducto) {
        String sql = "DELETE FROM PRODUCTOS WHERE ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, idProducto);

            int filasEliminadas = statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }}

    //el producto que hay que restar y cuantos de ellos quieres quitar del stock

    public static void restarstock(Producto p, int n) {
        eliminarProductosilencio(p.getID_DISPOSITIVO());
        añadirProducto(p.getNOMBRE(),p.getSTOCK()-n,p.getPRECIO());
    }

    public static void actualizarProducto(Producto producto) {
        String updateQuery = "UPDATE productos SET nombre =?, stock =?, precio =? WHERE id =?";
        PreparedStatement statement;

        try (Connection connection = getConnection()) { // Get the connection instance
            statement = connection.prepareStatement(updateQuery);
            statement.setString(1, producto.getNOMBRE());
            statement.setInt(2, producto.getSTOCK());
            statement.setDouble(3, producto.getPRECIO());
            statement.setInt(4, producto.getID_DISPOSITIVO());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Producto actualizado correctamente.");
            } else {
                System.out.println("No se ha podido actualizar el producto.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    public static void reorganizarIDs() {
        String updateQuery = "SET @count = 0";
        String updateQuery2 = "UPDATE productos SET ID = @count:= @count + 1";
        String updateQuery3 = "ALTER TABLE productos AUTO_INCREMENT = 1";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(updateQuery);
            statement.executeUpdate(updateQuery2);
            statement.executeUpdate(updateQuery3);
        } catch (SQLException e) {
            System.err.println("Error al reorganizar los IDs de los productos: " + e.getMessage());
        }
    }


    private void mostrarAlerta(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static Producto obtenerProductoPorNombre(String nombreProducto) {
        String query = "SELECT * FROM productos WHERE NOMBRE = ?";
        Producto producto = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nombreProducto);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int stock = resultSet.getInt("STOCK");
                double precio = resultSet.getDouble("PRECIO");

                producto = new Producto(id, nombreProducto, stock, precio);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el producto por nombre: " + e.getMessage());
        }
        return producto;
    }

    public static void actualizarStockProducto(int idProducto, int nuevoStock) {
        String query = "UPDATE productos SET STOCK = ? WHERE ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, nuevoStock);
            pstmt.setInt(2, idProducto);

            pstmt.executeUpdate();

            System.out.println("Stock del producto actualizado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar el stock del producto: " + e.getMessage());
        }
    }


    public static int cookie(){
        return 0;
    }



    public static void deleteUserFromDatabase(String username) {
        String sql = "DELETE FROM USUARIOS WHERE USERNAME = ?";

        try (Connection conn = MYSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.executeUpdate();

            System.out.println("Usuario eliminado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    public static void setUsername(String username){
        MYSQL.username = username;
    }

    public static String getUsername(){
        return MYSQL.username;
    }
}
