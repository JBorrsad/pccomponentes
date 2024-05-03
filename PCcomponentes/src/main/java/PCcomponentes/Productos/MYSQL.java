package PCcomponentes;

import PCcomponentes.Productos.Producto;

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
                int ID_DISPOSITIVO = resultSet.getInt("ID_DISPOSITIVO");
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
        String sql = "INSERT INTO PRODUCTOS (NOMBRE, STOCK, PRECIO) VALUES (?, ?, ?)";

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
}
