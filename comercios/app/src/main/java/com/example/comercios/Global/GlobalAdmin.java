package com.example.comercios.Global;
import com.example.comercios.Modelo.Administrador;
import com.example.comercios.R;

public class GlobalAdmin {

    private Administrador admin = null;

    private int ventanaActual = R.layout.frag_home_admin;

    private static GlobalAdmin instance = null;

    protected GlobalAdmin() {}
    public static GlobalAdmin getInstance() {
        if(instance == null) {instance = new GlobalAdmin(); }
        return instance;
    }

    public Administrador getAdmin() {
        return admin;
    }

    public void setAdmin(Administrador admin) {
        this.admin = admin;
    }

    public int getVentanaActual() {
        return ventanaActual;
    }

    public void setVentanaActual(int ventanaActual) {
        this.ventanaActual = ventanaActual;
    }
}
