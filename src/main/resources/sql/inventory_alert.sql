-- 创建产品表
CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_code` varchar(50) NOT NULL COMMENT '产品编码',
  `product_name` varchar(100) NOT NULL COMMENT '产品名称',
  `description` varchar(255) DEFAULT NULL COMMENT '产品描述',
  `safety_stock` int(11) NOT NULL COMMENT '安全库存阈值',
  `unit` varchar(20) NOT NULL COMMENT '单位',
  `cost_price` decimal(10,2) NOT NULL COMMENT '成本价',
  `sale_price` decimal(10,2) NOT NULL COMMENT '销售价',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态（0：禁用，1：启用）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_code` (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- 创建库存表
CREATE TABLE IF NOT EXISTS `inventory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `current_stock` int(11) NOT NULL COMMENT '当前库存数量',
  `in_stock` int(11) DEFAULT '0' COMMENT '入库数量',
  `out_stock` int(11) DEFAULT '0' COMMENT '出库数量',
  `update_time` datetime NOT NULL COMMENT '库存更新时间',
  `last_in_time` datetime DEFAULT NULL COMMENT '上次入库时间',
  `last_out_time` datetime DEFAULT NULL COMMENT '上次出库时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_inventory_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 创建销售记录表
CREATE TABLE IF NOT EXISTS `sales_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `sales_quantity` int(11) NOT NULL COMMENT '销售数量',
  `sales_price` decimal(10,2) NOT NULL COMMENT '销售单价',
  `sales_amount` decimal(10,2) NOT NULL COMMENT '销售金额',
  `sales_date` datetime NOT NULL COMMENT '销售日期',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_sales_date` (`sales_date`),
  CONSTRAINT `fk_sales_record_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售记录表';

-- 创建预警记录表
CREATE TABLE IF NOT EXISTS `alert_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `alert_type` int(11) NOT NULL COMMENT '预警类型（1：库存不足，2：库存周转率异常，3：呆滞库存）',
  `alert_level` int(11) NOT NULL COMMENT '预警级别（1：提醒，2：警告，3：紧急）',
  `alert_content` varchar(255) NOT NULL COMMENT '预警内容',
  `handle_status` int(11) NOT NULL DEFAULT '0' COMMENT '处理状态（0：未处理，1：已处理）',
  `handler` varchar(50) DEFAULT NULL COMMENT '处理人',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_remark` varchar(255) DEFAULT NULL COMMENT '处理备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_alert_type` (`alert_type`),
  KEY `idx_alert_level` (`alert_level`),
  KEY `idx_handle_status` (`handle_status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_alert_record_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警记录表';
