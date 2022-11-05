package pl.edu.pjwstk.s22796.xyz.cinemareservation;

public class SeatReservedException extends RuntimeException {
    public SeatReservedException(){}

    @Override
    public String getMessage() {
        return "At least one of the requested seats has already been reserved";
    }
}
