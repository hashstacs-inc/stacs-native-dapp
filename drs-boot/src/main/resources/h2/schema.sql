
CREATE TABLE IF NOT EXISTS `tx_request` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `tx_id` varchar(64) NOT NULL COMMENT 'the transaction id',
    `policy_id` varchar(32) NOT NULL COMMENT 'policy id',
    `submitter` varchar(40) NOT NULL COMMENT 'tx submitter',
    `bd_code` varchar(32) NOT NULL COMMENT 'tx business code',
    `func_name` varchar(128) NOT NULL COMMENT 'tx function name for BD',
    `tx_data` MEDIUMTEXT NOT NULL  COMMENT 'the request json data',
    `block_height` bigint(20) DEFAULT NULL COMMENT 'block height',
    `tx_receipt` TEXT DEFAULT NULL COMMENT 'tx receipt json data',
    `status` varchar(16) NOT NULL COMMENT 'status,INIT、SUBMITTING、PROCESSING、END',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT 'create time',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`),
    UNIQUE (`tx_id`)
) ;

CREATE TABLE IF NOT EXISTS `tx_callback` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `block_height` bigint(20) DEFAULT NULL COMMENT 'block height',
 `tx_receipts` TEXT DEFAULT NULL COMMENT 'tx receipt json datas',
 `status` varchar(16) NOT NULL COMMENT 'status,INIT、PROCESSED',
 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT 'create time',
 `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
 PRIMARY KEY (`id`),
 UNIQUE (`block_height`)
);

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
  UNIQUE (`name`)
);