-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema MesaFlow
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `MesaFlow` ;

-- -----------------------------------------------------
-- Schema MesaFlow
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `MesaFlow` DEFAULT CHARACTER SET utf8 ;
USE `MesaFlow` ;

-- -----------------------------------------------------
-- Table `usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `usuario` ;

CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `rol` VARCHAR(45) NOT NULL,
  `activo` TINYINT NOT NULL,
  `fecha_creacion` DATETIME NOT NULL,
  PRIMARY KEY (`id_usuario`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `email_UNIQUE` ON `usuario` (`email`);


-- -----------------------------------------------------
-- Table `establecimiento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `establecimiento` ;

CREATE TABLE IF NOT EXISTS `establecimiento` (
  `id_establecimiento` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `tipo` VARCHAR(45) NOT NULL,
  `direccion` VARCHAR(45) NOT NULL,
  `telefono` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(45) NOT NULL,
  `permite_reserva` TINYINT NOT NULL,
  `activo` TINYINT NOT NULL,
  `fecha_creacion` DATETIME NOT NULL,
  PRIMARY KEY (`id_establecimiento`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mesa`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mesa` ;

CREATE TABLE IF NOT EXISTS `mesa` (
  `id_mesa` INT NOT NULL AUTO_INCREMENT,
  `codigo` VARCHAR(45) NOT NULL,
  `capacidad` INT NOT NULL,
  `agrupable` TINYINT NOT NULL,
  `activa` TINYINT NOT NULL,
  `id_establecimiento` INT NOT NULL,
  PRIMARY KEY (`id_mesa`),
  CONSTRAINT `fk_mesa_establecimiento1`
    FOREIGN KEY (`id_establecimiento`)
    REFERENCES `establecimiento` (`id_establecimiento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reserva`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reserva` ;

CREATE TABLE IF NOT EXISTS `reserva` (
  `id_reserva` INT NOT NULL AUTO_INCREMENT,
  `fecha_hora_inicio` DATETIME NOT NULL,
  `fecha_hora_fin_calculada` DATETIME NOT NULL,
  `comensales` INT NOT NULL,
  `estado` TINYINT NOT NULL,
  `fecha_creacion` DATETIME NOT NULL,
  `id_usuario` INT NOT NULL,
  `id_establecimiento` INT NOT NULL,
  PRIMARY KEY (`id_reserva`),
  CONSTRAINT `fk_reserva_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reserva_establecimiento1`
    FOREIGN KEY (`id_establecimiento`)
    REFERENCES `establecimiento` (`id_establecimiento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reserva_configuracion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reserva_configuracion` ;

CREATE TABLE IF NOT EXISTS `reserva_configuracion` (
  `id_reserva_configuracion` INT NOT NULL AUTO_INCREMENT,
  `duracion_reserva_minutos` INT NOT NULL,
  `tiempo_tolerancia_minutos` INT NOT NULL,
  `anticipacion_minima_minutos` INT NOT NULL,
  `anticipacion_maxima_dias` INT NOT NULL,
  `permite_cancelacion` TINYINT NOT NULL,
  `tiempo_limite_cancelacion_minutos` INT NOT NULL,
  `activo` TINYINT NOT NULL,
  `porcentaje_ocupacion_maxima` DECIMAL(5,2) NOT NULL,
  `permite_combinacion_dinamica` TINYINT NOT NULL,
  `id_establecimiento` INT NOT NULL,
  PRIMARY KEY (`id_reserva_configuracion`),
  CONSTRAINT `fk_reserva_configuracion_establecimiento1`
    FOREIGN KEY (`id_establecimiento`)
    REFERENCES `establecimiento` (`id_establecimiento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `usuario_establecimiento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `usuario_establecimiento` ;

CREATE TABLE IF NOT EXISTS `usuario_establecimiento` (
  `id_usuario_establecimiento` INT UNSIGNED NOT NULL,
  `id_usuario` INT NOT NULL,
  `id_establecimiento` INT NOT NULL,
  `rol_establecimiento` VARCHAR(45) NOT NULL,
  `activo` TINYINT NOT NULL,
  PRIMARY KEY (`id_usuario_establecimiento`),
  CONSTRAINT `fk_usuario_establecimiento_usuario1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_establecimiento_establecimiento1`
    FOREIGN KEY (`id_establecimiento`)
    REFERENCES `establecimiento` (`id_establecimiento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reserva_mesa`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reserva_mesa` ;

CREATE TABLE IF NOT EXISTS `reserva_mesa` (
  `id_reserva_mesa` INT NOT NULL AUTO_INCREMENT,
  `id_reserva` INT NOT NULL,
  `id_mesa` INT NOT NULL,
  `mesa_principal` TINYINT NOT NULL,
  PRIMARY KEY (`id_reserva_mesa`),
  CONSTRAINT `fk_reserva_mesa_reserva1`
    FOREIGN KEY (`id_reserva`)
    REFERENCES `reserva` (`id_reserva`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reserva_mesa_mesa1`
    FOREIGN KEY (`id_mesa`)
    REFERENCES `mesa` (`id_mesa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


USE MesaFlow;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE reserva_mesa;
TRUNCATE TABLE reserva;
TRUNCATE TABLE mesa;
TRUNCATE TABLE reserva_configuracion;
TRUNCATE TABLE usuario_establecimiento;
TRUNCATE TABLE establecimiento;
TRUNCATE TABLE usuario;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================
-- USUARIOS
-- =========================

INSERT INTO usuario
(id_usuario, nombre, apellido, email, password, rol, activo, fecha_creacion)
VALUES
(1,'Facundo','Villar','facundo@test.com','123456','CLIENTE',1,NOW()),
(2,'Juan','Perez','juan@test.com','123456','CLIENTE',1,NOW()),
(3,'Ana','Lopez','ana@test.com','123456','CLIENTE',1,NOW()),
(4,'Carlos','Gomez','carlos@test.com','123456','CLIENTE',1,NOW()),
(5,'Maria','Diaz','maria@test.com','123456','CLIENTE',1,NOW()),
(6,'Pedro','Sosa','pedro@test.com','123456','CLIENTE',1,NOW()),
(7,'Lucia','Fernandez','lucia@test.com','123456','CLIENTE',1,NOW()),
(8,'Martin','Suarez','martin@test.com','123456','CLIENTE',1,NOW()),
(9,'Sofia','Romero','sofia@test.com','123456','CLIENTE',1,NOW()),
(10,'Diego','Alvarez','diego@test.com','123456','CLIENTE',1,NOW()),
(11,'Laura','Torres','laura@test.com','123456','CLIENTE',1,NOW()),
(12,'Javier','Latorre','javier@test.com','123456','ADMIN_ESTABLECIMIENTO',1,NOW()),
(13,'Nicolas','Castro','nicolas@test.com','123456','ADMIN_ESTABLECIMIENTO',1,NOW()),
(14,'Valentina','Molina','valentina@test.com','123456','ADMIN_ESTABLECIMIENTO',1,NOW()),
(15,'Tomas','Ruiz','tomas@test.com','123456','MOZO',1,NOW()),
(16,'Camila','Silva','camila@test.com','123456','MOZO',1,NOW()),
(17,'Bruno','Herrera','bruno@test.com','123456','MOZO',1,NOW()),
(18,'Paula','Medina','paula@test.com','123456','CLIENTE',1,NOW()),
(19,'Agustin','Rojas','agustin@test.com','123456','CLIENTE',1,NOW()),
(20,'Florencia','Acosta','florencia@test.com','123456','CLIENTE',1,NOW());

-- =========================
-- ESTABLECIMIENTOS
-- permite_reserva: 1 = sí, 0 = no
-- =========================

INSERT INTO establecimiento
(id_establecimiento, nombre, tipo, direccion, telefono, descripcion, permite_reserva, activo, fecha_creacion)
VALUES
(1,'Bar Central','BAR','Av Corrientes 1234','1111','Bar urbano',1,1,NOW()),
(2,'Cerveceria Norte','CERVECERIA','Av Cabildo 2500','2222','Cerveceria artesanal',1,1,NOW()),
(3,'Cafe Palermo','CAFETERIA','Honduras 4500','3333','Cafe de especialidad',0,1,NOW()),
(4,'Resto Puerto','RESTAURANTE','Puerto Madero 100','4444','Restaurante premium',1,1,NOW()),
(5,'Patio Gourmet','RESTAURANTE','Av Santa Fe 3000','5555','Patio gastronomico',1,1,NOW()),
(6,'La Parrilla del Sur','RESTAURANTE','Av San Juan 900','6666','Parrilla familiar',1,1,NOW()),
(7,'Birra House','CERVECERIA','Av Cordoba 2000','7777','Cerveceria joven',1,1,NOW()),
(8,'Cafe Centro','CAFETERIA','Florida 500','8888','Cafe sin reservas',0,1,NOW()),
(9,'La Esquina','BAR','Defensa 700','9999','Bar barrial',1,1,NOW()),
(10,'Rincon Italiano','RESTAURANTE','Italia 123','1010','Pastas caseras',1,1,NOW()),
(11,'Sushi Flow','RESTAURANTE','Libertador 4000','1112','Sushi resto',1,1,NOW()),
(12,'Burger Point','BAR','Av Rivadavia 6000','1212','Hamburgueseria',1,1,NOW()),
(13,'Cafe Norte','CAFETERIA','Juramento 1700','1313','Cafe pequeno',0,1,NOW()),
(14,'Terraza Beer','CERVECERIA','Av Las Heras 2100','1414','Terraza cervecera',1,1,NOW()),
(15,'Bistro Sur','RESTAURANTE','Chile 800','1515','Bistro moderno',1,1,NOW());

-- =========================
-- USUARIO_ESTABLECIMIENTO
-- =========================

INSERT INTO usuario_establecimiento
(id_usuario_establecimiento, id_usuario, id_establecimiento, rol_establecimiento, activo)
VALUES
(1,12,1,'ADMIN',1),
(2,12,2,'ADMIN',1),
(3,13,4,'ADMIN',1),
(4,13,5,'ADMIN',1),
(5,14,6,'ADMIN',1),
(6,14,7,'ADMIN',1),
(7,12,9,'ADMIN',1),
(8,13,10,'ADMIN',1),
(9,14,11,'ADMIN',1),
(10,12,12,'ADMIN',1),
(11,13,14,'ADMIN',1),
(12,14,15,'ADMIN',1),
(13,15,1,'MOZO',1),
(14,16,2,'MOZO',1),
(15,17,5,'MOZO',1);

-- =========================
-- CONFIGURACION RESERVAS
-- No se carga configuración para establecimientos sin reserva: 3, 8, 13
-- =========================

INSERT INTO reserva_configuracion
(id_reserva_configuracion, duracion_reserva_minutos, tiempo_tolerancia_minutos,
 anticipacion_minima_minutos, anticipacion_maxima_dias, permite_cancelacion,
 tiempo_limite_cancelacion_minutos, activo, porcentaje_ocupacion_maxima,
 permite_combinacion_dinamica, id_establecimiento)
VALUES
(1,90,15,30,30,1,120,1,90.00,1,1),
(2,120,15,60,30,1,180,1,90.00,1,2),
(3,120,15,60,45,1,180,1,85.00,1,4),
(4,120,15,60,45,1,180,1,90.00,1,5),
(5,90,10,30,20,1,120,1,85.00,1,6),
(6,120,15,60,30,1,180,1,90.00,1,7),
(7,90,10,30,20,1,120,1,80.00,1,9),
(8,120,15,60,30,1,180,1,85.00,0,10),
(9,120,15,60,30,1,180,1,90.00,0,11),
(10,90,10,30,15,1,60,1,85.00,1,12),
(11,120,15,60,30,1,180,1,90.00,1,14),
(12,120,15,60,30,1,180,1,85.00,0,15);

-- =========================
-- MESAS
-- agrupable: 1 = puede entrar en combinación dinámica
-- =========================

INSERT INTO mesa
(id_mesa, codigo, capacidad, agrupable, activa, id_establecimiento)
VALUES
-- Establecimiento 1 - Bar Central
(1,'A1',2,1,1,1),(2,'A2',2,1,1,1),(3,'B1',4,1,1,1),(4,'B2',4,1,1,1),

-- Establecimiento 2 - Cerveceria Norte
(5,'C1',2,1,1,2),(6,'C2',2,1,1,2),(7,'C3',2,1,1,2),(8,'D1',4,1,1,2),(9,'D2',4,1,1,2),

-- Establecimiento 3 - Cafe Palermo sin reservas
(10,'CF1',2,1,1,3),(11,'CF2',2,1,1,3),(12,'CF3',4,0,1,3),

-- Establecimiento 4 - Resto Puerto
(13,'R1',2,1,1,4),(14,'R2',2,1,1,4),(15,'R3',4,1,1,4),(16,'R4',4,0,1,4),

-- Establecimiento 5 - Patio Gourmet, caso fuerte
(17,'A1',2,1,1,5),(18,'A2',2,1,1,5),
(19,'B1',2,1,1,5),(20,'B2',2,1,1,5),
(21,'C1',2,1,1,5),(22,'C2',2,1,1,5),
(23,'D1',2,1,1,5),(24,'D2',2,1,1,5),
(25,'F1',2,1,1,5),(26,'F2',2,1,1,5),
(27,'G1',2,1,1,5),(28,'G2',2,1,1,5),
(29,'H1',4,0,1,5),(30,'I1',4,0,1,5),

-- Establecimiento 6
(31,'P1',2,1,1,6),(32,'P2',2,1,1,6),(33,'P3',4,1,1,6),(34,'P4',6,0,1,6),

-- Establecimiento 7
(35,'BH1',2,1,1,7),(36,'BH2',2,1,1,7),(37,'BH3',2,1,1,7),(38,'BH4',4,1,1,7),(39,'BH5',4,1,1,7),

-- Establecimiento 8 sin reservas
(40,'CC1',2,1,1,8),(41,'CC2',2,1,1,8),

-- Establecimiento 9
(42,'E1',2,1,1,9),(43,'E2',2,1,1,9),(44,'E3',4,1,1,9),

-- Establecimiento 10 sin combinación dinámica
(45,'IT1',2,1,1,10),(46,'IT2',4,0,1,10),(47,'IT3',6,0,1,10),

-- Establecimiento 11 sin combinación dinámica
(48,'S1',2,1,1,11),(49,'S2',2,1,1,11),(50,'S3',4,0,1,11),(51,'S4',6,0,1,11),

-- Establecimiento 12
(52,'BP1',2,1,1,12),(53,'BP2',2,1,1,12),(54,'BP3',4,1,1,12),(55,'BP4',4,1,1,12),

-- Establecimiento 13 sin reservas
(56,'CN1',2,1,1,13),(57,'CN2',2,1,1,13),

-- Establecimiento 14
(58,'TB1',2,1,1,14),(59,'TB2',2,1,1,14),(60,'TB3',2,1,1,14),(61,'TB4',4,1,1,14),(62,'TB5',6,0,1,14),

-- Establecimiento 15 sin combinación dinámica
(63,'BS1',2,1,1,15),(64,'BS2',4,0,1,15),(65,'BS3',4,0,1,15),(66,'BS4',6,0,1,15);

-- =========================
-- RESERVAS
-- estado:
-- 0 PENDIENTE
-- 1 CONFIRMADA
-- 2 CANCELADA
-- 3 FINALIZADA
-- 4 NO_SHOW
-- =========================

INSERT INTO reserva
(id_reserva, fecha_hora_inicio, fecha_hora_fin_calculada, comensales, estado, fecha_creacion, id_usuario, id_establecimiento)
VALUES
-- Establecimiento 1 lleno el 2026-07-10 21:00
(1,'2026-07-10 21:00:00','2026-07-10 22:30:00',2,1,NOW(),1,1),
(2,'2026-07-10 21:00:00','2026-07-10 22:30:00',2,1,NOW(),2,1),
(3,'2026-07-10 21:00:00','2026-07-10 22:30:00',4,1,NOW(),3,1),
(4,'2026-07-10 21:00:00','2026-07-10 22:30:00',4,1,NOW(),4,1),

-- Establecimiento 2 lleno el 2026-07-10 21:00
(5,'2026-07-10 21:00:00','2026-07-10 23:00:00',2,1,NOW(),5,2),
(6,'2026-07-10 21:00:00','2026-07-10 23:00:00',2,1,NOW(),6,2),
(7,'2026-07-10 21:00:00','2026-07-10 23:00:00',2,1,NOW(),7,2),
(8,'2026-07-10 21:00:00','2026-07-10 23:00:00',4,1,NOW(),8,2),
(9,'2026-07-10 21:00:00','2026-07-10 23:00:00',4,1,NOW(),9,2),

-- Establecimiento 5 caso de ocupación real
(10,'2026-07-15 20:00:00','2026-07-15 22:00:00',2,1,NOW(),1,5),
(11,'2026-07-15 20:00:00','2026-07-15 22:00:00',3,1,NOW(),2,5),
(12,'2026-07-15 20:00:00','2026-07-15 22:00:00',2,1,NOW(),3,5),
(13,'2026-07-15 20:00:00','2026-07-15 22:00:00',3,1,NOW(),4,5),
(14,'2026-07-15 20:00:00','2026-07-15 22:00:00',8,1,NOW(),5,5),
(15,'2026-07-15 20:00:00','2026-07-15 22:00:00',6,1,NOW(),6,5),
(16,'2026-07-16 20:00:00','2026-07-16 22:00:00',4,0,NOW(),7,5),
(17,'2026-07-17 20:00:00','2026-07-17 22:00:00',4,2,NOW(),8,5),
(18,'2026-07-18 20:00:00','2026-07-18 22:00:00',2,4,NOW(),9,5),
(19,'2026-07-19 20:00:00','2026-07-19 22:00:00',4,3,NOW(),10,5),

-- Reservas varias
(20,'2026-07-12 20:00:00','2026-07-12 22:00:00',4,1,NOW(),11,4),
(21,'2026-07-12 21:00:00','2026-07-12 23:00:00',2,0,NOW(),18,4),
(22,'2026-07-13 20:00:00','2026-07-13 21:30:00',6,1,NOW(),19,6),
(23,'2026-07-13 22:00:00','2026-07-13 23:30:00',4,2,NOW(),20,6),
(24,'2026-07-14 20:00:00','2026-07-14 22:00:00',6,1,NOW(),1,7),
(25,'2026-07-14 21:00:00','2026-07-14 23:00:00',4,0,NOW(),2,7),
(26,'2026-07-15 21:00:00','2026-07-15 22:30:00',2,1,NOW(),3,9),
(27,'2026-07-16 21:00:00','2026-07-16 23:00:00',6,1,NOW(),4,10),
(28,'2026-07-16 21:00:00','2026-07-16 23:00:00',4,1,NOW(),5,11),
(29,'2026-07-17 21:00:00','2026-07-17 22:30:00',4,1,NOW(),6,12),
(30,'2026-07-18 21:00:00','2026-07-18 23:00:00',8,1,NOW(),7,14),
(31,'2026-07-19 21:00:00','2026-07-19 23:00:00',6,0,NOW(),8,15);

-- =========================
-- RESERVA_MESA
-- =========================

INSERT INTO reserva_mesa
(id_reserva, id_mesa, mesa_principal)
VALUES
-- Establecimiento 1 lleno
(1,1,1),
(2,2,1),
(3,3,1),
(4,4,1),

-- Establecimiento 2 lleno
(5,5,1),
(6,6,1),
(7,7,1),
(8,8,1),
(9,9,1),

-- Establecimiento 5 caso fuerte
(10,17,1),              -- A1
(11,23,1),(11,24,0),    -- D1 + D2
(12,25,1),              -- F1
(13,27,1),(13,28,0),    -- G1 + G2
(14,29,1),(14,30,0),    -- H1 + I1
(15,19,1),(15,20,0),(15,21,0), -- B1 + B2 + C1

-- Pendiente/cancelada/finalizada/no show
(16,18,1),(16,22,0),
(17,26,1),(17,28,0),
(18,17,1),
(19,29,1),

-- Varias
(20,15,1),
(21,13,1),
(22,34,1),
(23,33,1),
(24,38,1),(24,39,0),
(25,35,1),(25,36,0),
(26,42,1),
(27,47,1),
(28,50,1),
(29,54,1),
(30,61,1),(30,62,0),
(31,66,1);
