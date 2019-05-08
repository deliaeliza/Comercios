package com.example.comercios.Modelo;

public class Administrador {
    private int id;
    private int tipo;
    private long telefono;
    private boolean estado;
    private String codigoRestablecer;
    private String correo;
    private String usuario;
    private String contrasena;

    public Administrador(int id, int tipo, long telefono, boolean estado, String codigoRestablecer, String correo, String usuario, String contrasena) {
        this.id = id;
        this.tipo = tipo;
        this.telefono = telefono;
        this.estado = estado;
        this.codigoRestablecer = codigoRestablecer;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public Administrador(int tipo, long telefono, String correo, String usuario, String contrasena) {
        this.tipo = tipo;
        this.telefono = telefono;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public Administrador(int id, int tipo, long telefono, boolean estado, String correo, String usuario, String contrasena) {
        this.tipo = tipo;
        this.id = id;
        this.estado = estado;
        this.telefono = telefono;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public Administrador(int id, int tipo, long telefono, boolean estado, String correo, String usuario) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
        this.correo = correo;
        this.usuario = usuario;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public int getTipo() {
        return tipo;
    }
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public long getTelefono() {
        return telefono;
    }
    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public boolean isEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getCodigoRestablecer() {
        return codigoRestablecer;
    }
    public void setCodigoRestablecer(String codigoRestablecer) {
        this.codigoRestablecer = codigoRestablecer;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
