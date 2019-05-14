package com.example.comercios.Global;

import com.example.comercios.Modelo.Comercio;
import com.example.comercios.Modelo.UsuarioEstandar;
import com.example.comercios.R;

public class GlobalUsuarios {

    private UsuarioEstandar userE = null;
    private Comercio comercio = null; //Comercio escojido

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

}
