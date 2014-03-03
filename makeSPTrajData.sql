-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

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
END