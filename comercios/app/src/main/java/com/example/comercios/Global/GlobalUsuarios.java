package com.example.comercios.Global;
import com.example.comercios.Modelo.UsuarioEstandar;

public class GlobalUsuarios {

    private UsuarioEstandar userE = null;
    private static GlobalUsuarios instance = null;

    protected GlobalUsuarios() {}
    public static GlobalUsuarios getInstance() {
        if(instance == null) {instance = new GlobalUsuarios(); }
        return instance;
    }

    public UsuarioEstandar getUserE() {
        return userE;
    }

    public void setUserE(UsuarioEstandar userE) {
        this.userE = userE;
    }
}
