-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

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
  `id` INT NOT NULL AUTO_INCREMENT,
  `tipo` TINYINT NOT NULL,
  `codigoRestablecer` VARCHAR(45) NULL,
  `correo` VARCHAR(45) NOT NULL,
  `usuario` VARCHAR(60) NOT NULL,
  `contrasena` VARCHAR(45) NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `correo_UNIQUE` (`correo`))
ENGINE = InnoDB;


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
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


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
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


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
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


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
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `calificacion_fk_idUsuario`
    FOREIGN KEY (`idUsuarioE`)
    REFERENCES `comercios`.`UsuariosEstandar` (`idUsuario`)
    ON DELETE NO ACTION
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
    ON DELETE NO ACTION
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
    ON DELETE NO ACTION
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
    REFERENCES `comercios`.`Productos` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
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
    REFERENCES `comercios`.`Secciones` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `producto_fk_seccion`
    FOREIGN KEY (`idProducto`)
    REFERENCES `comercios`.`Productos` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
