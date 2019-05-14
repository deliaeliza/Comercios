package com.example.comercios.Modelo;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Comercio {
    private int id;
    private int tipo;
    private long telefono;
    private float calificacion; //Promedio de todos usuariosEstandar que lo han calificado
    private int cantCalificaciones;
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
    private String latitud;
    private String longitud;
    private int idCategoria;
    private Bitmap imagen = null;
    private ArrayList<Seccion> secciones;


    public Comercio(int id, int tipo, long telefono, float calificacion, boolean verificado,
                    boolean estado, String codigoRestablecer, String correo, String usuario, String contrasena,
                    String descripcion, String ubicacion, String categoria, ArrayList<Seccion> secciones) {
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

    public Comercio(int id, int tipo, long telefono, boolean verificado, boolean estado, String correo, String usuario, String descripcion, String ubicacion, String categoria, String urlImagen,String longitud,String latitud ,int idCategoria, String contrasena) {
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
        this.longitud = longitud;
        this.latitud = latitud;
        this.idCategoria = idCategoria;
        this.contrasena = contrasena;
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

    public Comercio(int id, int tipo, long telefono, float calificacion, boolean verificado, boolean estado, String correo, String usuario, String descripcion, String ubicacion, String categoria, ArrayList<Seccion> secciones) {
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
    public Comercio(int id, int tipo, long telefono, float calificacion, int cantCalificaciones, boolean verificado, boolean estado, String correo, String usuario, String descripcion, String categoria, String urlImagen, Bitmap imagen) {
        this.id = id;
        this.tipo = tipo;
        this.telefono = telefono;
        this.calificacion = calificacion;
        this.cantCalificaciones = cantCalificaciones;
        this.verificado = verificado;
        this.estado = estado;
        this.correo = correo;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.urlImagen = urlImagen;
        this.imagen = imagen;
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

    public float getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public int getCantCalificaciones() {
        return cantCalificaciones;
    }
    public void setCantCalificaciones(int cantCalificaciones) {
        this.cantCalificaciones = cantCalificaciones;
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

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
