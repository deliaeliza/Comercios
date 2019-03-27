-- MySQL Workbench Forward Engineering

-- -----------------------------------------------------
-- Schema comercios
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `comercios` ;

-- -----------------------------------------------------
-- Schema comercios
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `comercios` DEFAULT CHARACTER SET utf8 ;
USE `comercios` ;

-- -----------------------------------------------------
-- Table `comercios`.`Usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercios`.`Usuarios` (
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
ALTER TABLE `comercios`.`Usuarios` ADD CONSTRAINT `usuario_ck_tipo` CHECK(tipo in(0, 1, 2, 3));
DELIMITER //
CREATE TRIGGER TcontrasenaActualizada BEFORE UPDATE on `comercios`.`Usuarios` FOR EACH ROW
BEGIN
	DECLARE contrasenaActual VARCHAR(45);
	SELECT contrasena INTO contrasenaActual FROM comercios.Usuarios WHERE comercios.Usuarios.id = NEW.id;
	IF(contrasenaActual <> NEW.contrasena) THEN 
		UPDATE comercios.Usuarios SET codigoRestablecer = NULL WHERE comercios.Usuarios.id = NEW.id;
	END IF;
END;
//
DELIMITER ;
-- -----------------------------------------------------
-- Table `comercios`.`Comercio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercios`.`Comercio` (
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
    REFERENCES `comercios`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

DELIMITER //
CREATE PROCEDURE PAregistrarComercio(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), 
IN Pcontrasena VARCHAR(45), IN Ptelefono INT, IN Pdescripcion VARCHAR(500), IN Pubicacion VARCHAR(200), IN Pcategoria VARCHAR(50))
BEGIN
	DECLARE idC INT;
	SET idC = (SELECT COUNT(id) FROM comercios.Usuarios) + 1;
	INSERT INTO comercios.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idC, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO comercios.Comercio(idUsuario, telefono, verificado, descripcion, ubicacion, categoria) VALUES (idC, Ptelefono, FALSE, Pdescripcion, Pubicacion, Pcategoria);
END;
//
DELIMITER ;
-- -----------------------------------------------------
-- Table `comercios`.`UsuariosEstandar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercios`.`UsuariosEstandar` (
  `idUsuario` INT NOT NULL,
  `fechaNac` DATE NOT NULL,
  INDEX `estandar_fk_id_idx` (`idUsuario`) ,
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario`),
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `estandar_fk_id`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `comercios`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
DELIMITER //
CREATE PROCEDURE PAregistrarUsuarioEstandar(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), IN Pcontrasena VARCHAR(45), IN PfechaNac DATE)
BEGIN
	DECLARE idE INT;
	SET idE = (SELECT COUNT(id) FROM comercios.Usuarios) + 1;
	INSERT INTO comercios.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idE, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO comercios.UsuariosEstandar(idUsuario, fechaNac) VALUES (idE, PfechaNac);
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `comercios`.`Administradores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercios`.`Administradores` (
  `idUsuario` INT NOT NULL,
  `telefono` INT NOT NULL,
  UNIQUE INDEX `telefono_UNIQUE` (`telefono`),
  INDEX `admin_fk_id_idx` (`idUsuario`),
  UNIQUE INDEX `idUsuario_UNIQUE` (`idUsuario`),
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `admin_fk_id`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `comercios`.`Usuarios` (`id`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
DELIMITER //
CREATE PROCEDURE PAregistrarAdministrador(IN Ptipo TINYINT(5), IN Pcorreo VARCHAR(45), IN Pusuario VARCHAR(45), IN Pcontrasena VARCHAR(45), IN Ptelefono INT)
BEGIN
	DECLARE idA INT;
	SET idA = (SELECT COUNT(id) FROM comercios.Usuarios) + 1;
	INSERT INTO comercios.Usuarios(id, tipo, correo, usuario, contrasena, estado) VALUES (idA, Ptipo, Pcorreo, Pusuario, Pcontrasena, TRUE);
	INSERT INTO comercios.Administradores(idUsuario, telefono) VALUES (idA, Ptelefono);
END;
//
DELIMITER ;

-- -----------------------------------------------------
-- Table `comercios`.`Calificaciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercios`.`Calificaciones` (
  `idComercio` INT NOT NULL,
  `idUsuarioE` INT NOT NULL,
  `calificacion` TINYINT(10) NOT NULL,
  INDEX `calificacion_fk_idTienda_idx` (`idComercio`),
  INDEX `calificacion_fk_idUsuario_idx` (`idUsuarioE`),
  CONSTRAINT `calificacion_fk_idComercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `comercios`.`Comercio` (`idUsuario`)
    ON UPDATE NO ACTION,
  CONSTRAINT `calificacion_fk_idUsuario`
    FOREIGN KEY (`idUsuarioE`)
    REFERENCES `comercios`.`UsuariosEstandar` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercios`.`Parametros`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercios`.`Parametros` (
  `id` INT NOT NULL,
  `descripcion` VARCHAR(50) NOT NULL,
  `tipo` TINYINT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercios`.`Secciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercios`.`Secciones` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idComercio` INT NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `seccion_fk_idComercio_idx` (`idComercio`),
  CONSTRAINT `seccion_fk_idComercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `comercios`.`Comercio` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercios`.`Productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercios`.`Productos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idComercio` INT NOT NULL,
  `descripcion` VARCHAR(80) NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `producto_fk_comercio_idx` (`idComercio`),
  CONSTRAINT `producto_fk_comercio`
    FOREIGN KEY (`idComercio`)
    REFERENCES `comercios`.`Comercio` (`idUsuario`)
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercios`.`ProductoImagenes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercios`.`ProductoImagenes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idProducto` INT NOT NULL,
  `imagen` MEDIUMBLOB NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `imagenes_fk_producto_idx` (`idProducto`),
  CONSTRAINT `imagenes_fk_producto`
    FOREIGN KEY (`idProducto`)
    REFERENCES `comercios`.`Productos` (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercios`.`SeccionesProductos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comercios`.`SeccionesProductos` (
  `idSeccion` INT NOT NULL,
  `idProducto` INT NOT NULL,
  INDEX `seccion_fk_producto_idx` (`idSeccion`),
  INDEX `producto_fk_seccion_idx` (`idProducto`),
  CONSTRAINT `seccion_fk_producto`
    FOREIGN KEY (`idSeccion`)
    REFERENCES `comercios`.`Secciones` (`id`),
  CONSTRAINT `producto_fk_seccion`
    FOREIGN KEY (`idProducto`)
    REFERENCES `comercios`.`Productos` (`id`))
ENGINE = InnoDB;



