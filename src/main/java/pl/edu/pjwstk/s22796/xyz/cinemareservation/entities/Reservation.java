package pl.edu.pjwstk.s22796.xyz.cinemareservation.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents a submitted and confirmed reservation of
 * one or more seats for a particular cinema visit.
 */
@SuppressWarnings("unused")
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
    private LocalDateTime expiration;

    public int getID() {
        return idReservation;
    }

    public void setID(int idReservation) {
        this.idReservation = idReservation;
    }

    /**
     * Returns the time at which this reservation is set to expire.
     * @return expiration time in UTC
     */
    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    /**
     * Returns the (first) name of the person who made the reservation.
     * @return first name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the surname (last name) of the person who made the reservation.
     * @return last name
     */
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
