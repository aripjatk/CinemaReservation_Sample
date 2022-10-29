package pl.edu.pjwstk.s22796.xyz.cinemareservation.entities;

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
