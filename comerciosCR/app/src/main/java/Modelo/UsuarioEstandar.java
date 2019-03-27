package Modelo;

import java.util.ArrayList;
import java.util.Date;

public class UsuarioEstandar {
    private int id;
    private int tipo;
    private boolean estado;
    private String codigoRestablecer;
    private String correo;
    private String usuario;
    private String contrasena;
    private Date fehcaNacimiento;
    private ArrayList<Calificacion> calificaciones;
}
