package pl.edu.pjwstk.s22796.xyz.cinemareservation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Room {
    @Id
    @Column(name = "RoomNumber", length = 4)
    private char[] roomNumber;
    @Column(nullable = false)
    private int numRows;
    @Column(nullable = false)
    private int seatsPerRow;

    public char[] getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(char[] roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }
}
