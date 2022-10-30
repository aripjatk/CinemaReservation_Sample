package pl.edu.pjwstk.s22796.xyz.cinemareservation;

import pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata.ReservationRequest;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata.SeatingAvailability;

public class SeatReservedException extends RuntimeException {
    ReservationRequest.Seat seat;
    public SeatReservedException(ReservationRequest.Seat seat) {
        this.seat = seat;
    }

    @Override
    public String getMessage() {
        return "Seat " + seat.getRowNumber() + SeatingAvailability.seat(seat.getSeatNumber()) +
                " has already been reserved";
    }
}
