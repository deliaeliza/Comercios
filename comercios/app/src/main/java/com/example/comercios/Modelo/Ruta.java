package com.example.comercios.Modelo;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Ruta {

    private HashMap<String,Integer> distancia;
    private HashMap<String,Integer> duracion;
    private LatLng partida;
    private LatLng llegada;

    public Ruta(HashMap<String, Integer> distancia, HashMap<String, Integer> duracion, LatLng llegada, LatLng partida) {
        this.distancia = distancia;
        this.duracion = duracion;
        this.partida = partida;
        this.llegada = llegada;
    }

    public HashMap<String, Integer> getDistancia() {
        return distancia;
    }

    public void setDistancia(HashMap<String, Integer> distancia) {
        this.distancia = distancia;
    }

    public HashMap<String, Integer> getDuracion() {
        return duracion;
    }

    public void setDuracion(HashMap<String, Integer> duracion) {
        this.duracion = duracion;
    }

    public LatLng getPartida() {
        return partida;
    }

    public void setPartida(LatLng partida) {
        this.partida = partida;
    }

    public LatLng getLlegada() {
        return llegada;
    }

    public void setLlegada(LatLng llegada) {
        this.llegada = llegada;
    }

}
