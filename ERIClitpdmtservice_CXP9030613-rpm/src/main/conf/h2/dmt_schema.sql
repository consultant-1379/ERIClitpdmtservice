-- Create schema
CREATE SCHEMA IF NOT EXISTS DMT;

-- Create tables
CREATE TABLE IF NOT EXISTS DMT.WORKING_COPY (
	ID VARCHAR(100) NOT NULL, 
	NAME VARCHAR(150) NOT NULL,
	DESCRIPTION VARCHAR(400),
	DESIGN_MODE VARCHAR(10) NOT NULL, 
	MODEL_DEFINITION CLOB, 
	CREATED TIMESTAMP NOT NULL, 	 
	LAST_MODIFIED TIMESTAMP NOT NULL, 	 
	VERSION BIGINT, 
	PRIMARY KEY (ID)
)