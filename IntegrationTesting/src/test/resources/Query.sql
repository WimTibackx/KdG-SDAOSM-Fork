-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.13 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             8.0.0.4396
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for groepa
DROP DATABASE IF EXISTS `groepa`;
CREATE DATABASE IF NOT EXISTS `groepa` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `groepa`;


-- Dumping structure for procedure groepa.trajdata
DROP PROCEDURE IF EXISTS `trajdata`;
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `trajdata`()
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
END//
DELIMITER ;


-- Dumping structure for table groepa.t_car
DROP TABLE IF EXISTS `t_car`;
CREATE TABLE IF NOT EXISTS `t_car` (
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- Dumping data for table groepa.t_car: ~19 rows (approximately)
DELETE FROM `t_car`;
/*!40000 ALTER TABLE `t_car` DISABLE KEYS */;
INSERT INTO `t_car` (`carId`, `name`, `consumption`, `fuelType`, `pictureURL`, `type`, `userId`) VALUES
	(1, 'Ford', 8, 2, NULL, 'Fiesta', 2),
	(2, 'Ford', 8, 2, NULL, 'Fiesta', 1),
	(3, 'Ford', 8.1, 2, NULL, 'Fiesta', 3),
	(4, 'Ford', 8.1, 2, NULL, 'Fiesta', 4),
	(5, 'Audi', 11, 0, NULL, 'A5', NULL),
	(6, 'Renault', 9.9, 0, NULL, 'Civic', 10),
	(7, 'Renault', 9.9, 0, NULL, 'Civic', 10),
	(8, 'Renault', 9.9, 0, NULL, 'Civic', 10),
	(9, 'Skoda', 10.3, 1, NULL, 'Sködalike', NULL),
	(10, 'Peugeot', 7.2, 0, NULL, 'Partner', 15),
	(11, 'Peugeot', 7.2, 0, NULL, 'Partner', 16),
	(12, 'Audi', 11, 0, 'src\\main\\webapp\\carImages\\AudiA5-537470279.jpg', 'A5', NULL),
	(13, 'Audi', 10.2, 0, NULL, 'C4', NULL),
	(14, 'BMW', 11, 0, NULL, 'X9', 21),
	(15, 'Renault', 9.9, 0, NULL, 'Civic', 21),
	(16, 'Renault', 9.9, 0, NULL, 'Civic', 21),
	(17, 'Renault', 9.9, 0, NULL, 'Civic', 21),
	(18, 'Opel', 8.3, 0, NULL, 'Insignia', 29),
	(19, 'Ford', 7.9, 2, NULL, 'Focus', 30);
/*!40000 ALTER TABLE `t_car` ENABLE KEYS */;


-- Dumping structure for table groepa.t_place
DROP TABLE IF EXISTS `t_place`;
CREATE TABLE IF NOT EXISTS `t_place` (
  `placeId` int(11) NOT NULL AUTO_INCREMENT,
  `lat` double DEFAULT NULL,
  `lon` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`placeId`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- Dumping data for table groepa.t_place: ~19 rows (approximately)
DELETE FROM `t_place`;
/*!40000 ALTER TABLE `t_place` DISABLE KEYS */;
INSERT INTO `t_place` (`placeId`, `lat`, `lon`, `name`) VALUES
	(1, 51.40011, 4.76071, 'N14 163-193, 2320 Hoogstraten, Belgium'),
	(2, 51.351255, 4.641555, 'N115 2-30, 2960 Brecht, Belgium'),
	(3, 51.208078, 4.442945, 'Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium'),
	(4, 231.98880004882812, 132.5668487548828, 'Kieldrecht'),
	(5, 431.9898681640625, 411.98895263671875, 'Zwijndrecht Krijgsbaan'),
	(6, 564.9873046875, 342.97137451171875, 'Carpoolparking Vrasene'),
	(7, 154.9871368408203, 189.98745727539062, 'Melsele Dijk'),
	(8, 231.98880004882812, 132.5668487548828, 'Kieldrecht'),
	(9, 431.9898681640625, 411.98895263671875, 'Zwijndrecht Krijgsbaan'),
	(10, 564.9873046875, 342.97137451171875, 'Carpoolparking Vrasene'),
	(11, 154.9871368408203, 189.98745727539062, 'Melsele Dijk'),
	(12, 51.40011, 4.76071, 'N14 163-193, 2320 Hoogstraten, Belgium'),
	(13, 51.351255, 4.641555, 'N115 2-30, 2960 Brecht, Belgium'),
	(14, 51.208078, 4.442945, 'Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium'),
	(15, 51.1922, 4.420979, 'Binnensingel, 2600 Antwerpen, Belgium'),
	(16, 51.175893, 4.388159, 'Boomsesteenweg 174-180, 2610 Antwerpen, Belgium'),
	(17, 51.029592, 4.488086, 'Zwartzustersvest 47-50, 2800 Mechelen, Belgium'),
	(18, 51.090334, 4.365175, 'N177 100-122, 2850 Boom, Belgium'),
	(19, 50.862557, 4.352118, 'Willebroekkaai 35, 1000 Brussel, Belgium');
/*!40000 ALTER TABLE `t_place` ENABLE KEYS */;


-- Dumping structure for table groepa.t_placetime
DROP TABLE IF EXISTS `t_placetime`;
CREATE TABLE IF NOT EXISTS `t_placetime` (
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
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- Dumping data for table groepa.t_placetime: ~33 rows (approximately)
DELETE FROM `t_placetime`;
/*!40000 ALTER TABLE `t_placetime` DISABLE KEYS */;
INSERT INTO `t_placetime` (`placetimeId`, `time`, `placeId`, `routeId`, `weekdayRouteId`) VALUES
	(1, '07:45:00', 1, 2, NULL),
	(2, '08:03:00', 2, 2, NULL),
	(3, '08:17:00', 3, 2, NULL),
	(4, '09:00:00', 4, 3, NULL),
	(5, '09:10:00', 5, 3, NULL),
	(6, '09:20:00', 6, 3, NULL),
	(7, '09:25:00', 7, 3, NULL),
	(8, '08:00:00', 4, 3, NULL),
	(9, '08:10:00', 5, 3, NULL),
	(10, '08:20:00', 6, 3, NULL),
	(11, '08:25:00', 7, 3, NULL),
	(12, '09:00:00', 8, 4, 1),
	(13, '09:10:00', 9, 4, 1),
	(14, '09:20:00', 10, 4, 1),
	(15, '09:25:00', 11, 4, 1),
	(16, '08:00:00', 8, 4, 2),
	(17, '08:10:00', 9, 4, 2),
	(18, '08:20:00', 10, 4, 2),
	(19, '08:25:00', 11, 4, 2),
	(20, '06:45:00', 12, 5, 3),
	(21, '07:03:00', 13, 5, 3),
	(22, '07:17:00', 14, 5, 3),
	(23, '07:33:00', 18, 5, 3),
	(24, '07:55:00', 19, 5, 3),
	(25, '06:45:00', 12, 5, 4),
	(26, '07:10:00', 14, 5, 4),
	(27, '07:18:00', 16, 5, 4),
	(28, '07:31:00', 18, 5, 4),
	(29, '07:53:00', 19, 5, 4),
	(30, '07:30:00', 14, 6, 5),
	(31, '07:36:00', 15, 6, 5),
	(32, '07:41:00', 16, 6, 5),
	(33, '07:56:00', 17, 6, 5);
/*!40000 ALTER TABLE `t_placetime` ENABLE KEYS */;


-- Dumping structure for table groepa.t_ride
DROP TABLE IF EXISTS `t_ride`;
CREATE TABLE IF NOT EXISTS `t_ride` (
  `rideId` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `totalCost` double DEFAULT NULL,
  `trajectId` int(11) DEFAULT NULL,
  PRIMARY KEY (`rideId`),
  KEY `FK_3dpj3g1e03l906pqegwt4kp2r` (`trajectId`),
  CONSTRAINT `FK_3dpj3g1e03l906pqegwt4kp2r` FOREIGN KEY (`trajectId`) REFERENCES `t_traject` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table groepa.t_ride: ~0 rows (approximately)
DELETE FROM `t_ride`;
/*!40000 ALTER TABLE `t_ride` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_ride` ENABLE KEYS */;


-- Dumping structure for table groepa.t_route
DROP TABLE IF EXISTS `t_route`;
CREATE TABLE IF NOT EXISTS `t_route` (
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- Dumping data for table groepa.t_route: ~6 rows (approximately)
DELETE FROM `t_route`;
/*!40000 ALTER TABLE `t_route` DISABLE KEYS */;
INSERT INTO `t_route` (`id`, `begin_date`, `capacity`, `end_date`, `repeating`, `carId`, `userId`, `routeId`) VALUES
	(1, '2014-02-19', 3, '2014-02-27', 1, 1, 3, NULL),
	(2, '2014-03-15', 2, '2014-03-15', 0, 4, 4, NULL),
	(3, '2014-03-13', 69, '2014-03-13', 0, 10, 15, NULL),
	(4, '2014-03-13', 69, '2014-03-13', 1, 11, 16, NULL),
	(5, '2014-02-10', 3, '2014-09-12', 1, 18, 29, NULL),
	(6, '2014-02-10', 2, '2014-09-12', 1, 19, 30, NULL);
/*!40000 ALTER TABLE `t_route` ENABLE KEYS */;


-- Dumping structure for table groepa.t_session
DROP TABLE IF EXISTS `t_session`;
CREATE TABLE IF NOT EXISTS `t_session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `expirationDate` datetime DEFAULT NULL,
  `sessionToken` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_td2jkj3d323m8fprao4wn3bed` (`user_id`),
  CONSTRAINT `FK_td2jkj3d323m8fprao4wn3bed` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- Dumping data for table groepa.t_session: ~12 rows (approximately)
DELETE FROM `t_session`;
/*!40000 ALTER TABLE `t_session` DISABLE KEYS */;
INSERT INTO `t_session` (`id`, `expirationDate`, `sessionToken`, `user_id`) VALUES
	(1, '2014-03-14 17:35:26', 'e6375cfc-3135-4e45-8d33-5d339c9fe01a', 1),
	(2, '2014-03-14 17:35:27', 'ff5fd4f8-27f2-43be-9280-0cc0c475179e', 3),
	(3, '2014-03-14 17:35:27', 'fce02b86-f862-4794-811d-67d308b9f4f6', 5),
	(4, '2014-03-14 17:35:31', '71627b04-05e2-414c-8875-e14978f5563d', 7),
	(5, '2014-03-14 17:35:28', 'a701a2e7-1887-4218-876c-12e5ddc0b487', 8),
	(6, '2014-03-14 17:35:31', 'd9441697-4ca6-4afe-a82a-00ed9e043fc4', 9),
	(7, '2014-03-14 17:35:32', '31767d54-cd79-49d6-9ba8-a3f2a936fdd3', 11),
	(8, '2014-03-14 17:35:32', 'cea6fd27-d762-40f2-b288-2939730acfc7', 12),
	(10, '2014-03-14 17:35:34', '7b9b424d-bf05-40d7-8ce2-b9bf4e9f14a1', 19),
	(11, '2014-03-14 17:35:34', '9e2d3f84-373a-413d-a1fd-df75f3b750a2', 17),
	(12, '2014-03-14 17:35:35', '823c729b-dc96-4284-822e-296087990b19', 20),
	(13, '2014-03-14 17:35:35', '07c04f41-0d6e-425b-9992-50bf7943839b', 24);
/*!40000 ALTER TABLE `t_session` ENABLE KEYS */;


-- Dumping structure for table groepa.t_textmessage
DROP TABLE IF EXISTS `t_textmessage`;
CREATE TABLE IF NOT EXISTS `t_textmessage` (
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table groepa.t_textmessage: ~3 rows (approximately)
DELETE FROM `t_textmessage`;
/*!40000 ALTER TABLE `t_textmessage` DISABLE KEYS */;
INSERT INTO `t_textmessage` (`id`, `isRead`, `messageBody`, `subject`, `receiverId`, `senderId`) VALUES
	(1, 0, 'TestUser heeft een traject aangevraagd van N14 163-193, 2320 Hoogstraten, Belgium tot Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium. U kan dit via uw profiel accepteren of weigeren.', '[Automatisch bericht] Traject aangevraagd.', 3, 4),
	(2, 0, 'New message body', 'New message header', 14, 13),
	(3, 0, 'New message body TWO', 'New message header TWO', 14, 13),
	(4, 0, 'New message body 3', 'New message header 3', 23, 22),
	(5, 0, 'New message body 4', 'New message header 4', 23, 22),
	(6, 0, 'New message body 5', 'New message header 5', 23, 22),
	(7, 0, 'New message body 6', 'New message header 6', 23, 22),
	(8, 0, 'New message body 7', 'New message header 7', 23, 22),
	(9, 0, 'New message body 8', 'New message header 8', 23, 22),
	(10, 0, 'New message body 9', 'New message header 9', 23, 22),
	(11, 0, 'New message body 10', 'New message header 10', 23, 22),
	(12, 0, 'New message body 11', 'New message header 11', 23, 22),
	(13, 0, 'New message body 12', 'New message header 12', 23, 22),
	(14, 0, 'New message body 13', 'New message header 13', 23, 22);
/*!40000 ALTER TABLE `t_textmessage` ENABLE KEYS */;


-- Dumping structure for table groepa.t_traject
DROP TABLE IF EXISTS `t_traject`;
CREATE TABLE IF NOT EXISTS `t_traject` (
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- Dumping data for table groepa.t_traject: ~15 rows (approximately)
DELETE FROM `t_traject`;
/*!40000 ALTER TABLE `t_traject` DISABLE KEYS */;
INSERT INTO `t_traject` (`id`, `isAccepted`, `dropoffId`, `pickupId`, `routeId`, `userId`, `weekdayrouteId`, `trajectId`) VALUES
	(1, 0, 3, 1, 2, 4, NULL, 3),
	(2, 0, 3, 1, 2, 3, NULL, 3),
	(3, 0, 11, 4, 3, 15, NULL, 11),
	(4, 0, 19, 12, 4, 16, NULL, 19),
	(5, 1, 24, 22, 5, 30, NULL, NULL),
	(6, 1, 29, 26, 5, 30, NULL, NULL),
	(7, 1, 33, 30, 6, 30, NULL, NULL),
	(8, 1, 24, 20, 5, 29, NULL, NULL),
	(9, 1, 23, 21, 5, 25, NULL, NULL),
	(10, 1, 24, 23, 5, 26, NULL, NULL),
	(11, 1, 29, 25, 5, 29, NULL, NULL),
	(12, 1, 29, 28, 5, 26, NULL, NULL),
	(13, 1, 29, 27, 5, 27, NULL, NULL),
	(14, 1, 33, 32, 6, 27, NULL, NULL),
	(15, 1, 33, 31, 6, 28, NULL, NULL);
/*!40000 ALTER TABLE `t_traject` ENABLE KEYS */;


-- Dumping structure for table groepa.t_user
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE IF NOT EXISTS `t_user` (
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
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- Dumping data for table groepa.t_user: ~30 rows (approximately)
DELETE FROM `t_user`;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`id`, `androidId`, `avatarURL`, `dateOfBirth`, `gender`, `name`, `password`, `smoker`, `username`) VALUES
	(1, NULL, NULL, '1993-01-01', 1, 'TestUser', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'username@addcar.controller.it.example.com'),
	(2, NULL, NULL, '1993-01-01', 1, 'otherUser', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'otheruser@car.controller.it.example.com'),
	(3, NULL, NULL, '1993-01-01', 1, 'TestUser', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'username@route.controller.it.example.com'),
	(4, NULL, NULL, '1975-07-29', 0, 'Benjamin Verdin', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'benjamin.verdin1@traj.example.com'),
	(5, NULL, NULL, '1956-07-10', 1, 'Jeanne Wijffels', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'jeanne.wijffels1@traj.example.com'),
	(6, 'BBBBBBBBBBBBBBBBBBBBBBBBBBBAAAAAAAAAAAAAAAAAAAJJJJAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL99999999AAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL999999999999999999999999_____________________SSSSSSSSSSSSSSSSSSSSTTTTTTTTTTTTTTTTTTTTRRRRRRRRRRRRRRRRRRRRRRIIIIIIIIIIIIIIIIIIIIINNNNNNNNNNNNNNNNNNNNGGGGGGGGGGGGGGG', NULL, '1993-10-20', 0, 'Android user', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'username@ac.test.com'),
	(7, NULL, NULL, '1993-10-20', 0, 'username', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'username@cp.test.com'),
	(8, NULL, NULL, '1993-10-20', 1, 'Username 2', 'a6bea32bac25db54ddae672a5bf7fb78', 0, 'username2@cp.test.com'),
	(9, NULL, NULL, '1993-10-20', 0, 'username', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'username@lc.test.com'),
	(10, NULL, NULL, '1993-10-20', 1, 'TestUser', 'adbd9581843b090f677a7570ed47f791', 0, 'profile@test.com'),
	(11, NULL, NULL, '1993-10-03', 0, 'Test User', '2ac9cb7dc02b3c0083eb70898e549b63', 1, 'username@rc.test.com'),
	(12, NULL, NULL, '1993-10-03', 0, 'Test User', '2ac9cb7dc02b3c0083eb70898e549b63', 1, 'username2@rc.test.com'),
	(13, NULL, NULL, '1993-10-03', 0, 'user@tmc.test.com', 'adbd9581843b090f677a7570ed47f791', 0, 'user@tmc.test.com'),
	(14, 'APA91bG1lNfmlJVl0g4wdhicPBS454KrJCPqpTBXhW9RshTe9DQxWOEtMPr8Vh6X0iQ0UqvMhHxS65Y_ChD2lkhWANaFetNQvT6OsoaNsCpV3OrwF8WJJDXq2GH48vTD9M1_FGY5THgnHWsiKZsDc-02DoxOB_BGUQ', NULL, '1993-10-03', 0, 'user2@tmc.test.com', 'adbd9581843b090f677a7570ed47f791', 0, 'user2@tmc.test.com'),
	(15, NULL, NULL, '1993-04-12', 1, 'Tim', 'b5058238d6ae739867815a4ddf405de3', 1, 'thierryv@nnn.loo'),
	(16, NULL, NULL, '1993-04-12', 1, 'Wimpie', 'b5058238d6ae739867815a4ddf405de3', 1, 'wimpie@swag.com'),
	(17, NULL, NULL, '1993-10-20', 1, 'TestUser', 'adbd9581843b090f677a7570ed47f791', 0, 'Thierry@test.com'),
	(18, NULL, NULL, '1993-10-20', 1, 'TestUser', 'adbd9581843b090f677a7570ed47f791', 0, 'TestUser2@test.com'),
	(19, NULL, NULL, '1993-10-20', 1, 'TestUser', 'adbd9581843b090f677a7570ed47f791', 0, 'TestUser3@test.com'),
	(20, NULL, 'src\\main\\webapp\\userImages\\TestUser-927652533.png', '1993-10-20', 1, 'TestUser', 'adbd9581843b090f677a7570ed47f791', 0, 'Thierry2@test.com'),
	(21, NULL, NULL, '1993-10-20', 0, 'OneCarUser', 'adbd9581843b090f677a7570ed47f791', 0, 'Onecar@test.com'),
	(22, NULL, NULL, '1993-10-20', 0, 'Wimmetje', '48df5fcbd46f7beb49840eb03af4c190', 0, 'test@user.com'),
	(23, NULL, NULL, '1993-05-05', 0, 'My Name', 'bb0739bd5ce6d7c38bd15681f6f5b1f5', 1, 'ihaveacar@cars.car'),
	(24, NULL, NULL, '1914-02-08', 0, 'PJ', 'd1557bbeb3b6a8d1689728a1afbb3928', 1, 'pieterjanvdp@gmail.com'),
	(25, NULL, NULL, '1975-07-29', 1, 'Benjamin Verdin', '2ac9cb7dc02b3c0083eb70898e549b63', 1, 'benjamin.verdin@traj.example.com'),
	(26, NULL, NULL, '1956-07-10', 0, 'Jeanne Wijffels', '2ac9cb7dc02b3c0083eb70898e549b63', 1, 'jeanne.wijffels@traj.example.com'),
	(27, NULL, NULL, '1980-04-24', 0, 'Shauni Ordelman', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'shauni.ordelman@traj.example.com'),
	(28, NULL, NULL, '1983-11-14', 1, 'Patrick Verhellen', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'patrick.verhellen@traj.example.com'),
	(29, NULL, NULL, '1971-09-03', 1, 'Ludo van Rosmalen', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'ludo.vanrosmalen@traj.example.com'),
	(30, NULL, NULL, '1965-03-16', 0, 'Kris Breddels', '2ac9cb7dc02b3c0083eb70898e549b63', 0, 'kris.breddels@traj.example.com');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;


-- Dumping structure for table groepa.t_weekdayroute
DROP TABLE IF EXISTS `t_weekdayroute`;
CREATE TABLE IF NOT EXISTS `t_weekdayroute` (
  `weekdayrouteId` int(11) NOT NULL AUTO_INCREMENT,
  `day` int(11) DEFAULT NULL,
  `routeId` int(11) NOT NULL,
  PRIMARY KEY (`weekdayrouteId`),
  KEY `FK_p9evtkccgmxmcnxax3kfs84gt` (`routeId`),
  CONSTRAINT `FK_p9evtkccgmxmcnxax3kfs84gt` FOREIGN KEY (`routeId`) REFERENCES `t_route` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table groepa.t_weekdayroute: ~5 rows (approximately)
DELETE FROM `t_weekdayroute`;
/*!40000 ALTER TABLE `t_weekdayroute` DISABLE KEYS */;
INSERT INTO `t_weekdayroute` (`weekdayrouteId`, `day`, `routeId`) VALUES
	(1, 0, 4),
	(2, 1, 4),
	(3, 3, 5),
	(4, 5, 5),
	(5, 4, 6);
/*!40000 ALTER TABLE `t_weekdayroute` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
