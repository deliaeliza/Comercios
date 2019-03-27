package DAO;

public class ConjuntoAdministradores {


    private static ConjuntoAdministradores instancia = null;

    public ConjuntoAdministradores() {
    }
    public static ConjuntoAdministradores obtenerInstancia() {
        if (instancia == null) {
            instancia = new ConjuntoAdministradores();
        }
        return instancia;
    }
}
