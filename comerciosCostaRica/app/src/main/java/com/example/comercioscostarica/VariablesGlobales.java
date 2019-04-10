package com.example.comercioscostarica;

// Se debe crear en el paquete com.example...
// Forma de uso:
// VariablesGlobales vg = VariablesGlobales.getInstance(); vg.setMitexto("Hola");    int i = vg.getMivalor();
public class VariablesGlobales {
    private String mitexto="";
    private int mivalor=-1;

    private static VariablesGlobales instance = null;

    protected VariablesGlobales() {}
    public static VariablesGlobales getInstance() {
        if(instance == null) {instance = new VariablesGlobales(); }
        return instance;
    }


    public String getMitexto() {
        return mitexto;
    }

    public void setMitexto(String mitexto) {
        this.mitexto = mitexto;
    }

    public int getMivalor() {
        return mivalor;
    }

    public void setMivalor(int mivalor) {
        this.mivalor = mivalor;
    }
}// fin de la clase de variables globales

