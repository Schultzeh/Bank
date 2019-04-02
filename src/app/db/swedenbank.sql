-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.25-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             10.1.0.5464
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for swedenbank
CREATE DATABASE IF NOT EXISTS `swedenbank` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `swedenbank`;

-- Dumping structure for table swedenbank.account
CREATE TABLE IF NOT EXISTS `account` (
  `account_number` int(8) unsigned NOT NULL AUTO_INCREMENT,
  `account_name` varchar(50) DEFAULT NULL,
  `owner` varchar(20) DEFAULT NULL,
  `account_type` varchar(20) DEFAULT 'savings',
  `balance` float DEFAULT '0',
  PRIMARY KEY (`account_number`),
  KEY `owner` (`owner`)
) ENGINE=InnoDB AUTO_INCREMENT=64574460 DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table swedenbank.transaction
CREATE TABLE IF NOT EXISTS `transaction` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `from_account` int(11) unsigned DEFAULT NULL,
  `to_account` int(11) unsigned DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `message` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sender` (`from_account`),
  KEY `receiver` (`to_account`)
) ENGINE=InnoDB AUTO_INCREMENT=564 DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table swedenbank.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `social_number` varchar(20) DEFAULT '',
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `mail` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Social_number` (`social_number`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for view swedenbank.persondata
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `persondata` (
	`account_number` INT(8) UNSIGNED NOT NULL,
	`account_name` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
	`owner` VARCHAR(20) NULL COLLATE 'utf8mb4_general_ci',
	`account_type` VARCHAR(20) NULL COLLATE 'utf8mb4_general_ci',
	`balance` FLOAT NULL
) ENGINE=MyISAM;

-- Dumping structure for procedure swedenbank.create_account
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `create_account`(
	IN `a_name` VARCHAR(50),
	IN `social_num` VARCHAR(50)
)
BEGIN
INSERT INTO account SET
account_name = a_name,
owner = social_num;
END//
DELIMITER ;

-- Dumping structure for procedure swedenbank.create_card_transaction
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `create_card_transaction`()
BEGIN
INSERT INTO transaction SET
from_account = t_sender,
to_account = "Bank",
amount = "t_amount",
message = t_message;
UPDATE account SET balance = (balance-t_amount) WHERE account_number = t_sender;
UPDATE account SET balance = (balance+t_amount) WHERE account_number = "64574453";
END//
DELIMITER ;

-- Dumping structure for procedure swedenbank.create_transaction
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `create_transaction`(
	IN `t_sender` INT,
	IN `t_receiver` INT,
	IN `t_amount` FLOAT,
	IN `t_message` VARCHAR(50)



)
BEGIN
INSERT INTO transaction SET
from_account = t_sender,
to_account = t_receiver,
amount = t_amount,
message = t_message;
UPDATE account SET balance = (balance-t_amount) WHERE account_number = t_sender;
UPDATE account SET balance = (balance+t_amount) WHERE account_number = t_receiver;
END//
DELIMITER ;

-- Dumping structure for procedure swedenbank.delete_account
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_account`(
	IN `a_number` VARCHAR(50)
)
BEGIN
DELETE FROM account WHERE account_number = a_number;
END//
DELIMITER ;

-- Dumping structure for procedure swedenbank.list_latest_five_transactions
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `list_latest_five_transactions`(
	IN `social_num` VARCHAR(50)
)
BEGIN
    SELECT * FROM transaction
    WHERE to_account IN (SELECT account_number FROM ACCOUNT WHERE OWNER = social_num)
    OR from_account IN (SELECT account_number FROM ACCOUNT WHERE OWNER = social_num)
    ORDER BY date DESC
    LIMIT 5;
END//
DELIMITER ;

-- Dumping structure for procedure swedenbank.rename_account
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `rename_account`(
	IN `a_name` VARCHAR(50),
	IN `a_number` VARCHAR(50)


)
BEGIN
UPDATE account SET account_name = a_name WHERE account_number = a_number;
END//
DELIMITER ;

-- Dumping structure for procedure swedenbank.test
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `test`()
BEGIN

UPDATE account SET balance = (balance-t_amount) WHERE account_number = t_sender;
END//
DELIMITER ;

-- Dumping structure for event swedenbank.salary_payment
DELIMITER //
CREATE DEFINER=`root`@`localhost` EVENT `salary_payment` ON SCHEDULE EVERY 1 MONTH STARTS '2019-04-25 00:00:10' ENDS '2020-04-25 18:41:24' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
INSERT INTO transaction (from_account, to_account, amount, message)
SELECT
64574453, account_number, 10000, "Lön"
	FROM `account` WHERE account_type = "salary";
	
UPDATE account SET balance = (balance-30000) WHERE account_number = 64574453;
UPDATE account SET balance = (balance+10000) WHERE account_number IN(SELECT account_number WHERE account_type = "salary");
END//
DELIMITER ;

-- Dumping structure for view swedenbank.persondata
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `persondata`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `persondata` AS select `account`.`account_number` AS `account_number`,`account`.`account_name` AS `account_name`,`account`.`owner` AS `owner`,`account`.`account_type` AS `account_type`,`account`.`balance` AS `balance` from `account` where (`account`.`owner` = '890505-4113');

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
