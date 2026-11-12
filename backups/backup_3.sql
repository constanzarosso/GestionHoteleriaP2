-- MySQL dump 10.13  Distrib 26.7.0, for Win64 (x86_64)
--
-- Host: localhost    Database: hotel
-- ------------------------------------------------------
-- Server version	26.7.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `backups`
--

DROP TABLE IF EXISTS `backups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backups` (
  `id` int NOT NULL,
  `fecha_hora` datetime NOT NULL,
  `nombre_usuario` varchar(20) NOT NULL,
  `nombre_archivo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backups`
--

LOCK TABLES `backups` WRITE;
/*!40000 ALTER TABLE `backups` DISABLE KEYS */;
INSERT INTO `backups` VALUES (1,'2026-11-12 19:23:50','test','backup_1.sql'),(2,'2026-11-12 20:01:11','admin','backup_2.sql');
/*!40000 ALTER TABLE `backups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consumos`
--

DROP TABLE IF EXISTS `consumos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consumos` (
  `id` int NOT NULL,
  `id_ocupacion` int NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `monto` double NOT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_ocupacion` (`id_ocupacion`),
  CONSTRAINT `consumos_ibfk_1` FOREIGN KEY (`id_ocupacion`) REFERENCES `ocupaciones` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consumos`
--

LOCK TABLES `consumos` WRITE;
/*!40000 ALTER TABLE `consumos` DISABLE KEYS */;
/*!40000 ALTER TABLE `consumos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habitaciones`
--

DROP TABLE IF EXISTS `habitaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitaciones` (
  `numero` int NOT NULL,
  `tipo` enum('SIMPLE','DOBLE','SUITE') NOT NULL,
  `piso` int NOT NULL,
  `capacidad` int NOT NULL,
  `precio_base` double NOT NULL,
  `estado` enum('DISPONIBLE','OCUPADA','LIMPIEZA','REPARACION','DESINFECCION','FUERA_DE_SERVICIO') NOT NULL,
  `motivo` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitaciones`
--

LOCK TABLES `habitaciones` WRITE;
/*!40000 ALTER TABLE `habitaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `habitaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habitaciones_dobles`
--

DROP TABLE IF EXISTS `habitaciones_dobles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitaciones_dobles` (
  `numero` int NOT NULL,
  `camas_separadas` tinyint(1) NOT NULL,
  PRIMARY KEY (`numero`),
  CONSTRAINT `habitaciones_dobles_ibfk_1` FOREIGN KEY (`numero`) REFERENCES `habitaciones` (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitaciones_dobles`
--

LOCK TABLES `habitaciones_dobles` WRITE;
/*!40000 ALTER TABLE `habitaciones_dobles` DISABLE KEYS */;
/*!40000 ALTER TABLE `habitaciones_dobles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habitaciones_suites`
--

DROP TABLE IF EXISTS `habitaciones_suites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitaciones_suites` (
  `numero` int NOT NULL,
  `tiene_jacuzzi` tinyint(1) NOT NULL,
  PRIMARY KEY (`numero`),
  CONSTRAINT `habitaciones_suites_ibfk_1` FOREIGN KEY (`numero`) REFERENCES `habitaciones` (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitaciones_suites`
--

LOCK TABLES `habitaciones_suites` WRITE;
/*!40000 ALTER TABLE `habitaciones_suites` DISABLE KEYS */;
/*!40000 ALTER TABLE `habitaciones_suites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ocupaciones`
--

DROP TABLE IF EXISTS `ocupaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ocupaciones` (
  `id` int NOT NULL,
  `id_reserva` int NOT NULL,
  `fecha_check_in` date NOT NULL,
  `fecha_check_out` date DEFAULT NULL,
  `total` double NOT NULL DEFAULT '0',
  `metodo_pago` enum('EFECTIVO','DEBITO','CREDITO','TRANSFERENCIA') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_reserva` (`id_reserva`),
  CONSTRAINT `ocupaciones_ibfk_1` FOREIGN KEY (`id_reserva`) REFERENCES `reservas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ocupaciones`
--

LOCK TABLES `ocupaciones` WRITE;
/*!40000 ALTER TABLE `ocupaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `ocupaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pasajeros`
--

DROP TABLE IF EXISTS `pasajeros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pasajeros` (
  `dni` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `origen` varchar(45) NOT NULL,
  `domicilio_origen` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pasajeros`
--

LOCK TABLES `pasajeros` WRITE;
/*!40000 ALTER TABLE `pasajeros` DISABLE KEYS */;
/*!40000 ALTER TABLE `pasajeros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservas`
--

DROP TABLE IF EXISTS `reservas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservas` (
  `id` int NOT NULL,
  `dni_pasajero` int NOT NULL,
  `numero_habitacion` int NOT NULL,
  `fecha_desde` date NOT NULL,
  `fecha_hasta` date NOT NULL,
  `cantidad_personas` int NOT NULL,
  `estado` enum('PENDIENTE','EN_CURSO','FINALIZADA','CANCELADA') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `dni_pasajero` (`dni_pasajero`),
  KEY `numero_habitacion` (`numero_habitacion`),
  CONSTRAINT `reservas_ibfk_1` FOREIGN KEY (`dni_pasajero`) REFERENCES `pasajeros` (`dni`),
  CONSTRAINT `reservas_ibfk_2` FOREIGN KEY (`numero_habitacion`) REFERENCES `habitaciones` (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservas`
--

LOCK TABLES `reservas` WRITE;
/*!40000 ALTER TABLE `reservas` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_permisos`
--

DROP TABLE IF EXISTS `usuario_permisos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_permisos` (
  `id_usuario` int NOT NULL,
  `permiso` enum('GESTIONAR_USUARIOS','ASIGNAR_PERMISOS','HACER_BACKUP','GESTIONAR_HABITACIONES','CAMBIAR_ESTADO_HABITACION','VER_HABITACIONES','CARGAR_PASAJEROS','GESTIONAR_RESERVAS','HACER_CHECKIN_CHECKOUT','VER_INFORMES','SOLICITAR_RESERVAS','REGISTRAR_CONSUMO') NOT NULL,
  PRIMARY KEY (`id_usuario`,`permiso`),
  CONSTRAINT `usuario_permisos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_permisos`
--

LOCK TABLES `usuario_permisos` WRITE;
/*!40000 ALTER TABLE `usuario_permisos` DISABLE KEYS */;
INSERT INTO `usuario_permisos` VALUES (1,'GESTIONAR_USUARIOS'),(1,'ASIGNAR_PERMISOS'),(1,'HACER_BACKUP'),(1,'GESTIONAR_HABITACIONES'),(1,'CAMBIAR_ESTADO_HABITACION'),(1,'VER_HABITACIONES'),(1,'CARGAR_PASAJEROS'),(1,'GESTIONAR_RESERVAS'),(1,'HACER_CHECKIN_CHECKOUT'),(1,'VER_INFORMES'),(1,'SOLICITAR_RESERVAS'),(1,'REGISTRAR_CONSUMO'),(2,'GESTIONAR_HABITACIONES'),(2,'CAMBIAR_ESTADO_HABITACION'),(2,'VER_HABITACIONES'),(2,'CARGAR_PASAJEROS'),(2,'GESTIONAR_RESERVAS'),(2,'HACER_CHECKIN_CHECKOUT'),(2,'SOLICITAR_RESERVAS'),(2,'REGISTRAR_CONSUMO'),(3,'GESTIONAR_HABITACIONES'),(3,'CAMBIAR_ESTADO_HABITACION'),(3,'VER_HABITACIONES'),(3,'CARGAR_PASAJEROS'),(3,'GESTIONAR_RESERVAS'),(3,'HACER_CHECKIN_CHECKOUT'),(3,'SOLICITAR_RESERVAS'),(3,'REGISTRAR_CONSUMO'),(4,'GESTIONAR_HABITACIONES'),(4,'CAMBIAR_ESTADO_HABITACION'),(4,'VER_HABITACIONES'),(4,'CARGAR_PASAJEROS'),(4,'GESTIONAR_RESERVAS'),(4,'HACER_CHECKIN_CHECKOUT'),(4,'SOLICITAR_RESERVAS'),(4,'REGISTRAR_CONSUMO'),(5,'GESTIONAR_USUARIOS'),(5,'ASIGNAR_PERMISOS'),(5,'HACER_BACKUP'),(5,'GESTIONAR_HABITACIONES'),(5,'CAMBIAR_ESTADO_HABITACION'),(5,'VER_HABITACIONES'),(5,'CARGAR_PASAJEROS'),(5,'GESTIONAR_RESERVAS'),(5,'HACER_CHECKIN_CHECKOUT'),(5,'VER_INFORMES'),(5,'SOLICITAR_RESERVAS'),(5,'REGISTRAR_CONSUMO');
/*!40000 ALTER TABLE `usuario_permisos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL,
  `nombre_usuario` varchar(40) NOT NULL,
  `contrasenia` varchar(255) NOT NULL,
  `tipo` enum('ADMINISTRADOR','RECEPCIONISTA','PASAJERO') NOT NULL,
  `activo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_usuario` (`nombre_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'admin','Admin123!','ADMINISTRADOR',1),(2,'recepcion','Recepcion123!','RECEPCIONISTA',1),(3,'marcos','Marcos123!','RECEPCIONISTA',1),(4,'pablo','Pablo123!','RECEPCIONISTA',0),(5,'coty','Coty123asd!','ADMINISTRADOR',1);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-12 20:14:25
