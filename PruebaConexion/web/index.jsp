<%-- 
    Document   : index
    Created on : 27/03/2019, 09:57:45 PM
    Author     : Elizabeth Hernandez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1><%=DAO.ConjuntoAdministradores.obtenerInstancia().recuperarAdministrador(1).getCorreo()%></h1>
    </body>
</html>
