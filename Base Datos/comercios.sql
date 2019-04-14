-- MySQL Workbench Forward Engineering

-- -----------------------------------------------------
-- Schema comercios
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `COMERCIOSCR` ;

-- -----------------------------------------------------
-- Schema comercios
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `COMERCIOSCR` DEFAULT CHARACTER SET utf8 ;
USE `COMERCIOSCR` ;

-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`Categorias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`Categorias`(
    `id` INT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `nombre_UNIQUE` (`nombre`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`Usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`Usuarios` (
  `id` INT NOT NULL,
  `tipo` TINYINT(5) NOT NULL,
  `correo` VARCHAR(45) NOT NULL,
  `usuario` VARCHAR(45) NOT NULL,
  `contrasena` VARCHAR(45) NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `correo_UNIQUE` (`correo`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`RestablecerPWD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`RestablecerPWD` (
    `idUsuario` INT NOT NULL,
    `codigo`    VARCHAR(45) NOT NULL,
    PRIMARY KEY (`idUsuario`),
    INDEX `retablecer_fk_usuario_idx` (`idUsuario`),
    CONSTRAINT `restablecer_fk_usuarios`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `COMERCIOSCR`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

ALTER TABLE `COMERCIOSCR`.`Usuarios` ADD CONSTRAINT `usuario_ck_tipo` CHECK(tipo in(0, 1, 2, 3));

DELIMITER //
CREATE TRIGGER TcontrasenaActualizada BEFORE UPDATE on COMERCIOSCR.Usuarios FOR EACH ROW
BEGIN
	DECLARE contrasenaActual VARCHAR(45);
    DECLARE idRestablecer INT;
	SELECT contrasena INTO contrasenaActual FROM COMERCIOSCR.Usuarios WHERE COMERCIOSCR.Usuarios.id = NEW.id;
    SELECT idUsuario INTO idRestablecer FROM COMERCIOSCR.RestablecerPWD WHERE idUsuario = NEW.id;
	IF(contrasenaActual <> NEW.contrasena) AND(idRestablecer IS NOT NULL) THEN
		DELETE FROM COMERCIOSCR.RestablecerPWD WHERE idUsuario = NEW.id;
	END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER TusuarioSuper BEFORE INSERT ON COMERCIOSCR.Usuarios FOR EACH ROW
BEGIN
	DECLARE cantidad INT;
	IF (NEW.tipo = 0) THEN
		SELECT COUNT(id) INTO cantidad FROM COMERCIOSCR.Usuarios WHERE COMERCIOSCR.Usuarios.tipo = 0;
		IF(cantidad >= 1) THEN 
			SIGNAL SQLSTATE '45000' SET message_text = 'Ya existe el usuario de tipo super';
		END IF;
	END IF;
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`Comercios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`Comercios` (
  `idUsuario` INT NOT NULL,
  `telefono` INT NULL,
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
    REFERENCES `COMERCIOSCR`.`Usuarios` (`id`)
    ON UPDATE NO ACTION,
  CONSTRAINT `comercios_fk_cat`
    FOREIGN KEY (`idCategoria`)
    REFERENCES `COMERCIOSCR`.`Categorias` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

DELIMITER //
CREATE PROCEDURE PAregistrarComercio(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), 
IN Pcontrasena VARCHAR(45), IN Ptelefono INT, IN Pdescripcion VARCHAR(500), IN Pubicacion VARCHAR(200), IN Pcategoria VARCHAR(50), IN PUrlImagen VARCHAR(100))
BEGIN
	DECLARE idC INT;
	SET idC = (SELECT COUNT(id) FROM COMERCIOSCR.Usuarios) + 1;
	INSERT INTO COMERCIOSCR.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idC, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO COMERCIOSCR.Comercio(idUsuario, telefono, verificado, descripcion, ubicacion, categoria, urlImagen) VALUES (idC, Ptelefono, FALSE, Pdescripcion, Pubicacion, Pcategoria, PUrlImagen);
END;
//
DELIMITER ;
-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`UsuariosEstandar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`UsuariosEstandar` (
  `idUsuario` INT NOT NULL,
  `fechaNac` DATE NOT NULL,
  INDEX `estandar_fk_id_idx` (`idUsuario`),
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario`),
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `estandar_fk_id`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `COMERCIOSCR`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
DELIMITER //
CREATE PROCEDURE PAregistrarUsuarioEstandar(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), IN Pcontrasena VARCHAR(45), IN PfechaNac DATE)
BEGIN
	DECLARE idE INT;
	SET idE = (SELECT COUNT(id) FROM COMERCIOSCR.Usuarios) + 1;
	INSERT INTO COMERCIOSCR.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idE, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO COMERCIOSCR.UsuariosEstandar(idUsuario, fechaNac) VALUES (idE, PfechaNac);
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`Administradores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`Administradores` (
  `idUsuario` INT NOT NULL,
  `codigoArea` INT NOT NULL,
  `telefono` INT NOT NULL,
  UNIQUE INDEX `telefono_UNIQUE` (`telefono`),
  INDEX `admin_fk_id_idx` (`idUsuario`),
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario`),
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `admin_fk_id`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `COMERCIOSCR`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
DELIMITER //
CREATE PROCEDURE PAregistrarAdministrador(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), IN Pcontrasena VARCHAR(45), IN Ptelefono INT, IN PCodigoArea INT)
BEGIN
	DECLARE idA INT;
	SET idA = (SELECT COUNT(id) FROM COMERCIOSCR.Usuarios) + 1;
	INSERT INTO COMERCIOSCR.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idA, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO COMERCIOSCR.Administradores(idUsuario, telefono, codigoArea) VALUES (idA, Ptelefono, PCodigoArea);
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`Calificaciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`Calificaciones` (
  `idComercio` INT NOT NULL,
  `idUsuarioE` INT NOT NULL,
  `calificacion` TINYINT(10) NOT NULL,
  INDEX `calificacion_fk_idTienda_idx` (`idComercio`),
  INDEX `calificacion_fk_idUsuario_idx` (`idUsuarioE`),
  PRIMARY KEY (`idComercio`, `idUsuarioE`),
  CONSTRAINT `calificacion_fk_idComercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `COMERCIOSCR`.`Comercios` (`idUsuario`)
    ON UPDATE NO ACTION,
  CONSTRAINT `calificacion_fk_idUsuario`
    FOREIGN KEY (`idUsuarioE`)
    REFERENCES `COMERCIOSCR`.`UsuariosEstandar` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`Parametros`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`Parametros` (
  `id` INT NOT NULL,
  `descripcion` VARCHAR(50) NOT NULL,
  `tipo` TINYINT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`Secciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`Secciones` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idComercio` INT NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `seccion_fk_idComercio_idx` (`idComercio`),
  CONSTRAINT `seccion_fk_idComercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `COMERCIOSCR`.`Comercios` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`Productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`Productos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idComercio` INT NOT NULL,
  `precio`     INT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(200) NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `producto_fk_comercio_idx` (`idComercio`),
  CONSTRAINT `producto_fk_comercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `COMERCIOSCR`.`Comercios` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`ProductoImagenes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`ProductoImagenes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idProducto` INT NOT NULL,
  `imagen` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `imagenes_fk_producto_idx` (`idProducto`),
  CONSTRAINT `imagenes_fk_producto`
    FOREIGN KEY (`idProducto`)
    REFERENCES `COMERCIOSCR`.`Productos` (`id`))
