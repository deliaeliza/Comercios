package com.example.comercios.Modelo;

import java.util.regex.Pattern;

public class Util {
    /*******************************************Tipos de usuarios*******************************************/
    /*******************************************************************************************************/
    public static final int USUARIO_SUPER = 0;
    public static final int USUARIO_ADMINISTRADOR = 1;
    public static final int USUARIO_COMERCIO = 2;
    public static final int USUARIO_ESTANDAR = 3;
    /*******************************************************************************************************/

    /******************************************Esatados de la cuenta*****************************************/
    /*******************************************************************************************************/
    public static final int CUENTA_DESACTIVA = 0;
    public static final int CUENTA_ACTIVA = 1;
    /*******************************************************************************************************/

    /*******************************************Tipos de parametro******************************************/
    /*******************************************************************************************************/
    public static final int PARAMETRO_CATEGORIA = 0;
    /*******************************************************************************************************/

    /*************************************Maximo de imagenes por producto************************************/
    /*******************************************************************************************************/
    public static final int MAX_IMAGENES_PRODUCTO = 5;
    /*******************************************************************************************************/


    /***********************************URL para conectar con el web service*********************************/
    /*******************************************************************************************************/
    public static final String urlWebService = "https://comercioscr.webcindario.com";
    /*******************************************************************************************************/


    //Patron que admite campos vacios, pero verifica que haya al menos un caracter alfanumerico
    /**************************************************************/
    public static final Pattern PATRON_UN_CARACTER_ALFANUMERICO = Pattern.compile(".*[1-9a-zA-ZáéíóúÁÉÍÓÚñÑäëïöüÄËÏÜàèìòùÀÈÌÒÙýÝ].*");
    /**************************************************************/

    /****************************NOMBRE DE LA APP**********************************/
    public static  final String nombreApp = "ComerciosCR";

    /****************************KEY DE GOOGLE MAPS**********************************/
    public static  final String key = "AIzaSyBs6WB83UDRuIcs2fH3DU6pgJweP9ePJko";

    /****************************SERVICIO WEB DE DIRECTIONS**********************************/
    public static  final String URL_API_DIRECTIONS = "https://maps.googleapis.com/maps/api/directions";

    /****************************TAMAÑOS DE IMAGENES**********************************/
    public static  final int IMAGEN_ANCHO = 800;
    public static final int IMAGEN_ALTO = 600;

    /****************************IMAGENES**********************************/
    public static final int MIS_PERMISOS = 100;
    public static final int COD_SELECCIONA = 10;
    public static final int COD_FOTO = 20;

    public static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    public static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    public static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios

}
