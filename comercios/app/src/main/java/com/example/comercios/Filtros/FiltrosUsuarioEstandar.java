package com.example.comercios.Filtros;

import com.example.comercios.Global.GlobalAdmin;

public class FiltrosUsuarioEstandar {
    private boolean usarFiltros = false;
    private int edadMin = -1;
    private int edadMax = -1;
    private String correo = "";
    private String usuario = "";

    private static FiltrosUsuarioEstandar instance = null;

    protected FiltrosUsuarioEstandar() {}
    public static FiltrosUsuarioEstandar getInstance() {
        if(instance == null) {instance = new FiltrosUsuarioEstandar(); }
        return instance;
    }

    public int getEdadMin() {
        return edadMin;
    }
    public void setEdadMin(int edadMin) {
        this.edadMin = edadMin;
    }

    public int getEdadMax() {
        return edadMax;
    }
    public void setEdadMax(int edadMax) {
        this.edadMax = edadMax;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public boolean isUsarFiltros() {
        return usarFiltros;
    }
    public void setUsarFiltros(boolean usarFiltros) {
        this.usarFiltros = usarFiltros;
    }
}
