<?php

/* * * mysql hostname ** */
$hostname = 'localhost';
/* * * mysql username ** */
$username = 'root';
/* * * mysql password ** */
$password = '';

$bdname = "COMERCIOS_CR";

$correo = $_REQUEST['email'];
$pass = $_REQUEST['pass'];


function testdb_connect($hostname, $username, $password, $bdname) {
    $dbh = new PDO("mysql:host=$hostname;dbname=$bdname", $username, $password);
    return $dbh;
}

try {
    $dbh = testdb_connect($hostname, $username, $password, $bdname);
    $res = $dbh->query("select usuario from COMERCIOS_CR.Usuarios where correo='$correo' and contrasena='$pass';");
    $datos = array();
    foreach ($res as $row) {
        $datos[] = $row;
    }
    echo json_encode($datos);
} catch (PDOException $e) {
    echo $e->getMessage();
}