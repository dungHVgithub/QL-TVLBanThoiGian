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
  `id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (2);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'IT'),(2,'Kế toán'),(4,'Sale'),(3,'Tiếp Thị& Makerting');
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
  CONSTRAINT `company_images_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `company_information` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_images`
--

LOCK TABLES `company_images` WRITE;
/*!40000 ALTER TABLE `company_images` DISABLE KEYS */;
INSERT INTO `company_images` VALUES (1,1,'images/companyA_front.png','Mặt tiền công ty A','2025-04-06 15:04:30'),(2,1,'images/companyA_team.jpg','Team công ty A','2025-04-06 15:04:30'),(3,1,'images/companyA_office.png','Văn phòng công ty A','2025-04-06 15:04:30'),(4,2,'images/companyB_logo.jpg','Logo công ty B','2025-04-06 15:04:30'),(5,2,'images/companyB_event.png','Sự kiện tuyển dụng công ty B','2025-04-06 15:04:30'),(6,3,'images/companyC_hall.jpg','Sảnh công ty C','2025-04-06 15:04:30'),(7,4,'images/companyD_award.jpg','Giải thưởng công ty D','2025-04-06 15:04:30'),(8,5,'images/companyE_building.png','Tòa nhà công ty E','2025-04-06 15:04:30');
/*!40000 ALTER TABLE `company_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_information`
--

DROP TABLE IF EXISTS `company_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_information` (
  `id` int NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `tax_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_information`
--

LOCK TABLES `company_information` WRITE;
/*!40000 ALTER TABLE `company_information` DISABLE KEYS */;
INSERT INTO `company_information` VALUES (1,'Company A','HCM','1234567890'),(2,'Company B','HN','9876543210'),(3,'Company C','Hue','1357924680'),(4,'Company D','Da Nang','2468013579'),(5,'Company E','Can Tho','1122334455');
/*!40000 ALTER TABLE `company_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` int NOT NULL,
  `level` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'Junior'),(4,'Senior'),(8,'Middle'),(9,'Junior'),(10,'Senior');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employer`
--

DROP TABLE IF EXISTS `employer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employer` (
  `id` int NOT NULL,
  `company` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `company` (`company`),
  CONSTRAINT `employer_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`),
  CONSTRAINT `employer_ibfk_2` FOREIGN KEY (`company`) REFERENCES `company_information` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employer`
--

LOCK TABLES `employer` WRITE;
/*!40000 ALTER TABLE `employer` DISABLE KEYS */;
INSERT INTO `employer` VALUES (2,1),(5,2),(6,3),(7,4);
/*!40000 ALTER TABLE `employer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follow_notice`
--

DROP TABLE IF EXISTS `follow_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follow_notice` (
  `employee_id` int NOT NULL,
  `employer_id` int NOT NULL,
  `notice` text,
  `isFollow` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`employee_id`,`employer_id`),
  KEY `employer_id` (`employer_id`),
  CONSTRAINT `follow_notice_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `follow_notice_ibfk_2` FOREIGN KEY (`employer_id`) REFERENCES `employer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follow_notice`
--

LOCK TABLES `follow_notice` WRITE;
/*!40000 ALTER TABLE `follow_notice` DISABLE KEYS */;
INSERT INTO `follow_notice` VALUES (1,2,'thong bao1',1),(1,5,'thong bao3',1),(4,2,'thong bao4',1),(4,5,'thong bao2',1);
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
  CONSTRAINT `hoso_ungtuyen_ibfk_1` FOREIGN KEY (`job_id`) REFERENCES `job_posting` (`id`),
  CONSTRAINT `hoso_ungtuyen_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoso_ungtuyen`
--

LOCK TABLES `hoso_ungtuyen` WRITE;
/*!40000 ALTER TABLE `hoso_ungtuyen` DISABLE KEYS */;
INSERT INTO `hoso_ungtuyen` VALUES (1,1,'isPending','Tôi mong muốn tham gia','Chúng tôi sẽ xem xét'),(2,1,'isApproved','Xin cảm ơn','Chào mừng bạn'),(3,4,'isRejected','Tôi chưa có kinh nghiệm','Rất tiếc'),(4,4,'isPending','Mong có cơ hội học hỏi','Chờ duyệt'),(5,1,'quit','Tôi đã rút hồ sơ','OK');
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
  `job_posting_id` int DEFAULT NULL,
  `description` text,
  `type` enum('Job_Description','Skill_Requirement') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `job_posting_id` (`job_posting_id`),
  CONSTRAINT `job_description_ibfk_1` FOREIGN KEY (`job_posting_id`) REFERENCES `job_posting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_description`
--

