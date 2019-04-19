package com.example.comercios.Modelo;

import com.example.comercios.Modelo.Producto;

import java.util.ArrayList;

public class Seccion {
    private int id;
    private String nombre;
    private int cantProductos; //Evita traer los productos de la base solo para saber la cantidad
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
    public Seccion(int id, int cantProductos, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.cantProductos = cantProductos;
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

    public int getCantProductos(){
        return cantProductos;
    }
    public void setCantProductos(int cantProductos){
        this.cantProductos = cantProductos;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }
    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
}
