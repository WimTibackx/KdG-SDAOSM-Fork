SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE  IF NOT EXISTS `groepA` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `groepA`;

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
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `t_car` WRITE;
/*!40000 ALTER TABLE `t_car` DISABLE KEYS */;
INSERT INTO `t_car` VALUES (1,'Ford',8.3,2,NULL,'Fiesta',1),(2,'Ford',8.3,2,NULL,'Fiesta',2),(3,'Ford',8.3,2,NULL,'Fiesta',3),(4,'Ford',8.3,2,NULL,'Fiesta',4),(5,'Opel',8.6,0,NULL,'Astra',4),(6,'Fiat',7.2,2,NULL,'Panda',5),(7,'BMW',7.2,2,NULL,'X6 - M',8),(8,'Peugeot',7.2,0,NULL,'Partner',11),(9,'Peugeot',7.2,0,NULL,'Partner',12),(10,'Ford',8.3,2,NULL,'Fiesta',13),(11,'Ford',8.3,2,NULL,'Fiesta',14),(12,'Ford',8.3,2,NULL,'Fiesta',18),(13,'Ford',8.1,2,NULL,'Fiesta',20),(14,'Ford',8.1,2,NULL,'Fiesta',21),(15,'Ford',8,2,NULL,'Fiesta',23),(16,'Ford',8,2,NULL,'Fiesta',24),(17,'Audi',11,0,NULL,'A5',NULL),(18,'Renault',9.9,0,NULL,'Civic',26),(19,'Renault',9.9,0,NULL,'Civic',26),(20,'Renault',9.9,0,NULL,'Civic',26),(21,'Skoda',10.3,1,NULL,'Sködalike',NULL),(22,'Audi',11,0,'src/main/webapp/carImages/AudiA5338515744.jpg','A5',NULL),(23,'BMW',11,0,NULL,'X9',35),(24,'Renault',9.9,0,NULL,'Civic',35),(25,'Renault',9.9,0,NULL,'Civic',35),(26,'Renault',9.9,0,NULL,'Civic',35),(27,'Audi',10.2,0,NULL,'C4',NULL),(28,'Opel',8.3,0,NULL,'Insignia',43),(29,'Ford',7.9,2,NULL,'Focus',44);
/*!40000 ALTER TABLE `t_car` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `t_place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_place` (
  `placeId` int(11) NOT NULL AUTO_INCREMENT,
  `lat` double DEFAULT NULL,
  `lon` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`placeId`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `t_place` WRITE;
/*!40000 ALTER TABLE `t_place` DISABLE KEYS */;
INSERT INTO `t_place` VALUES (1,51.3,4.2,'Place1'),(2,51.5,4.4,'Place2'),(3,51.7,4.6,'Place3'),(4,51.3,4.2,'Place1'),(5,51.6,4.5,'Place2B'),(6,51.7,4.6,'Place3'),(7,51.3,4.2,'Place1'),(8,51.5,4.4,'Place2'),(9,51.7,4.6,'Place3'),(10,51.3,4.2,'Place1'),(11,51.5,4.4,'Place2'),(12,51.7,4.6,'Place3'),(13,51.3,4.2,'Place1'),(14,51.5,4.4,'Place2'),(15,51.7,4.6,'Place3'),(16,51.8,4.7,'Place4'),(17,51.3,4.2,'Place1'),(18,51.5,4.4,'Place2'),(19,51.7,4.6,'Place3'),(20,231.98880004882812,132.5668487548828,'Brasschaat'),(21,431.9898681640625,411.98895263671875,'Willekeurige Carpoolparking'),(22,564.9873046875,342.97137451171875,'Rooseveltplaats'),(23,154.9871368408203,189.98745727539062,'Andere plek'),(24,231.98880004882812,132.5668487548828,'Brasschaat'),(25,431.9898681640625,411.98895263671875,'Grote Markt'),(26,564.9873046875,342.97137451171875,'Schouwburg'),(27,154.9871368408203,189.98745727539062,'Groenplaats'),(28,231.98880004882812,132.5668487548828,'Kieldrecht'),(29,431.9898681640625,411.98895263671875,'Zwijndrecht Krijgsbaan'),(30,564.9873046875,342.97137451171875,'Carpoolparking Vrasene'),(31,154.9871368408203,189.98745727539062,'Melsele Dijk'),(32,231.98880004882812,132.5668487548828,'Kieldrecht'),(33,431.9898681640625,411.98895263671875,'Zwijndrecht Krijgsbaan'),(34,564.9873046875,342.97137451171875,'Carpoolparking Vrasene'),(35,154.9871368408203,189.98745727539062,'Melsele Dijk'),(36,51.3,4.2,'Place1'),(37,51.5,4.4,'Place2'),(38,51.7,4.6,'Place3'),(39,51.3,4.2,'Place1'),(40,51.5,4.4,'Place2'),(41,51.7,4.6,'Place3'),(42,51.40011,4.76071,'N14 163-193, 2320 Hoogstraten, Belgium'),(43,51.090334,4.365175,'N177 100-122, 2850 Boom, Belgium'),(44,50.862557,4.352118,'Willebroekkaai 35, 1000 Brussel, Belgium'),(45,51.40011,4.76071,'N14 163-193, 2320 Hoogstraten, Belgium'),(46,51.351255,4.641555,'N115 2-30, 2960 Brecht, Belgium'),(47,51.208078,4.442945,'Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium'),(48,51.40011,4.76071,'N14 163-193, 2320 Hoogstraten, Belgium'),(49,51.351255,4.641555,'N115 2-30, 2960 Brecht, Belgium'),(50,51.208078,4.442945,'Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium'),(51,51.1922,4.420979,'Binnensingel, 2600 Antwerpen, Belgium'),(52,51.175893,4.388159,'Boomsesteenweg 174-180, 2610 Antwerpen, Belgium'),(53,51.029592,4.488086,'Zwartzustersvest 47-50, 2800 Mechelen, Belgium'),(54,51.090334,4.365175,'N177 100-122, 2850 Boom, Belgium'),(55,50.862557,4.352118,'Willebroekkaai 35, 1000 Brussel, Belgium');
/*!40000 ALTER TABLE `t_place` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `t_placetime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_placetime` (
  `placetimeId` int(11) NOT NULL AUTO_INCREMENT,
  `time` time DEFAULT NULL,
  `placeId` int(11) DEFAULT NULL,
  `routeId` int(11) NOT NULL,
  `weekdayRouteId` int(11) DEFAULT NULL,
  PRIMARY KEY (`placetimeId`),
  KEY `FK_jvcdxdqbcyewowuwi5vh6pjkf` (`placeId`),
  KEY `FK_9pn673sj6a63iu76e0hrscj9o` (`routeId`),
  KEY `FK_gsqkf1sih7wlj5mtqim3yy98x` (`weekdayRouteId`),
  CONSTRAINT `FK_gsqkf1sih7wlj5mtqim3yy98x` FOREIGN KEY (`weekdayRouteId`) REFERENCES `t_weekdayroute` (`weekdayrouteId`),
  CONSTRAINT `FK_9pn673sj6a63iu76e0hrscj9o` FOREIGN KEY (`routeId`) REFERENCES `t_route` (`id`),
  CONSTRAINT `FK_jvcdxdqbcyewowuwi5vh6pjkf` FOREIGN KEY (`placeId`) REFERENCES `t_place` (`placeId`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `t_placetime` WRITE;
/*!40000 ALTER TABLE `t_placetime` DISABLE KEYS */;
INSERT INTO `t_placetime` VALUES (1,'10:05:00',1,1,1),(2,'10:15:00',2,1,1),(3,'10:25:00',3,1,1),(4,'10:05:00',1,2,2),(5,'10:15:00',2,2,2),(6,'10:25:00',3,2,2),(7,'10:05:00',4,2,3),(8,'10:20:00',5,2,3),(9,'10:25:00',6,2,3),(10,'10:05:00',7,3,4),(11,'10:15:00',8,3,4),(12,'10:25:00',9,3,4),(13,'10:05:00',10,3,4),(14,'10:15:00',11,3,4),(15,'10:25:00',12,3,4),(16,'10:05:00',7,4,6),(17,'10:05:00',10,4,6),(18,'10:15:00',8,4,6),(19,'10:15:00',11,4,6),(20,'10:25:00',12,4,6),(21,'10:25:00',9,4,6),(22,'10:05:00',13,5,7),(23,'10:15:00',14,5,7),(24,'10:25:00',15,5,7),(25,'10:05:00',13,6,8),(26,'10:15:00',14,6,8),(27,'10:25:00',15,6,8),(28,'10:30:00',16,6,8),(29,'10:05:00',17,7,9),(30,'10:15:00',18,7,9),(31,'10:25:00',19,7,9),(32,'10:05:00',17,8,10),(33,'10:15:00',18,8,10),(34,'10:25:00',19,8,10),(35,'09:00:00',20,9,11),(36,'09:10:00',21,9,11),(37,'09:20:00',22,9,11),(38,'09:25:00',23,9,11),(39,'08:00:00',20,9,12),(40,'08:10:00',21,9,12),(41,'08:20:00',22,9,12),(42,'08:25:00',23,9,12),(43,'09:00:00',24,10,NULL),(44,'09:10:00',25,10,NULL),(45,'09:20:00',26,10,NULL),(46,'09:25:00',27,10,NULL),(47,'08:00:00',24,10,NULL),(48,'08:10:00',25,10,NULL),(49,'08:20:00',26,10,NULL),(50,'08:25:00',27,10,NULL),(51,'09:00:00',28,11,NULL),(52,'09:10:00',29,11,NULL),(53,'09:20:00',30,11,NULL),(54,'09:25:00',31,11,NULL),(55,'08:00:00',28,11,NULL),(56,'08:10:00',29,11,NULL),(57,'08:20:00',30,11,NULL),(58,'08:25:00',31,11,NULL),(59,'09:00:00',32,12,13),(60,'09:10:00',33,12,13),(61,'09:20:00',34,12,13),(62,'09:25:00',35,12,13),(63,'08:00:00',32,12,14),(64,'08:10:00',33,12,14),(65,'08:20:00',34,12,14),(66,'08:25:00',35,12,14),(67,'10:05:00',36,13,15),(68,'10:15:00',37,13,15),(69,'10:25:00',38,13,15),(70,'10:05:00',36,14,16),(71,'10:20:00',38,14,16),(72,'10:05:00',39,15,17),(73,'10:15:00',40,15,17),(74,'10:25:00',41,15,17),(75,'09:05:00',39,16,18),(76,'09:15:00',40,16,18),(77,'10:25:00',41,16,18),(78,'12:45:29',42,17,19),(79,'13:45:29',43,17,19),(80,'14:45:29',44,17,19),(81,'07:45:00',45,19,NULL),(82,'08:03:00',46,19,NULL),(83,'08:17:00',47,19,NULL),(84,'06:45:00',48,20,20),(85,'07:03:00',49,20,20),(86,'07:17:00',50,20,20),(87,'07:33:00',54,20,20),(88,'07:55:00',55,20,20),(89,'06:45:00',48,20,21),(90,'07:10:00',50,20,21),(91,'07:18:00',52,20,21),(92,'07:31:00',54,20,21),(93,'07:53:00',55,20,21),(94,'07:30:00',50,21,22),(95,'07:36:00',51,21,22),(96,'07:41:00',52,21,22),(97,'07:56:00',53,21,22);
/*!40000 ALTER TABLE `t_placetime` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `t_ride`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_ride` (
  `rideId` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `totalCost` double DEFAULT NULL,
  `trajectId` int(11) DEFAULT NULL,
  PRIMARY KEY (`rideId`),
  KEY `FK_3dpj3g1e03l906pqegwt4kp2r` (`trajectId`),
  CONSTRAINT `FK_3dpj3g1e03l906pqegwt4kp2r` FOREIGN KEY (`trajectId`) REFERENCES `t_traject` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `t_ride` WRITE;
/*!40000 ALTER TABLE `t_ride` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_ride` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `t_route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_route` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `begin_date` date DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `t_route` WRITE;
/*!40000 ALTER TABLE `t_route` DISABLE KEYS */;
INSERT INTO `t_route` VALUES (1,'2014-02-14',3,'2014-04-13',1,1,1,NULL),(2,'2014-04-14',3,'2014-05-14',1,1,1,NULL),(3,'2014-02-14',3,'2014-04-13',1,2,2,NULL),(4,'2014-04-14',3,'2014-05-14',1,2,2,NULL),(5,'2014-02-14',3,'2014-04-13',1,3,3,NULL),(6,'2014-04-14',3,'2014-05-14',1,3,3,NULL),(7,'2014-02-14',3,'2014-04-13',1,4,4,NULL),(8,'2014-04-14',5,'2014-05-14',1,5,4,NULL),(9,'2014-03-14',4,'2014-03-14',1,6,5,NULL),(10,'2014-03-14',4,'2014-03-14',0,7,8,NULL),(11,'2014-03-14',69,'2014-03-14',0,8,11,NULL),(12,'2014-03-14',69,'2014-03-14',1,9,12,NULL),(13,'2014-02-14',3,'2014-04-13',1,10,13,NULL),(14,'2014-04-14',3,'2014-05-14',1,10,13,NULL),(15,'2014-02-14',3,'2014-04-13',1,11,14,NULL),(16,'2014-04-14',3,'2014-05-14',1,11,14,NULL),(17,'2014-03-14',2,'2014-03-15',1,12,18,NULL),(18,'2014-02-19',3,'2014-02-27',1,1,20,NULL),(19,'2014-03-16',2,'2014-03-16',0,14,21,NULL),(20,'2014-02-10',3,'2014-09-12',1,28,43,NULL),(21,'2014-02-10',2,'2014-09-12',1,29,44,NULL);
/*!40000 ALTER TABLE `t_route` ENABLE KEYS */;
UNLOCK TABLES;
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `t_session` WRITE;
/*!40000 ALTER TABLE `t_session` DISABLE KEYS */;
INSERT INTO `t_session` VALUES (1,'2014-03-15 12:45:27','8a468e20-cace-408e-83f0-288607a68209',15),(3,'2014-03-15 12:45:28','dd789694-41b4-433d-be67-86199f1a8351',17),(4,'2014-03-15 12:45:43','fe119a82-9cb7-4cb7-bab4-4361216d3057',20),(5,'2014-03-15 12:45:42','9cebfec0-26f6-4044-b68f-2795507ae416',22),(6,'2014-03-15 12:45:47','e4c9363b-09b7-4d8e-8c26-d061b5c0c248',23),(7,'2014-03-15 12:45:49','4c3772a6-afd8-454e-9dd4-65ba769695d2',27),(8,'2014-03-15 12:45:56','2b387443-f879-42e2-827f-c7a2242a4650',30),(9,'2014-03-15 12:45:57','a34ea8e6-9b34-4801-8a40-5d4cb7decc90',31),(10,'2014-03-15 12:46:02','4713db37-97c8-44e0-a304-311733262146',32),(11,'2014-03-15 12:46:03','d0aceb8d-0456-4e07-b780-dcfc5bd73258',33),(12,'2014-03-15 12:46:11','bbc07ca6-8fc6-432e-afae-3e959061f152',36),(13,'2014-03-15 12:46:11','2f26500a-8bd5-4b0f-b8f4-2e4a09f40318',34);
/*!40000 ALTER TABLE `t_session` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `t_textmessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_textmessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isRead` tinyint(1) DEFAULT NULL,
  `messageBody` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `receiverId` int(11) DEFAULT NULL,
  `senderId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5rllkdv99lk3ycq3wbxjrlja4` (`receiverId`),
  KEY `FK_avorx6t4o1evct1qsx12k12dq` (`senderId`),
  CONSTRAINT `FK_avorx6t4o1evct1qsx12k12dq` FOREIGN KEY (`senderId`) REFERENCES `t_user` (`id`),
  CONSTRAINT `FK_5rllkdv99lk3ycq3wbxjrlja4` FOREIGN KEY (`receiverId`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `t_textmessage` WRITE;
/*!40000 ALTER TABLE `t_textmessage` DISABLE KEYS */;
INSERT INTO `t_textmessage` VALUES (1,0,'Tim heeft een traject aangevraagd van Willekeurige Carpoolparking tot Andere plek. U kan dit via uw profiel accepteren of weigeren.','[Automatisch bericht] Traject aangevraagd.',6,5),(2,0,'Melissa heeft een traject aangevraagd van Brasschaat tot Rooseveltplaats. U kan dit via uw profiel accepteren of weigeren.','[Automatisch bericht] Traject aangevraagd.',7,5),(3,0,'Tim heeft een traject aangevraagd van Rooseveltplaats tot Andere plek. U kan dit via uw profiel accepteren of weigeren.','[Automatisch bericht] Traject aangevraagd.',6,5),(4,0,'U vroeg een traject aan van Brasschaat tot Andere plek. Peter accepteerde uw aanvraag.','[Automatisch bericht] Trajectaanvraag geaccepteerd.',5,5),(5,0,'U vroeg een traject aan van Willekeurige Carpoolparking tot Andere plek. Peter accepteerde uw aanvraag.','[Automatisch bericht] Trajectaanvraag geaccepteerd.',6,5),(6,0,'U vroeg een traject aan van Brasschaat tot Rooseveltplaats. Peter accepteerde uw aanvraag.','[Automatisch bericht] Trajectaanvraag geaccepteerd.',7,5),(7,0,'U vroeg een traject aan van Rooseveltplaats tot Andere plek. Peter accepteerde uw aanvraag.','[Automatisch bericht] Trajectaanvraag geaccepteerd.',6,5),(8,0,'Chris heeft een traject aangevraagd van Grote Markt tot Groenplaats. U kan dit via uw profiel accepteren of weigeren.','[Automatisch bericht] Traject aangevraagd.',9,8),(9,0,'Kris heeft een traject aangevraagd van Brasschaat tot Schouwburg. U kan dit via uw profiel accepteren of weigeren.','[Automatisch bericht] Traject aangevraagd.',10,8),(10,0,'Chris heeft een traject aangevraagd van Schouwburg tot Groenplaats. U kan dit via uw profiel accepteren of weigeren.','[Automatisch bericht] Traject aangevraagd.',9,8),(11,0,'U vroeg een traject aan van Brasschaat tot Groenplaats. Bart accepteerde uw aanvraag.','[Automatisch bericht] Trajectaanvraag geaccepteerd.',8,8),(12,0,'U vroeg een traject aan van Grote Markt tot Groenplaats. Bart accepteerde uw aanvraag.','[Automatisch bericht] Trajectaanvraag geaccepteerd.',9,8),(13,0,'U vroeg een traject aan van Brasschaat tot Schouwburg. Bart accepteerde uw aanvraag.','[Automatisch bericht] Trajectaanvraag geaccepteerd.',10,8),(14,0,'U vroeg een traject aan van Schouwburg tot Groenplaats. Bart accepteerde uw aanvraag.','[Automatisch bericht] Trajectaanvraag geaccepteerd.',9,8),(15,0,'Foobar heeft een traject aangevraagd van N14 163-193, 2320 Hoogstraten, Belgium tot N177 100-122, 2850 Boom, Belgium. U kan dit via uw profiel accepteren of weigeren.','[Automatisch bericht] Traject aangevraagd.',19,18),(16,0,'TestUser heeft een traject aangevraagd van N14 163-193, 2320 Hoogstraten, Belgium tot Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium. U kan dit via uw profiel accepteren of weigeren.','[Automatisch bericht] Traject aangevraagd.',20,21),(17,0,'New message body','New message header',29,28),(18,0,'New message body TWO','New message header TWO',29,28),(19,0,'testDeleteWeekdayRoute has confirmed riding the route on 2014-03-12T09:45.\nPlease contribute to the fuel costs: €12.22','Ride confirmed - 2014-03-12T09:45',2,2);
/*!40000 ALTER TABLE `t_textmessage` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `t_traject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_traject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isAccepted` tinyint(1) DEFAULT NULL,
  `dropoffId` int(11) DEFAULT NULL,
  `pickupId` int(11) NOT NULL,
  `routeId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `weekdayrouteId` int(11) DEFAULT NULL,
  `trajectId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_gnw9bkc64556wxl9fphql09tn` (`dropoffId`),
  KEY `FK_ajndv3u8k11yfwmiaaotcpxv0` (`pickupId`),
  KEY `FK_ew6t1i1un66maaxl4xabmawj3` (`routeId`),
  KEY `FK_fhq5khy90118wkmefy7racewt` (`userId`),
  KEY `FK_91iau9srqxmdv0ge8l4eqo6ui` (`weekdayrouteId`),
  KEY `FK_lxketlms80d5hiyahwlgde1to` (`trajectId`),
  CONSTRAINT `FK_lxketlms80d5hiyahwlgde1to` FOREIGN KEY (`trajectId`) REFERENCES `t_placetime` (`placetimeId`),
  CONSTRAINT `FK_91iau9srqxmdv0ge8l4eqo6ui` FOREIGN KEY (`weekdayrouteId`) REFERENCES `t_weekdayroute` (`weekdayrouteId`),
  CONSTRAINT `FK_ajndv3u8k11yfwmiaaotcpxv0` FOREIGN KEY (`pickupId`) REFERENCES `t_placetime` (`placetimeId`),
  CONSTRAINT `FK_ew6t1i1un66maaxl4xabmawj3` FOREIGN KEY (`routeId`) REFERENCES `t_route` (`id`),
  CONSTRAINT `FK_fhq5khy90118wkmefy7racewt` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`),
  CONSTRAINT `FK_gnw9bkc64556wxl9fphql09tn` FOREIGN KEY (`dropoffId`) REFERENCES `t_placetime` (`placetimeId`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `t_traject` WRITE;
/*!40000 ALTER TABLE `t_traject` DISABLE KEYS */;
INSERT INTO `t_traject` VALUES (1,0,3,1,1,1,NULL,3),(2,0,15,10,3,2,NULL,15),(3,0,24,22,5,3,NULL,24),(4,0,31,29,7,4,NULL,31),(5,1,42,35,9,5,NULL,42),(6,1,38,36,9,6,NULL,38),(7,1,37,35,9,7,NULL,37),(8,1,42,41,9,6,NULL,42),(9,1,50,43,10,8,NULL,50),(10,1,50,48,10,9,NULL,50),(11,1,49,47,10,10,NULL,49),(12,1,46,49,10,9,NULL,46),(13,0,58,51,11,11,NULL,58),(14,0,66,59,12,12,NULL,66),(15,0,69,67,13,13,NULL,69),(16,0,74,72,15,14,NULL,74),(17,0,80,78,17,18,NULL,80),(18,0,79,78,17,19,NULL,79),(19,0,83,81,19,21,NULL,83),(20,0,83,81,19,20,NULL,83),(21,1,88,86,20,44,20,NULL),(22,1,93,90,20,44,21,NULL),(23,1,97,94,21,44,22,NULL),(24,1,88,84,20,43,20,NULL),(25,1,87,85,20,39,20,NULL),(26,1,88,87,20,40,20,NULL),(27,1,93,89,20,43,21,NULL),(28,1,93,92,20,40,21,NULL),(29,1,93,91,20,41,21,NULL),(30,1,97,96,21,41,22,NULL),(31,1,97,95,21,42,22,NULL);
/*!40000 ALTER TABLE `t_traject` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `androidId` varchar(1023) DEFAULT NULL,
  `avatarURL` varchar(255) DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `smoker` tinyint(1) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jhib4legehrm4yscx9t3lirqi` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,NULL,NULL,'1993-01-01',0,'testAddWeekdayRoute','2ac9cb7dc02b3c0083eb70898e549b63',1,'testAddWeekdayRoute@rcit.example.com'),(2,NULL,NULL,'1993-01-01',0,'testDeleteWeekdayRoute','2ac9cb7dc02b3c0083eb70898e549b63',1,'testDeleteWeekdayRoute@rcit.example.com'),(3,NULL,NULL,'1993-01-01',0,'testAddPlaceTime','2ac9cb7dc02b3c0083eb70898e549b63',1,'testAddPlaceTime@rcit.example.com'),(4,NULL,NULL,'1993-01-01',0,'testChangeCar','2ac9cb7dc02b3c0083eb70898e549b63',1,'testChangeCar@rcit.example.com'),(5,NULL,NULL,'1993-04-12',0,'Peter','13a6d0218cdeb73ea72c714257b6e252',1,'petertje@spoed.com'),(6,NULL,NULL,'1993-03-13',1,'Tim','c32be48670736baab83330f6bee9d77e',1,'timmetje@tim.tim'),(7,NULL,NULL,'1993-07-03',1,'Melissa','b086cfe3604a4a6891d0452b392f60fe',1,'melissa@tim.tim'),(8,NULL,NULL,'1993-04-12',0,'Bart','50a84b5119e5107a842bc60d5b57755d',1,'b.vochten@carpool.be'),(9,NULL,NULL,'1993-03-13',1,'Chris','59ecddc625e76920131b038960bbf005',1,'behielsje@carpool.be'),(10,NULL,NULL,'1993-07-03',1,'Kris','6d6d69857874ef94f09d84d9f701e8f6',1,'demuynck@carpool.be'),(11,NULL,NULL,'1993-04-12',1,'Tim','b5058238d6ae739867815a4ddf405de3',1,'thierryv@nnn.loo'),(12,NULL,NULL,'1993-04-12',1,'Wimpie','b5058238d6ae739867815a4ddf405de3',1,'wimpie@swag.com'),(13,NULL,NULL,'1993-01-01',0,'testdeleteplacetime','2ac9cb7dc02b3c0083eb70898e549b63',1,'testdeleteplacetime@rcit.example.com'),(14,NULL,NULL,'1993-01-01',0,'testchangetime','2ac9cb7dc02b3c0083eb70898e549b63',1,'testchangetime@rcit.example.com'),(15,NULL,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'Thierry@test.com'),(16,NULL,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'TestUser2@test.com'),(17,NULL,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'TestUser3@test.com'),(18,NULL,NULL,'1980-05-24',1,'Foobar','2ac9cb7dc02b3c0083eb70898e549b63',0,'successA@reqTraj.example.com'),(19,NULL,NULL,'1980-05-24',0,'Foobar','2ac9cb7dc02b3c0083eb70898e549b63',0,'successB@reqTraj.example.com'),(20,NULL,NULL,'1993-01-01',1,'TestUser','2ac9cb7dc02b3c0083eb70898e549b63',0,'username@route.controller.it.example.com'),(21,NULL,NULL,'1975-07-29',0,'Benjamin Verdin','2ac9cb7dc02b3c0083eb70898e549b63',0,'benjamin.verdin1@traj.example.com'),(22,NULL,NULL,'1956-07-10',1,'Jeanne Wijffels','2ac9cb7dc02b3c0083eb70898e549b63',0,'jeanne.wijffels1@traj.example.com'),(23,NULL,NULL,'1993-01-01',1,'TestUser','2ac9cb7dc02b3c0083eb70898e549b63',0,'username@addcar.controller.it.example.com'),(24,NULL,NULL,'1993-01-01',1,'otherUser','2ac9cb7dc02b3c0083eb70898e549b63',0,'otheruser@car.controller.it.example.com'),(25,'BBBBBBBBBBBBBBBBBBBBBBBBBBBAAAAAAAAAAAAAAAAAAAJJJJAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL99999999AAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL999999999999999999999999_____________________SSSSSSSSSSSSSSSSSSSSTTTTTTTTTTTTTTTTTTTTRRRRRRRRRRRRRRRRRRRRRRIIIIIIIIIIIIIIIIIIIIINNNNNNNNNNNNNNNNNNNNGGGGGGGGGGGGGGG',NULL,'1993-10-20',0,'Android user','2ac9cb7dc02b3c0083eb70898e549b63',0,'username@ac.test.com'),(26,NULL,NULL,'1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'profile@test.com'),(27,NULL,NULL,'1993-10-20',0,'username','2ac9cb7dc02b3c0083eb70898e549b63',0,'username@lc.test.com'),(28,NULL,NULL,'1993-10-03',0,'user@tmc.test.com','adbd9581843b090f677a7570ed47f791',0,'user@tmc.test.com'),(29,'APA91bG1lNfmlJVl0g4wdhicPBS454KrJCPqpTBXhW9RshTe9DQxWOEtMPr8Vh6X0iQ0UqvMhHxS65Y_ChD2lkhWANaFetNQvT6OsoaNsCpV3OrwF8WJJDXq2GH48vTD9M1_FGY5THgnHWsiKZsDc-02DoxOB_BGUQ',NULL,'1993-10-03',0,'user2@tmc.test.com','adbd9581843b090f677a7570ed47f791',0,'user2@tmc.test.com'),(30,NULL,NULL,'1993-10-20',0,'username','2ac9cb7dc02b3c0083eb70898e549b63',0,'username@cp.test.com'),(31,NULL,NULL,'1993-10-20',1,'Username 2','f87d662a66f805c3a351ae71e866c2d5',0,'username2@cp.test.com'),(32,NULL,NULL,'1993-10-03',0,'Test User','2ac9cb7dc02b3c0083eb70898e549b63',1,'username@rc.test.com'),(33,NULL,NULL,'1993-10-03',0,'Test User','2ac9cb7dc02b3c0083eb70898e549b63',1,'username2@rc.test.com'),(34,NULL,'src/main/webapp/userImages/TestUser-1756532862.png','1993-10-20',1,'TestUser','adbd9581843b090f677a7570ed47f791',0,'Thierry2@test.com'),(35,NULL,NULL,'1993-10-20',0,'OneCarUser','adbd9581843b090f677a7570ed47f791',0,'Onecar@test.com'),(36,NULL,NULL,'1914-02-08',0,'PJ','d1557bbeb3b6a8d1689728a1afbb3928',1,'pieterjanvdp@gmail.com'),(37,NULL,NULL,'1993-10-20',0,'Wimmetje','48df5fcbd46f7beb49840eb03af4c190',0,'test@user.com'),(38,NULL,NULL,'1993-05-05',0,'My Name','bb0739bd5ce6d7c38bd15681f6f5b1f5',1,'ihaveacar@cars.car'),(39,NULL,NULL,'1975-07-29',1,'Benjamin Verdin','2ac9cb7dc02b3c0083eb70898e549b63',1,'benjamin.verdin@traj.example.com'),(40,NULL,NULL,'1956-07-10',0,'Jeanne Wijffels','2ac9cb7dc02b3c0083eb70898e549b63',1,'jeanne.wijffels@traj.example.com'),(41,NULL,NULL,'1980-04-24',0,'Shauni Ordelman','2ac9cb7dc02b3c0083eb70898e549b63',0,'shauni.ordelman@traj.example.com'),(42,NULL,NULL,'1983-11-14',1,'Patrick Verhellen','2ac9cb7dc02b3c0083eb70898e549b63',0,'patrick.verhellen@traj.example.com'),(43,NULL,NULL,'1971-09-03',1,'Ludo van Rosmalen','2ac9cb7dc02b3c0083eb70898e549b63',0,'ludo.vanrosmalen@traj.example.com'),(44,NULL,NULL,'1965-03-16',0,'Kris Breddels','2ac9cb7dc02b3c0083eb70898e549b63',0,'kris.breddels@traj.example.com');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `t_weekdayroute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_weekdayroute` (
  `weekdayrouteId` int(11) NOT NULL AUTO_INCREMENT,
  `day` int(11) DEFAULT NULL,
  `routeId` int(11) NOT NULL,
  PRIMARY KEY (`weekdayrouteId`),
  KEY `FK_p9evtkccgmxmcnxax3kfs84gt` (`routeId`),
  CONSTRAINT `FK_p9evtkccgmxmcnxax3kfs84gt` FOREIGN KEY (`routeId`) REFERENCES `t_route` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `t_weekdayroute` WRITE;
/*!40000 ALTER TABLE `t_weekdayroute` DISABLE KEYS */;
INSERT INTO `t_weekdayroute` VALUES (1,4,1),(2,4,2),(3,6,2),(4,4,3),(5,5,3),(6,4,4),(7,4,5),(8,4,6),(9,4,7),(10,4,8),(11,0,9),(12,1,9),(13,0,12),(14,1,12),(15,4,13),(16,4,14),(17,4,15),(18,4,16),(19,5,17),(20,4,20),(21,6,20),(22,5,21);
/*!40000 ALTER TABLE `t_weekdayroute` ENABLE KEYS */;
UNLOCK TABLES;
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

INSERT INTO t_traject (isAccepted, pickupId, dropoffId, routeId, userId, weekdayrouteId) VALUES 
	(1, ptC, ptE, routeA, userF, wdrA),
	(1, ptG, ptJ, routeA, userF, wdrB),
	(1, ptK, ptN, routeB, userF, wdrC),
	(1, ptA, ptE, routeA, userE, wdrA),
	(1, ptB, ptD, routeA, userA, wdrA),
	(1, ptD, ptE, routeA, userB, wdrA),
	(1, ptF, ptJ, routeA, userE, wdrB),
	(1, ptI, ptJ, routeA, userB, wdrB),
	(1, ptH, ptJ, routeA, userC, wdrB),
	(1, ptM, ptN, routeB, userC, wdrC),
	(1, ptL, ptN, routeB, userD, wdrC);

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

SET FOREIGN_KEY_CHECKS = 1;
