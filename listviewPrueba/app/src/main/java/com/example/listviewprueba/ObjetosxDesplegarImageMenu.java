package com.example.listviewprueba;

public class ObjetosxDesplegarImageMenu {
    private String categoria;
    private int numDibujo;
    private int id;
    public ObjetosxDesplegarImageMenu(String categoria, int numDibujo, int id){
        super();
        this.categoria = categoria;
        this.numDibujo = numDibujo;
        this.id = id;
    }
    public String getCategoria() {
        return categoria;
    }
    public int getNumDibujo() {
        return numDibujo;
    }
    public int getId(){
        return id;
    }
}
