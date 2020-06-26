/*
Navicat MySQL Data Transfer

Source Server         : xujin环境
Source Server Version : 50726
Source Host           : 10.105.3.33:21951
Source Database       : drs

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-06-17 15:26:41
*/

CREATE DATABASE drs;

use drs;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_info
-- ----------------------------
DROP TABLE IF EXISTS `app_info`;
CREATE TABLE `app_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT 'the app name',
  `version` varchar(16) NOT NULL COMMENT 'the app version',
  `version_code` int(8) DEFAULT '0' COMMENT 'code of version',
  `context_path` varchar(32) DEFAULT NULL COMMENT 'context path for web app',
  `status` varchar(16) NOT NULL COMMENT 'status,DOWNLOAD、INITIALIZED、RUNNING、STOPPED',
  `run_error` varchar(128) DEFAULT NULL COMMENT 'app run error info',
  `file_name` varchar(64) NOT NULL COMMENT 'app jar file name',
  `icon` varchar(128) DEFAULT NULL COMMENT 'app icon url',
  `author` varchar(64) DEFAULT NULL COMMENT 'author of app ',
  `remark` varchar(128) DEFAULT NULL COMMENT 'the app description',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='the dapp info table';

-- ----------------------------
-- Table structure for app_upgrade_history
-- ----------------------------
DROP TABLE IF EXISTS `app_upgrade_history`;
CREATE TABLE `app_upgrade_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT 'the app name',
  `version` varchar(16) NOT NULL COMMENT 'the app version',
  `version_code` int(8) DEFAULT '0' COMMENT 'code of version',
  `context_path` varchar(32) DEFAULT NULL COMMENT 'context path for web app',
  `run_error` varchar(128) DEFAULT NULL COMMENT 'app run error info',
  `file_name` varchar(128) NOT NULL COMMENT 'app jar file name',
  `config_name` varchar(128) NOT NULL COMMENT 'app config file name',
  `icon` varchar(128) DEFAULT NULL COMMENT 'app icon url',
  `author` varchar(64) DEFAULT NULL COMMENT 'author of app ',
  `remark` varchar(128) DEFAULT NULL COMMENT 'the app description',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='the dapp upgrade history';

