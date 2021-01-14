-- 创建产品表，从wc_product_202005 ~ wc_product_202104。SHARD_DATE为分片键，例：20210418，即在wc_product_202104表
CREATE TABLE `wc_product_202104` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `product_code` varchar(30) DEFAULT NULL COMMENT '产品代码',
  `product_name` varchar(30) DEFAULT NULL COMMENT '产品名称',
  `product_num` int(11) DEFAULT NULL COMMENT '产品数量',
  `insert_time` datetime DEFAULT NULL COMMENT '插入时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `SHARD_DATE` varchar(32) NOT NULL COMMENT '分表日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COMMENT='产品表';

-- 创建用户表，从wc_user_0 ~ wc_user_9。CONTRACT_NO为分片键，以客户号最后一位计算表名
CREATE TABLE `wc_user_9` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(32) DEFAULT NULL COMMENT '代码',
  `name` varchar(32) DEFAULT NULL COMMENT '名称',
  `insert_time` datetime DEFAULT NULL COMMENT '插入时间',
  `CONTRACT_NO` varchar(32) NOT NULL COMMENT '客户号(分表键)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 创建配置表，单表，不分表
CREATE TABLE `wc_common_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `common_type` varchar(32) DEFAULT NULL COMMENT '类型',
  `common_key` varchar(32) DEFAULT NULL COMMENT 'key',
  `common_value` varchar(32) DEFAULT NULL COMMENT '值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置表';