DROP TABLE IF EXISTS PASSENGER;
DROP TABLE IF EXISTS TRAIN;

CREATE TABLE TRAIN (
    TRAIN_ID INT NOT NULL AUTO_INCREMENT,
    TRAIN_NAME VARCHAR(128) NOT NULL UNIQUE,
    TRAIN_DESTINATION VARCHAR(128) NULL,
    TRAIN_DEPARTURE_DATE DATE,
    CONSTRAINT TRAIN_PK PRIMARY KEY (TRAIN_ID)
);

CREATE TABLE PASSENGER (
    PASSENGER_ID INT NOT NULL AUTO_INCREMENT,
    PASSENGER_NAME VARCHAR(128) NOT NULL,
    TRAIN_ID INT NULL,
    CONSTRAINT PASSENGER_PK PRIMARY KEY (PASSENGER_ID),
    CONSTRAINT TRAIN_FK FOREIGN KEY (TRAIN_ID) REFERENCES TRAIN (TRAIN_ID)
);