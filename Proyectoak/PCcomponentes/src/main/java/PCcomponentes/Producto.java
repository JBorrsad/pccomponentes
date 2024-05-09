package PCcomponentes;



public class Producto {

    private int ID_DISPOSITIVO;
    private String NOMBRE;
    private int STOCK;
    private int PRECIO;



    public Producto(int ID_DISPOSITIVO, String NOMBRE, int STOCK, int PRECIO) {
        this.ID_DISPOSITIVO = ID_DISPOSITIVO;
        this.NOMBRE = NOMBRE;
        this.STOCK = STOCK;
        this.PRECIO = PRECIO;
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

    public int getPRECIO() {
        return PRECIO;
    }

    public void setPRECIO(int PRECIO) {
        this.PRECIO = PRECIO;
    }


}
