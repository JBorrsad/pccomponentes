package org.example.demo;

public class Producto {

    private int ID_DISPOSITIVO;
    private String NOMBRE;
   private int STOCK;
    private int PRECI0;



    public Producto(int ID_DISPOSITIVO, String NOMBRE, int STOCK, int PRECI0) {
        this.ID_DISPOSITIVO = ID_DISPOSITIVO;
        this.NOMBRE = NOMBRE;
        this.STOCK = STOCK;
        this.PRECI0 = PRECI0;
    }


    public int getID_DISPOSITIVO() {
        return ID_DISPOSITIVO;
    }

    public void setID_DISPOSITIVO(int ID_DISPOSITIVO) {
        this.ID_DISPOSITIVO = ID_DISPOSITIVO;
    }




    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public int getSTOCK() {
        return STOCK;
    }

    public void setSTOCK(int STOCK) {
        this.STOCK = STOCK;
    }

    public int getPRECI0() {
        return PRECI0;
    }

    public void setPRECI0(int PRECI0) {
        this.PRECI0 = PRECI0;
    }


}
