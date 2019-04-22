-- MySQL Workbench Forward Engineering

-- -----------------------------------------------------
-- Schema comercios
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `comercioscr` ;

-- -----------------------------------------------------
-- Schema comercios
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `comercioscr` DEFAULT CHARACTER SET utf8 ;
USE `comercioscr` ;

-- -----------------------------------------------------
-- Table `comercioscr`.`Categorias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`Categorias`(
    `id` INT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `nombre_UNIQUE` (`nombre`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercioscr`.`Usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`Usuarios` (
  `id` INT NOT NULL,
  `tipo` TINYINT(5) NOT NULL,
  `correo` VARCHAR(45) NOT NULL,
  `usuario` VARCHAR(45) NOT NULL,
  `contrasena` VARCHAR(45) NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `correo_UNIQUE` (`correo`))
ENGINE = InnoDB;

ALTER TABLE `comercioscr`.`Usuarios` ADD CONSTRAINT `usuario_ck_tipo` CHECK(tipo in(0, 1, 2, 3));
-- -----------------------------------------------------
-- Table `comercioscr`.`RestablecerPWD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`RestablecerPWD` (
    `idUsuario` INT NOT NULL,
    `codigo`    VARCHAR(45) NOT NULL,
    PRIMARY KEY (`idUsuario`),
    INDEX `retablecer_fk_usuario_idx` (`idUsuario`),
    CONSTRAINT `restablecer_fk_usuarios`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `comercioscr`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

DELIMITER //
CREATE TRIGGER TcontrasenaActualizada BEFORE UPDATE on comercioscr.Usuarios FOR EACH ROW
BEGIN
	DECLARE contrasenaActual VARCHAR(45);
    DECLARE idRestablecer INT;
	SELECT contrasena INTO contrasenaActual FROM comercioscr.Usuarios WHERE comercioscr.Usuarios.id = NEW.id;
    SELECT idUsuario INTO idRestablecer FROM comercioscr.RestablecerPWD WHERE idUsuario = NEW.id;
	IF(contrasenaActual <> NEW.contrasena) AND(idRestablecer IS NOT NULL) THEN
		DELETE FROM comercioscr.RestablecerPWD WHERE idUsuario = NEW.id;
	END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER TusuarioSuper BEFORE INSERT ON comercioscr.Usuarios FOR EACH ROW
BEGIN
	DECLARE cantidad INT;
	IF (NEW.tipo = 0) THEN
		SELECT COUNT(id) INTO cantidad FROM comercioscr.Usuarios WHERE comercioscr.Usuarios.tipo = 0;
		IF(cantidad >= 1) THEN 
			SIGNAL SQLSTATE '45000' SET message_text = 'Ya existe el usuario de tipo super';
		END IF;
	END IF;
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `comercioscr`.`Comercios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`Comercios` (
  `idUsuario` INT NOT NULL,
  `telefono` BIGINT NULL,
  `idCategoria` INT NOT NULL,
  `verificado` TINYINT(1) NOT NULL,
  `descripcion` VARCHAR(500) NOT NULL,
  `ubicacion` VARCHAR(200) NOT NULL,
  `urlImagen` VARCHAR(100) NULL,
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario`),
  UNIQUE INDEX `telefono_UNIQUE` (`telefono`),
  INDEX `comercio_fk_categoria_idx` (`idCategoria`),
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `comercios_fk_id`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `comercioscr`.`Usuarios` (`id`)
    ON UPDATE NO ACTION,
  CONSTRAINT `comercios_fk_cat`
    FOREIGN KEY (`idCategoria`)
    REFERENCES `comercioscr`.`Categorias` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

DELIMITER //
CREATE PROCEDURE PAregistrarComercio(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), 
IN Pcontrasena VARCHAR(45), IN Ptelefono BIGINT, IN Pdescripcion VARCHAR(500), IN Pubicacion VARCHAR(200), IN Pcategoria INT)
BEGIN
	DECLARE idC INT;
	SET idC = (SELECT COALESCE(MAX(id),0) FROM comercioscr.Usuarios) + 1;
	INSERT INTO comercioscr.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idC, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO comercioscr.Comercios(idUsuario, telefono, verificado, descripcion, ubicacion, idCategoria) VALUES (idC, Ptelefono, FALSE, Pdescripcion, Pubicacion, Pcategoria);
    INSERT INTO comercioscr.Secciones(idComercio, nombre) values (idC, 'DEFAULT');
    END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE PAeliminarComercio(IN Pid INT)
