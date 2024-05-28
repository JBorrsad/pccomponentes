package PCcomponentes;



public class Producto {

    private int ID_DISPOSITIVO;
    private String NOMBRE;
    private int STOCK;
    private double PRECIO;




    public Producto(int ID_DISPOSITIVO, String NOMBRE, int STOCK, double PRECIO) {
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

    public double getPRECIO() {
        return PRECIO;
    }

    public void setPRECIO(double PRECIO) {
        this.PRECIO = PRECIO;
    }


}
