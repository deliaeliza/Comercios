package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Modelo.Administrador;

public class ConjuntoAdministradores {
    private static final String CMD_AGREGAR_ADMIN = "{call COMERCIOS_CR.PAregistrarAdministrador(?,?,?,?,?)}";
    private static final String CMD_MODIFICAR_ADMIN = "UPDATE COMERCIOS_CR.Usuarios u, COMERCIOS_CR.Administradores a SET u.codigoRestablecer = ?, u.correo = ?, u.usuario = ?, u.contrasena = ?, u.estado = ?, a.telefono WHERE u.id = a.idUsuario AND u.id = ?;";
    private static final String CMD_RECUPERAR_ADMIN = "SELECT u.id, u.tipo, u.codigoRestablecer, u.correo, u.usuario, u.contrasena, u.estado, a.telefono FROM COMERCIOS_CR.Usuarios u, COMERCIOS_CR.Administradores a WHERE u.id = a.idUsuario AND u.id = ?";

    private static ConjuntoAdministradores instancia = null;

    public ConjuntoAdministradores() {
    }
    public static ConjuntoAdministradores obtenerInstancia() {
        if (instancia == null) {
            instancia = new ConjuntoAdministradores();
        }
        return instancia;
    }

    public boolean agregarAdministrador(Administrador nuevoAdmin) {
        try {
            try (Connection cnx = GestorBD.obtenerInstancia().obtenerConexion();
                 CallableStatement stm = cnx.prepareCall(CMD_AGREGAR_ADMIN)) {
                stm.clearParameters();
                stm.setInt(1, nuevoAdmin.getTipo());
                stm.setString(2, nuevoAdmin.getCorreo());
                stm.setString(3, nuevoAdmin.getUsuario());
                stm.setString(4, nuevoAdmin.getContrasena());
                stm.setInt(5, nuevoAdmin.getTelefono());
                stm.execute();
                return true;
            }
        } catch (SQLException ex) {
            System.err.printf("Exception: '%s'\n", ex.getMessage());
            return false;
        }
    }
    public boolean modificarAdministrador(Administrador administrador){
        try {
            try (Connection cnx = GestorBD.obtenerInstancia().obtenerConexion();
                 PreparedStatement stm = cnx.prepareStatement(CMD_MODIFICAR_ADMIN)) {
                stm.clearParameters();
                stm.setString(1, administrador.getCodigoRestablecer());
                stm.setString(2, administrador.getCorreo());
                stm.setString(3, administrador.getUsuario());
                stm.setString(4, administrador.getContrasena());
                stm.setBoolean(5, administrador.isEstado());
                stm.setInt(6, administrador.getTelefono());
                return stm.executeUpdate() == 1;
            }
        } catch (SQLException ex) {
            System.err.printf("Exception: '%s'\n", ex.getMessage());
            return false;
        }
    }
    public Administrador recuperarAdministrador(int id){
        Administrador admin = null;
        try{
            try (Connection cnx = GestorBD.obtenerInstancia().obtenerConexion();
                 PreparedStatement stm = cnx.prepareStatement(CMD_RECUPERAR_ADMIN)) {
                stm.clearParameters();
                stm.setInt(1, id);
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    admin = new Administrador(
                            rs.getInt("id"),
                            rs.getInt("tipo"),
                            rs.getInt("telefono"),
                            rs.getBoolean("estado"),
                            rs.getString("codigoRestablecer"),
                            rs.getString("correo"),
                            rs.getString("usuario"),
                            rs.getString("constrasena")
                    );
                }
            }
        } catch (SQLException ex) {
            System.err.printf("Excepción: '%s'\n", ex.getMessage());
        }
            return admin;
    }
}
