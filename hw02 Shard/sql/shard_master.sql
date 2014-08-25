USE shard_master;


DROP TABLE IF EXISTS where_user_in;
DROP TABLE IF EXISTS db_info;


CREATE TABLE db_info (
	dbid TINYINT(4),
	dbname CHAR(20) NOT NULL,
	address CHAR(15) NOT NULL,

	PRIMARY KEY ( dbid )
);

INSERT INTO db_info VALUES(0, "ShardServer1", "10.211.55.5");
INSERT INTO db_info VALUES(1, "ShardServer2", "10.211.55.6");


CREATE TABLE where_user_in (
	uid INT(11) AUTO_INCREMENT,
	dbid TINYINT(4),
	gid TINYINT(4),

	PRIMARY KEY ( uid ),
	FOREIGN KEY ( dbid ) REFERENCES db_info ( dbid )
);


DROP PROCEDURE IF EXISTS createAllUserLocation;
DELIMITER $$
CREATE PROCEDURE createAllUserLocation ( OUT outUid INT, OUT outDbid INT, OUT outGid TINYINT )
BEGIN
	START TRANSACTION;
		INSERT INTO where_user_in VALUES();
		SET outUid = LAST_INSERT_ID();
		SET outDbid = outUid % 2;
		SET outGid = outUid % 4;
		UPDATE where_user_in SET dbid = outDbid, gid = outGid WHERE uid = outUid;
	COMMIT;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS getRandomUserLocation;
DELIMITER $$
CREATE PROCEDURE getRandomUserLocation ( OUT outUid INT, OUT outDbid INT )
BEGIN
	SELECT FLOOR( RAND() * MAX( uid ) + 1 ) INTO outUid FROM where_user_in;
	SELECT dbid INTO outDbid FROM where_user_in WHERE uid = outUid;
END $$
DELIMITER ;

