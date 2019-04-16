package com.example.comercios.Global;
import com.example.comercios.modelo.Comercio;

public class GlobalComercios {

    private Comercio comercio = null;

    private static GlobalComercios instance = null;

    protected GlobalComercios() {}
    public static GlobalComercios getInstance() {
        if(instance == null) {instance = new GlobalComercios(); }
        return instance;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }
}
