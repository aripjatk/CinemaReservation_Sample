package pl.edu.pjwstk.s22796.xyz.cinemareservation.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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

    @Transient
    private SeatingAvailability availability;

    public SeatingAvailability getSeatingAvailability() {
        return availability;
    }

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

    /**
     * Assigns a value to SeatingAvailability by looking up all reserved seats
     * for this screening in the database.
     */
    public void calculateSeatingAvailability() {
        EntityManager em = Persistence.createEntityManagerFactory("default")
                .createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ReservedSeat> query = cb.createQuery(ReservedSeat.class);
        Root<ReservedSeat> seatRoot = query.from(ReservedSeat.class);
        // SELECT * FROM ReservedSeat WHERE IDScreening = (this screening's ID)
        query.where(cb.equal(seatRoot.get("screening").get("IDScreening"), getID()));

        availability = new SeatingAvailability(getRoom().getNumRows(),
                getRoom().getSeatsPerRow(), true);
        em.createQuery(query).getResultList().forEach(seat ->
            availability.markUnavailable(seat.getRowNumber(), seat.getSeatNumber())
        );
    }
}
