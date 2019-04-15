package com.example.listviewprueba;

public class Categoria {
    private int id;
    private int font;
    private String nombre;

    public Categoria(int id, int font, String nombre){
        this.id = id;
        this.font = font;
        this.nombre = nombre;
    }
    public int getId() {
        return id;
    }

    public int getFont() {
        return font;
    }
    public void setFont(int font) {
        this.font = font;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