BEGIN
	DECLARE termino BOOLEAN DEFAULT FALSE;
    DECLARE idProd INT;
    DECLARE productoImagenes CURSOR FOR select id from Productos where idComercio = Pid;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET termino = TRUE;
    OPEN productoImagenes;
    productos_loop: LOOP
        FETCH productoImagenes INTO idProd;
        IF termino THEN LEAVE productos_loop; END IF; 
		DELETE FROM SeccionesProductos WHERE idProducto = idProd;
		DELETE FROM ProductoImagenes WHERE idProducto = idProd;
    END LOOP productos_loop;
    CLOSE productoImagenes;
	
	DELETE FROM Productos WHERE idComercio = Pid;
    DELETE FROM Secciones WHERE idComercio = Pid;
	DELETE FROM Calificaciones WHERE idComercio = Pid;
	DELETE FROM Comercios WHERE idUsuario = Pid;
	DELETE FROM Usuarios WHERE id = Pid;
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `comercioscr`.`UsuariosEstandar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`UsuariosEstandar` (
  `idUsuario` INT NOT NULL,
  `fechaNac` DATE NOT NULL,
  INDEX `estandar_fk_id_idx` (`idUsuario`),
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario`),
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `estandar_fk_id`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `comercioscr`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
DELIMITER //
CREATE PROCEDURE PAregistrarUsuarioEstandar(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), IN Pcontrasena VARCHAR(45), IN PfechaNac DATE)
BEGIN
	DECLARE idE INT;
	SET idE = (SELECT COALESCE(MAX(id),0) FROM comercioscr.Usuarios) + 1;
	INSERT INTO comercioscr.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idE, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO comercioscr.UsuariosEstandar(idUsuario, fechaNac) VALUES (idE, PfechaNac);
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `comercioscr`.`Administradores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`Administradores` (
  `idUsuario` INT NOT NULL,
  `telefono` BIGINT NOT NULL,
  UNIQUE INDEX `telefono_UNIQUE` (`telefono`),
  INDEX `admin_fk_id_idx` (`idUsuario`),
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario`),
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `admin_fk_id`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `comercioscr`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
DELIMITER //
CREATE PROCEDURE PAregistrarAdministrador(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), IN Pcontrasena VARCHAR(45), IN Ptelefono BIGINT)
BEGIN
	DECLARE idA INT DEFAULT 1;
	SET idA = (SELECT COALESCE(MAX(id),0) FROM comercioscr.Usuarios) + 1;
	INSERT INTO comercioscr.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idA, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO comercioscr.Administradores(idUsuario, telefono) VALUES (idA, Ptelefono);
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE PAeliminarAdministrador(IN Pid INT)
BEGIN
    DELETE FROM Administradores WHERE idUsuario = Pid;
	DELETE FROM Usuarios WHERE id = Pid;
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `comercioscr`.`Calificaciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`Calificaciones` (
  `idComercio` INT NOT NULL,
  `idUsuarioE` INT NOT NULL,
  `calificacion` TINYINT(10) NOT NULL,
  INDEX `calificacion_fk_idTienda_idx` (`idComercio`),
  INDEX `calificacion_fk_idUsuario_idx` (`idUsuarioE`),
  PRIMARY KEY (`idComercio`, `idUsuarioE`),
  CONSTRAINT `calificacion_fk_idComercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `comercioscr`.`Comercios` (`idUsuario`)
    ON UPDATE NO ACTION,
  CONSTRAINT `calificacion_fk_idUsuario`
    FOREIGN KEY (`idUsuarioE`)
    REFERENCES `comercioscr`.`UsuariosEstandar` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

DELIMITER //
CREATE PROCEDURE PAeliminarUsuarioEstandar(IN Pid INT)
BEGIN
    DELETE FROM Calificaciones WHERE idUsuarioE = Pid;
    DELETE FROM UsuariosEstandar WHERE idUsuario = Pid;
	DELETE FROM Usuarios WHERE id = Pid;
