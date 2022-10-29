package pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata;

import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.TicketType;

import java.util.List;

/**
 * Represents a user's request to reserve a specified set of seats.
 * This class is used at the stage where the system has not yet confirmed the reservation.
 */
@SuppressWarnings("unused")
public class ReservationRequest {
    /**
     * Represents a specific seat to be reserved as part of this request.
     */
    public static class Seat {
        public Seat() {}
        public Seat(int row, int seat, int screening, TicketType ticketType) {
            setRowNumber(row);
            setSeatNumber(seat);
            setScreeningID(screening);
            setTicketType(ticketType);
        }
        private int rowNumber;
        private int seatNumber;
        private int screeningID;

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

        public int getScreeningID() {
            return screeningID;
        }

        public void setScreeningID(int screeningID) {
            this.screeningID = screeningID;
        }

        public TicketType getTicketType() {
            return ticketType;
        }

        public void setTicketType(TicketType ticketType) {
            this.ticketType = ticketType;
        }
    }
    private String name;
    private String surname;
    private List<Seat> seats;

    /**
     * Returns the string which the user claims to be their (first) name,
     * pending validation.
     * @return (first) name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the string which the user claims to be their surname
     * (last name), pending validation.
     * @return surname/last name
     */
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns a list of all the seats which the user wishes to reserve
     * for their visit.
     * @return list of ReservationRequest.Seat objects
     */
    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
