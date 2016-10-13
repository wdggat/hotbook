CREATE DATABASE IF NOT EXISTS iread DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
use iread;
CREATE TABLE IF NOT EXISTS category (
  id int(11) NOT NULL AUTO_INCREMENT primary key COMMENT '自增主键',
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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='类目表';

CREATE TABLE IF NOT EXISTS spider_work_sheet (
  categoryid int primary key,
  species varchar(30) NOT NULL COMMENT '种类,amazon/jd/..',
  last_fetch_time timestamp COMMENT '上一次抓取时间',
  fetch_times int COMMENT '抓取次数'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='爬虫打卡表';

-- MYSQL OR HIVE ?
CREATE TABLE IF NOT EXISTS book (
  id varchar(100) not null primary key
  species varchar(10) NOT NULL COMMENT 'AMAZON/JD..',
  title varchar(1000) NOT NULL COMMENT '书标题',
  wraptype varchar(20) COMMENT 'PAPERBACK/HARDBACK/KINDLE',
  onloadDate date COMMENT '上市日期',
  author varchar(500) COMMENT '作者',
  translator varchar(500) COMMENT '翻译',
  star float,
  commentNum int,
  categoryid int COMMENT '类目id',
  seller varchar(100) COMMENT '卖家',
  price float,
  description varchar(10000),
  posterUrl varchar(2000),
  imgUrls varchar(2000),
  publisher varchar(200),
  pagenum int(10),
  language varchar(50),
  size int(10),
  isbn varchar(50),
  barcode varchar(50),
  length float,
  width float,
  height float,
  weight float,
  brand varchar(200),
  asin varchar(100) not null,
  editorSuggest varchar(50000),
  celebritySuggest varchar(50000),
  mediaSuggest varchar(50000),
  authorIntro varchar(50000),
  catalog varchar(10000),
  preface varchar(10000),
  digest varchar(10000),
  starGroups varchar(100)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS rank (
  bookid varchar(100),
  categoryid varchar(100),
  rank int,
  primary key(bookid, categoryid)
  --unique key `unique_rank`('bookid', 'categoryid')
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='排名';

CREATE TABLE IF NOT EXISTS suggest (
  bookid varchar(100) comment '原商品',
  asin varchar(100) comment '推荐商品的asin',
  title varchar(1000),
  author varchar(500),
  wraptype int,
  price float,
  imgUrl varchar(1000),
  star float,
  commentNum int(8),
  url varchar(1000),
  suggestType int,
  primary key(bookid, asin)
--  unique key `unique_suggest`('bookid', 'asin')
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS comment (
  bookid varchar(100),
  commentid varchar(200),
  star int,
  createtime date,
  title varchar(1000),
  author varchar(200),
  content varchar(20000),
  praise int,
  primary key(bookid, commentid)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS wblist (
  `type` int NOT NULL COMMENT '1:CATEGORY/2:BOOK',
  blackorwhite int NOT NULL COMMENT '0: 黑/1: 白名单',
  value varchar(100) NOT NULL,
  primary key(`type`, blackorwhite, value)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='类目书名黑白名单';

--------------------------
--insert into wblist (`type`, blackorwhite, value) values(1, 1, 'Kindle'),(1, 1, '小说'),(1, 1, '文学'),(1, 1, '传记'),(1, 1, '社会科学'),(1, 1, '哲学与宗教'),(1, 1, '政治与军事'),(1, 1, '心理学'),(1, 1, '历史'),(1, 1, '国学'),(1, 1, '经济管理'),(1,1,'励志与成功'),(1,1,'科技'),(1,1,'科学与自然'),(1,1,'计算机与互联网'),(1,1,'旅游与地图'),(1,1,'进口原版');
insert into wblist (`type`, blackorwhite, value) values(1, 1, '传记'),(1, 1, '社会科学'),(1, 1, '哲学与宗教'),(1, 1, '政治与军事'),(1, 1, '心理学'),(1, 1, '历史'),(1, 1, '国学'),(1, 1, '经济管理'),(1,1,'励志与成功'),(1,1,'科技'),(1,1,'科学与自然'),(1,1,'计算机与互联网'),(1,1,'旅游与地图'),(1,1,'进口原版');
