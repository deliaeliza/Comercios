package com.example.comercios.Global;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.comercios.Modelo.Comercio;
import com.example.comercios.Modelo.Producto;
import com.example.comercios.Modelo.Seccion;
import com.example.comercios.R;

import java.util.ArrayList;

public class GlobalComercios {
    private Comercio comercio = null;
    private Seccion seccion = null;
    private Producto producto = null;
    private int posActProd;
    private int posSeccion = -1;
    private int opcActual;
    private int imgActual;

    private int idSeccionActual;

    private int ventanaActual = R.layout.frag_home_comercio;

    private ArrayList<Bitmap> imageViewsProductos;

    private ArrayList<String> idSecciones;

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

    public ArrayList<Bitmap> getImageViews() { return imageViewsProductos; }

    public void setImageViews(ArrayList<Bitmap> imageViews) { this.imageViewsProductos = imageViews; }

    public int getImgActual() { return imgActual; }

    public void setImgActual(int imgActual) { this.imgActual = imgActual; }

    public ArrayList<String> getIdSecciones() { return idSecciones; }

    public void setIdSecciones(ArrayList<String> idSecciones) { this.idSecciones = idSecciones; }

    public void agregarImagenes(Bitmap bit){
        imageViewsProductos.add(bit);
    }

    public void borrarImagenes(){
        imageViewsProductos.clear();
    }

    public void setProducto(Producto producto){
        this.producto = producto;
    }
    public Producto getProducto(){
        return producto;
    }

    public int getVentanaActual() {
        return ventanaActual;
    }
    public void setVentanaActual(int ventanaActual) {
        this.ventanaActual = ventanaActual;
    }

    public int getPosSeccion() {
        return posSeccion;
    }
    public void setPosSeccion(int posSeccion) {
        this.posSeccion = posSeccion;
    }

    public int getPosActProd() {
        return posActProd;
    }

    public void setPosActProd(int posActProd) {
        this.posActProd = posActProd;
    }

    public int getIdSeccionActual() {
        return idSeccionActual;
    }

    public void setIdSeccionActual(int idSeccionActual) {
        this.idSeccionActual = idSeccionActual;
    }
}
