package pl.edu.pjwstk.s22796.xyz.cinemareservation.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "IDReservation", nullable = false)
    private int idReservation;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private Instant expiration;

    public int getID() {
        return idReservation;
    }

    public void setID(int idReservation) {
        this.idReservation = idReservation;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
