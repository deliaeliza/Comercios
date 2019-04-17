package com.example.comercios.Global;
import com.example.comercios.Modelo.Comercio;

public class GlobalComercios {

    private Comercio comercio = null;

    private int opcActual;
    private int idSecModificar = -1;
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

    public int getOpcActual() { return opcActual; }
    public void setOpcActual(int opcActual) { this.opcActual = opcActual; }

    public int getIdSecModificar() { return idSecModificar; }

    public void setIdSecModificar(int idSecModificar) { this.idSecModificar = idSecModificar;}
}
