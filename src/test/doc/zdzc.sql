/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.34 : Database - gpsdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gpsdb` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `gpsdb`;

/*Table structure for table `t_gps` */

DROP TABLE IF EXISTS `t_gps`;

CREATE TABLE `t_gps` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `device_id` varchar(32) DEFAULT NULL COMMENT '终端设备号',
  `alarm_status` varchar(32) DEFAULT NULL COMMENT '报警标志 4位',
  `vehicle_status` varchar(32) DEFAULT NULL COMMENT '状态 4位',
  `longitude` double DEFAULT NULL COMMENT '经度 单位：度',
  `latitude` double DEFAULT NULL COMMENT '纬度 单位：度',
  `height` double DEFAULT NULL COMMENT '海拔高度，米(m)',
  `speed` double DEFAULT NULL COMMENT '速度km/h',
  `direction` double DEFAULT NULL COMMENT '方向 0-359，正北为 0，顺时针',
  `time` datetime DEFAULT NULL COMMENT 'GPS时间  (GMT+8 时间)',
  `milestone` double unsigned DEFAULT NULL COMMENT '里程 km',
  `oil` double DEFAULT NULL COMMENT '油量 L',
  `speed2` double DEFAULT NULL COMMENT '记录仪速度 km/h',
  `signal_status` varchar(32) DEFAULT NULL COMMENT '扩展车辆信号状态位',
  `io_status` varchar(32) DEFAULT NULL COMMENT 'IO状态位',
  `analog` varchar(32) DEFAULT NULL COMMENT '模拟量',
  `wiff` int(11) DEFAULT NULL COMMENT '无线通信网络信号强度',
  `satellite_num` int(11) DEFAULT NULL COMMENT '定位卫星数',
  `create_time` datetime DEFAULT NULL COMMENT '写库时间',
  `vendor` int(11) DEFAULT '0' COMMENT '标识（1：迈的）',
  PRIMARY KEY (`id`),
  KEY `ix_jt808_vehicle_sim` (`device_id`),
  KEY `ix_jt808_gtm` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_gps` */

/*Table structure for table `t_gps_alarm` */

DROP TABLE IF EXISTS `t_gps_alarm`;

CREATE TABLE `t_gps_alarm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `device_id` varchar(32) DEFAULT NULL COMMENT '终端设备号',
  `alarm_status` varchar(32) DEFAULT NULL COMMENT '报警标志 4位',
  `alarm_handle` int(1) DEFAULT '0' COMMENT '报警是否处理（0：否；1：是）',
  `vehicle_status` varchar(32) DEFAULT NULL COMMENT '状态 4位',
  `longitude` double DEFAULT NULL COMMENT '经度 单位：度',
  `latitude` double DEFAULT NULL COMMENT '纬度 单位：度',
  `height` double DEFAULT NULL COMMENT '海拔高度，米(m)',
  `speed` double DEFAULT NULL COMMENT '速度km/h',
  `direction` double DEFAULT NULL COMMENT '方向 0-359，正北为 0，顺时针',
  `time` datetime DEFAULT NULL COMMENT 'GPS时间  (GMT+8 时间)',
  `milestone` double unsigned DEFAULT NULL COMMENT '里程 km',
  `oil` double DEFAULT NULL COMMENT '油量 L',
  `speed2` double DEFAULT NULL COMMENT '记录仪速度 km/h',
  `signal_status` varchar(32) DEFAULT NULL COMMENT '扩展车辆信号状态位',
  `io_status` varchar(32) DEFAULT NULL COMMENT 'IO状态位',
  `analog` varchar(32) DEFAULT NULL COMMENT '模拟量',
  `wiff` int(11) DEFAULT NULL COMMENT '无线通信网络信号强度',
  `satellite_num` int(11) DEFAULT NULL COMMENT '定位卫星数',
  `create_time` datetime DEFAULT NULL COMMENT '写库时间',
  `vendor` int(11) DEFAULT '0' COMMENT '标识（1：迈的）',
  PRIMARY KEY (`id`),
  KEY `ix_jt808_vehicle_sim` (`device_id`),
  KEY `ix_jt808_gtm` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_gps_alarm` */

/*Table structure for table `t_gps_snapshoot` */

DROP TABLE IF EXISTS `t_gps_snapshoot`;

CREATE TABLE `t_gps_snapshoot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `device_id` varchar(32) DEFAULT NULL COMMENT '终端设备号',
  `alarm_status` varchar(32) DEFAULT NULL COMMENT '报警标志 4位',
  `vehicle_status` varchar(32) DEFAULT NULL COMMENT '状态 4位',
  `longitude` double DEFAULT NULL COMMENT '经度 单位：度',
  `latitude` double DEFAULT NULL COMMENT '纬度 单位：度',
  `height` double DEFAULT NULL COMMENT '海拔高度，米(m)',
  `speed` double DEFAULT NULL COMMENT '速度km/h',
  `direction` double DEFAULT NULL COMMENT '方向 0-359，正北为 0，顺时针',
  `time` datetime DEFAULT NULL COMMENT 'GPS时间  (GMT+8 时间)',
  `milestone` double unsigned DEFAULT NULL COMMENT '里程 km',
  `oil` double DEFAULT NULL COMMENT '油量 L',
  `speed2` double DEFAULT NULL COMMENT '记录仪速度 km/h',
  `signal_status` varchar(32) DEFAULT NULL COMMENT '扩展车辆信号状态位',
  `io_status` varchar(32) DEFAULT NULL COMMENT 'IO状态位',
  `analog` varchar(32) DEFAULT NULL COMMENT '模拟量',
  `wiff` int(11) DEFAULT NULL COMMENT '无线通信网络信号强度',
  `satellite_num` int(11) DEFAULT NULL COMMENT '定位卫星数',
  `create_time` datetime DEFAULT NULL COMMENT '写库时间',
  `vendor` int(11) DEFAULT '0' COMMENT '标识（1：迈的）',
  PRIMARY KEY (`id`),
  KEY `ix_jt808_vehicle_sim` (`device_id`),
  KEY `ix_jt808_gtm` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_gps_snapshoot` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
