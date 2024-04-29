package org.example.demo;

import java.sql.*;

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

    public static void getUsuarios() {
        try (Connection C = getConnection();
             Statement statement = C.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM USUARIOS")) {

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

    public static void getDispositivos() {
        try (Connection C = getConnection();
             Statement statement = C.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM DISPOSITIVOS")) {

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
}






