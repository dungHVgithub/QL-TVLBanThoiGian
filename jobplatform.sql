-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: jobplatform
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (2,1),(14,2),(15,3);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (3,' Makerting'),(5,'Du lịch'),(1,'IT'),(2,'Kế toán'),(4,'Sale');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_images`
--

DROP TABLE IF EXISTS `company_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_id` int NOT NULL,
  `image_path` varchar(255) NOT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `upload_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `company_images_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `company_information` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_images`
--

LOCK TABLES `company_images` WRITE;
/*!40000 ALTER TABLE `company_images` DISABLE KEYS */;
INSERT INTO `company_images` VALUES (1,1,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748093277/o3ssjepdjv3rqefsutmu.png','Logo công ty','2025-04-06 15:04:30'),(2,1,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748017449/qfdg2lzrplxsbz63jghf.jpg','Team fpt','2025-04-06 15:04:30'),(3,1,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748017474/l4fvrtnopmky5uqldofi.jpg','Văn phòng fpt','2025-04-06 15:04:30'),(4,2,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748064068/ey9dxfzfgeyoibi4j6jf.jpg','Chăm sóc khách hàng của công ty','2025-04-06 15:04:30'),(5,2,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748064115/z4a6bzdcdkv5ifausscj.jpg','Chương trình quảng cáo của công ty','2025-04-06 15:04:30'),(6,3,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748065946/rqdtaul7z6ft6ycgayqj.png','Logo công ty','2025-04-06 15:04:30'),(7,4,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1747231459/n06zcuofezr5v6nwbvbm.png','Giải thưởng công ty D','2025-04-06 15:04:30'),(9,5,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748066661/gjtlml9kxe4un1ech0sx.webp','Trụ sở của công ty','2025-04-06 15:04:30'),(10,2,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748064179/o1bywwusu21krswybivb.jpg','Ra mắt sản phẩm mới','2025-05-14 14:04:19'),(11,1,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748018875/qpsxuomf4y34y7jlkr2i.png','Khuôn viên công ty','2025-05-23 16:47:41'),(12,1,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748019327/mo2oxucldgqqxzb8jggm.jpg','Thủ tướng ghé thăm','2025-05-23 16:55:25'),(13,2,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748062855/spukakzuurcgyuzyjgxk.png','Logo công ty','2025-05-24 05:00:43'),(14,2,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748062900/cjahyte1rrkttgtklzrq.webp','Nhân sự của công ty','2025-05-24 05:01:40'),(15,2,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748062918/bcvcldazkngkzm1gb5ie.webp','Trụ sở của công ty','2025-05-24 05:01:58'),(16,3,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748066235/re7vjxaspfdr0ojljhws.png','Trụ sở công ty','2025-05-24 05:57:02'),(17,3,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748066261/ywvtkmcjppfydrgcljae.jpg','Văn phòng công ty','2025-05-24 05:57:40'),(18,5,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748066684/ibax380ifg2e3u829sdq.jpg','Lễ trao thưởng của công ty','2025-05-24 06:04:43'),(21,5,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748155270/txsfzyjhklxpetneiswd.png','Logo công ty','2025-05-25 06:41:09'),(23,7,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748155270/txsfzyjhklxpetneiswd.png',NULL,'2025-07-15 11:13:19'),(24,7,'https://res.cloudinary.com/dxymsdvsz/image/upload/v1748155270/txsfzyjhklxpetneiswd.png',NULL,'2025-07-15 11:13:19');
/*!40000 ALTER TABLE `company_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_information`
--

DROP TABLE IF EXISTS `company_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_information` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `tax_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_information`
--

LOCK TABLES `company_information` WRITE;
/*!40000 ALTER TABLE `company_information` DISABLE KEYS */;
INSERT INTO `company_information` VALUES (1,'Fpt software','HCM','1234567890'),(2,'Mobifone','Hà Nội','9876543210'),(3,'Vng Corporation','Huế','1357924680'),(4,'Cellphone','Hà Nội','2468013579'),(5,'Khang group','Long An','11223344512'),(6,'Dũng company','Hà nội','13456789'),(7,'Dũng companyDũng company','TPHCM','13456789');
/*!40000 ALTER TABLE `company_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (2,5),(6,29),(7,35),(8,37);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_job`
--

DROP TABLE IF EXISTS `employee_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_job` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `job_id` int DEFAULT NULL,
  `jobState` tinyint DEFAULT NULL,
  `favoriteJob` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employee_idx` (`employee_id`),
  KEY `jobPosting_idx` (`job_id`),
  CONSTRAINT `employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `jobPosting` FOREIGN KEY (`job_id`) REFERENCES `job_posting` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_job`
--

LOCK TABLES `employee_job` WRITE;
/*!40000 ALTER TABLE `employee_job` DISABLE KEYS */;
INSERT INTO `employee_job` VALUES (1,6,5,1,0),(2,6,9,1,1),(3,6,21,1,1),(4,2,5,1,0),(6,6,22,0,0),(7,6,21,0,NULL);
/*!40000 ALTER TABLE `employee_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employer`
--

DROP TABLE IF EXISTS `employer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  KEY `company` (`company`),
  CONSTRAINT `company` FOREIGN KEY (`company`) REFERENCES `company_information` (`id`),
  CONSTRAINT `fk_employer_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employer`
--

LOCK TABLES `employer` WRITE;
/*!40000 ALTER TABLE `employer` DISABLE KEYS */;
INSERT INTO `employer` VALUES (2,1,6,'2025-05-19 17:51:38'),(5,2,7,'2025-05-19 17:51:38'),(6,3,20,'2025-05-19 17:51:38'),(7,4,5,'2025-05-19 17:51:38'),(8,5,25,'2025-05-19 17:51:38'),(9,6,32,'2025-05-27 08:00:21'),(11,NULL,39,'2025-07-15 01:18:15'),(12,7,40,'2025-07-15 15:04:54');
/*!40000 ALTER TABLE `employer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follow_notice`
--

DROP TABLE IF EXISTS `follow_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follow_notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NOT NULL,
  `employer_id` int NOT NULL,
  `follow_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `employee_id` (`employee_id`),
  KEY `employer_id` (`employer_id`),
  CONSTRAINT `follow_notice_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `follow_notice_ibfk_2` FOREIGN KEY (`employer_id`) REFERENCES `employer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follow_notice`
--

LOCK TABLES `follow_notice` WRITE;
/*!40000 ALTER TABLE `follow_notice` DISABLE KEYS */;
INSERT INTO `follow_notice` VALUES (2,8,5,'2025-07-15 00:53:18'),(3,7,12,'2025-07-15 18:14:03'),(5,6,12,'2025-07-15 18:40:53'),(6,7,7,'2025-07-15 23:18:38'),(7,7,5,'2025-07-16 22:24:30');
/*!40000 ALTER TABLE `follow_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoso_ungtuyen`
--

DROP TABLE IF EXISTS `hoso_ungtuyen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoso_ungtuyen` (
  `job_id` int NOT NULL,
  `employee_id` int NOT NULL,
  `state` enum('quit','isApproved','isRejected','isPending') DEFAULT NULL,
  `rv_from_employee` text,
  `rv_from_employer` text,
  PRIMARY KEY (`job_id`,`employee_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `hoso_ungtuyen_ibfk_1` FOREIGN KEY (`job_id`) REFERENCES `job_posting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `hoso_ungtuyen_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoso_ungtuyen`
--

LOCK TABLES `hoso_ungtuyen` WRITE;
/*!40000 ALTER TABLE `hoso_ungtuyen` DISABLE KEYS */;
/*!40000 ALTER TABLE `hoso_ungtuyen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_description`
--

DROP TABLE IF EXISTS `job_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_description` (
  `id` int NOT NULL AUTO_INCREMENT,
  `job_posting` int DEFAULT NULL,
  `description` text,
  `level` enum('Intern','Junior','Mid','Senior') DEFAULT NULL,
  `experience` varchar(50) DEFAULT NULL,
  `submit_end` datetime DEFAULT NULL,
  `benefit` text,
  PRIMARY KEY (`id`),
  KEY `job_posting_id` (`job_posting`),
  CONSTRAINT `job_description_ibfk_1` FOREIGN KEY (`job_posting`) REFERENCES `job_posting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_description`
--

LOCK TABLES `job_description` WRITE;
/*!40000 ALTER TABLE `job_description` DISABLE KEYS */;
INSERT INTO `job_description` VALUES (3,5,'Kiểm tra báo cáo, thu chi của công ty','Senior','3-5','2025-05-19 17:52:12','Có hỗ trợ cơm trưa, bảo hiểm'),(4,9,'xxxxxxxxxxxxxxxxx','Mid','5','2025-05-26 07:00:00','yyyyyyyyyyyyyy'),(8,21,'Thông tạo java, có đồ án liên quan đến java','Mid','5','2025-05-26 07:00:00','Có hố trợ cơm trưa và phụ cấp bảo hiểm'),(10,29,'Yêu cầu 2 năm kn về tester','Mid','2','2025-07-30 07:00:00','lương tháng 13'),(11,44,'Yêu cầu 2 năm kn về tester','Mid','2','2025-07-30 07:00:00','lương tháng 13');
/*!40000 ALTER TABLE `job_description` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_posting`
--

DROP TABLE IF EXISTS `job_posting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_posting` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text,
  `salary` double DEFAULT NULL,
  `time_start` time DEFAULT NULL,
  `time_end` time DEFAULT NULL,
  `state` enum('pending','approved','rejected') DEFAULT NULL,
  `employer_id` int DEFAULT NULL,
  `approved_by_admin_id` int DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `employee_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employer_id` (`employer_id`),
  KEY `approved_by_admin_id` (`approved_by_admin_id`),
  KEY `job_posting_ibfk_2_idx` (`employee_id`),
  KEY `job_posting_ibfk_4_idx` (`category_id`),
  CONSTRAINT `job_posting_ibfk_1` FOREIGN KEY (`employer_id`) REFERENCES `employer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `job_posting_ibfk_2` FOREIGN KEY (`approved_by_admin_id`) REFERENCES `admin` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `job_posting_ibfk_3` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `job_posting_ibfk_4` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_posting`
--

LOCK TABLES `job_posting` WRITE;
/*!40000 ALTER TABLE `job_posting` DISABLE KEYS */;
INSERT INTO `job_posting` VALUES (5,'Kiểm toán ',1500,NULL,NULL,'approved',5,NULL,2,NULL,'2025-07-15 18:12:07',NULL),(9,'Tech Lead',1700,'08:00:00','17:00:00','approved',7,2,2,'2025-05-19 17:52:12','2025-05-27 04:11:00',6),(10,'Database Engineer',1150,NULL,NULL,'pending',6,NULL,2,NULL,'2025-07-15 16:34:41',NULL),(19,'Thực tập sinh Java',1500,NULL,NULL,'pending',2,NULL,3,NULL,'2025-07-15 16:34:20',NULL),(21,'Web java',1500,'17:00:00','20:08:00','approved',8,2,2,'2025-05-25 18:58:13','2025-05-26 21:40:13',6),(22,'Di Khang',1499.99,NULL,NULL,'rejected',7,NULL,5,NULL,'2025-06-10 12:21:45',NULL),(24,'Dung',2000,NULL,NULL,'approved',2,NULL,3,'2025-06-10 12:32:00','2025-06-10 12:32:00',NULL),(25,'1',950,NULL,NULL,'approved',2,NULL,3,NULL,'2025-07-15 18:10:02',NULL),(29,'Follow Demo job approved lần 3',2088,NULL,NULL,'approved',12,NULL,3,NULL,'2025-07-15 18:12:21',NULL),(37,'test follow 3',500,NULL,NULL,'approved',12,NULL,3,NULL,'2025-07-15 18:41:03',NULL),(38,'test follow 4',700,NULL,NULL,'approved',12,NULL,3,'2025-07-15 23:22:38','2025-07-15 23:23:18',NULL),(39,'test follow 4',6000,NULL,NULL,'approved',12,NULL,NULL,'2025-07-15 23:26:20','2025-07-15 23:26:30',NULL),(40,'test follow 5',2000,NULL,NULL,'approved',12,NULL,NULL,'2025-07-15 23:30:10','2025-07-15 23:45:40',NULL),(41,'test follow 5',NULL,NULL,NULL,'approved',12,NULL,NULL,'2025-07-15 23:46:42','2025-07-15 23:46:42',NULL),(42,'test follow 6',NULL,NULL,NULL,'approved',12,NULL,NULL,'2025-07-15 23:56:56','2025-07-15 23:56:56',NULL),(43,'test follow 7',NULL,NULL,NULL,'approved',12,NULL,NULL,'2025-07-16 21:33:41','2025-07-16 21:34:18',NULL),(44,'Follow Demo job approved lần 10',2000,'07:07:00','19:07:00','approved',12,NULL,3,'2025-07-16 22:10:17','2025-07-16 22:10:48',NULL),(45,'aaaaaaaaaaa',NULL,NULL,NULL,'approved',12,NULL,NULL,'2025-07-16 22:13:23','2025-07-16 22:13:52',NULL);
/*!40000 ALTER TABLE `job_posting` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_create_notification_after_approve` AFTER UPDATE ON `job_posting` FOR EACH ROW BEGIN
    -- Nếu trạng thái từ khác 'approved' chuyển thành 'approved'
    IF OLD.state IS NULL OR (OLD.state <> 'approved' AND NEW.state = 'approved') THEN
        INSERT INTO notification (content, employer_id, created_at)
        VALUES (
            CONCAT('Nhà tuyển dụng vừa đăng tin tuyển dụng mới: ', NEW.name),
            NEW.employer_id,
            NOW()
        );
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employer_id` int DEFAULT NULL,
  `content` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `employer_id` (`employer_id`),
  CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`employer_id`) REFERENCES `employer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (3,17,'thông báo 1 ','2025-05-26 20:25:55'),(4,17,'thông báo 2','2025-05-26 20:26:08'),(5,17,'thông báo 3','2025-05-26 20:26:12'),(9,16,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: demo follow test','2025-05-27 03:21:34'),(10,16,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: Follow Demo','2025-05-27 03:31:49'),(11,16,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: Follow Demo job approved lần 1','2025-05-27 04:56:10'),(12,11,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: Follow Demo job approved lần 3','2025-07-15 18:06:58'),(13,2,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: 1','2025-07-15 18:10:01'),(14,12,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: test follow 1','2025-07-15 18:15:51'),(15,12,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: test follow 2','2025-07-15 18:30:15'),(16,12,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: test follow 3','2025-07-15 18:39:50'),(17,12,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: test follow 3','2025-07-15 18:41:02'),(18,12,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: test follow 4','2025-07-15 23:23:18'),(19,12,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: test follow 4','2025-07-15 23:26:30'),(20,12,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: test follow 5','2025-07-15 23:45:40'),(21,12,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: test follow 7','2025-07-16 21:34:18'),(22,12,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: Follow Demo job approved lần 10','2025-07-16 22:10:48'),(23,12,'Nhà tuyển dụng vừa đăng tin tuyển dụng mới: aaaaaaaaaaa','2025-07-16 22:13:52');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_notify_followers_after_approved` AFTER INSERT ON `notification` FOR EACH ROW BEGIN
    INSERT INTO user_notification (employee_id, notification_id, is_read)
    SELECT fn.employee_id, NEW.id, 0
    FROM follow_notice fn
    WHERE fn.employer_id = NEW.employer_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `sdt` varchar(20) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `role` enum('ROLE_ADMIN','ROLE_EMPLOYER','ROLE_EMPLOYEE') DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'hvd','$2a$10$CukoeAY3.pvFnT37p/LPJ.qLgmTF8snio.YNd8JIFibajy9A9Ieju','Company A','','HCM','0922222222','1985-03-10','ROLE_ADMIN','hr@companya.com','2025-05-19 17:52:41','2025-05-19 17:52:41'),(5,'bossB','$2a$10$lU5IMIdn07B9iNtKnjwhMeQxYc4UoSQPIsbrZUCIYqqcv4bJPDh9y','Boss B',NULL,'Can Tho','0955555555',NULL,'ROLE_EMPLOYEE','hr@companyb.com',NULL,'2025-05-27 02:08:31'),(6,'employerC','pw789','Company C','','Nha Trang','0966666666','1986-08-08','ROLE_EMPLOYER','hr@companyc.com','2025-05-19 17:52:41','2025-05-19 17:52:41'),(7,'test','$2a$10$CukoeAY3.pvFnT37p/LPJ.qLgmTF8snio.YNd8JIFibajy9A9Ieju','Company D','','Vinh','0977777777','1981-09-09','ROLE_EMPLOYER','hr@companyd.com','2025-05-19 17:52:41','2025-07-15 00:55:12'),(15,'khang','','Duy Khang',NULL,'Cà Mau','0123456789',NULL,'ROLE_EMPLOYER','Khangadmin@gmail.com',NULL,'2025-05-22 16:34:01'),(20,'testreact12','','testreact1',NULL,'','',NULL,'ROLE_EMPLOYER','xyzaaad@gmail.com',NULL,'2025-06-10 12:38:36'),(21,'hvd1','$2a$10$db8GF.T8/i64s6fJb0MvyuUIJDIn2/WnuKS2dZM94mJsAva.oyGia','xxxxxzzz','https://res.cloudinary.com/dxymsdvsz/image/upload/v1747227081/dsghhqj1py6a0rzxe8et.jpg',NULL,NULL,NULL,'ROLE_EMPLOYER','jjj@gmail.com','2025-05-19 17:52:41','2025-05-19 17:52:41'),(25,'khangok','$2a$10$lU5IMIdn07B9iNtKnjwhMeQxYc4UoSQPIsbrZUCIYqqcv4bJPDh9y','Duy Khang','https://res.cloudinary.com/dxymsdvsz/image/upload/v1747845337/cxcxmqsv93iitgrr6wq0.jpg','Cà Mau','0123456789',NULL,'ROLE_EMPLOYER','khang@gmail.com','2025-05-21 23:35:34','2025-05-24 15:03:44'),(29,'khang111','$2a$10$lU5IMIdn07B9iNtKnjwhMeQxYc4UoSQPIsbrZUCIYqqcv4bJPDh9y','Duy Minh',NULL,'Cà Mau','0123456789',NULL,'ROLE_EMPLOYEE','dikhang@gmail.com',NULL,'2025-05-25 19:23:34'),(32,'khang123','$2a$10$5E8zPs7544aw.hez6vSySOnUmwJVBppzzA98oq8YTdJL9qCSK5S/K','Duy Khang','https://res.cloudinary.com/dxymsdvsz/image/upload/v1748307620/hfzauaqyw6ct7d7q9fj1.png','Cà mau','0123456789',NULL,'ROLE_EMPLOYER','dikhang@gmail.com',NULL,NULL),(35,NULL,NULL,'Dũng Hoàng','https://lh3.googleusercontent.com/a/ACg8ocLxAqU06OqRTh0vhMTwVb_dr_LO9RSl62ovN93XhR9pOoTevXQ=s96-c',NULL,NULL,NULL,'ROLE_EMPLOYEE','anhdung113.az@gmail.com','2025-07-14 21:53:01','2025-07-14 21:53:01'),(37,NULL,NULL,'Dung Hoang','https://lh3.googleusercontent.com/a/ACg8ocKYVEYCZD1ROaNqLWmtHlXxhkdpBi1DP2EkG0obIEhMXu7mhw=s96-c',NULL,NULL,NULL,'ROLE_EMPLOYEE','vandunghoang2004@gmail.com','2025-07-15 00:53:08','2025-07-15 00:53:08'),(39,NULL,NULL,'Dũng hoàng','https://lh3.googleusercontent.com/a/ACg8ocLGuE4xTf9vm9JJZp7UQX88kSRuTHWyww6XYSxLgVd5IlFVNg=s96-c',NULL,NULL,NULL,'ROLE_EMPLOYER','vandunghoang4002@gmail.com','2025-07-15 01:18:15','2025-07-15 01:18:15'),(40,NULL,NULL,'Dũng Hoàng Văn','https://lh3.googleusercontent.com/a/ACg8ocKywDbuZ8KGgU6c_0i7AtT4PJGTsnpzaXt4m7UQ4EhoKVv01w=s96-c',NULL,NULL,NULL,'ROLE_EMPLOYER','2251012042dung@ou.edu.vn','2025-07-15 15:04:55','2025-07-15 15:04:55');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_insert_user` AFTER INSERT ON `user` FOR EACH ROW BEGIN
    -- Nếu là ADMIN thì thêm vào bảng admin
    IF NEW.role = 'ROLE_ADMIN' THEN
        INSERT INTO admin (user_id)
        VALUES (NEW.id);
    END IF;

    -- Nếu là EMPLOYEE thì thêm vào bảng employee
    IF NEW.role = 'ROLE_EMPLOYEE' THEN
        INSERT INTO employee (user_id)
        VALUES (NEW.id);
    END IF;
    IF NEW.role = 'ROLE_EMPLOYER' THEN
        INSERT INTO employer (user_id)
        VALUES (NEW.id);
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user_documents`
--

DROP TABLE IF EXISTS `user_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_documents` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `document_type` enum('ID','CV','Diploma') DEFAULT NULL,
  `document_path` varchar(255) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `abc_idx` (`employee_id`),
  CONSTRAINT `abc` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_documents`
--

LOCK TABLES `user_documents` WRITE;
/*!40000 ALTER TABLE `user_documents` DISABLE KEYS */;
INSERT INTO `user_documents` VALUES (1,2,'CV','https://res.cloudinary.com/dxymsdvsz/image/upload/v1747228120/vxccpgim6mwcsyup6mbp.png','2025-04-06 14:55:20','2025-04-06 14:55:20','Của ai'),(2,2,'CV','https://res.cloudinary.com/dxymsdvsz/image/upload/v1747228120/vxccpgim6mwcsyup6mbp.png','2025-04-06 14:55:20','2025-04-06 14:55:20','Abc'),(3,2,'Diploma','https://res.cloudinary.com/dxymsdvsz/image/upload/v1747228120/vxccpgim6mwcsyup6mbp.png','2025-04-06 14:55:20','2025-04-06 14:55:20','Xyz'),(4,2,'CV','https://res.cloudinary.com/dxymsdvsz/image/upload/v1747228120/vxccpgim6mwcsyup6mbp.png','2025-04-06 14:55:20','2025-04-06 14:55:20','Okok'),(14,2,'Diploma','http://res.cloudinary.com/dxymsdvsz/image/upload/v1748286558/sdxrnaohmhm9lfl9khwv.jpg','2025-05-26 19:09:20','2025-05-26 19:09:20','Khang ne'),(15,6,NULL,NULL,'2025-05-26 21:45:49','2025-05-26 21:50:48','Thực tập sinh Java');
/*!40000 ALTER TABLE `user_documents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_notification`
--

DROP TABLE IF EXISTS `user_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_notification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `notification_id` int DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  `read_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employee_id` (`employee_id`),
  KEY `notification_id` (`notification_id`),
  CONSTRAINT `user_notification_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `user_notification_ibfk_2` FOREIGN KEY (`notification_id`) REFERENCES `notification` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_notification`
--

LOCK TABLES `user_notification` WRITE;
/*!40000 ALTER TABLE `user_notification` DISABLE KEYS */;
INSERT INTO `user_notification` VALUES (9,6,4,0,NULL),(10,5,4,0,NULL),(11,6,5,0,NULL),(12,5,5,0,NULL),(16,6,9,0,NULL),(17,6,10,0,NULL),(18,12,10,1,'2025-05-27 04:57:49'),(19,6,11,0,NULL),(20,12,11,1,'2025-05-27 04:57:50'),(21,7,15,0,NULL),(22,7,16,0,NULL),(23,7,17,0,NULL),(24,6,17,0,NULL),(26,7,18,1,'2025-07-16 22:11:17'),(27,6,18,0,NULL),(29,7,19,1,'2025-07-16 22:11:17'),(30,6,19,0,NULL),(32,7,20,1,'2025-07-16 21:21:55'),(33,6,20,0,NULL),(34,7,21,1,'2025-07-16 22:11:15'),(35,6,21,0,NULL),(37,7,22,1,'2025-07-16 22:11:23'),(38,6,22,0,NULL),(40,7,23,0,NULL),(41,6,23,0,NULL);
/*!40000 ALTER TABLE `user_notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'jobplatform'
--

--
-- Dumping routines for database 'jobplatform'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-16 22:25:57
