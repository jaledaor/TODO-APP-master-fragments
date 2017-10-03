package co.edu.ucc.todolist.entidades;

/**
 * Created by Jaledaor on 29/09/2017.
 */

public class Usuario {
    private String nombres;
    private String token;

    public Usuario() {

    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
