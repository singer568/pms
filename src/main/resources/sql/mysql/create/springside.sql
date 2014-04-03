drop table if exists ss_task;
drop table if exists ss_user;
drop table if exists PMS_GROUP;
drop table if exists pms_keywords;
drop table if exists PMS_LEVEL;
drop table if exists PMS_EMAIL;
drop table if exists pms_rule;
drop table if exists pms_url;
drop table if exists pms_task;
drop table if exists pms_taskhistory;
drop table if exists pms_urlhistory;
drop table if exists pms_subjects;



create table ss_task (
	id bigint auto_increment,
	title varchar(128) not null,
	description varchar(255),
	user_id bigint not null,
    primary key (id)
)engine=InnoDB;

create table ss_user (
	id bigint auto_increment,
	login_name varchar(64) not null unique,
	name varchar(64) not null,
	password varchar(255) not null,
	salt varchar(64) not null,
	roles varchar(255) not null,
	register_date timestamp not null,
	primary key (id)
)engine=InnoDB;


create table PMS_GROUP(
	id bigint auto_increment,
	code varchar(64),
	name varchar(255),
	user_id bigint  ,
	description varchar(255),
	parent_id bigint  ,
	primary key (id)
)engine=InnoDB;
create table pms_keywords(
	id bigint auto_increment,
	code varchar(64),
	name varchar(255),
	description varchar(255),
	key_words varchar(500)  ,
	primary key (id)
)engine=InnoDB;
create table PMS_LEVEL(
	id bigint auto_increment,
	code varchar(64),
	name varchar(255),
	description varchar(255),
	user_id bigint  ,
	parent_id bigint  ,
	primary key (id)
)engine=InnoDB;
create table PMS_EMAIL(
	id bigint auto_increment,
	code varchar(64) ,
	name varchar(255),
	email varchar(255),
	host  varchar(255),
	user_name varchar(255),
	pwd  varchar(255),
	subject  varchar(255),
	email_content  varchar(255),
	description varchar(255),
	user_id bigint  ,
	email_rule varchar(255),
	primary key (id)
)engine=InnoDB;

create table pms_rule(
	id bigint auto_increment,
	code varchar(64),
	name varchar(255),
	rule varchar(500),
	description varchar(255),
	primary key (id)
)engine=InnoDB;

create table pms_url(
	id bigint auto_increment,
	code varchar(64),
	name varchar(255)   null,
	province varchar(255)   null,
	department varchar(255)   null,
	module varchar(255)   null,
	submodule varchar(255)   null,
	url varchar(1000)   null,	
	url_prefix varchar(1000) ,
	subj_path varchar(1000) ,
	subj_replace  varchar(1000) ,
	link_path  varchar(1000) ,
	date_path varchar(1000) ,
	date_replace  varchar(1000) ,	
	rule_id bigint,
	task_id bigint,
	charset varchar(20),
	start_begin bigint,
	description varchar(255),
	level_id bigint,
	group_id bigint,
	catch_next_page char(1) default 0,
	next_page_xpath varchar(255),
	area varchar(50),
	is_capital char(1) default 0,
	catch_type varchar(50) default 'NORMAL',
	primary key (id)
)engine=InnoDB;

create table pms_task(
	id bigint auto_increment,
	code varchar(64),
	name varchar(255),
	cron varchar(255),
	description varchar(1000),
	duration varchar(200),
	url_rule varchar(200),
	end_date timestamp,
	start_date timestamp,
	status varchar(30),
	catch_type varchar(20),
	email_rule varchar(255),
	primary key (id)
)engine=InnoDB;


create table pms_taskhistory(
	id bigint auto_increment,
	task_id bigint,
	start_date timestamp,
	end_date timestamp,
	duration varchar(100),
	status varchar(100),
	logInfo varchar(3000),
	primary key (id)
)engine=InnoDB;
create table pms_urlhistory(
	id bigint auto_increment,
	task_id bigint,
	url_id bigint,
	task_history_id bigint,
	start_date timestamp,
	end_date timestamp,
	duration varchar(100),
	status varchar(100),
	subjects varchar(1000),
	primary key (id)
)engine=InnoDB;

create table pms_subjects(
	id bigint auto_increment,
	url_id bigint,
	level_id bigint,
	group_id bigint,
	from_url varchar(1000) null,
	subject varchar(500)   null,
	publish_date timestamp   null,
	publish_date_time timestamp   null,
	subj_url varchar(1000)   null,
	content varchar(3000)   null,
	catch_time timestamp   null,
	relative_url  varchar(1000)   null,
	key_flag char(1) default 0,
	task_id bigint,
	is_send char(1) default 0,
	dr int default 0,
	primary key (id)
)engine=InnoDB;

---2014-03-19   updated sql

ALTER TABLE `pms_url` ADD COLUMN `valid`  char(1) NULL DEFAULT '1' AFTER `catch_type`;
ALTER TABLE `pms_url` ADD COLUMN `success`  char(1) NULL DEFAULT '1' AFTER `valid`;
ALTER TABLE `pms_url` ADD COLUMN `catch_time`  varchar(30) NULL AFTER `success`;
ALTER TABLE `pms_url` ADD COLUMN `filter`  char(1) NULL DEFAULT '1' AFTER `catch_time`;
ALTER TABLE `pms_url` ADD COLUMN `err_msg`  text NULL AFTER `filter`;


ALTER TABLE `pms_email` ADD COLUMN `err_email`  varchar(255) NULL AFTER `email_rule`;


ALTER TABLE `pms_task` ADD COLUMN `key_words`  text NULL AFTER `email_rule`;
ALTER TABLE `pms_task` ADD COLUMN `type`  varchar(10) NULL AFTER `key_words`;
ALTER TABLE `pms_task` ADD COLUMN `days`  varchar(5)  NULL;

ALTER TABLE `pms_subjects` 
MODIFY COLUMN `publish_date`  varchar(50) NULL DEFAULT NULL AFTER `subject`,
MODIFY COLUMN `publish_date_time`  varchar(50) NULL DEFAULT NULL AFTER `publish_date`;
ALTER TABLE `pms_subjects` MODIFY COLUMN `catch_time`  varchar(30) NULL DEFAULT NULL AFTER `content`;

ALTER TABLE `pms_subjects` ADD COLUMN `sended_mail`  varchar(500) NULL AFTER `dr`;
ALTER TABLE `pms_subjects`
ADD COLUMN `code`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `sended_mail`,
ADD COLUMN `name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `code`,
ADD COLUMN `province`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `name`,
ADD COLUMN `department`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `province`,
ADD COLUMN `module`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `department`,
ADD COLUMN `submodule`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `module`,
ADD COLUMN `area`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `submodule`,
ADD COLUMN `is_capital`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' AFTER `area`,
ADD COLUMN `filter`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' AFTER `is_capital`;
ALTER TABLE `pms_subjects` ADD COLUMN `days`  varchar(5) NULL AFTER `filter`;


ALTER TABLE `pms_url` ADD COLUMN `catch_num`  varchar(20) NULL AFTER `err_msg`;






