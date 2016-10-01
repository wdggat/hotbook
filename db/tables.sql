CREATE DATABASE IF NOT EXISTS iread DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
use iread;
CREATE TABLE IF NOT EXISTS categorys (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  species varchar(30) NOT NULL COMMENT '种类,amazon/jd/..',
  `type` int(2) NOT NULL COMMENT '类目类别，普通/促销/..',
  fullname varchar(100) NOT NULL COMMENT '类目名称',
  url varchar(1000) NOT NULL,
  amount int(8) COMMENT '类目下商品数',
  `level` int(2) NOT NULL COMMENT '类目级别',
  isleaf int(1) NOT NULL COMMENT '是否页子类目',
  cat1name varchar(100) COMMENT '一级类目名称',
  cat2name varchar(100),
  cat3name varchar(100),
  unique key `unique_cat`(species, cat1name, cat2name, cat3name)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='类目表';

CREATE TABLE IF NOT EXISTS spider_work_sheet (
  categoryid int primary key,
  species varchar(30) NOT NULL COMMENT '种类,amazon/jd/..',
  last_fetch_time timestamp COMMENT '上一次抓取时间',
  fetch_times int COMMENT '抓取次数'
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='爬虫打卡表';

