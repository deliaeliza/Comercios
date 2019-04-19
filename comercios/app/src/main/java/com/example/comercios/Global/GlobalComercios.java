package com.example.comercios.Global;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.comercios.Modelo.Comercio;
import com.example.comercios.Modelo.Seccion;

import java.util.ArrayList;

public class GlobalComercios {
    private Comercio comercio = null;
    private Seccion seccion = null;
    private int opcActual;
    private int imgActual;

    private ArrayList<Bitmap> imageViewsProductos;

    private static GlobalComercios instance = null;

    protected GlobalComercios() {
        imageViewsProductos = new ArrayList<>();
    }

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

    public Seccion getSeccion() { return seccion; }
    public void setSeccion(Seccion seccion) { this.seccion = seccion; }

    public ArrayList<Bitmap> getImageViews() {
        return imageViewsProductos;
    }

    public void setImageViews(ArrayList<Bitmap> imageViews) {
        this.imageViewsProductos = imageViews;
    }

    public int getImgActual() {
        return imgActual;
    }

    public void setImgActual(int imgActual) {
        this.imgActual = imgActual;
    }

}
