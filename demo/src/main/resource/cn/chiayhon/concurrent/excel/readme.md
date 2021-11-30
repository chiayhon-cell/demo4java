# 需求

报表导出：百万条数据，要求1分钟内导出到Excel

测试专用SQL

```sql
-- 使用数据库
USE demo4java;
-- 生成人员表
DROP TABLE
IF
	EXISTS `person`;
CREATE TABLE `person` (
	`id` BIGINT ( 20 ) UNSIGNED NOT NULL,
	`fname` VARCHAR ( 100 ) NOT NULL,
	`lname` VARCHAR ( 100 ) NOT NULL,
	`age` TINYINT ( 3 ) UNSIGNED NOT NULL,
	`sex` TINYINT ( 1 ) UNSIGNED NOT NULL,
	PRIMARY KEY ( `id` ) 
) ENGINE = INNODB DEFAULT CHARSET = utf8;
-- 生成部门表
DROP TABLE
IF
	EXISTS `department`;
CREATE TABLE `department` ( `id` BIGINT ( 20 ) UNSIGNED NOT NULL, `department` VARCHAR ( 100 ) NOT NULL, PRIMARY KEY ( `id` ) ) ENGINE = INNODB DEFAULT CHARSET = utf8;
-- 生成家庭地址表
DROP TABLE
IF
	EXISTS `address`;
CREATE TABLE `address` ( `id` BIGINT ( 20 ) UNSIGNED NOT NULL, `address` VARCHAR ( 100 ) NOT NULL, PRIMARY KEY ( `id` ) ) ENGINE = INNODB DEFAULT CHARSET = utf8;
-- 存储过程:生成人员
delimiter $$
DROP PROCEDURE
IF
	EXISTS generate;
CREATE DEFINER = `root` @`localhost` PROCEDURE `generate` ( IN num INT ) BEGIN
	DECLARE
		chars VARCHAR ( 100 ) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
	DECLARE
		fname VARCHAR ( 10 ) DEFAULT '';
	DECLARE
		lname VARCHAR ( 25 ) DEFAULT '';
	DECLARE
		id INT UNSIGNED;
	DECLARE
		len INT;
	
	SET id = 1;
	DELETE 
	FROM
		person;
	WHILE
			id <= num DO SET len = FLOOR( 1 + RAND()* 10 ); SET fname = ''; WHILE len > 0 DO
			
			SET fname = CONCAT(
				fname,
			substring( chars, FLOOR( 1 + RAND()* 62 ), 1 ));
			
			SET len = len - 1;
			
		END WHILE;
		
		SET len = FLOOR( 1+RAND ()* 25 );
		
		SET lname = '';
		WHILE
				len > 0 DO
				
				SET lname = CONCAT(
					lname,
				SUBSTR( chars, FLOOR( 1 + RAND()* 62 ), 1 ));
			
			SET len = len - 1;
			
		END WHILE;
		INSERT INTO person
		VALUES
			(
				id,
				fname,
				lname,
				FLOOR( RAND()* 100 ),
			FLOOR( RAND()* 2 ));
		
		SET id = id + 1;
		
	END WHILE;
	
END $$;
-- 生成部门和地址
delimiter $$
DROP PROCEDURE
IF
	EXISTS genDepAdd;
CREATE DEFINER = `root` @`localhost` PROCEDURE `genDepAdd` ( IN num INT ) BEGIN
	DECLARE
		chars VARCHAR ( 100 ) DEFAULT '行政技术研发财务人事开发公关推广营销咨询客服运营测试';
	DECLARE
		chars2 VARCHAR ( 100 ) DEFAULT '北京上海青岛重庆成都安徽福建浙江杭州深圳温州内蒙古天津河北西安三期';
	DECLARE
		depart VARCHAR ( 10 ) DEFAULT '';
	DECLARE
		address VARCHAR ( 25 ) DEFAULT '';
	DECLARE
		id INT UNSIGNED;
	DECLARE
		len INT;
	
	SET id = 1;
	WHILE
			id <= num DO SET len = FLOOR( 2 + RAND()* 2 ); SET depart = ''; WHILE len > 0 DO
			
			SET depart = CONCAT(
				depart,
			substring( chars, FLOOR( 1 + RAND()* 26 ), 1 ));
			
			SET len = len - 1;
			
		END WHILE;
		
		SET depart = CONCAT( depart, '部' );
		
		SET len = FLOOR( 6+RAND ()* 18 );
		
		SET address = '';
		WHILE
				len > 0 DO
				
				SET address = CONCAT(
					address,
				SUBSTR( chars2, FLOOR( 1 + RAND()* 33 ), 1 ));
			
			SET len = len - 1;
			
		END WHILE;
		INSERT INTO department
		VALUES
			( id, depart );
		INSERT INTO address
		VALUES
			( id, address );
		
		SET id = id + 1;
		
	END WHILE;
	
END $$;

-- 停掉事务
SET autocommit = 0;
-- 生成人员表
CALL generate ( 1000000 );
-- 生成部门和家庭地址表
-- call genDepAdd(1000000);
-- 重启事务
SET autocommit = 1;
```

​	