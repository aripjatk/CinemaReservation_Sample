package pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata;

import java.time.Instant;

/**
 * Represents a confirmation of a reservation request made by the user.
 * To be transmitted via REST.
 */
@SuppressWarnings("unused")
public class ReservationInvoice {
    private double amountToPay;
    private Instant expiration;

    public ReservationInvoice(double amountToPay, Instant expiration) {
        this.setAmountToPay(amountToPay);
        this.setExpiration(expiration);
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }
}
