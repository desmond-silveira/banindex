-- MySQL dump 10.13  Distrib 5.1.73, for redhat-linux-gnu (x86_64)
--
-- Host: localhost    Database: statstrats
-- ------------------------------------------------------
-- Server version       5.1.73-log

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
-- Table structure for table `champion`
--

DROP TABLE IF EXISTS `champion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `champion` (
  `id` int(11) NOT NULL,
  `wins` int(11) DEFAULT NULL,
  `games` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `champion`
--

LOCK TABLES `champion` WRITE;
/*!40000 ALTER TABLE `champion` DISABLE KEYS */;
INSERT INTO `champion` VALUES (1,4601,8723),(2,635,1343),(3,1049,1809),(4,2728,5405),(5,4120,7423),(6,1139,2222),(7,7644,14911),(8,2543,4786),(9,1822,3735),(10,1362,2568),(11,8741,15458),(12,2421,4639),(13,2896,5990),(14,869,1869),(15,2524,4879),(16,1947,3717),(17,8908,16040),(18,1174,2585),(19,1386,2661),(20,1156,2473),(21,1116,2378),(22,5148,10153),(23,2451,4623),(24,4050,7093),(25,7125,13127),(26,789,1892),(27,854,1754),(28,5217,9016),(29,952,2109),(30,4414,8125),(31,3150,6282),(32,2124,3994),(33,534,1043),(34,709,1641),(35,10964,18481),(36,1307,2556),(37,7284,10577),(38,4020,8152),(39,964,1765),(40,651,1299),(41,2740,5454),(42,650,1521),(43,3173,6086),(44,427,865),(45,3063,6040),(48,396,797),(50,2207,4299),(51,1172,2467),(53,7723,14399),(54,4532,8374),(55,9039,14704),(56,485,952),(57,3784,6807),(58,482,1000),(59,689,1341),(60,220,482),(61,1849,3451),(62,4156,7207),(63,1760,3640),(64,2545,5538),(67,1558,3206),(68,537,960),(69,802,1753),(72,429,838),(74,1642,3348),(75,3799,7186),(76,13100,22451),(77,782,1530),(78,1444,2577),(79,923,1969),(80,2182,4508),(81,15134,24794),(82,1637,3282),(83,735,1441),(84,3445,6842),(85,964,1851),(86,1296,2723),(89,2331,4790),(90,1447,2817),(91,1808,3289),(92,4154,7986),(96,814,1610),(98,1139,2154),(99,7685,15043),(101,1402,3163),(102,207,457),(103,5777,10468),(104,1219,2420),(105,6497,11551),(106,618,1229),(107,1465,2802),(110,975,2140),(111,676,1332),(112,631,1325),(113,1351,2450),(114,3032,5579),(115,2994,6070),(117,1599,3098),(119,1309,2603),(120,9906,15670),(121,1390,2772),(122,2028,3925),(126,2444,5030),(127,1132,2398),(131,1751,3111),(133,740,1459),(134,1920,3905),(143,927,1822),(150,591,1353),(154,195,399),(157,2929,6018),(161,1692,3695),(201,273,573),(222,9579,18016),(236,4611,9278),(238,10321,16119),(254,3547,6719),(266,473,903),(267,998,1979),(268,2184,4339),(412,1023,2483),(421,614,1386),(429,479,1099),(432,2529,5900);
/*!40000 ALTER TABLE `champion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `match_stats`
--

DROP TABLE IF EXISTS `match_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `match_stats` (
  `id` varchar(16) NOT NULL DEFAULT 'match_stats',
  `total_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_stats`
--

LOCK TABLES `match_stats` WRITE;
/*!40000 ALTER TABLE `match_stats` DISABLE KEYS */;
/*!40000 ALTER TABLE `match_stats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nurf_interval`
--

DROP TABLE IF EXISTS `nurf_interval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nurf_interval` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `begin_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `region` varchar(16) NOT NULL,
  `match_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `begin_date_region_match` (`begin_date`,`region`,`match_id`),
  KEY `nurf_interval_region_match_idx` (`region`,`match_id`),
  CONSTRAINT `nurf_interval_raw_match_data_region_match` FOREIGN KEY (`region`, `match_id`) REFERENCES `raw_match_data` (`region`, `match_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=114625 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nurf_interval`
--

LOCK TABLES `nurf_interval` WRITE;
/*!40000 ALTER TABLE `nurf_interval` DISABLE KEYS */;
/*!40000 ALTER TABLE `nurf_interval` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `raw_match_data`
--

DROP TABLE IF EXISTS `raw_match_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `raw_match_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `region` varchar(16) NOT NULL,
  `match_id` bigint(20) NOT NULL,
  `json` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `region_match` (`region`,`match_id`),
  CONSTRAINT `raw_match_data_region_id` FOREIGN KEY (`region`) REFERENCES `region` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=114676 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `raw_match_data`
--

LOCK TABLES `raw_match_data` WRITE;
/*!40000 ALTER TABLE `raw_match_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `raw_match_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `region` (
  `id` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region`
--

LOCK TABLES `region` WRITE;
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
INSERT INTO `region` VALUES ('br'),('eune'),('euw'),('kr'),('lan'),('las'),('na'),('oce'),('ru'),('tr');
/*!40000 ALTER TABLE `region` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-04-17 19:18:32
