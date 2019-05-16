package com.example.comercios.Global;

import com.example.comercios.Modelo.Comercio;
import com.example.comercios.Modelo.UsuarioEstandar;
import com.example.comercios.R;

public class GlobalUsuarios {

    private UsuarioEstandar userE = null;
    private Comercio comercio = null; //Comercio escojido
    private int posComercio = -1; //Permite actualizar la calificacion
    private int idSeccion = -1; //Seccion escojida
    private static GlobalUsuarios instance = null;
    private int ventanaActual = R.layout.frag_home_usuario_estandar;

    protected GlobalUsuarios() {
    }

    public static GlobalUsuarios getInstance() {
        if (instance == null) {
            instance = new GlobalUsuarios();
        }
        return instance;
    }

    public UsuarioEstandar getUserE() {
        return userE;
    }

    public void setUserE(UsuarioEstandar userE) {
        this.userE = userE;
    }

    public int getVentanaActual() {
        return ventanaActual;
    }

    public void setVentanaActual(int ventanaActual) {
        this.ventanaActual = ventanaActual;
    }

    public Comercio getComercio() {
        return comercio;
    }
    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public int getIdSeccion() {
        return idSeccion;
    }
    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public int getPosComercio() {
        return posComercio;
    }
    public void setPosComercio(int posComercio) {
        this.posComercio = posComercio;
    }
}
