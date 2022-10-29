package pl.edu.pjwstk.s22796.xyz.cinemareservation.entities;

import jakarta.persistence.*;

@Entity
@SuppressWarnings("unused")
public class ReservedSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "IDReservedSeat", nullable = false)
    private int idReservedSeat;

    @Column(nullable = false)
    private int rowNumber;
    @Column(nullable = false)
    private int seatNumber;
    @ManyToOne
    @JoinColumn(name = "IDScreening")
    private Screening screening;
    @ManyToOne
    @JoinColumn(name = "IDReservation")
    private Reservation reservation;
    @Column(nullable = false)
    private TicketType ticketType;

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getIdReservedSeat() {
        return idReservedSeat;
    }

    public void setIdReservedSeat(int idReservedSeat) {
        this.idReservedSeat = idReservedSeat;
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
}
