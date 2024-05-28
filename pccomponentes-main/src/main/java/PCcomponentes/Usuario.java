package PCcomponentes;

public class Usuario {



    private int id;
    private String username;
    private String contrasena;
    private String rol;
    private String pagoNombre;
    private String pagoApellido;
    private int codigoPostal;
    private String provincia;
    private String localidad;
    private String direccion;
    private String cuentaBanco;
    private String foto;

    public Usuario(int id,String username, String contrasena, String rol, String pagoNombre, String pagoApellido, int codigoPostal, String provincia, String localidad, String direccion, String cuentaBanco, String foto) {
        this.username = username;
        this.contrasena = contrasena;
        this.rol = rol;
        this.pagoNombre = pagoNombre;
        this.pagoApellido = pagoApellido;
        this.codigoPostal = codigoPostal;
        this.provincia = provincia;
        this.localidad = localidad;
        this.direccion = direccion;
        this.cuentaBanco = cuentaBanco;
        this.foto = foto;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPagoNombre() {
        return pagoNombre;
    }

    public void setPagoNombre(String pagoNombre) {
        this.pagoNombre = pagoNombre;
    }

    public String getPagoApellido() {
        return pagoApellido;
    }

    public void setPagoApellido(String pagoApellido) {
        this.pagoApellido = pagoApellido;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCuentaBanco() {
        return cuentaBanco;
    }

    public void setCuentaBanco(String cuentaBanco) {
        this.cuentaBanco = cuentaBanco;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
