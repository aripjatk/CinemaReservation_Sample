package pl.edu.pjwstk.s22796.xyz.cinemareservation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Represents a room in which movie screenings are held.
 */
@Entity
@SuppressWarnings("unused")
public class Room {
    @Id
    @Column(name = "RoomNumber", length = 4)
    private char[] roomNumber;
    @Column(nullable = false)
    private int numRows;
    @Column(nullable = false)
    private int seatsPerRow;

    public Room() {}

    public Room(char[] roomNumber, int numRows, int seatsPerRow) {
        this.roomNumber = roomNumber;
        this.numRows = numRows;
        this.seatsPerRow = seatsPerRow;
    }

    /**
     * Returns the number of this room in the cinema.
     * This may be a Roman numeral or a number and letter such as "2A".
     * @return room number, max. 4 characters
     */
    public char[] getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(char[] roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Returns the number of rows of seats available for viewers in the room.
     * @return number of rows
     */
    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    /**
     * Returns the number of seats in each row of this room. It is assumed
     * that all rows have the same number of seats.
     * @return number of seats
     */
    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }
}
