package com.example.comercios.Filtros;

public class FilstrosSeccion {
    private boolean usarFiltros = false;
    private int cantMin = -1;
    private int cantMax = -1;
    private String nombre = "";

    private static FilstrosSeccion instance = null;

    protected FilstrosSeccion() {}
    public static FilstrosSeccion getInstance() {
        if(instance == null) {instance = new FilstrosSeccion(); }
        return instance;
    }

    public int getCantMin() {
        return cantMin;
    }
    public void setCantMin(int cantMin) {
        this.cantMin = cantMin;
    }

    public int getCantMax() {
        return cantMax;
    }
    public void setCantMax(int cantMax) {
        this.cantMax = cantMax;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isUsarFiltros() {
        return usarFiltros;
    }
    public void setUsarFiltros(boolean usarFiltros) {
        this.usarFiltros = usarFiltros;
    }
}