ENGINE = InnoDB;

DELIMITER //
CREATE TRIGGER TimagenesMaximo BEFORE UPDATE ON COMERCIOSCR.ProductoImagenes FOR EACH ROW
BEGIN
	DECLARE cantidad INT;
	SELECT COUNT(id) INTO cantidad FROM COMERCIOSCR.ProductoImagenes WHERE COMERCIOSCR.ProductoImagenes.idProducto = NEW.idProducto;
	IF(cantidad >= 10) THEN 
		SIGNAL SQLSTATE '45000' SET message_text = 'Ya llego al maximo de imagenes por producto';
	END IF;
END;
//
DELIMITER ;
-- -----------------------------------------------------
-- Table `COMERCIOSCR`.`SeccionesProductos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `COMERCIOSCR`.`SeccionesProductos` (
  `idSeccion` INT NOT NULL,
  `idProducto` INT NOT NULL,
  INDEX `seccion_fk_producto_idx` (`idSeccion`),
  INDEX `producto_fk_seccion_idx` (`idProducto`),
  CONSTRAINT `seccion_fk_producto`
    FOREIGN KEY (`idSeccion`)
    REFERENCES `COMERCIOSCR`.`Secciones` (`id`),
  CONSTRAINT `producto_fk_seccion`
    FOREIGN KEY (`idProducto`)
    REFERENCES `COMERCIOSCR`.`Productos` (`id`))
ENGINE = InnoDB;

-- Dropeo de usuario, si existe

-- NOTA**** Comentar linea 283 la primera vez que corran el script, descomentar luego de la segunda ejecucion.
-- -----------------------------------------------------
DROP USER USUARIOCOMERCIOSCR;
-- -----------------------------------------------------
-- Creacion de USUARIOCAFP
-- Se crea con la intencion de que solo pueda modificar datos en el alcance del proyecto.
-- -----------------------------------------------------
CREATE USER USUARIOCOMERCIOSCR IDENTIFIED BY '123USUARIOCOMERCIOSCR';

-- -----------------------------------------------------
-- Asignamos permisos a USUARIOCAFP
-- -----------------------------------------------------
GRANT ALL PRIVILEGES ON  COMERCIOSCR.* to USUARIOCOMERCIOSCR;


CALL PAregistrarAdministrador(1, 'jonathanvasquez@gmail.com', 'Jonathan', '123Jonathan', 85602147, 506);

