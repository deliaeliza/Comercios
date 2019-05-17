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
    public static final int MAX_IMAGENES_PRODUCTO = 10;
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

    /****************************MODOS DE VIAJE**********************************/
    public static final String  MODO_MANEJANDO = "";
    public static final String  MODO_CAMINANDO = "";
    public static final String  MODO_BICICLETA = "";
    public static final String  MODO_BUS = "";

    /****************************OPCION ESCOGIDA**********************************/
    public static final int OPCION_MANEJANDO = 1;
    public static final int OPCION_CAMINANDO = 2;
    public static final int OPCION_BICICLETA = 3;
    public static final int OPCION_BUS = 4;

}
