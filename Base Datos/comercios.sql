-- MySQL Workbench Forward Engineering

-- -----------------------------------------------------
-- Schema comercios
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `COMERCIOS_CR` ;

-- -----------------------------------------------------
-- Schema comercios
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `COMERCIOS_CR` DEFAULT CHARACTER SET utf8 ;
USE `COMERCIOS_CR` ;

-- -----------------------------------------------------
-- Table `COMERCIOS_CR`.`Usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOS_CR`.`Usuarios` (
  `id` INT NOT NULL,
  `tipo` TINYINT(5) NOT NULL,
  `codigoRestablecer` VARCHAR(45) NULL,
  `correo` VARCHAR(45) NOT NULL,
  `usuario` VARCHAR(45) NOT NULL,
  `contrasena` VARCHAR(45) NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `correo_UNIQUE` (`correo`))
ENGINE = InnoDB;

ALTER TABLE `COMERCIOS_CR`.`Usuarios` ADD CONSTRAINT `usuario_ck_tipo` CHECK(tipo in(0, 1, 2, 3));

DELIMITER //
CREATE TRIGGER TcontrasenaActualizada BEFORE UPDATE on COMERCIOS_CR.Usuarios FOR EACH ROW
BEGIN
	DECLARE contrasenaActual VARCHAR(45);
	SELECT contrasena INTO contrasenaActual FROM COMERCIOS_CR.Usuarios WHERE COMERCIOS_CR.Usuarios.id = NEW.id;
	IF(contrasenaActual <> NEW.contrasena) THEN 
		UPDATE COMERCIOS_CR.Usuarios SET codigoRestablecer = NULL WHERE COMERCIOS_CR.Usuarios.id = NEW.id;
	END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER TusuarioSuper BEFORE INSERT on COMERCIOS_CR.Usuarios FOR EACH ROW
BEGIN
	DECLARE cantidad INT;
	IF (NEW.tipo = 0) THEN
		SELECT COUNT(id) INTO cantidad FROM COMERCIOS_CR.Usuarios WHERE COMERCIOS_CR.Usuarios.tipo = 0;
		IF(cantidad >= 1) THEN 
			SIGNAL SQLSTATE '45000' SET message_text = 'Ya existe el usuario de tipo super';
		END IF;
	END IF;
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `COMERCIOS_CR`.`Comercios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOS_CR`.`Comercios` (
  `idUsuario` INT NOT NULL,
  `telefono` INT NULL,
  `verificado` TINYINT(1) NOT NULL,
  `descripcion` VARCHAR(500) NOT NULL,
  `ubicacion` VARCHAR(200) NOT NULL,
  `categoria` VARCHAR(50) NOT NULL,
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario`),
  UNIQUE INDEX `telefono_UNIQUE` (`telefono`),
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `comercios_fk_id`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `COMERCIOS_CR`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

DELIMITER //
CREATE PROCEDURE PAregistrarComercio(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), 
IN Pcontrasena VARCHAR(45), IN Ptelefono INT, IN Pdescripcion VARCHAR(500), IN Pubicacion VARCHAR(200), IN Pcategoria VARCHAR(50))
BEGIN
	DECLARE idC INT;
	SET idC = (SELECT COUNT(id) FROM COMERCIOS_CR.Usuarios) + 1;
	INSERT INTO COMERCIOS_CR.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idC, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO COMERCIOS_CR.Comercio(idUsuario, telefono, verificado, descripcion, ubicacion, categoria) VALUES (idC, Ptelefono, FALSE, Pdescripcion, Pubicacion, Pcategoria);
END;
//
DELIMITER ;
-- -----------------------------------------------------
-- Table `COMERCIOS_CR`.`UsuariosEstandar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOS_CR`.`UsuariosEstandar` (
  `idUsuario` INT NOT NULL,
  `fechaNac` DATE NOT NULL,
  INDEX `estandar_fk_id_idx` (`idUsuario`) ,
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario`),
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `estandar_fk_id`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `COMERCIOS_CR`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
DELIMITER //
CREATE PROCEDURE PAregistrarUsuarioEstandar(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), IN Pcontrasena VARCHAR(45), IN PfechaNac DATE)
BEGIN
	DECLARE idE INT;
	SET idE = (SELECT COUNT(id) FROM COMERCIOS_CR.Usuarios) + 1;
	INSERT INTO COMERCIOS_CR.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idE, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO COMERCIOS_CR.UsuariosEstandar(idUsuario, fechaNac) VALUES (idE, PfechaNac);
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `COMERCIOS_CR`.`Administradores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOS_CR`.`Administradores` (
  `idUsuario` INT NOT NULL,
  `telefono` INT NOT NULL,
  UNIQUE INDEX `telefono_UNIQUE` (`telefono`),
  INDEX `admin_fk_id_idx` (`idUsuario`),
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario`),
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `admin_fk_id`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `COMERCIOS_CR`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
DELIMITER //
CREATE PROCEDURE PAregistrarAdministrador(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), IN Pcontrasena VARCHAR(45), IN Ptelefono INT)
BEGIN
	DECLARE idA INT;
	SET idA = (SELECT COUNT(id) FROM COMERCIOS_CR.Usuarios) + 1;
	INSERT INTO COMERCIOS_CR.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idA, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO COMERCIOS_CR.Administradores(idUsuario, telefono) VALUES (idA, Ptelefono);
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `COMERCIOS_CR`.`Calificaciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOS_CR`.`Calificaciones` (
  `idComercio` INT NOT NULL,
  `idUsuarioE` INT NOT NULL,
  `calificacion` TINYINT(10) NOT NULL,
  INDEX `calificacion_fk_idTienda_idx` (`idComercio`),
  INDEX `calificacion_fk_idUsuario_idx` (`idUsuarioE`),
  CONSTRAINT `calificacion_fk_idComercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `COMERCIOS_CR`.`Comercios` (`idUsuario`)
    ON UPDATE NO ACTION,
  CONSTRAINT `calificacion_fk_idUsuario`
    FOREIGN KEY (`idUsuarioE`)
    REFERENCES `COMERCIOS_CR`.`UsuariosEstandar` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `COMERCIOS_CR`.`Parametros`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOS_CR`.`Parametros` (
  `id` INT NOT NULL,
  `descripcion` VARCHAR(50) NOT NULL,
  `tipo` TINYINT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `COMERCIOS_CR`.`Secciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOS_CR`.`Secciones` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idComercio` INT NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `seccion_fk_idComercio_idx` (`idComercio`),
  CONSTRAINT `seccion_fk_idComercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `COMERCIOS_CR`.`Comercios` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `COMERCIOS_CR`.`Productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOS_CR`.`Productos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idComercio` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(100) NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `producto_fk_comercio_idx` (`idComercio`),
  CONSTRAINT `producto_fk_comercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `COMERCIOS_CR`.`Comercios` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `COMERCIOS_CR`.`ProductoImagenes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOS_CR`.`ProductoImagenes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idProducto` INT NOT NULL,
  `imagen` MEDIUMBLOB NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `imagenes_fk_producto_idx` (`idProducto`),
  CONSTRAINT `imagenes_fk_producto`
    FOREIGN KEY (`idProducto`)
    REFERENCES `COMERCIOS_CR`.`Productos` (`id`))
