package com.example.comercios.Global;
import com.example.comercios.Modelo.Administrador;

public class GlobalAdmin {

    private Administrador admin = null;

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

}
