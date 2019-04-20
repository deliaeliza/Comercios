package com.example.comercios.Modelo;

import com.example.comercios.Modelo.Calificacion;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class UsuarioEstandar {
    private int id;
    private int tipo;
    private boolean estado;
    private String codigoRestablecer;
    private String correo;
    private String usuario;
    private String contrasena;
    private Date fehcaNacimiento;
    private int edad;
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
        this.edad = edad;
        this.estado = estado;
        this.codigoRestablecer = codigoRestablecer;
        this.correo = correo;
        this.usuario = usuario;
        this.fehcaNacimiento = fehcaNacimiento;
    }

    public UsuarioEstandar(int id, int tipo, int edad, boolean estado, String codigoRestablecer, String correo, String usuario, Date fehcaNacimiento) {
        this.id = id;
        this.tipo = tipo;
        this.edad = edad;
        this.estado = estado;
        this.codigoRestablecer = codigoRestablecer;
        this.correo = correo;
        this.usuario = usuario;
        this.fehcaNacimiento = fehcaNacimiento;
    }
    public UsuarioEstandar(int id, int tipo, int edad, boolean estado, String correo, String usuario, Date fehcaNacimiento) {
        this.id = id;
        this.tipo = tipo;
        this.edad = edad;
        this.estado = estado;
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

    public int getEdad(){
        return edad;
    }
    public void setEdad(int edad){
        this.edad = edad;
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
