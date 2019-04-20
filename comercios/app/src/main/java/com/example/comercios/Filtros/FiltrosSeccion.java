package com.example.comercios.Filtros;

public class FiltrosSeccion {
    private boolean usarFiltros = false;
    private String nombre = "";

    private static FiltrosSeccion instance = null;

    protected FiltrosSeccion() {}
    public static FiltrosSeccion getInstance() {
        if(instance == null) {instance = new FiltrosSeccion(); }
        return instance;
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

    public void reiniciarFiltros(){
        usarFiltros = false;
        nombre = "";
    }
}
