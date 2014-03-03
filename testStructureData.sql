CREATE DATABASE  IF NOT EXISTS `groepA` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `groepA`;
-- MySQL dump 10.13  Distrib 5.5.35, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: groepA
-- ------------------------------------------------------
-- Server version	5.5.35-0ubuntu0.13.10.2

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
-- Table structure for table `t_car`
--

DROP TABLE IF EXISTS `t_car`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_car` (
  `carId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `consumption` double DEFAULT NULL,
  `fuelType` int(11) DEFAULT NULL,
  `pictureURL` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`carId`),
  KEY `FK_5rytyidabbijiq937r65gi4go` (`userId`),
  CONSTRAINT `FK_5rytyidabbijiq937r65gi4go` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_car`
--

LOCK TABLES `t_car` WRITE;
/*!40000 ALTER TABLE `t_car` DISABLE KEYS */;
INSERT INTO `t_car` VALUES (1,'Lamborghini',18.3,2,NULL,'Aventador',1),(2,'Peugeot',7.2,0,NULL,'Partner',2),(3,'Peugeot',7.2,0,NULL,'Partner',3),(4,'Audi',11,0,NULL,'A5',NULL),(5,'Ford',8,2,NULL,'Fiesta',12),(6,'Ford',8,2,NULL,'Fiesta',13),(7,'Opel',11,0,NULL,'Vectra',NULL),(8,'Audi',11,0,NULL,'A5',NULL),(9,'Renault',9.9,0,NULL,'Civic',15),(10,'Renault',9.9,0,NULL,'Civic',15),(11,'Renault',9.9,0,NULL,'Civic',15),(12,'Skoda',10.3,1,NULL,'Sködalike',NULL),(13,'Audi',11,0,'src/main/webapp/carImages/AudiA5414750548.jpg','A5',NULL),(14,'BMW',11,0,NULL,'X9',21),(15,'Renault',9.9,0,NULL,'Civic',21),(16,'Renault',9.9,0,NULL,'Civic',21),(17,'Renault',9.9,0,NULL,'Civic',21),(18,'Audi',10.2,0,NULL,'C4',NULL);
/*!40000 ALTER TABLE `t_car` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_place`
--

