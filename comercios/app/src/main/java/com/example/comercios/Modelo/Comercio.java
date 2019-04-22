package com.example.comercios.Modelo;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Comercio {
    private int id;
    private int tipo;
    private long telefono;
    private int calificacion; //Promedio de todos usuariosEstandar que lo han calificado
    private boolean verificado;
    private boolean estado;
    private String codigoRestablecer;
    private String correo;
    private String usuario;
    private String contrasena;
    private String descripcion;
    private String ubicacion;
    private String categoria;
    private String urlImagen;
    private int idCategoria;
    private Bitmap imagen = null;
    private ArrayList<Seccion> secciones;

    public Comercio(int id, int tipo, long telefono, int calificacion, boolean verificado, boolean estado, String codigoRestablecer, String correo, String usuario, String contrasena, String descripcion, String ubicacion, String categoria, ArrayList<Seccion> secciones) {
        this.id = id;
        this.tipo = tipo;
        this.telefono = telefono;
        this.calificacion = calificacion;
        this.verificado = verificado;
        this.estado = estado;
        this.codigoRestablecer = codigoRestablecer;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.categoria = categoria;
        this.secciones = secciones;
    }

    public Comercio(int id, int tipo, long telefono, boolean verificado, boolean estado, String correo, String usuario, String descripcion, String ubicacion, String categoria, String urlImagen, int idCategoria) {
        this.id = id;
        this.tipo = tipo;
        this.telefono = telefono;
        this.verificado = verificado;
        this.estado = estado;
        this.correo = correo;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.categoria = categoria;
        this.urlImagen = urlImagen;
        this.idCategoria = idCategoria;
    }
/*
    public Comercio(int id, int tipo, long telefono, boolean verificado, boolean estado, String correo, String usuario, String descripcion, String ubicacion, String categoria) {
        this.id = id;
        this.tipo = tipo;
        this.telefono = telefono;
        this.verificado = verificado;
        this.estado = estado;
        this.correo = correo;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.categoria = categoria;
    }*/

    public Comercio(int id, int tipo, long telefono, int calificacion, boolean verificado, boolean estado, String correo, String usuario, String descripcion, String ubicacion, String categoria, ArrayList<Seccion> secciones) {
        this.id = id;
        this.tipo = tipo;
        this.telefono = telefono;
        this.calificacion = calificacion;
        this.verificado = verificado;
        this.estado = estado;
        this.correo = correo;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.categoria = categoria;
        this.secciones = secciones;
    }

    public Comercio(int id, int tipo, boolean verificado, boolean estado, String correo, String usuario, String categoria, String urlImagen) {
        this.id = id;
        this.tipo = tipo;
        this.verificado = verificado;
        this.estado = estado;
        this.correo = correo;
        this.usuario = usuario;
        this.categoria = categoria;
        this.urlImagen = urlImagen;
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
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public boolean isVerificado() {
        return verificado;
    }
    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
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

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public ArrayList<Seccion> getSecciones() {
        return secciones;
    }
    public void setSecciones(ArrayList<Seccion> secciones) {
        this.secciones = secciones;
    }

    public String getUrlImagen() {
        return urlImagen;
    }
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Bitmap getImagen() {
        return imagen;
    }
    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
