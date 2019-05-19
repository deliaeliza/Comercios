package com.example.comercios.Modelo;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Producto {
    private int id;
    private int precio;
    private boolean estado;
    private String nombre;
    private String descripcion;
    private String[] urlsImagenes;
    private ArrayList<Bitmap> imagenes;
    private boolean pertenece;
    private String urlPrueba;
    private Bitmap imagen = null;

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public boolean isPertenece() {
        return pertenece;
    }

    public void setPertenece(boolean pertenece) {
        this.pertenece = pertenece;
    }

    public Producto(int id, boolean estado, int precio, String nombre, String descripcion, boolean pertenece) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pertenece = pertenece;
    }
    ///Angelo
    public Producto(int id,boolean estado, int precio, String nombre, String descripcion, boolean pertenece, String urlPrueba) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pertenece = pertenece;
        this.urlPrueba = urlPrueba;
    }

    public Producto(int id, boolean estado, int precio, String nombre, String descripcion) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public Producto(int id, boolean estado, int precio, String nombre, String descripcion, String[] urlsImagenes) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlsImagenes = urlsImagenes;
    }

    public Producto(int id, boolean estado, int precio, String nombre, String descripcion, ArrayList<Bitmap> imagenes) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenes = imagenes;
    }

    public Producto(boolean estado, String nombre, String descripcion) {
        this.estado = estado;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getUrlPrueba() {
        return urlPrueba;
    }

    public void setUrlPrueba(String urlPrueba) {
        this.urlPrueba = urlPrueba;
    }

    public int getPrecio() {
        return precio;
    }
    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public boolean isEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String[] getUrlsImagenes() {
        return urlsImagenes;
    }
    public void setUrlsImagenes(String[] urlsImagenes) {
        this.urlsImagenes = urlsImagenes;
    }

    public ArrayList<Bitmap> getImagenes() {
        return imagenes;
    }
    public void setImagenes(ArrayList<Bitmap> imagenes) {
        this.imagenes = imagenes;
    }
}
