package PCcomponentes.Login;

import PCcomponentes.Usuario;

public class Cookie {
    private static Cookie instance;
    private Usuario usuario;

    private Cookie() {}

    public static Cookie getInstance() {
        if (instance == null) {
            instance = new Cookie();
        }
        return instance;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


}

