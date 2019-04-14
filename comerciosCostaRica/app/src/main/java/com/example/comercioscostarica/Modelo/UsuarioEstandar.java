package com.example.comercioscostarica.Modelo;

import java.util.ArrayList;
import java.util.Date;

public class UsuarioEstandar {
    private int id;
    private int tipo;
    private boolean estado;
    private String codigoRestablecer;
    private String correo;
    private String usuario;
    private String contrasena;
    private Date fehcaNacimiento;
    private ArrayList<Calificacion> calificaciones;

    public UsuarioEstandar(int id, int tipo, boolean estado, String codigoRestablecer, String correo, String usuario, String contrasena, Date fehcaNacimiento, ArrayList<Calificacion> calificaciones) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
        this.codigoRestablecer = codigoRestablecer;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.fehcaNacimiento = fehcaNacimiento;
        this.calificaciones = calificaciones;
    }

    public UsuarioEstandar(int tipo, String correo, String usuario, String contrasena, Date fehcaNacimiento) {
        this.tipo = tipo;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.fehcaNacimiento = fehcaNacimiento;
    }

    public UsuarioEstandar(int id, int tipo, boolean estado, String codigoRestablecer, String correo, String usuario, Date fehcaNacimiento) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
        this.codigoRestablecer = codigoRestablecer;
        this.correo = correo;
        this.usuario = usuario;
        this.fehcaNacimiento = fehcaNacimiento;
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

    public Date getFehcaNacimiento() {
        return fehcaNacimiento;
    }
    public void setFehcaNacimiento(Date fehcaNacimiento) {
        this.fehcaNacimiento = fehcaNacimiento;
    }

    public ArrayList<Calificacion> getCalificaciones() {
        return calificaciones;
    }
    public void setCalificaciones(ArrayList<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }
}