END;
//
DELIMITER ;
-- -----------------------------------------------------
-- Table `comercioscr`.`Parametros`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`Parametros` (
  `id` INT NOT NULL,
  `descripcion` VARCHAR(50) NOT NULL,
  `tipo` TINYINT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercioscr`.`Secciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`Secciones` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idComercio` INT NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idComNombre_UNIQUE` (`idComercio`,`nombre`),
  CONSTRAINT `seccion_fk_idComercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `comercioscr`.`Comercios` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `comercioscr`.`Productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`Productos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idComercio` INT NOT NULL,
  `precio`     INT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(200) NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `producto_fk_comercio_idx` (`idComercio`),
  CONSTRAINT `producto_fk_comercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `comercioscr`.`Comercios` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercioscr`.`ProductoImagenes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`ProductoImagenes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idProducto` INT NOT NULL,
  `imagen` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `imagenes_fk_producto_idx` (`idProducto`),
  CONSTRAINT `imagenes_fk_producto`
    FOREIGN KEY (`idProducto`)
    REFERENCES `comercioscr`.`Productos` (`id`))
ENGINE = InnoDB;

DELIMITER //
CREATE TRIGGER TimagenesMaximo BEFORE UPDATE ON comercioscr.ProductoImagenes FOR EACH ROW
BEGIN
	DECLARE cantidad INT;
	SELECT COUNT(id) INTO cantidad FROM comercioscr.ProductoImagenes WHERE comercioscr.ProductoImagenes.idProducto = NEW.idProducto;
	IF(cantidad >= 5) THEN 
		SIGNAL SQLSTATE '45000' SET message_text = 'Ya llego al maximo de imagenes por producto';
	END IF;
END;
//
DELIMITER ;
-- -----------------------------------------------------
-- Table `comercioscr`.`SeccionesProductos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`SeccionesProductos` (
  `idSeccion` INT NOT NULL,
  `idProducto` INT NOT NULL,
  INDEX `seccion_fk_producto_idx` (`idSeccion`),
  INDEX `producto_fk_seccion_idx` (`idProducto`),
  PRIMARY KEY (idSeccion, idProducto),
  CONSTRAINT `seccion_fk_producto`
    FOREIGN KEY (`idSeccion`)
    REFERENCES `comercioscr`.`Secciones` (`id`),
  CONSTRAINT `producto_fk_seccion`
    FOREIGN KEY (`idProducto`)
    REFERENCES `comercioscr`.`Productos` (`id`))
ENGINE = InnoDB;


DELIMITER //
CREATE PROCEDURE PAverificarProductosSinSeccon(IN PidComercio INT)
BEGIN
    DECLARE termino BOOLEAN DEFAULT FALSE;
    DECLARE idProd INT;
    DECLARE idDefault INT;
    DECLARE productosSinSeccion CURSOR FOR SELECT p.id FROM Productos p LEFT OUTER JOIN SeccionesProductos s ON s.idProducto = p.id WHERE s.idProducto IS NULL AND p.idComercio = PidComercio;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET termino = TRUE;
    SELECT id INTO idDefault FROM Secciones WHERE idComercio = PidComercio AND nombre = 'DEFAULT';
    OPEN productosSinSeccion;
    producto_loop: LOOP
        FETCH productosSinSeccion INTO idProd;
        IF termino THEN LEAVE producto_loop; END IF; 
        INSERT INTO SeccionesProductos(idSeccion, idProducto) VALUES (idDefault, idProd);
    END LOOP producto_loop;
    CLOSE productosSinSeccion;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE PAborrarSeccion(IN Pid INT)
BEGIN
    DECLARE nombreSec VARCHAR(50);
    DECLARE idCom INT;
	SELECT nombre into nombreSec FROM Secciones WHERE id = Pid;
    IF(nombreSec <> 'DEFAULT') THEN
        DELETE FROM SeccionesProductos WHERE idSeccion = Pid;
        SELECT idComercio INTO idCom FROM Secciones WHERE id = Pid;      
        DELETE FROM Secciones WHERE id = Pid;
        CALL PAverificarProductosSinSeccon(idCom);
    ELSE
        SIGNAL SQLSTATE '45000' SET message_text = 'La seccion elegida no se puede borrar';
    END IF;
END;
//
DELIMITER ;

-- Dropeo de usuario, si existe

-- NOTA**** Comentar linea 283 la primera vez que corran el script, descomentar luego de la segunda ejecucion.
-- -----------------------------------------------------
-- DROP USER USUARIOcomercioscr;
-- -----------------------------------------------------
-- Creacion de USUARIOCAFP
-- Se crea con la intencion de que solo pueda modificar datos en el alcance del proyecto.
-- -----------------------------------------------------
-- CREATE USER USUARIOcomercioscr IDENTIFIED BY '123USUARIOcomercioscr';

-- -----------------------------------------------------
-- Asignamos permisos a USUARIOCAFP
-- -----------------------------------------------------
-- GRANT ALL PRIVILEGES ON  comercioscr.* to USUARIOcomercioscr;


CALL PAregistrarAdministrador(1, 'jonathanvasquezmora@gmail.com', 'Jonathan', '123Jonathan', 0050685502508);
CALL PAregistrarAdministrador(1, 'deliaeliza72@gmail.com', 'Elizabeth', '123Delia', 0050689583027);
CALL PAregistrarAdministrador(1, 'angelocalderon.17@hotmail.com', 'Angelo', '123Angelo', 0050686096895);

INSERT INTO comercioscr.Categorias(nombre) VALUES ('Restaurante');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Hotel');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Bar');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Cafe');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Ropa');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Tecnologia');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Farmacia');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Deportes');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Musica');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Videojuegos');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Jugueteria');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Ferreteria');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Zapateria');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Libreria');
INSERT INTO comercioscr.Categorias(nombre) VALUES ('Otro');



