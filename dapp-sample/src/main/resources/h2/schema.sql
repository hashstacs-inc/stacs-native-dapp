

CREATE TABLE IF NOT EXISTS `sample` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `tx_id` varchar(64) NOT NULL COMMENT 'the transaction id',
    `policy_id` varchar(32) NOT NULL COMMENT 'policy id',
    `create_time` datetime NOT NULL COMMENT 'create time',
    `update_time` datetime DEFAULT NULL COMMENT 'update time',
    PRIMARY KEY (`id`),
    UNIQUE (`tx_id`)
);
