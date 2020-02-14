
CREATE TABLE IF NOT EXISTS `app_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT 'the app name',
  `version` VARCHAR(16) NOT NULL  COMMENT 'the app version',
  `version_code` int(8) DEFAULT 0 COMMENT 'code of version',
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


CREATE TABLE IF NOT EXISTS `app_upgrade_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT 'the app name',
  `version` VARCHAR(16) NOT NULL  COMMENT 'the app version',
  `version_code` int(8) DEFAULT 0 COMMENT 'code of version',
  `context_path` varchar(32) DEFAULT NULL COMMENT 'context path for web app',
  `run_error` varchar(128) DEFAULT NULL COMMENT 'app run error info',
  `file_name` varchar(128) NOT NULL COMMENT 'app jar file name',
  `config_name` varchar(128) NOT NULL COMMENT 'app config file name',
  `icon` varchar(128) DEFAULT NULL COMMENT 'app icon url',
  `author` varchar(64) DEFAULT NULL COMMENT 'author of app ',
  `remark` varchar(128) DEFAULT NULL COMMENT 'the app description',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT 'create time',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'the dapp upgrade history';


CREATE TABLE IF NOT EXISTS `sys_conf` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `_key` varchar(64) NOT NULL COMMENT 'key name',
  `_value` VARCHAR(4096) NOT NULL  COMMENT 'value',
  `remark` varchar(128) DEFAULT NULL COMMENT 'remark of config',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT 'create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_key` (`_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'sys_conf';