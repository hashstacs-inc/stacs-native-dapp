
CREATE TABLE IF NOT EXISTS `app_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT 'the app name',
  `version` VARCHAR(16) NOT NULL  COMMENT 'the app version',
  `context_path` varchar(32) DEFAULT NULL COMMENT 'context path for web app',
  `status` varchar(16) NOT NULL COMMENT 'status,DOWNLOAD、INITIALIZED、RUNNING、STOPPED',
  `run_error` varchar(128) DEFAULT NULL COMMENT 'app run error info',
  `file_name` varchar(64) NOT NULL COMMENT 'app jar file name',
  `icon` varchar(128) DEFAULT NULL COMMENT 'app icon url',
  `author` varchar(64) DEFAULT NULL COMMENT 'author of app ',
  `remark` varchar(128) DEFAULT NULL COMMENT 'the app description',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT 'create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'the dapp info table';