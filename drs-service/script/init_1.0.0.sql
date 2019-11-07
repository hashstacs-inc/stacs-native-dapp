
CREATE TABLE IF NOT EXISTS `tx_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tx_id` varchar(64) NOT NULL COMMENT 'the transaction id',
  `policy_id` varchar(32) NOT NULL COMMENT 'policy id',
  `tx_api` varchar(64) NOT NULL COMMENT 'tx request api',
  `tx_data` MEDIUMTEXT NOT NULL  COMMENT 'the request json data',
  `block_height` bigint(20) DEFAULT NULL COMMENT 'block height',
  `tx_receipt` TEXT DEFAULT NULL COMMENT 'tx receipt json data',
  `status` varchar(16) NOT NULL COMMENT 'status,INIT、PROCESSING、END',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT 'create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_tx_id` (`tx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'the request tx info';

CREATE TABLE IF NOT EXISTS `tx_callback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `block_height` bigint(20) DEFAULT NULL COMMENT 'block height',
  `tx_receipts` TEXT DEFAULT NULL COMMENT 'tx receipt json datas',
  `status` varchar(16) NOT NULL COMMENT 'status,INIT、PROCESSED',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT 'create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_block_height` (`block_height`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'the callback info';