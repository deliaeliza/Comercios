package com.example.comercios.Modelo;

public class Producto {
    private int id;
    private int precio;
    private boolean estado;
    private String nombre;
    private String descripcion;

    public Producto(int id, boolean estado, int precio, String nombre, String descripcion) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Producto(boolean estado, String nombre, String descripcion) {
        this.estado = estado;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
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
}
