package com.example.comercios.Global;

import com.example.comercios.R;

public class GlobalGeneral {

    private static GlobalGeneral instance = null;

    private int ventanaActual = R.layout.activity_login;


    protected GlobalGeneral() {
    }

    public static GlobalGeneral getInstance() {
        if (instance == null) {
            instance = new GlobalGeneral();
        }
        return instance;
    }

    public int getVentanaActual() {
        return ventanaActual;
    }

    public void setVentanaActual(int ventanaActual) {
        this.ventanaActual = ventanaActual;
    }

}
