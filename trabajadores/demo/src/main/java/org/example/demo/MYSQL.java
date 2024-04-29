package org.example.demo;

import java.sql.*;
import java.util.ArrayList;

public class MYSQL {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/tiendaelectronica";
        String user = "root";
        String password = "root";

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void getUsuariosSQL() {
        try (Connection C = getConnection(); Statement statement = C.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM USUARIOS")) {

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String NOMBRE = resultSet.getString("NOMBRE");
                String CONTRASEÑA = resultSet.getString("CONTRASEÑA");
                String ROL = resultSet.getString("ROL");

                System.out.println("ID: " + ID + ", NOMBRE: " + NOMBRE + ", CONTRASEÑA: " + CONTRASEÑA + ", ROL: " + ROL);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
        }
    }

    public static void getDispositivosSQL() {
        try (Connection C = getConnection(); Statement statement = C.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM DISPOSITIVOS")) {

            while (resultSet.next()) {
                String ID_DISPOSITIVO = resultSet.getString("ID_DISPOSITIVO");
                String NOMBRE = resultSet.getString("NOMBRE");
                int STOCK = resultSet.getInt("STOCK");
                int PRECIO = resultSet.getInt("PRECIO");

                System.out.println("ID_DISPOSITIVO: " + ID_DISPOSITIVO + ", NOMBRE: " + NOMBRE + ", STOCK: " + STOCK + ", PRECIO: " + PRECIO);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
        }
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

    public static boolean checkusuario(ArrayList<Usuario> usuarios, String nombre, String contraseña) {
        boolean resultado = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNOMBRE().equals(nombre) && (usuarios.get(i).getCONTRASENA().equals(contraseña))) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }


}






