CREATE TABLE `activity` (
  `id` int(11) NOT NULL auto_increment COMMENT 'id',
  `itemId` int(11) NOT NULL COMMENT '子项目id',
  `title` varchar(50) default NULL COMMENT '名称',
  `score` double default '0' COMMENT '分数',
  `desc` varchar(100) default NULL COMMENT '描述',
  `schoolNum` char(11) default NULL COMMENT '学号',
  `createTime` datetime default NULL COMMENT '创建时间',
  `updateTime` datetime default NULL COMMENT '更新时间',
  `valid` enum('1','0') default '1',
  PRIMARY KEY  (`id`),
  KEY `index_item_schoolNum_time` (`itemId`,`schoolNum`,`createTime`,`updateTime`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

CREATE TABLE `authority` (
  `id` int(11) NOT NULL auto_increment COMMENT 'id',
  `title` varchar(50) default NULL COMMENT '描述',
  `authCode` varchar(40) default NULL COMMENT '权限码',
  `createTime` datetime default NULL COMMENT '创建时间',
  `updateTime` datetime default NULL COMMENT '更新时间',
  `valid` enum('1','0') default '1',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique` (`authCode`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE `goHome` (
  `id` int(11) NOT NULL auto_increment COMMENT 'id',
  `schoolNum` char(11) NOT NULL COMMENT '学号',
  `year` int(4) NOT NULL COMMENT '年份',
  `vacation` enum('寒假','暑假') NOT NULL COMMENT '寒暑假',
  `startDate` date default NULL COMMENT '开始时间',
  `endDate` date default NULL COMMENT '结束时间',
  `address` varchar(200) default NULL COMMENT '地址',
  `phone` char(11) default NULL COMMENT '手机号',
  `type` varchar(100) default NULL COMMENT '类型',
  `operator` char(11) default NULL COMMENT '操作人',
  `createTime` datetime default NULL COMMENT '创建时间',
  `updateTime` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`),
  KEY `schoolNum_index` (`schoolNum`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `item` (
  `id` int(11) NOT NULL auto_increment COMMENT 'id',
  `projectId` int(11) default NULL COMMENT '项目id',
  `title` varchar(50) default NULL COMMENT '标题',
  `maxScore` double default NULL COMMENT '最高分',
  `minScore` double default '0' COMMENT '最低分',
  `operator` char(11) default NULL COMMENT '操作人',
  `createTime` datetime default NULL COMMENT '创建时间',
  `updateTime` datetime default NULL COMMENT '更新时间',
  `valid` enum('1','0') default '1' COMMENT '是否有效',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

CREATE TABLE `loginRecord` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'id',
  `schoolNum` char(11) NOT NULL COMMENT '学号',
  `loginTime` datetime default NULL COMMENT '登录时间',
  `ip` varchar(15) default NULL COMMENT '登录ip',
  `location` varchar(50) default NULL COMMENT '地址',
  PRIMARY KEY  (`id`),
  KEY `loginTime` (`loginTime`)
) ENGINE=InnoDB AUTO_INCREMENT=225 DEFAULT CHARSET=utf8;

CREATE TABLE `project` (
  `id` int(11) NOT NULL auto_increment COMMENT 'id',
  `title` varchar(50) default NULL COMMENT '大标题',
  `majorCode` char(4) default NULL COMMENT '专业号',
  `grade` char(4) default NULL COMMENT '年级',
  `year` int(4) default NULL COMMENT '年份',
  `desc` varchar(255) default NULL COMMENT '描述',
  `maxScore` double default NULL COMMENT '最高分',
  `minScore` double default NULL COMMENT '最低分',
  `operator` char(11) default NULL COMMENT '操作人',
  `createTime` datetime default NULL COMMENT '创建时间',
  `updateTime` datetime default NULL COMMENT '更新时间',
  `valid` enum('1','0') default '1' COMMENT '是否有效',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

CREATE TABLE `stu_auth` (
  `id` int(11) NOT NULL auto_increment COMMENT 'id',
  `schoolNum` char(11) default NULL COMMENT '学号',
  `authCode` varchar(40) default NULL COMMENT '权限码',
  `createTime` datetime default NULL COMMENT '创建时间',
  `updateTime` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`),
  KEY `index` (`schoolNum`,`authCode`),
  KEY `authCode` (`authCode`),
  CONSTRAINT `stu_auth_ibfk_1` FOREIGN KEY (`authCode`) REFERENCES `authority` (`authCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

CREATE TABLE `student` (
  `schoolNum` char(11) NOT NULL COMMENT '学号',
  `grade` int(4) default NULL COMMENT '年级',
  `name` varchar(15) default NULL COMMENT '姓名',
  `sex` enum('男','女') default NULL COMMENT '性别',
  `classCode` char(5) default NULL COMMENT '专业班级',
  `birthday` date default NULL COMMENT '生日',
  `nation` varchar(20) default NULL COMMENT '民族',
  `politicalStatus` varchar(20) default NULL COMMENT '政治面貌',
  `idNo` char(20) default NULL COMMENT '身份证号',
  `accountNo` char(20) default NULL COMMENT '光大卡号',
  `originAddress` varchar(100) default NULL COMMENT '生源地',
  `homeAddress` varchar(255) default NULL COMMENT '家庭地址',
  `entranceDate` date default NULL COMMENT '入学时间',
  `schoolYear` int(1) default '4' COMMENT '学制',
  `residence` enum('农村户口','城市户口') default NULL COMMENT '家庭户口',
  `phone` varchar(20) default NULL COMMENT '手机',
  `qq` varchar(12) default NULL COMMENT 'qq',
  `email` varchar(50) default NULL COMMENT '邮箱',
  `parentPhone` varchar(20) default NULL COMMENT '家庭成员手机',
  `dormNo` varchar(20) default NULL COMMENT '宿舍',
  `direction` tinyint(4) default NULL COMMENT '专业方向',
  `createTime` datetime default NULL COMMENT '创建时间',
  `updateTime` datetime default NULL COMMENT '更新时间',
  `valid` enum('0','1') default '1' COMMENT '是否有效',
  PRIMARY KEY  (`schoolNum`),
  UNIQUE KEY `index_schoolNum_name` (`schoolNum`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;