package com.example.listviewprueba;

public class ObjetosxDesplegar {
    private int id;
    private int numDibujo;
    private String nombre;
    public ObjetosxDesplegar(String nombre, int numDibujo, int id){
        super();
        this.nombre = nombre;
        this.numDibujo = numDibujo;
        this.id = id;
    }
    public String getNombre() { return nombre; }
    public int getNumDibujo() {
        return numDibujo;
    }
    public int getId() { return id; }
}