CALL PAregistrarComercio(2, 'empresa1@gmail.com', 'Empresa1', '123Empresa1', 50685505001, 'Esta es una tienda de tecnologia', 'Heredia', 5);
CALL PAregistrarComercio(2, 'empresa2@gmail.com', 'Empresa2', '123Empresa2', 50850015001, 'Este es un restaurante', 'Puntarenas', 1);
CALL PAregistrarComercio(2, 'empresa3@gmail.com', 'Empresa3', '123Empresa3', 50985508550, 'Esta es una tienda de ropa', 'San jose', 4);

INSERT INTO comercioscr.Secciones(idComercio, nombre) VALUES (4, 'Celulares');
INSERT INTO comercioscr.Secciones(idComercio, nombre) VALUES (4, 'Computadoras');
INSERT INTO comercioscr.Secciones(idComercio, nombre) VALUES (5, 'Postres');
INSERT INTO comercioscr.Secciones(idComercio, nombre) VALUES (5, 'Plato fuerte');

INSERT INTO comercioscr.Productos(idComercio, precio, nombre, descripcion, estado) VALUES (4, 100, 'Computadora', '8 RAM, 1TB', 1);
INSERT INTO comercioscr.Productos(idComercio, precio, nombre, descripcion, estado) VALUES (4, 200, 'Celular', '4 RAM, 128GB', 1);
INSERT INTO comercioscr.Productos(idComercio, precio, nombre, descripcion, estado) VALUES (5, 300, 'Gallo pinto', null, 1);
INSERT INTO comercioscr.Productos(idComercio, precio, nombre, descripcion, estado) VALUES (5, 400, 'Cereal', 'Es del mejor', 1);
INSERT INTO comercioscr.Productos(idComercio, precio, nombre, descripcion, estado) VALUES (6, 500, 'Pantalon volcom', null, 1);
INSERT INTO comercioscr.Productos(idComercio, precio, nombre, descripcion, estado) VALUES (6, 600, 'Camisa quicksilver', null, 1);

