-- 事务消息表
CREATE TABLE `transaction_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `message_id` varchar(64) NOT NULL COMMENT '消息ID',
  `business_key` varchar(64) NOT NULL COMMENT '业务标识',
  `business_type` varchar(32) NOT NULL COMMENT '业务类型',
  `content` text NOT NULL COMMENT '消息内容',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0-待发送 1-已发送 2-已消费 3-失败',
  `retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '重试次数',
  `max_retry_count` int(11) NOT NULL DEFAULT '3' COMMENT '最大重试次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `consumer_time` datetime DEFAULT NULL COMMENT '消费时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_message_id` (`message_id`),
  KEY `idx_business_key` (`business_key`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事务消息表';

-- 分布式事务表
CREATE TABLE `distributed_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `transaction_id` varchar(64) NOT NULL COMMENT '事务ID',
  `business_key` varchar(64) NOT NULL COMMENT '业务标识',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0-初始 1-已提交 2-已回滚 3-超时 4-补偿中 5-补偿成功 6-补偿失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transaction_id` (`transaction_id`),
  KEY `idx_business_key` (`business_key`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分布式事务表';
