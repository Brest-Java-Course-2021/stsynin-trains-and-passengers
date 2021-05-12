[![Java CI with Maven](https://github.com/Brest-Java-Course-2021/stsynin-trains-and-passengers/actions/workflows/maven.yml/badge.svg)](https://github.com/Brest-Java-Course-2021/stsynin-trains-and-passengers/actions/workflows/maven.yml)
# trains-and-passengers
Web приложение для работы по учёту рейсов и пассажиров в базе данных.

## Program specification

[See here](/documentation/srs/program specification.md)

## Requirements:

* JDK 11
* Apache Maven
  
[How to install](/documentation/srs/environment_setup.md)

## Build application:
```
mvn clean install
```

## Start Rest:

```
java -jar ./t-and-p-rest-app/target/t-and-p-rest-app.jar
```

## Start WEB application:

```
java -jar ./t-and-p-web-app/target/t-and-p-web-app.jar
```
## Rest commands:

[See here](/documentation/srs/rest.md)