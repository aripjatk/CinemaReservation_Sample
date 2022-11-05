package pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata;

import java.time.LocalDateTime;

/**
 * Represents a confirmation of a reservation request made by the user.
 * To be transmitted via REST.
 */
@SuppressWarnings("unused")
public class ReservationInvoice {
    private double amountToPay;
    private LocalDateTime expiration;

    public ReservationInvoice(double amountToPay, LocalDateTime expiration) {
        this.setAmountToPay(amountToPay);
        this.setExpiration(expiration);
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
