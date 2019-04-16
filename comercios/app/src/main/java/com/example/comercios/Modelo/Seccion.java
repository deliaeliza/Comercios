package com.example.comercios.Modelo;

import com.example.comercios.Modelo.Producto;

import java.util.ArrayList;

public class Seccion {
    private int id;
    private String nombre;
    private ArrayList<Producto> productos;

    public Seccion(int id, String nombre, ArrayList<Producto> productos) {
        this.id = id;
        this.nombre = nombre;
        this.productos = productos;
    }

    public Seccion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Seccion(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }
    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
}
