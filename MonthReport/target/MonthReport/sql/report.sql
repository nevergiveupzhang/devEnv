
DROP TABLE IF EXISTS REPORT_TOP_TBL;
CREATE TABLE REPORT_TOP_TBL (
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	SERVICE_NAME VARCHAR(20) NOT NULL COMMENT "ccwhoisd,newgwhoisd",
	REPORT_TYPE VARCHAR(20) NOT NULL COMMENT "ccwhoisd,ccwhoisdweb,monitor",
	REPORT_DATE CHAR(7) NOT NULL COMMENT "yyyy-MM",
	FIELD_NAME VARCHAR(30) NOT NULL COMMENT "domain.keyword,client.keyword",
	FIELD_VALUE VARCHAR(250) NOT NULL,
	DOC_COUNT INT NOT NULL DEFAULT 0
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS REPORT_DAILY_TBL;
CREATE TABLE REPORT_DAILY_TBL (
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	SERVICE_NAME VARCHAR(20) NOT NULL COMMENT "ccwhoisd,newgwhoisd",
	REPORT_TYPE VARCHAR(20) NOT NULL COMMENT "ccwhoisd,ccwhoisdweb,monitor",
	REPORT_DATE CHAR(10) NOT NULL COMMENT "yyyy-MM-dd",
	DOC_COUNT INT NOT NULL DEFAULT 0
)ENGINE=InnoDB DEFAULT CHARSET=utf8;