DROP TABLE IF EXISTS `t_place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_place` (
  `placeId` int(11) NOT NULL AUTO_INCREMENT,
  `lat` double DEFAULT NULL,
  `lon` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `placeTime` tinyblob,
  PRIMARY KEY (`placeId`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_place`
--

LOCK TABLES `t_place` WRITE;
/*!40000 ALTER TABLE `t_place` DISABLE KEYS */;
INSERT INTO `t_place` VALUES (1,10,20,'RouteHome',NULL),(2,11,20,'RouteWork',NULL),(3,231.98880004882812,132.5668487548828,'Kieldrecht',NULL),(4,431.9898681640625,411.98895263671875,'Zwijndrecht Krijgsbaan',NULL),(5,564.9873046875,342.97137451171875,'Carpoolparking Vrasene',NULL),(6,154.9871368408203,189.98745727539062,'Melsele Dijk',NULL),(7,10,10,'Home',NULL),(8,20,10,'Work',NULL),(9,231.98880004882812,132.5668487548828,'Kieldrecht',NULL),(10,431.9898681640625,411.98895263671875,'Zwijndrecht Krijgsbaan',NULL),(11,564.9873046875,342.97137451171875,'Carpoolparking Vrasene',NULL),(12,154.9871368408203,189.98745727539062,'Melsele Dijk',NULL),(13,10,20,'RouteAHome',NULL),(14,11,20,'RouteAWork',NULL),(15,10,20,'RouteBHome',NULL),(16,11,20,'RouteBWork',NULL),(17,10,20,'RouteCHome',NULL),(18,11,20,'RouteCWork',NULL),(19,10,20,'RouteCHome',NULL),(20,11,20,'RouteCWork',NULL),(21,10,20,'UserHome',NULL),(22,11,20,'UserWork',NULL),(23,10,20,'UserHome',NULL),(24,11,20,'UserWork',NULL),(25,9,18,'OtherUserHome',NULL),(26,9,18,'OtherUserHome',NULL),(27,9,20,'OtherUserWork',NULL),(28,9,10,'TrajContTestHome',NULL),(29,11.222,0,'TrajContTestWork',NULL),(30,10,9,'TrajContTestHome',NULL),(31,8,11.4,'TrajContTestWork',NULL);
/*!40000 ALTER TABLE `t_place` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_placetime`
--

DROP TABLE IF EXISTS `t_placetime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_placetime` (
  `placetimeId` int(11) NOT NULL AUTO_INCREMENT,
  `time` time DEFAULT NULL,
  `placeId` int(11) DEFAULT NULL,
  `routeId` int(11) DEFAULT NULL,
  `trajectId` int(11) DEFAULT NULL,
  `weekdayRouteId` int(11) DEFAULT NULL,
  PRIMARY KEY (`placetimeId`),
  KEY `FK_jvcdxdqbcyewowuwi5vh6pjkf` (`placeId`),
  KEY `FK_9pn673sj6a63iu76e0hrscj9o` (`routeId`),
  KEY `FK_na5s06eldnoiw1djfjr2og74n` (`trajectId`),
  KEY `FK_gsqkf1sih7wlj5mtqim3yy98x` (`weekdayRouteId`),
  CONSTRAINT `FK_gsqkf1sih7wlj5mtqim3yy98x` FOREIGN KEY (`weekdayRouteId`) REFERENCES `t_weekdayroute` (`weekdayrouteId`),
  CONSTRAINT `FK_9pn673sj6a63iu76e0hrscj9o` FOREIGN KEY (`routeId`) REFERENCES `t_route` (`id`),
  CONSTRAINT `FK_jvcdxdqbcyewowuwi5vh6pjkf` FOREIGN KEY (`placeId`) REFERENCES `t_place` (`placeId`),
  CONSTRAINT `FK_na5s06eldnoiw1djfjr2og74n` FOREIGN KEY (`trajectId`) REFERENCES `t_traject` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_placetime`
--

LOCK TABLES `t_placetime` WRITE;
/*!40000 ALTER TABLE `t_placetime` DISABLE KEYS */;
INSERT INTO `t_placetime` VALUES (1,'09:30:00',1,1,1,NULL),(2,'17:30:00',2,1,1,NULL),(3,'09:00:00',3,NULL,NULL,1),(4,'09:10:00',4,NULL,NULL,1),(5,'09:20:00',5,NULL,NULL,1),(6,'09:25:00',6,NULL,NULL,1),(7,'08:00:00',3,NULL,NULL,2),(8,'08:10:00',4,NULL,NULL,2),(9,'08:20:00',5,NULL,NULL,2),(10,'08:25:00',6,NULL,NULL,2),(11,'09:20:00',7,2,2,NULL),(12,'19:20:00',8,2,2,NULL),(13,'09:00:00',9,3,3,NULL),(14,'09:10:00',10,3,NULL,NULL),(15,'09:20:00',11,3,NULL,NULL),(16,'09:25:00',12,3,NULL,NULL),(17,'08:00:00',9,3,NULL,NULL),(18,'08:10:00',10,3,NULL,NULL),(19,'08:20:00',11,3,NULL,NULL),(20,'08:25:00',12,3,3,NULL),(21,'09:30:00',13,4,4,NULL),(22,'17:30:00',14,4,4,NULL),(23,'09:30:00',15,5,5,NULL),(24,'17:30:00',16,5,5,NULL),(25,'09:30:00',17,6,6,NULL),(26,'17:30:00',18,6,6,NULL),(27,'09:30:00',19,7,7,NULL),(28,'17:30:00',20,7,7,NULL),(29,'09:25:00',21,4,8,NULL),(30,'13:30:00',22,4,8,NULL),(33,'10:30:00',25,NULL,NULL,NULL),(34,'10:30:00',26,NULL,10,NULL),(35,'18:45:00',27,NULL,10,NULL),(38,'09:10:00',30,NULL,12,NULL),(39,'10:20:00',31,NULL,12,NULL);
/*!40000 ALTER TABLE `t_placetime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_ride`
--

DROP TABLE IF EXISTS `t_ride`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_ride` (
  `rideId` int(11) NOT NULL AUTO_INCREMENT,
  `distance` double DEFAULT NULL,
  `totalCost` double DEFAULT NULL,
  PRIMARY KEY (`rideId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_ride`
--

LOCK TABLES `t_ride` WRITE;
/*!40000 ALTER TABLE `t_ride` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_ride` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_route`
--

DROP TABLE IF EXISTS `t_route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_route` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `begin_date` datetime DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `repeating` tinyint(1) DEFAULT NULL,
  `carId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `routeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_o1uk5yrlj7pbptj9616qiv6vl` (`carId`),
  KEY `FK_dl2p57ymq27nuc5mxu7g9mfq7` (`userId`),
  KEY `FK_3awlwf9tbh1c2hm5yp2mjt8uy` (`routeId`),
  CONSTRAINT `FK_3awlwf9tbh1c2hm5yp2mjt8uy` FOREIGN KEY (`routeId`) REFERENCES `t_car` (`carId`),
  CONSTRAINT `FK_dl2p57ymq27nuc5mxu7g9mfq7` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`),
  CONSTRAINT `FK_o1uk5yrlj7pbptj9616qiv6vl` FOREIGN KEY (`carId`) REFERENCES `t_car` (`carId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_route`
--

LOCK TABLES `t_route` WRITE;
/*!40000 ALTER TABLE `t_route` DISABLE KEYS */;
INSERT INTO `t_route` VALUES (1,'2014-03-03 15:45:55',69,'2014-03-03 15:45:55',0,1,1,1),(2,'2014-03-03 15:46:41',69,'2014-03-03 15:46:41',1,2,2,NULL),(3,'2014-03-03 15:46:43',69,'2014-03-03 15:46:43',1,3,3,NULL),(4,'2014-10-03 10:25:00',4,'2014-10-03 19:00:00',1,4,7,NULL),(5,'2014-10-03 10:25:00',4,'2014-10-03 19:00:00',1,4,8,NULL),(6,'2014-10-03 10:25:00',4,'2014-10-03 19:00:00',1,4,9,NULL),(7,'2014-10-03 10:25:00',4,'2014-10-03 19:00:00',1,4,10,NULL),(8,'2014-10-03 08:44:00',3,'2015-10-04 09:22:00',0,7,14,NULL);
/*!40000 ALTER TABLE `t_route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_session`
--

DROP TABLE IF EXISTS `t_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `expirationDate` datetime DEFAULT NULL,
  `sessionToken` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_td2jkj3d323m8fprao4wn3bed` (`user_id`),
  CONSTRAINT `FK_td2jkj3d323m8fprao4wn3bed` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_session`
--

LOCK TABLES `t_session` WRITE;
/*!40000 ALTER TABLE `t_session` DISABLE KEYS */;
INSERT INTO `t_session` VALUES (1,'2014-03-04 15:46:45','29dbb177-d65b-46ec-afa1-cee216e9df2c',4),(3,'2014-03-04 15:46:46','581bef25-55ad-4bac-b12d-241e2c8f4ccf',6),(4,'2014-03-04 15:46:51','4ea40743-55b5-4d90-8409-f2927de17541',11),(5,'2014-03-04 15:46:53','b594af17-2c75-4e3e-b6b4-0a526be62d55',12),(6,'2014-03-04 15:46:54','93612c5d-867a-4053-a4b2-0a2d00485e7e',14),(7,'2014-03-04 15:46:57','6211a192-f8b1-4196-bfe8-03b07054891a',16),(8,'2014-03-04 15:46:58','5d179c67-6d7e-4a0a-b5ac-078873cbe8ba',17),(9,'2014-03-04 15:46:58','e4c2d842-7edc-42a7-afae-155d68889a9f',18),(10,'2014-03-04 15:46:58','d560da76-3d8e-49e7-96f4-aa527b9c814b',19),(11,'2014-03-04 15:47:22','a714dd10-57b1-4088-a20a-13f93e241743',22),(12,'2014-03-04 15:47:27','b34a9138-0047-4c20-b0cc-5ef9a6466c39',20);
/*!40000 ALTER TABLE `t_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_traject`
--

DROP TABLE IF EXISTS `t_traject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_traject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isAccepted` tinyint(1) DEFAULT NULL,
  `dropoffId` int(11) DEFAULT NULL,
  `pickupId` int(11) NOT NULL,
  `rideId` int(11) DEFAULT NULL,
  `routeId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_gnw9bkc64556wxl9fphql09tn` (`dropoffId`),
  KEY `FK_ajndv3u8k11yfwmiaaotcpxv0` (`pickupId`),
  KEY `FK_1igehg3judmh8jc12v32ysb63` (`rideId`),
  KEY `FK_ew6t1i1un66maaxl4xabmawj3` (`routeId`),
  KEY `FK_fhq5khy90118wkmefy7racewt` (`userId`),
  CONSTRAINT `FK_fhq5khy90118wkmefy7racewt` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`),
  CONSTRAINT `FK_1igehg3judmh8jc12v32ysb63` FOREIGN KEY (`rideId`) REFERENCES `t_ride` (`rideId`),
  CONSTRAINT `FK_ajndv3u8k11yfwmiaaotcpxv0` FOREIGN KEY (`pickupId`) REFERENCES `t_placetime` (`placetimeId`),
  CONSTRAINT `FK_ew6t1i1un66maaxl4xabmawj3` FOREIGN KEY (`routeId`) REFERENCES `t_route` (`id`),
  CONSTRAINT `FK_gnw9bkc64556wxl9fphql09tn` FOREIGN KEY (`dropoffId`) REFERENCES `t_placetime` (`placetimeId`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_traject`
--

LOCK TABLES `t_traject` WRITE;
/*!40000 ALTER TABLE `t_traject` DISABLE KEYS */;
INSERT INTO `t_traject` VALUES (1,0,2,1,NULL,1,1),(2,0,12,11,NULL,2,2),(3,0,20,13,NULL,3,3),(4,0,22,21,NULL,4,7),(5,0,24,23,NULL,5,8),(6,0,26,25,NULL,6,9),(7,0,28,27,NULL,7,10),(8,0,30,29,NULL,4,7),(10,0,35,34,NULL,7,10),(12,0,39,38,NULL,8,14);
/*!40000 ALTER TABLE `t_traject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `avatarURL` varchar(255) DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `smoker` tinyint(1) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jhib4legehrm4yscx9t3lirqi` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,NULL,'1993-10-20',0,'PJ','0907a61f74e304e8538d84914c99e2c5',0,'gio@degruyter.com'),(2,NULL,'1993-04-12',1,'Tim','b5058238d6ae739867815a4ddf405de3',1,'timv@nroe.yen'),(3,NULL,'1993-04-12',1,'Tim','b5058238d6ae739867815a4ddf405de3',1,'thierryv@nnn.loo'),(4,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'Thierry@test.com'),(5,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'TestUser2@test.com'),(6,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'TestUser3@test.com'),(7,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'user@tt.test.com'),(8,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'user2@tt.test.com'),(9,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'user3@tt.test.com'),(10,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'user4@tt.test.com'),(11,NULL,'1993-01-01',1,'TestUser','2ac9cb7dc02b3c0083eb70898e549b63',0,'username@route.controller.it.example.com'),(12,NULL,'1993-01-01',1,'TestUser','2ac9cb7dc02b3c0083eb70898e549b63',0,'username@addcar.controller.it.example.com'),(13,NULL,'1993-01-01',1,'otherUser','2ac9cb7dc02b3c0083eb70898e549b63',0,'otheruser@car.controller.it.example.com'),(14,NULL,'1993-10-20',0,'username','adbd9581843b090f677a7570ed47f791',0,'user@tcntrl.test.be'),(15,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'profile@test.com'),(16,NULL,'1993-10-20',0,'username','2ac9cb7dc02b3c0083eb70898e549b63',0,'username@lc.test.com'),(17,NULL,'1993-10-20',0,'username','2ac9cb7dc02b3c0083eb70898e549b63',0,'username@cp.test.com'),(18,NULL,'1993-10-03',0,'Test User','2ac9cb7dc02b3c0083eb70898e549b63',1,'username@rc.test.com'),(19,NULL,'1993-10-03',0,'Test User','2ac9cb7dc02b3c0083eb70898e549b63',1,'username2@rc.test.com'),(20,'src/main/webapp/userImages/TestUser400538067.png','1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'Thierry2@test.com'),(21,NULL,'1993-10-20',0,'OneCarUser','adbd9581843b090f677a7570ed47f791',0,'Onecar@test.com'),(22,NULL,'1914-02-08',0,'PJ','d1557bbeb3b6a8d1689728a1afbb3928',1,'pieterjanvdp@gmail.com'),(23,NULL,'1993-10-20',0,'Wimmetje','48df5fcbd46f7beb49840eb03af4c190',0,'test@user.com'),(24,NULL,'1993-05-05',0,'My Name','bb0739bd5ce6d7c38bd15681f6f5b1f5',1,'ihaveacar@cars.car');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_weekdayroute`
--

DROP TABLE IF EXISTS `t_weekdayroute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_weekdayroute` (
  `weekdayrouteId` int(11) NOT NULL AUTO_INCREMENT,
  `day` int(11) DEFAULT NULL,
  `routeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`weekdayrouteId`),
  KEY `FK_p9evtkccgmxmcnxax3kfs84gt` (`routeId`),
  CONSTRAINT `FK_p9evtkccgmxmcnxax3kfs84gt` FOREIGN KEY (`routeId`) REFERENCES `t_route` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_weekdayroute`
--

LOCK TABLES `t_weekdayroute` WRITE;
/*!40000 ALTER TABLE `t_weekdayroute` DISABLE KEYS */;
INSERT INTO `t_weekdayroute` VALUES (1,0,NULL),(2,1,NULL);
/*!40000 ALTER TABLE `t_weekdayroute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'groepA'
--
/*!50003 DROP PROCEDURE IF EXISTS `trajdata` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`groepA`@`%` PROCEDURE `trajdata`()
BEGIN
DECLARE userA, userB, userC, userD, userE, userF int;
DECLARE carE, carF int;
DECLARE routeA, routeB int;
DECLARE placeA, placeB, placeC, placeD, placeE, placeF, placeG, placeH int;
DECLARE wdrA, wdrB, wdrC int;
DECLARE ptA, ptB, ptC, ptD, ptE, ptF, ptG, ptH, ptI, ptJ, ptK, ptL, ptM, ptN int;

 -- Info at https://docs.google.com/document/d/1ryW3y_QeR4dwlXttqmkhcN3DXaX2h7mm9uyE6k9FB1w/edit
 -- Make sure to change "ROLLBACK;" at the end to "COMMIT;"

START TRANSACTION;


INSERT INTO t_user (dateOfBirth, gender, name, password, smoker, username) VALUES
	("1975-07-29", 1, "Benjamin Verdin", "2ac9cb7dc02b3c0083eb70898e549b63", 1, "benjamin.verdin@traj.example.com");
SELECT last_insert_id() INTO userA;
INSERT INTO t_user (dateOfBirth, gender, name, password, smoker, username) VALUES
	("1956-07-10", 0, "Jeanne Wijffels", "2ac9cb7dc02b3c0083eb70898e549b63", 1, "jeanne.wijffels@traj.example.com");
SELECT last_insert_id() INTO userB;
INSERT INTO t_user (dateOfBirth, gender, name, password, smoker, username) VALUES
	("1980-04-24", 0, "Shauni Ordelman", "2ac9cb7dc02b3c0083eb70898e549b63", 0, "shauni.ordelman@traj.example.com");
SELECT last_insert_id() INTO userC;
INSERT INTO t_user (dateOfBirth, gender, name, password, smoker, username) VALUES
	("1983-11-14", 1, "Patrick Verhellen", "2ac9cb7dc02b3c0083eb70898e549b63", 0, "patrick.verhellen@traj.example.com");
SELECT last_insert_id() INTO userD;
INSERT INTO t_user (dateOfBirth, gender, name, password, smoker, username) VALUES
	("1971-09-03", 1, "Ludo van Rosmalen", "2ac9cb7dc02b3c0083eb70898e549b63", 0, "ludo.vanrosmalen@traj.example.com");
SELECT last_insert_id() INTO userE;
INSERT INTO t_user (dateOfBirth, gender, name, password, smoker, username) VALUES
	("1965-03-16", 0, "Kris Breddels", "2ac9cb7dc02b3c0083eb70898e549b63", 0, "kris.breddels@traj.example.com");
SELECT last_insert_id() INTO userF;

INSERT INTO t_car (name, type, fuelType, consumption, userId) VALUES
	("Opel", "Insignia", 0, 8.3, userE),
	("Ford", "Focus", 2, 7.9, userF);

SELECT c.carId INTO carE FROM t_car c WHERE c.userId = userE;
SELECT c.carId INTO carF FROM t_car c WHERE c.userId = userF;

INSERT INTO t_route (begin_date, end_date, capacity, repeating, carId, userId) VALUES
	("2014-02-10", "2014-09-12", 3, 1, carE, userE),
	("2014-02-10", "2014-09-12", 2, 1, carF, userF);

SELECT id INTO routeA FROM t_route WHERE userId = userE;
SELECT id INTO routeB FROM t_route WHERE userId = userF;

INSERT INTO t_place (lat, lon, name) VALUES (51.400110, 4.760710, "N14 163-193, 2320 Hoogstraten, Belgium");
SELECT last_insert_id() INTO placeH;

INSERT INTO t_place (lat, lon, name) VALUES (51.351255, 4.641555, "N115 2-30, 2960 Brecht, Belgium");
SELECT last_insert_id() INTO placeD;

INSERT INTO t_place (lat, lon, name) VALUES (51.208078, 4.442945, "Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium");
SELECT last_insert_id() INTO placeA;

INSERT INTO t_place (lat, lon, name) VALUES (51.192200, 4.420979, "Binnensingel, 2600 Antwerpen, Belgium");
SELECT last_insert_id() INTO placeG;

INSERT INTO t_place (lat, lon, name) VALUES (51.175893, 4.388159, "Boomsesteenweg 174-180, 2610 Antwerpen, Belgium");
SELECT last_insert_id() INTO placeF;

INSERT INTO t_place (lat, lon, name) VALUES (51.029592, 4.488086, "Zwartzustersvest 47-50, 2800 Mechelen, Belgium");
SELECT last_insert_id() INTO placeC;

INSERT INTO t_place (lat, lon, name) VALUES (51.090334, 4.365175, "N177 100-122, 2850 Boom, Belgium");
SELECT last_insert_id() INTO placeE;

INSERT INTO t_place (lat, lon, name) VALUES (50.862557, 4.352118, "Willebroekkaai 35, 1000 Brussel, Belgium");
SELECT last_insert_id() INTO placeB;

INSERT INTO t_weekdayroute (day, routeId) VALUES (weekday(now()), routeA);
SELECT last_insert_id() INTO wdrA;

INSERT INTO t_weekdayroute (day, routeId) VALUES (weekday(now())+2, routeA);
SELECT last_insert_id() INTO wdrB;

INSERT INTO t_weekdayroute (day, routeId) VALUES (weekday(now())+1, routeB);
SELECT last_insert_id() INTO wdrC;

INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("06:45", placeH, routeA, wdrA);
SELECT last_insert_id() INTO ptA;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:03", placeD, routeA, wdrA);
SELECT last_insert_id() INTO ptB;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:17", placeA, routeA, wdrA);
SELECT last_insert_id() INTO ptC;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:33", placeE, routeA, wdrA);
SELECT last_insert_id() INTO ptD;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:55", placeB, routeA, wdrA);
SELECT last_insert_id() INTO ptE;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("06:45", placeH, routeA, wdrB);
SELECT last_insert_id() INTO ptF;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:10", placeA, routeA, wdrB);
SELECT last_insert_id() INTO ptG;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:18", placeF, routeA, wdrB);
SELECT last_insert_id() INTO ptH;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:31", placeE, routeA, wdrB);
SELECT last_insert_id() INTO ptI;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:53", placeB, routeA, wdrB);
SELECT last_insert_id() INTO ptJ;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:30", placeA, routeB, wdrC);
SELECT last_insert_id() INTO ptK;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:36", placeG, routeB, wdrC);
SELECT last_insert_id() INTO ptL;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:41", placeF, routeB, wdrC);
SELECT last_insert_id() INTO ptM;
INSERT INTO t_placetime (time, placeId, routeId, weekdayRouteId) VALUES ("07:56", placeC, routeB, wdrC);
SELECT last_insert_id() INTO ptN;

INSERT INTO t_traject (isAccepted, pickupId, dropoffId, routeId, userId) VALUES 
	(1, ptC, ptE, routeA, userF),
	(1, ptG, ptJ, routeA, userF),
	(1, ptK, ptN, routeB, userF),
	(1, ptA, ptE, routeA, userE),
	(1, ptB, ptD, routeA, userA),
	(1, ptD, ptE, routeA, userB),
	(1, ptF, ptJ, routeA, userE),
	(1, ptI, ptJ, routeA, userB),
	(1, ptH, ptJ, routeA, userC),
	(1, ptM, ptN, routeB, userC),
	(1, ptL, ptN, routeB, userD);

COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-03-03 14:50:57
