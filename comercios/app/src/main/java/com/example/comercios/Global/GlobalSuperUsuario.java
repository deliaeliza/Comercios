package com.example.comercios.Global;
import com.example.comercios.Modelo.Administrador;
import com.example.comercios.R;

public class GlobalSuperUsuario {

    private Administrador admin = null;
    private static GlobalSuperUsuario instance = null;

    private int ventanaActual = R.layout.frag_home_super_usuario;

    protected GlobalSuperUsuario() {}
    public static GlobalSuperUsuario getInstance() {
        if(instance == null) {instance = new GlobalSuperUsuario(); }
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
