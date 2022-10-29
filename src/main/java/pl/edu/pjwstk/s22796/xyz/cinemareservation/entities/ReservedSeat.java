package pl.edu.pjwstk.s22796.xyz.cinemareservation.entities;

import jakarta.persistence.*;

/**
 * Represents the reservation of a single seat in one screening.
 */
@Entity
@SuppressWarnings("unused")
public class ReservedSeat {

    @EmbeddedId
    private ReservedSeatPrimaryKey primaryKey;

    @ManyToOne
    @JoinColumn(name = "IDScreening", insertable = false, updatable = false)
    private Screening screening;

    @ManyToOne
    @JoinColumn(name = "IDReservation")
    private Reservation reservation;

    @Column(nullable = false)
    private TicketType ticketType;

    public ReservedSeatPrimaryKey getPrimaryKey() {
        if(primaryKey == null)
            primaryKey = new ReservedSeatPrimaryKey();
        return primaryKey;
    }

    public void setPrimaryKey(ReservedSeatPrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public int getRowNumber() {
        return getPrimaryKey().getRowNumber();
    }

    public void setRowNumber(int rowNumber) {
        getPrimaryKey().setRowNumber(rowNumber);
    }

    public int getSeatNumber() {
        return getPrimaryKey().getSeatNumber();
    }

    public void setSeatNumber(int seatNumber) {
        getPrimaryKey().setSeatNumber(seatNumber);
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public int getIDScreening() {
        return getPrimaryKey().getIDScreening();
    }

    public void setIDScreening(int IDScreening) {
        getPrimaryKey().setIDScreening(IDScreening);
    }
}
