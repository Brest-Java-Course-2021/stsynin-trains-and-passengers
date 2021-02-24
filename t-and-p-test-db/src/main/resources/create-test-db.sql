DROP TABLE IF EXISTS TRAIN;
DROP TABLE IF EXISTS passengers;

CREATE TABLE TRAIN (
    TRAIN_ID INT NOT NULL AUTO_INCREMENT,
    TRAIN_NAME VARCHAR(128) NOT NULL UNIQUE,
    TRAIN_DESTINATION VARCHAR(128) NULL,
    TRAIN_DEPARTURE_DATE DATE,
    PRIMARY KEY (TRAIN_ID)
);

CREATE TABLE passengers (
    passenger_id INT NOT NULL AUTO_INCREMENT,
    passenger_name VARCHAR(128) NOT NULL,
    train_id INT NULL,
    PRIMARY KEY (passenger_id)
--    CONSTRAINT trainFk FOREIGN KEY (TRAIN_ID) REFERENCES TRAIN (TRAIN_ID) ON DELETE CASCADE
);