YEAR=$(date +%Y)
MONTH=$(date +%m)
DAY=$(date +%e)

echo Step 1 - Find screenings
curl http://localhost:8080/CinemaReservation/cinema/screenings/list\?year=$YEAR\&month=$MONTH\&day=$DAY\&hStart=0\&hEnd=23\&mStart=0\&mEnd=59
echo ""
echo -------------
echo ""
echo Step 2 - Get screening details
curl http://localhost:8080/CinemaReservation/cinema/screenings/details/6
echo ""
echo -------------
echo ""
echo Step 3 - Make reservation
echo '{ "name": "Żaneta", "surname": "Konstantynopolitańczykiewiczówna", "seats": [ { "screeningID": 6, "seatNumber": 1, "rowNumber": 2, "ticketType": "ADULT" } ] }' > testpost.json
curl -H "Content-Type: application/json" -T testpost.json -X POST http://localhost:8080/CinemaReservation/cinema/reservation/create
echo ""
