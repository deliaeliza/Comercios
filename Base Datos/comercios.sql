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

ALTER TABLE `comercioscr`.`Usuarios` ADD CONSTRAINT `usuario_ck_tipo` CHECK(tipo in(0, 1, 2, 3));

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
	SET idE = (SELECT COUNT(id) FROM comercioscr.Usuarios) + 1;
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
	DECLARE idA INT;
	SET idA = (SELECT COUNT(id) FROM comercioscr.Usuarios) + 1;
	INSERT INTO comercioscr.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idA, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO comercioscr.Administradores(idUsuario, telefono) VALUES (idA, Ptelefono);
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
  INDEX `seccion_fk_idComercio_idx` (`idComercio`),
  CONSTRAINT `seccion_fk_idComercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `comercioscr`.`Comercios` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

DELIMITER //
CREATE PROCEDURE PAregistrarComercio(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), 
IN Pcontrasena VARCHAR(45), IN Ptelefono BIGINT, IN Pdescripcion VARCHAR(500), IN Pubicacion VARCHAR(200), IN Pcategoria INT)
BEGIN
	DECLARE idC INT;
	SET idC = (SELECT COUNT(id) FROM comercioscr.Usuarios) + 1;
	INSERT INTO comercioscr.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idC, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO comercioscr.Comercio(idUsuario, telefono, verificado, descripcion, ubicacion, categoria) VALUES (idC, Ptelefono, FALSE, Pdescripcion, Pubicacion, Pcategoria);
    INSERT INTO comercioscr.Secciones(idComercio, nombre) values (idC, 'DEFAULT');

    END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `comercioscr`.`Productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercioscr`.`Productos` (
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
	IF(cantidad >= 10) THEN 
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
  CONSTRAINT `seccion_fk_producto`
    FOREIGN KEY (`idSeccion`)
    REFERENCES `comercioscr`.`Secciones` (`id`),
  CONSTRAINT `producto_fk_seccion`
    FOREIGN KEY (`idProducto`)
    REFERENCES `comercioscr`.`Productos` (`id`))
ENGINE = InnoDB;

DELIMITER //
CREATE PROCEDURE PAborrarSeccion(IN Pid INT)
BEGIN
    DECLARE nombreSec VARCHAR(50);
    DECLARE idDefault INT;
	SELECT nombre into nombreSec FROM Secciones WHERE id = Pid;
    IF(nombreSec <> 'DEFAULT') THEN
        DELETE FROM SeccionesProductos WHERE idSeccion = Pid;
        DELETE FROM Secciones WHERE id = Pid;
    ELSE
        SIGNAL SQLSTATE '45000' SET message_text = 'La seccion elegida no se puede borrar';
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER TborrarSeccion BEFORE DELETE ON comercioscr.SeccionesProductos FOR EACH ROW
BEGIN
	DECLARE cantidad INT;
    DECLARE idDefault INT;
	SELECT COUNT(id) INTO cantidad FROM comercioscr.SeccionesProductos WHERE comercioscr.SeccionesProductos.idProducto = NEW.idProducto;
	IF(cantidad <= 1) THEN 
        SELECT id INTO idDefault FROM SeccionesProductos WHERE nombre='DEFAULT';
		INSERT INTO SeccionesProductos(idSeccion, idProducto) VALUES (idDefault, NEW.idProducto);
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


CALL PAregistrarAdministrador(1, 'jonathanvasquez@gmail.com', 'Jonathan', '123Jonathan', 0050685502508);
CALL PAregistrarAdministrador(1, 'deliaeliza72@gmail.com', 'Elizabeth', '123Delia', 0050689583027);
CALL PAregistrarAdministrador(1, 'angelocalderon.17@hotmail.com', 'Angelo', '123Angelo', 0050686096895);

CALL PAregistrarComercio();
CALL PAregistrarComercio();
CALL PAregistrarComercio();
