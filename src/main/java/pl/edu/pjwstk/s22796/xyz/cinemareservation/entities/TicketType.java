package pl.edu.pjwstk.s22796.xyz.cinemareservation.entities;

/**
 * Represents each category of cinema visitor that has a separate ticket price.
 */
public enum TicketType {
    ADULT, CHILD, STUDENT;
    public double getPrice() {
        return switch(this) {
            case ADULT -> 25.00;
            case CHILD -> 12.50;
            case STUDENT -> 18.00;
        };
    }
}
