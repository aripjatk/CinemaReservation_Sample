package pl.edu.pjwstk.s22796.xyz.cinemareservation.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "IDScreening")
    private int IDScreening;
    @Column(nullable = false)
    private LocalDateTime dateAndTime;
    @ManyToOne(optional = false)
    @JoinColumn(name = "IDMovie")
    private Movie movie;
    @ManyToOne(optional = false)
    @JoinColumn(name = "RoomNumber")
    private Room room;

    public int getID() {
        return IDScreening;
    }

    public void setID(int ID) {
        IDScreening = ID;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime localDateTime) {
        dateAndTime = localDateTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
