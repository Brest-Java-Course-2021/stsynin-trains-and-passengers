DROP TABLE IF EXISTS TRAIN;
DROP TABLE IF EXISTS passengers;

CREATE TABLE TRAIN (
    TRAIN_ID INT NOT NULL AUTO_INCREMENT,
    TRAIN_NAME VARCHAR(128) NOT NULL UNIQUE,
    TRAIN_DESTINATION VARCHAR(128),
    TRAIN_DEPARTURE_DATE DATE,
    PRIMARY KEY (TRAIN_ID)
);

CREATE TABLE passengers (
  passengerId INT NOT NULL AUTO_INCREMENT,
  passengerName VARCHAR(128) NOT NULL,
  PRIMARY KEY (passengerId)
);