LOCK TABLES `job_description` WRITE;
/*!40000 ALTER TABLE `job_description` DISABLE KEYS */;
/*!40000 ALTER TABLE `job_description` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_posting`
--

DROP TABLE IF EXISTS `job_posting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_posting` (
  `id` int NOT NULL,
  `description` text,
  `salary` double DEFAULT NULL,
  `time_start` time DEFAULT NULL,
  `time_end` time DEFAULT NULL,
  `state` enum('pending','approved','rejected') DEFAULT NULL,
  `employer_id` int DEFAULT NULL,
  `approved_by_admin_id` int DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `submit_end` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employer_id` (`employer_id`),
  KEY `approved_by_admin_id` (`approved_by_admin_id`),
  KEY `job_posting_ibfk_3_idx` (`category_id`),
  CONSTRAINT `job_posting_ibfk_1` FOREIGN KEY (`employer_id`) REFERENCES `employer` (`id`),
  CONSTRAINT `job_posting_ibfk_2` FOREIGN KEY (`approved_by_admin_id`) REFERENCES `admin` (`id`),
  CONSTRAINT `job_posting_ibfk_3` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_posting`
--

LOCK TABLES `job_posting` WRITE;
/*!40000 ALTER TABLE `job_posting` DISABLE KEYS */;
INSERT INTO `job_posting` VALUES (1,'Backend Developer',1200,'08:00:00','17:00:00','approved',2,2,1,'2025-05-01'),(2,'Frontend Developer',1000,'08:30:00','17:30:00','approved',5,2,1,'2025-05-01'),(3,'DevOps Engineer',1300,'09:00:00','18:00:00','pending',5,2,1,'2025-05-01'),(4,'Tester',900,'08:00:00','16:00:00','approved',2,2,1,'2025-05-01'),(5,'Project Manager',1500,'08:00:00','17:00:00','rejected',2,2,1,'2025-05-01'),(6,'Mobile App Developer',1100,'08:00:00','17:00:00','approved',6,2,1,'2025-05-01'),(7,'UI/UX Designer',950,'09:00:00','18:00:00','pending',7,2,2,'2025-05-01'),(8,'System Analyst',1250,'08:30:00','17:30:00','approved',6,2,2,'2025-05-01'),(9,'Tech Lead',1700,'08:00:00','17:00:00','approved',7,2,2,'2025-05-01'),(10,'Database Engineer',1150,'09:00:00','18:00:00','rejected',6,2,2,'2025-05-01'),(11,'Sale milk',1150,'08:00:00','17:00:00','pending',2,2,3,'2025-05-01');
/*!40000 ALTER TABLE `job_posting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `sdt` varchar(20) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `verification_status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'john123','$2a$10$CukoeAY3.pvFnT37p/LPJ.qLgmTF8snio.YNd8JIFibajy9A9Ieju','John Doe','','Hanoi','0911111111','1990-01-01','ROLE_EMPLOYEE','john@example.com',1),(2,'hvd','$2a$10$CukoeAY3.pvFnT37p/LPJ.qLgmTF8snio.YNd8JIFibajy9A9Ieju','Company A','','HCM','0922222222','1985-03-10','ROLE_ADMIN','hr@companya.com',1),(4,'jane456','xyz456','Jane Smith','','Hue','0944444444','1992-07-15','ROLE_EMPLOYEE','jane@jobmail.com',1),(5,'bossB','pw456','Boss B','','Can Tho','0955555555','1983-11-11','ROLE_EMPLOYER','hr@companyb.com',0),(6,'employerC','pw789','Company C','','Nha Trang','0966666666','1986-08-08','ROLE_EMPLOYER','hr@companyc.com',1),(7,'employerD','pw890','Company D','','Vinh','0977777777','1981-09-09','ROLE_EMPLOYER','hr@companyd.com',1),(8,'alice_dev','pw321','Alice Dev','','Da Lat','0988888888','1995-10-10','ROLE_EMPLOYEE','alice@example.com',1),(9,'bob_test','pw654','Bob Tester','','Quy Nhon','0999999999','1993-11-11','ROLE_EMPLOYEE','bob@example.com',1),(10,'carol_ui','pw987','Carol UI','','Bien Hoa','0900000000','1991-12-12','ROLE_EMPLOYEE','carol@example.com',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_documents`
--

DROP TABLE IF EXISTS `user_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_documents` (
  `id` int NOT NULL,
  `User_id` int DEFAULT NULL,
  `document_type` enum('ID','CV','Diploma') DEFAULT NULL,
  `document_path` varchar(255) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `status` enum('pending','approved','rejected') DEFAULT NULL,
  `approved_by_admin_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `User_id` (`User_id`),
  KEY `approved_by_admin_id` (`approved_by_admin_id`),
  CONSTRAINT `user_documents_ibfk_1` FOREIGN KEY (`User_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_documents_ibfk_2` FOREIGN KEY (`approved_by_admin_id`) REFERENCES `admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_documents`
--

LOCK TABLES `user_documents` WRITE;
/*!40000 ALTER TABLE `user_documents` DISABLE KEYS */;
INSERT INTO `user_documents` VALUES (1,1,'ID','docs/john_id.png','2025-04-06 14:55:20','approved',2),(2,1,'CV','docs/john_cv.pdf','2025-04-06 14:55:20','approved',2),(3,4,'ID','docs/jane_id.png','2025-04-06 14:55:20','pending',2),(4,4,'CV','docs/jane_cv.pdf','2025-04-06 14:55:20','approved',2),(5,2,'Diploma','docs/employerA_diploma.pdf','2025-04-06 14:55:20','rejected',2);
/*!40000 ALTER TABLE `user_documents` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-03 16:22:33
