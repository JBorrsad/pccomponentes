package PCcomponentes.Productos;

import PCcomponentes.Producto;
import PCcomponentes.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class MYSQL {
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

    public static Usuario getUsuariosSQL(String username, String password) {
        Usuario usuario = null;
        String query = "SELECT * FROM USUARIOS WHERE NOMBRE = ? AND CONTRASEÑA = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String nombre = resultSet.getString("NOMBRE");
                String contraseña = resultSet.getString("CONTRASEÑA");
                String rol = resultSet.getString("ROL");
                usuario = new Usuario(id, nombre, contraseña, rol);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
        }
        return usuario;
    }

    public static boolean checkusuario(String nombre, String contraseña) {
        ArrayList<Usuario> usuarios = crearUsuarios();
        boolean resultado = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNOMBRE().equals(nombre) && (usuarios.get(i).getCONTRASENA().equals(contraseña))) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }

    public static ArrayList<Usuario> crearUsuarios() {
        ArrayList<Usuario> resultado = new ArrayList<>();

        try (Connection C = getConnection(); Statement statement = C.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM USUARIOS")) {

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String NOMBRE = resultSet.getString("NOMBRE");
                String CONTRASEÑA = resultSet.getString("CONTRASEÑA");
                String ROL = resultSet.getString("ROL");

                Usuario user = new Usuario(ID, NOMBRE, CONTRASEÑA, ROL);
                resultado.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
        }
        return resultado;
    }

    public static ArrayList<Producto> crearProducto() {
        ArrayList<Producto> resultado = new ArrayList<>();

        try (Connection C = getConnection(); Statement statement = C.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCTOS")) {

            while (resultSet.next()) {
                int ID_DISPOSITIVO = resultSet.getInt("ID");
                String NOMBRE = resultSet.getString("NOMBRE");
                int STOCK = resultSet.getInt("STOCK");
                int PRECIO = resultSet.getInt("PRECIO");

                Producto prod = new Producto(ID_DISPOSITIVO, NOMBRE, STOCK, PRECIO);
                resultado.add(prod);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
        }
        return resultado;
    }

    public static void añadirProducto(String nombre, int stock, int precio) {
        String sql = "INSERT INTO PRODUCTOS (ID, NOMBRE, STOCK, PRECIO) VALUES (?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, nombre);
            statement.setInt(2, stock);
            statement.setInt(3, precio);

            int filasInsertadas = statement.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Producto insertado correctamente.");
            } else {
                System.out.println("No se pudo insertar el producto.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
        }
    }

    public static void eliminarProducto(int idProducto) {
        String sql = "DELETE FROM productos WHERE ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            pstmt.executeUpdate();

            System.out.println("Producto eliminado exitosamente.");

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
}
