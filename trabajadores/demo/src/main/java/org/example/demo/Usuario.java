package org.example.demo;

public class Usuario {

    private int ID;
    private String NOMBRE;
    private String CONTRASENA;
    private String ROL;

    public Usuario (int ID, String nombre, String contra, String rol) {
        this.ID = ID;
        this.NOMBRE = nombre;
        this.CONTRASENA= contra;
        this.ROL=rol;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getCONTRASENA() {
        return CONTRASENA;
    }

    public void setCONTRASENA(String CONTRASENA) {
        this.CONTRASENA = CONTRASENA;
    }

    public String getROL() {
        return ROL;
    }

    public void setROL(String ROL) {
        this.ROL = ROL;
    }
}
