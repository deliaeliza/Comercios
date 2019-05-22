package com.example.comercios.Modelo;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Producto {
    private int id;
    private int precio;
    private boolean estado;
    private String nombre;
    private String descripcion;
    private String[] urlsImagenes;
    private ArrayList<Bitmap> imagenes;
    private boolean pertenece;
    private String urlPrueba;
    private Bitmap imagen = null;
    private Timer timer = null;
    private Handler handler = new Handler();
    private Runnable update;

    public Producto(Producto producto){
        this.id = producto.getId();
        this.precio = producto.getPrecio();
        this.estado = producto.isEstado();
        this.nombre = producto.getNombre();
        this.imagenes = new ArrayList(producto.getImagenes());
        this.urlsImagenes = producto.getUrlsImagenes();
    }

    public Producto(int id, boolean estado, int precio, String nombre, String descripcion, boolean pertenece) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pertenece = pertenece;
    }
    ///Angelo
    public Producto(int id,boolean estado, int precio, String nombre, String descripcion, boolean pertenece, String urlPrueba) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pertenece = pertenece;
        this.urlPrueba = urlPrueba;
    }

    public Producto(int id, boolean estado, int precio, String nombre, String descripcion) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public Producto(int id, boolean estado, int precio, String nombre, String descripcion, String[] urlsImagenes) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlsImagenes = urlsImagenes;
    }

    /*public Producto(int id, boolean estado, int precio, String nombre, String descripcion, String[] urlsImagenes) {
        this.id = id;
        this.estado = estado;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlsImagenes = urlsImagenes;
    }*/

    public Producto(boolean estado, String nombre, String descripcion) {
        this.estado = estado;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getUrlPrueba() {
        return urlPrueba;
    }

    public void setUrlPrueba(String urlPrueba) {
        this.urlPrueba = urlPrueba;
    }

    public int getPrecio() {
        return precio;
    }
    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public boolean isEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String[] getUrlsImagenes() {
        return urlsImagenes;
    }
    public void setUrlsImagenes(String[] urlsImagenes) {
        this.urlsImagenes = urlsImagenes;
    }

    public ArrayList<Bitmap> getImagenes() {
        return imagenes;
    }
    public void setImagenes(ArrayList<Bitmap> imagenes) {
        this.imagenes = imagenes;
    }

    public boolean isPertenece() {
        return pertenece;
    }
    public void setPertenece(boolean pertenece) {
        this.pertenece = pertenece;
    }

    public void agregarImagen(Bitmap imagen){
        if(imagenes == null){
            imagenes = new ArrayList();
        }
        imagenes.add(imagen);
    }

    public Timer getTimer() {
        return timer;
    }
    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Handler getHandler() {
        return handler;
    }
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Runnable getUpdate() {
        return update;
    }
    public void setUpdate(Runnable update) {
        this.update = update;
    }

    public Bitmap getImagen() {
        return imagen;
    }
    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    /*public void setTimer(final ViewPager viewPager){
        if(imagenes != null && imagenes.size() > 1){
            if(timer != null){
                timer.cancel();
                timer.purge();
            }
            final int paginas = imagenes.size();
            handler = new Handler();
            update = new Runnable() {
                int pagActual = 0;
                public void run() {
                    if (pagActual == paginas) {
                        pagActual = 0;
                    }
                    viewPager.setCurrentItem(pagActual++, false);
                }
            };
            timer = new Timer(); //This will create a new Thread
            timer.schedule(new TimerTask() { //task to be scheduled
                @Override
                public void run() {
                    handler.post(update);
                }
            }, 500, 3000);
        }
    }*/
}
