# CinemaReservation_Sample

## Run instructions
1. Install JDK 17. On Ubuntu/Debian:

```
sudo apt install openjdk-17-jdk-headless
```

2. Install MariaDB SQL server. On Ubuntu/Debian:

```
sudo apt install mariadb-server
```

3. Start the SQL server.

```
sudo /etc/init.d/mysql start
```

4. Create the required database and SQL user with the provided SQL script.

```
cat init_sql.sql | sudo mysql
```

5. Finally, compile and run the software with the provided Shell script.

```
./compileAndRun.sh
```

## Assumptions
- Each row in a given screening room has the same number of seats.
- Each row has a maximum of 26 seats (to allow for a numbering system consisting of a digit and a letter, e.g. Seat 2C - third seat in row 2)

## API methods

`<WAR>` = name of the WAR file in the application server's target directory. When using the provided `compileAndRun.sh` script, this will be `CinemaReservation`

### List screenings

```
GET /<WAR>/cinema/screenings/list
```

Required query parameters:
- `year`, `month`, `day` - return screenings happening on a given date
- `hStart`, `mStart` - return screenings happening no earlier than at a given time
- `hEnd`, `mEnd` - return screenings happening no later than at a given time

Example request:

```
GET /<WAR>/cinema/screenings/list?year=2022&month=12&day=24&hStart=7&mStart=30&hEnd=15&mEnd=0
```

Returns screenings on Christmas Eve 2022 between 7:30 AM and 3:00 PM.

Example response:

```
[{"movie":{"title":"Avatar 2","id":1},"dateAndTime":[2022,12,24,8,50,51],"id":4}
```

Meaning: There's a screening of Avatar 2 at 8:50:51 AM. The screening ID (see below) is 4.

### Screening details

```
GET /<WAR>/cinema/screenings/details/<id>
```

Returns detailed information about a screening specified by `<id>`. This `id` is returned by the `/cinema/screenings/list` method.

Example request:

```
GET /<WAR>/cinema/screenings/details/4
```

Example response:

```
{"dateAndTime":[2022,12,24,8,50,51],"movie":{"title":"Avatar 2","id":1},"room":{"roomNumber":"3","numRows":6,"seatsPerRow":6},"id":4,"seatingAvailability":"1A-F,2B,2D-F,3A-F,4A-F,5A-F,6A-F"}
```

Meaning: This screening will be held in room number 3, which has 6 rows of 6 seats each. All seats are available except for 2A and 2C.

### Create a reservation

```
POST /<WAR>/cinema/reservation/create
```

A JSON file must be sent as the contents of the request. It must contain an object with the properties `name`, `surname`, and `seats`. `name` and `surname` are simple string values, while `seats` is an array of objects with the following properties:
- `screeningID` - see above
- `rowNumber` - number of the row in which to reserve the seat, minimum is `1`, maximum is `"room"."numRows"` (see above)
- `seatNumber` - number of the seat to reserve, minimum is `1`, maximum is `"room"."seatsPerRow"` (see above). `1` = seat A, `2` = seat B, and so on.
- `ticketType` - either `ADULT`, `CHILD`, or `STUDENT`

Example request:

```
POST /<WAR>/cinema/reservation/create

{
    "name": "Test",
    "surname": "Testowski",
    "seats": [
        {
            "rowNumber": 3,
            "seatNumber": 4,
            "screeningID": 4,
            "ticketType": "STUDENT"
        },
        {
            "rowNumber": 3,
            "seatNumber": 5,
            "screeningID": 4,
            "ticketType": "STUDENT"
        }
    ]
}
```

Meaning: Reserve seat 3D and 3E in screening ID 1 for two students under the name "Test Testowski".

Example response:

```
{"amountToPay":36.0, "expiration":[2022,12,24,8,35,51]}
```

Meaning: Reservation successful. It will expire on Christmas Eve 2022 at 8:35:51 AM. By then you need to pay 36 PLN to confirm it.
