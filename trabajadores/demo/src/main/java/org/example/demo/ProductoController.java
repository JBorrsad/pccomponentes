package org.example.demo;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class ProductoController {
    @FXML
    private TableView tablaproductos;
    private Button añadir;
    @FXML
    private Button eliminar;
    @FXML
    private Button refrescar;

    @FXML void refrescar() {

        ArrayList<Producto> misproductos= MYSQL.crearProducto();
        for (Producto p : misproductos) {
            tablaproductos.getItems().add(p);
            System.out.println(p.getNOMBRE() + "  Añadido a la tabla");
        }

    }

    @FXML void eliminar() {}

    @FXML void añadirproducto(String nombre, int stock, int precio)
    {
        MYSQL.añadirProducto(nombre, stock, precio);
        refrescar();
    }






}
