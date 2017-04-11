-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: heck
-- ------------------------------------------------------
-- Server version	5.7.17-0ubuntu0.16.04.1

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
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointment` (
  `id_appointment` int(11) NOT NULL AUTO_INCREMENT,
  `id_doctor` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `occupied_appointment` tinyint(4) NOT NULL,
  `date_from_appointment` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_to_appointment` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `holiday_appointment` tinyint(4) NOT NULL,
  `canceled_appointment` tinyint(4) NOT NULL,
  `patitent_name` varchar(100) NOT NULL,
  `note_appointment` varchar(100) DEFAULT NULL,
  `result_appointment` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_appointment`),
  KEY `doctors_appointment_idx` (`id_doctor`),
  KEY `users_appointment_idx` (`id_user`),
  CONSTRAINT `doctors_appointment` FOREIGN KEY (`id_doctor`) REFERENCES `doctor` (`id_doctor`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `users_appointment` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doctor` (
  `id_doctor` int(11) NOT NULL AUTO_INCREMENT,
  `email_doctor` varchar(320) NOT NULL,
  `login_doctor` varchar(100) NOT NULL,
  `password_doctor` varchar(320) NOT NULL,
  `salt_doctor` varchar(30) NOT NULL,
  `specialization_doctor` int(11) NOT NULL,
  `business_name_doctor` varchar(100) NOT NULL,
  `first_name_doctor` varchar(100) NOT NULL,
  `last_name_doctor` varchar(100) NOT NULL,
  `phone_number_doctor` varchar(15) NOT NULL,
  `postal_code_doctor` int(11) NOT NULL,
  `city_doctor` varchar(100) NOT NULL,
  `address_doctor` varchar(100) NOT NULL,
  `activation_time_doctor` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active_doctor` tinyint(4) NOT NULL,
  PRIMARY KEY (`id_doctor`),
  UNIQUE KEY `email_doctor_UNIQUE` (`email_doctor`),
  UNIQUE KEY `login_doctor_UNIQUE` (`login_doctor`),
  KEY `doctors_specialization_idx` (`specialization_doctor`),
  CONSTRAINT `doctors_specialization` FOREIGN KEY (`specialization_doctor`) REFERENCES `specialization` (`specialization_doctor`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `national_holiday`
--

DROP TABLE IF EXISTS `national_holiday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `national_holiday` (
  `id_national_holiday` int(11) NOT NULL AUTO_INCREMENT,
  `date_national_holiday` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_national_holiday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `national_holiday`
--

LOCK TABLES `national_holiday` WRITE;
/*!40000 ALTER TABLE `national_holiday` DISABLE KEYS */;
/*!40000 ALTER TABLE `national_holiday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specialization`
--

DROP TABLE IF EXISTS `specialization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specialization` (
  `specialization_doctor` int(11) NOT NULL AUTO_INCREMENT,
  `type_specialization` varchar(250) NOT NULL,
  PRIMARY KEY (`specialization_doctor`),
  UNIQUE KEY `type_specialization_UNIQUE` (`type_specialization`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialization`
--

LOCK TABLES `specialization` WRITE;
/*!40000 ALTER TABLE `specialization` DISABLE KEYS */;
INSERT INTO `specialization` VALUES (20,'Chirurg'),(19,'Gynekológ'),(30,'Hematológ'),(26,'Imunológ a alergológ'),(28,'Interný'),(27,'Kardiológ'),(24,'Kožný'),(18,'Neurológ'),(17,'Očný'),(21,'Ortopéd'),(25,'Psychológ'),(29,'Reumatológ'),(22,'Urológ'),(16,'Ušno-nosno-krčný'),(23,'Zubár');
/*!40000 ALTER TABLE `specialization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `email_user` varchar(320) NOT NULL,
  `login_user` varchar(100) NOT NULL,
  `password_user` varchar(320) NOT NULL,
  `salt_user` varchar(30) NOT NULL,
  `first_name_user` varchar(100) NOT NULL,
  `last_name_user` varchar(100) NOT NULL,
  `phone_user` varchar(15) NOT NULL,
  `postal_code_user` int(11) NOT NULL,
  `city_user` varchar(100) NOT NULL,
  `address_user` varchar(100) NOT NULL,
  `active_user` tinyint(4) NOT NULL,
  `is_admin` tinyint(4) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `email_user_UNIQUE` (`email_user`),
  UNIQUE KEY `login_user_UNIQUE` (`login_user`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (0,'admin@admin.sk','admin','9225FFA77B74F2E1214C4F9DCEC02934B580FE57B3AF86DD9F6E5D5777151F88','qbhmlilieminks1bc7v106vvui','admin','adminovic','+4217936248',4424,'Poproc','Najpoprockovejsia 13',1,1),(10,'admin2@admin.sk','2','C8FF01836FE0BE1908778CBD69D3C74ECB6AD14107123D204A61DA426B61B8D0','db490j5l3ipt56j7ltr468aibl','Istvan','Ferenc','+4217936248',2324,'Poproc','Najpoprockovejsia 13',1,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `working_time`
--

DROP TABLE IF EXISTS `working_time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `working_time` (
  `id_doctor` int(11) NOT NULL,
  `day_of_the_week` tinyint(4) NOT NULL,
  `starting_hour` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ending_hour` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_doctor`),
  CONSTRAINT `doctors_working_hours` FOREIGN KEY (`id_doctor`) REFERENCES `doctor` (`id_doctor`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `working_time`
--

LOCK TABLES `working_time` WRITE;
/*!40000 ALTER TABLE `working_time` DISABLE KEYS */;
/*!40000 ALTER TABLE `working_time` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-02 12:35:50
