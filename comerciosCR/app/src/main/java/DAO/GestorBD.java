package DAO;

import java.sql.Connection;
import java.sql.SQLException;

import cr.ac.database.managers.DBManager;

public class GestorBD {

    private GestorBD() {
        try{
            db = DBManager.getDBManager(DBManager.DB_MGR.MARIADB_SERVER);/*MARIADB_SERVER*/
        } catch(InstantiationException | IllegalAccessException | ClassNotFoundException ex){
            System.err.printf("Excepción: '%s'\n", ex.getMessage());
        }
    }

    public static GestorBD obtenerInstancia(){
        if(instancia == null){
            instancia = new GestorBD();
        }
        return instancia;
    }

    public Connection obtenerConexion() throws SQLException {
        return db.getConnection(BASE_DATOS, USUARIO, CLAVE);
    }

    private static final String BASE_DATOS = "COMERCIOS_CR";
    private static final String USUARIO = "USUARIOCOMERCIOS_CR";
    private static final String CLAVE = "123USUARIOCOMERCIOS_CR";

    private DBManager db = null;
    private static GestorBD instancia = null;
}