ENGINE = InnoDB;

DELIMITER //
CREATE TRIGGER TimagenesMaximo BEFORE UPDATE on COMERCIOS_CR.ProductoImagenes FOR EACH ROW
BEGIN
	DECLARE cantidad INT;
	SELECT COUNT(id) INTO cantidad FROM COMERCIOS_CR.ProductoImagenes WHERE COMERCIOS_CR.ProductoImagenes.idProducto = NEW.idProducto;
	IF(cantidad >= 10) THEN 
		SIGNAL SQLSTATE '45000' SET message_text = 'Ya llego al maximo de imagenes por producto';
	END IF;
END;
//
DELIMITER ;
-- -----------------------------------------------------
-- Table `COMERCIOS_CR`.`SeccionesProductos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOS_CR`.`SeccionesProductos` (
  `idSeccion` INT NOT NULL,
  `idProducto` INT NOT NULL,
  INDEX `seccion_fk_producto_idx` (`idSeccion`),
  INDEX `producto_fk_seccion_idx` (`idProducto`),
  CONSTRAINT `seccion_fk_producto`
    FOREIGN KEY (`idSeccion`)
    REFERENCES `COMERCIOS_CR`.`Secciones` (`id`),
  CONSTRAINT `producto_fk_seccion`
    FOREIGN KEY (`idProducto`)
    REFERENCES `COMERCIOS_CR`.`Productos` (`id`))
ENGINE = InnoDB;

-- Dropeo de usuario, si existe

-- NOTA**** Comentar linea 231 la primera vez que corran el script, descomentar luego de la segunda ejecucion.
-- -----------------------------------------------------
DROP USER USUARIOCOMERCIOS_CR;
-- -----------------------------------------------------
-- Creacion de USUARIOCAFP
-- Se crea con la intencion de que solo pueda modificar datos en el alcance del proyecto.
-- -----------------------------------------------------
CREATE USER USUARIOCOMERCIOS_CR IDENTIFIED BY '123USUARIOCOMERCIOS_CR';

-- -----------------------------------------------------
-- Asignamos permisos a USUARIOCAFP
-- -----------------------------------------------------
GRANT ALL PRIVILEGES ON  USUARIOCOMERCIOS_CR.* to USUARIOCOMERCIOS_CR;

