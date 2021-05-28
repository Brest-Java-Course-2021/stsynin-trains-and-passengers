[![Java CI with Maven](https://github.com/Brest-Java-Course-2021/stsynin-trains-and-passengers/actions/workflows/maven.yml/badge.svg)](https://github.com/Brest-Java-Course-2021/stsynin-trains-and-passengers/actions/workflows/maven.yml)
# trains-and-passengers
Web приложение для работы по учёту рейсов и пассажиров в базе данных.

## Program specification

[See here](/documentation/srs/program_specification.md)

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
java -jar ./rest-app/target/rest-app.jar
```

## Start WEB application:

```
java -jar ./web-app/target/web-app.jar
```
## Rest commands:

[See here](/documentation/srs/rest.md)

## Swagger:

[http://localhost:8088/swagger-ui/](http://localhost:8088/swagger-ui/)