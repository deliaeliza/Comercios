package com.example.comercios.Global;
import com.example.comercios.modelo.Administrador;

public class GlobalSuperUsuario {

    private Administrador admin = null;
    private static GlobalSuperUsuario instance = null;

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
}