INSERT INTO comercioscr.SeccionesProductos(idSeccion,idProducto) VALUES(5,1);
INSERT INTO comercioscr.SeccionesProductos(idSeccion,idProducto) VALUES(5,2);
INSERT INTO comercioscr.SeccionesProductos(idSeccion,idProducto) VALUES(4,2);
INSERT INTO comercioscr.SeccionesProductos(idSeccion,idProducto) VALUES(7,3);
INSERT INTO comercioscr.SeccionesProductos(idSeccion,idProducto) VALUES(3,5);
INSERT INTO comercioscr.SeccionesProductos(idSeccion,idProducto) VALUES(7,4);
INSERT INTO comercioscr.SeccionesProductos(idSeccion,idProducto) VALUES(2,4);
INSERT INTO comercioscr.SeccionesProductos(idSeccion,idProducto) VALUES(2,5);
INSERT INTO comercioscr.SeccionesProductos(idSeccion,idProducto) VALUES(2,6);
INSERT INTO comercioscr.SeccionesProductos(idSeccion,idProducto) VALUES(3,6);

CALL PAregistrarUsuarioEstandar(3, 'correo1@gmail.com', 'usuario1', '123usuario1', CURDATE() - 50000);
CALL PAregistrarUsuarioEstandar(3, 'correo2@gmail.com', 'usuario2', '123usuario2', CURDATE() - 30000);
CALL PAregistrarUsuarioEstandar(3, 'correo3@gmail.com', 'usuario3', '123usuario3', CURDATE() - 20000);
CALL PAregistrarUsuarioEstandar(3, 'correo4@gmail.com', 'usuario4', '123usuario4', CURDATE() - 10000);
CALL PAregistrarUsuarioEstandar(3, 'correo5@gmail.com', 'usuario5', '123usuario5', CURDATE() - 10000);
CALL PAregistrarUsuarioEstandar(3, 'correo6@gmail.com', 'usuario6', '123usuario6', CURDATE() - 70000);
CALL PAregistrarUsuarioEstandar(3, 'correo7@gmail.com', 'usuario7', '123usuario7', CURDATE() - 80000);
CALL PAregistrarUsuarioEstandar(3, 'correo8@gmail.com', 'usuario8', '123usuario8', CURDATE() - 90000);
CALL PAregistrarUsuarioEstandar(3, 'correo9@gmail.com', 'usuario9', '123usuario9', CURDATE() - 500000);
CALL PAregistrarUsuarioEstandar(3, 'correo10@gmail.com', 'usuario10', '123usuario10', CURDATE() - 50000);
CALL PAregistrarUsuarioEstandar(3, 'correo11@gmail.com', 'usuario11', '123usuario11', CURDATE() - 80000);
CALL PAregistrarUsuarioEstandar(3, 'correo12@gmail.com', 'usuario12', '123usuario12', CURDATE() - 100000);
CALL PAregistrarUsuarioEstandar(3, 'correo13@gmail.com', 'usuario13', '123usuario13', CURDATE() - 200000);
CALL PAregistrarUsuarioEstandar(3, 'correo14@gmail.com', 'usuario14', '123usuario14', CURDATE() - 300000);
CALL PAregistrarUsuarioEstandar(3, 'correo15@gmail.com', 'usuario15', '123usuario15', CURDATE() - 400000);
CALL PAregistrarUsuarioEstandar(3, 'correo16@gmail.com', 'usuario16', '123usuario16', CURDATE() - 500000);
CALL PAregistrarUsuarioEstandar(3, 'correo17@gmail.com', 'usuario17', '123usuario17', CURDATE() - 600000);
CALL PAregistrarUsuarioEstandar(3, 'correo18@gmail.com', 'usuario18', '123usuario18', CURDATE() - 700000);
CALL PAregistrarUsuarioEstandar(3, 'correo19@gmail.com', 'usuario19', '123usuario19', CURDATE() - 800000);
CALL PAregistrarUsuarioEstandar(3, 'correo20@gmail.com', 'usuario20', '123usuario20', CURDATE() - 900000);
CALL PAregistrarUsuarioEstandar(3, 'correo21@gmail.com', 'usuario21', '123usuario21', CURDATE() - 1000000);
CALL PAregistrarUsuarioEstandar(3, 'correo22@gmail.com', 'usuario22', '123usuario22', CURDATE() - 2000000);
CALL PAregistrarUsuarioEstandar(3, 'correo23@gmail.com', 'usuario23', '123usuario23', CURDATE() - 3000000);
CALL PAregistrarUsuarioEstandar(3, 'correo24@gmail.com', 'usuario24', '123usuario24', CURDATE() - 4000000);
CALL PAregistrarUsuarioEstandar(3, 'correo25@gmail.com', 'usuario25', '123usuario25', CURDATE() - 5000000);
CALL PAregistrarUsuarioEstandar(3, 'correo26@gmail.com', 'usuario26', '123usuario26', CURDATE() - 6000000);
CALL PAregistrarUsuarioEstandar(3, 'correo27@gmail.com', 'usuario27', '123usuario27', CURDATE() - 7000000);
CALL PAregistrarUsuarioEstandar(3, 'correo28@gmail.com', 'usuario28', '123usuario28', CURDATE() - 8000000);
CALL PAregistrarUsuarioEstandar(3, 'correo29@gmail.com', 'usuario29', '123usuario29', CURDATE() - 9000000);
CALL PAregistrarUsuarioEstandar(3, 'correo30@gmail.com', 'usuario30', '123usuario30', CURDATE() - 1000000);
CALL PAregistrarAdministrador(1, 'adminprueaba@gmail.com', 'Anonimo', '123Anonimo', 896589653215);
CALL PAregistrarAdministrador(1, 'adminprueaba2@gmail.com', 'Anonimo2', '123Anonimo', 896587963215);
CALL PAregistrarAdministrador(1, 'adminprueaba3@gmail.com', 'Anonimo3', '123Anonimo', 89658963225);
CALL PAregistrarAdministrador(1, 'adminprueaba4@gmail.com', 'Anonimo4', '123Anonimo', 89558963213);
CALL PAregistrarAdministrador(1, 'adminprueaba5@gmail.com', 'Anonimo5', '123Anonimo', 89658968214);
CALL PAregistrarAdministrador(1, 'adminprueaba6@gmail.com', 'Anonimo6', '123Anonimo', 89658963216);
CALL PAregistrarAdministrador(1, 'adminprueaba7@gmail.com', 'Anonimo7', '123Anonimo', 89658963217);
CALL PAregistrarAdministrador(1, 'adminprueaba8@gmail.com', 'Anonimo8', '123Anonimo', 89658963218);
CALL PAregistrarAdministrador(1, 'adminprueaba9@gmail.com', 'Anonimo9', '123Anonimo', 896585963219);
CALL PAregistrarAdministrador(1, 'adminprueaba10@gmail.com', 'Anonimo10', '123Anonimo', 89658963210);
CALL PAregistrarAdministrador(1, 'adminprueaba11@gmail.com', 'Anonimo11', '123Anonimo', 89658963211);
CALL PAregistrarAdministrador(1, 'adminprueaba12@gmail.com', 'Anonimo12', '123Anonimo', 89658963212);
CALL PAregistrarAdministrador(1, 'adminprueaba13@gmail.com', 'Anonimo13', '123Anonimo', 89658963213);
CALL PAregistrarAdministrador(1, 'adminprueaba14@gmail.com', 'Anonimo14', '123Anonimo', 89658963214);
CALL PAregistrarAdministrador(1, 'adminprueaba15@gmail.com', 'Anonimo15', '123Anonimo', 89658963816);
CALL PAregistrarAdministrador(1, 'adminprueaba16@gmail.com', 'Anonimo16', '123Anonimo', 89658969217);
CALL PAregistrarAdministrador(1, 'adminprueaba17@gmail.com', 'Anonimo17', '123Anonimo', 89655896215);
CALL PAregistrarAdministrador(1, 'adminprueaba18@gmail.com', 'Anonimo18', '123Anonimo', 89658966215);
CALL PAregistrarAdministrador(1, 'adminprueaba19@gmail.com', 'Anonimo19', '123Anonimo', 89648963215);
CALL PAregistrarAdministrador(0, 'superusuario@gmail.com', 'root', '123Root', 89586321478);
CALL PAregistrarComercio(2, 'esteesuncorreolargo@gmail.com', 'CorreoLargo', '123Largo', 50985508559, 'Esta es una tienda de ropa', 'Guapiles', 4);