-- ----------------------------
-- Table structure for block
-- ----------------------------
DROP TABLE IF EXISTS `block`;
CREATE TABLE `block` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `height` bigint(20) NOT NULL COMMENT 'block height',
  `version` varchar(32) NOT NULL COMMENT 'version',
  `previous_hash` varchar(64) NOT NULL COMMENT 'previous block hash',
  `block_hash` varchar(64) NOT NULL COMMENT 'block hash',
  `tx_root_hash` varchar(64) NOT NULL COMMENT 'transaction merkle tree root hash',
  `account_root_hash` varchar(64) NOT NULL COMMENT 'account state merkel tree root hash',
  `contract_root_hash` varchar(64) NOT NULL COMMENT 'contract state merkel tree root hash',
  `policy_root_hash` varchar(64) NOT NULL COMMENT 'policy merkle tree root hash',
  `rs_root_hash` varchar(64) NOT NULL COMMENT 'rs merkle tree root hash',
  `tx_receipt_root_hash` varchar(64) NOT NULL COMMENT 'tx receipt merkel tree root hash',
  `ca_root_hash` varchar(64) NOT NULL COMMENT 'ca merkel tree root hash',
  `state_root_hash` varchar(64) DEFAULT NULL COMMENT 'state root hash',
  `block_time` bigint(20) NOT NULL COMMENT 'block time',
  `tx_num` int(8) DEFAULT '0' COMMENT 'transaction num',
  `total_tx_num` bigint(20) DEFAULT '0' COMMENT 'total transaction num',
  `total_block_size` decimal(8,2) DEFAULT NULL COMMENT 'total block size,unit:kb',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_block` (`height`)
) ENGINE=InnoDB AUTO_INCREMENT=46077 DEFAULT CHARSET=utf8mb4 COMMENT='block';

-- ----------------------------
-- Table structure for block_callback
-- ----------------------------
DROP TABLE IF EXISTS `block_callback`;
CREATE TABLE `block_callback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `block_height` bigint(20) DEFAULT NULL COMMENT 'block height',
  `block_data` MEDIUMTEXT COMMENT 'block json data',
  `status` varchar(16) NOT NULL COMMENT 'status,INIT、PROCESSED',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_block_height` (`block_height`)
) ENGINE=InnoDB AUTO_INCREMENT=19166 DEFAULT CHARSET=utf8mb4 COMMENT='the callback info';

-- ----------------------------
-- Table structure for business_define
-- ----------------------------
DROP TABLE IF EXISTS `business_define`;
CREATE TABLE `business_define` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) NOT NULL DEFAULT '' COMMENT 'bd code',
  `name` varchar(64) DEFAULT NULL,
  `bd_type` varchar(32) DEFAULT NULL COMMENT 'bd type',
  `desc` varchar(1024) DEFAULT NULL COMMENT 'bd desc',
  `init_permission` varchar(64) DEFAULT NULL COMMENT 'bd init permission',
  `init_policy` varchar(32) DEFAULT NULL COMMENT 'bd init policy',
  `functions` varchar(10240) NOT NULL COMMENT 'bd functions',
  `bd_version` varchar(4) NOT NULL DEFAULT '' COMMENT 'bd version',
  `create_time` bigint(20) NOT NULL COMMENT 'create time',
  `update_time` datetime(3) DEFAULT NULL COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_bd_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for contract
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `address` varchar(64) NOT NULL COMMENT 'contract address',
  `name` varchar(64) DEFAULT '' COMMENT 'name',
  `extension` varchar(1024) DEFAULT '' COMMENT 'extension',
  `bd_code` varchar(255) NOT NULL,
  `symbol` varchar(64) DEFAULT NULL,
  `bd_code_version` varchar(4) NOT NULL DEFAULT '' COMMENT 'bd version',
  `status` varchar(32) NOT NULL,
  `block_height` bigint(20) DEFAULT NULL COMMENT 'block height',
  `tx_id` varchar(64) NOT NULL COMMENT 'the id create transaction',
  `action_index` int(11) NOT NULL COMMENT 'the index create action',
  `language` varchar(32) DEFAULT NULL COMMENT 'contract code language',
  `version` varchar(5) NOT NULL,
  `code` text NOT NULL COMMENT 'contract code',
  `create_time` bigint(20) NOT NULL COMMENT 'create time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_address` (`address`),
  UNIQUE KEY `uniq_txid_actionindex` (`tx_id`,`action_index`)
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8mb4 COMMENT='smart contract';

