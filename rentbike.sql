-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: rentbike
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bikeproduct`
--

DROP TABLE IF EXISTS `bikeproduct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bikeproduct` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bikeId` int(11) NOT NULL,
  `parkingId` int(11) NOT NULL,
  `purchaseDate` date NOT NULL,
  `value` decimal(9,2) NOT NULL,
  `rentPrice` decimal(6,2) NOT NULL,
  `state` enum('available','reserved','broken','decommissioned') NOT NULL DEFAULT 'available',
  `stateDescription` tinytext,
  PRIMARY KEY (`id`),
  KEY `fk_bikelist_bikes1_idx` (`bikeId`),
  KEY `fk_bikeproduct_parking_idx` (`parkingId`),
  CONSTRAINT `fk_bikelist_bikes1` FOREIGN KEY (`bikeId`) REFERENCES `bikes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bikeproduct_parking` FOREIGN KEY (`parkingId`) REFERENCES `parkings` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bikes`
--

DROP TABLE IF EXISTS `bikes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bikes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brandId` int(11) NOT NULL,
  `model` varchar(150) NOT NULL,
  `wheelSize` tinyint(4) NOT NULL,
  `speedCount` tinyint(4) NOT NULL,
  `picture` tinytext,
  `bikeTypeId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `constraint_bike` (`brandId`,`model`),
  KEY `fk_bike_bike_type1_idx` (`bikeTypeId`),
  KEY `fk_bikes_brand_idx` (`brandId`),
  CONSTRAINT `fk_bike_bike_type1` FOREIGN KEY (`bikeTypeId`) REFERENCES `biketype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bikes_brand` FOREIGN KEY (`brandId`) REFERENCES `brands` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biketype`
--

DROP TABLE IF EXISTS `biketype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biketype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(25) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_UNIQUE` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `brands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brand` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `brand_UNIQUE` (`brand`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `bikeProductId` int(11) NOT NULL,
  `startTime` datetime NOT NULL,
  `startParkingId` int(11) NOT NULL,
  `bikeValue` decimal(9,2) NOT NULL,
  `rentPrice` decimal(6,2) NOT NULL,
  `finishParkingId` int(11) DEFAULT NULL,
  `finishTime` datetime DEFAULT NULL,
  `payment` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_user1_idx` (`userId`),
  KEY `fk_order_bike1_idx` (`bikeProductId`),
  KEY `fk_order_start_parking_id_idx` (`startParkingId`),
  KEY `fk_order_finish_parking_id_idx` (`finishParkingId`),
  CONSTRAINT `fk_order_bike1` FOREIGN KEY (`bikeProductId`) REFERENCES `bikeproduct` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_finish_parking_id` FOREIGN KEY (`finishParkingId`) REFERENCES `parkings` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_start_parking_id` FOREIGN KEY (`startParkingId`) REFERENCES `parkings` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_user1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parkings`
--

DROP TABLE IF EXISTS `parkings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parkings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(250) NOT NULL,
  `capacity` int(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `address_UNIQUE` (`address`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `birthday` date NOT NULL,
  `registrationDate` date NOT NULL,
  `role` enum('admin','user') NOT NULL DEFAULT 'user' COMMENT 'role: 1 - admin, 2 - user.',
  `state` enum('active','blocked','deactivated') NOT NULL DEFAULT 'active' COMMENT '0 - active, 1-blocked',
  `creditCard` varchar(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-18 12:02:10
