package com.example.comercioscostarica.Modelo;

public class Calificacion {
    private int calificacion;
    private Comercio comercio;

    public Calificacion(int calificacion, Comercio comercio) {
        this.calificacion = calificacion;
        this.comercio = comercio;
    }

    public int getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public Comercio getComercio() {
        return comercio;
    }
    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }
}