-- ----------------------------
-- Table structure for policy
-- ----------------------------
DROP TABLE IF EXISTS `policy`;
CREATE TABLE `policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `policy_id` varchar(32) NOT NULL COMMENT 'policyID',
  `policy_name` varchar(64) NOT NULL COMMENT 'policy name',
  `domain_ids` varchar(1024) NOT NULL COMMENT 'the id list create related to domain',
  `decision_type` varchar(16) NOT NULL COMMENT 'the decision type for vote ,1.FULL_VOTE,2.ONE_VOTE',
  `vote_pattern` varchar(16) NOT NULL COMMENT 'rs vote pattern 1.SYNC 2.ASYNC',
  `callback_type` varchar(16) NOT NULL COMMENT 'callback type of slave 1.ALL 2.SELF',
  `verifyNum` int(8) DEFAULT NULL COMMENT 'verifyNum',
  `mustDomainIds` varchar(1024) DEFAULT NULL COMMENT 'mustDomainIds',
  `_expression` varchar(64) DEFAULT NULL COMMENT 'expression',
  `require_auth_ids` varchar(1024) DEFAULT NULL COMMENT 'require_auth_ids',
  `create_time` datetime(3) NOT NULL COMMENT 'create time',
  `update_time` datetime(3) DEFAULT NULL COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_policy` (`policy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COMMENT='business policy';

-- ----------------------------
-- Table structure for sys_conf
-- ----------------------------
DROP TABLE IF EXISTS `sys_conf`;
CREATE TABLE `sys_conf` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `_key` varchar(64) NOT NULL COMMENT 'key name',
  `_value` varchar(4096) NOT NULL COMMENT 'value',
  `remark` varchar(128) DEFAULT NULL COMMENT 'remark of config',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_key` (`_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='sys_conf';

-- ----------------------------
-- Table structure for transaction
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tx_id` varchar(64) NOT NULL COMMENT 'transaction id',
  `biz_model` text COMMENT 'the save data create the biz',
  `policy_id` varchar(32) NOT NULL COMMENT 'policy id',
  `lock_time` datetime(3) DEFAULT NULL COMMENT 'the lock time create the tx . use in rs and slave to deal tx',
  `sender` varchar(32) NOT NULL COMMENT 'the rsId if the sender  for the tx',
  `version` varchar(32) NOT NULL COMMENT 'the version create the tx',
  `block_height` bigint(20) NOT NULL COMMENT 'the block height create the tx',
  `block_time` bigint(20) NOT NULL COMMENT 'the create time create the block for the tx',
  `send_time` bigint(20) NOT NULL COMMENT 'the transaction create time',
  `action_datas` mediumtext COMMENT 'the action list by json',
  `sign_datas` varchar(4096) DEFAULT NULL COMMENT 'the signatures by json',
  `execute_result` varchar(24) DEFAULT NULL COMMENT 'tx execute result,0:fail,1:success',
  `error_code` varchar(128) DEFAULT NULL COMMENT 'tx execute error code',
  `error_message` varchar(512) DEFAULT NULL COMMENT 'tx execute error message',
  `function_name` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT 'the type of transaction',
  `policy_version` int(8) DEFAULT NULL COMMENT 'the policy version',
  `deal_count` int(8) DEFAULT '1' COMMENT 'the real deal count',
  `max_allow_fee` varchar(28) DEFAULT NULL COMMENT 'maximum fee allowed',
  `fee_amount` varchar(28) DEFAULT NULL COMMENT 'actual fee amount',
  `fee_payment_address` varchar(64) DEFAULT NULL COMMENT 'address of fee payment',
  `fee_currency` varchar(24) DEFAULT NULL COMMENT 'fee currency',
  `bd_code` varchar(32) DEFAULT NULL COMMENT 'bd code',
  `receipt_data` mediumtext COMMENT 'receipt data json',
  `submitter` varchar(40) DEFAULT '' COMMENT 'submitter',
  `submitter_sign` varchar(130) DEFAULT '' COMMENT 'data signature of submitter',
  `vote_info` varchar(2048) DEFAULT NULL COMMENT 'vote info json string',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_tx_id` (`tx_id`),
  KEY `idx_bd_code` (`bd_code`),
  KEY `idx_block_height` (`block_height`)
) ENGINE=InnoDB AUTO_INCREMENT=92200 DEFAULT CHARSET=utf8mb4 COMMENT='the table create transaction';

-- ----------------------------
-- Table structure for tx_request
-- ----------------------------
DROP TABLE IF EXISTS `tx_request`;
CREATE TABLE `tx_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tx_id` varchar(64) NOT NULL COMMENT 'the transaction id',
  `policy_id` varchar(32) NOT NULL COMMENT 'policy id',
  `submitter` varchar(40) NOT NULL COMMENT 'tx submitter',
  `bd_code` varchar(32) NOT NULL COMMENT 'tx business code',
  `func_name` varchar(128) NOT NULL COMMENT 'tx function name for BD',
  `tx_data` mediumtext NOT NULL COMMENT 'the request json data',
  `block_height` bigint(20) DEFAULT NULL COMMENT 'block height',
  `tx_receipt` text COMMENT 'tx receipt json data',
  `status` varchar(16) NOT NULL COMMENT 'status,INIT、SUBMITTING、PROCESSING、END',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_tx_id` (`tx_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='the request tx info';
