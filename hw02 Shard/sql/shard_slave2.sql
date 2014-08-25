USE shard_slave;


DROP TABLE IF EXISTS log;
DROP TABLE IF EXISTS ship;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS galaxy;


CREATE TABLE galaxy (
	gid TINYINT(4),
	name VARCHAR(16),
	hp INT(11) DEFAULT 100000,

	PRIMARY KEY ( gid )
);

INSERT INTO galaxy VALUES( 1, "galaxy1", 100000 );
INSERT INTO galaxy VALUES( 3, "galaxy3", 100000 );


CREATE TABLE user (
	uid INT(11),
	gid TINYINT(4),

	PRIMARY KEY ( uid, gid ),
	FOREIGN KEY ( gid ) REFERENCES galaxy ( gid )
);


CREATE TABLE ship (
	sid INT(11) AUTO_INCREMENT,
	uid INT(11),
	gid TINYINT(4),
	atk INT(11),

	PRIMARY KEY ( sid ),
	FOREIGN KEY ( uid, gid) REFERENCES user ( uid, gid )
);


CREATE TABLE log (
	logid INT(11) AUTO_INCREMENT,
	uid INT(11),
	log VARCHAR(64),
	log_time TIMESTAMP,

	PRIMARY KEY ( logid ),
	FOREIGN KEY ( uid ) REFERENCES user ( uid )
);


DROP PROCEDURE IF EXISTS createUser;
DELIMITER $$
CREATE PROCEDURE createUser( IN inUid INT, IN inGid TINYINT )
BEGIN
	START TRANSACTION;
		INSERT INTO user VALUES( inUid, inGid );

		INSERT INTO ship VALUES( NULL, inUid, inGid, FLOOR( RAND() * 96 + 5 ) );
		INSERT INTO ship VALUES( NULL, inUid, inGid, FLOOR( RAND() * 96 + 5 ) );
		INSERT INTO ship VALUES( NULL, inUid, inGid, FLOOR( RAND() * 96 + 5 ) );
		INSERT INTO ship VALUES( NULL, inUid, inGid, FLOOR( RAND() * 96 + 5 ) );
		INSERT INTO ship VALUES( NULL, inUid, inGid, FLOOR( RAND() * 96 + 5 ) );
		INSERT INTO ship VALUES( NULL, inUid, inGid, FLOOR( RAND() * 96 + 5 ) );
		INSERT INTO ship VALUES( NULL, inUid, inGid, FLOOR( RAND() * 96 + 5 ) );
		INSERT INTO ship VALUES( NULL, inUid, inGid, FLOOR( RAND() * 96 + 5 ) );
		INSERT INTO ship VALUES( NULL, inUid, inGid, FLOOR( RAND() * 96 + 5 ) );
		INSERT INTO ship VALUES( NULL, inUid, inGid, FLOOR( RAND() * 96 + 5 ) );

		INSERT INTO log( uid, log ) VALUES( inUid, CONCAT( "User #", inUid, " created ships on Galaxy #", inGid ) );
	COMMIT;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS attack;
DELIMITER $$
CREATE PROCEDURE attack( IN inUid INT, OUT outAtk INT, OUT outGid TINYINT )
BEGIN
	START TRANSACTION;
		SELECT SUM(atk) INTO outAtk FROM ship WHERE uid = inUid;
		SELECT FLOOR( RAND() * 4 ) INTO outGid;

		IF ISNULL( outAtk ) THEN
			SET outAtk = 0;
		ELSE
			INSERT INTO log( uid, log ) VALUES( inUid, CONCAT( "User #", inUid, " attacks ", outAtk, " damage to Galaxy #", outGid ) );
		END IF;
	COMMIT;
END $$
DELIMITER ;

