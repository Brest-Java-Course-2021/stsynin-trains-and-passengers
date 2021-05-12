## Available REST endpoints

### passengers

#### findAll

return list of all passengers

```
curl --request GET 'http://localhost:8088/passengers'
```

#### findById

return information about the passenger with id [id]

```
curl --request GET 'http://localhost:8088/passengers/[id]'
```

#### create

save new passenger

```
curl --request POST 'http://localhost:8088/passengers' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
	"passengerName": "Bill Gilbert",
	"trainId": 1
}'
```

#### update

update passenger information

```
curl --request PUT 'http://localhost:8088/passengers' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
	"passengerId": 7,
	"passengerName": "Bill Gilbert",
	"trainId": 2
}'
```

#### delete

delete information about the passenger with id [id]

```
curl --request DELETE 'http://localhost:8088/passengers/[id]' 
```

#### count

count of passenger records

```
curl --request GET 'http://localhost:8088/passengers/count' 
```

### passengers-dtos

return list of all passengers with trains names

```
curl --request GET 'http://localhost:8088/passengers-dtos'
```

### trains

#### findAll

return list of all trains

```
curl --request GET 'http://localhost:8088/trains'
```

#### findById

return information about the train with id [id]

```
curl --request GET 'http://localhost:8088/trains/[id]'
```

#### create

save new train

```
curl --request POST 'http://localhost:8088/trains' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
	"trainName": "Arbalete",
	"trainDestination": "Zurich",
	"trainDepartureDate": "1957-06-02"
}'
```

#### update

update train information

```
curl --request PUT 'http://localhost:8088/trains' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
	"trainId": 1,
	"trainName": "Arbalete",
	"trainDestination": "Paris",
	"trainDepartureDate": "1957-06-02"
}'
```

#### delete

delete information about the trains with id [id]

```
curl --request DELETE 'http://localhost:8088/trains/[id]' 
```

#### count

count of train records

```
curl --request GET 'http://localhost:8088/trains/count' 
```

### trains-dtos

return list of trains with count of passengers, selected by date (from [dateStart] to [dateEnd])

```
curl --request GET 'http://localhost:8088/trains-dtos?dateStart=2020-01-01&dateEnd=2020-02-25'
```

### version

return 0.0.1 just for fun.

```
curl --request GET 'http://localhost:8088/version'
```

### interesting fact

If you want to see more readable answer, you can pipe command

```
| json_pp
```