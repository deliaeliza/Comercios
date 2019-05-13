package com.example.comercios.Global;

import com.example.comercios.Modelo.Comercio;
import com.example.comercios.Modelo.UsuarioEstandar;
import com.example.comercios.R;
import com.example.comercios.ViewPager.ViewPagerNoSwipe;

public class GlobalUsuarios {

    private UsuarioEstandar userE = null;
    private Comercio comercio = null; //Comercio escojido
    public static final int PAGINA_INICIO = 0;
    public static final int PAGINA_CUENTA = 1;
    public static final int PAGINA_ACERCA = 2;
    public static final int PAGINA_MAPA = 3;
    public static final int PAGINA_LISTACOMERCIOS = 4;
    public static final int PAGINA_VER_COMERCIOS = 5;



    private static GlobalUsuarios instance = null;
    public static ViewPagerNoSwipe viewPagerNoSwipe;
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
