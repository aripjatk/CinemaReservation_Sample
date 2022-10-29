package pl.edu.pjwstk.s22796.xyz.cinemareservation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@SuppressWarnings("unused")
@Embeddable
public class ReservedSeatPrimaryKey implements java.io.Serializable {
    private int rowNumber;

    private int seatNumber;

    @Column(name="IDScreening", insertable = false, updatable = false)
    private int IDScreening;

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

    public int getIDScreening() {
        return IDScreening;
    }

    public void setIDScreening(int IDScreening) {
        this.IDScreening = IDScreening;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ReservedSeatPrimaryKey) {
            ReservedSeatPrimaryKey target = (ReservedSeatPrimaryKey) obj;
            return target.getIDScreening() == getIDScreening()
                    && target.getSeatNumber() == getSeatNumber()
                    && target.getRowNumber() == getRowNumber();
        }
        return false;
    }

    @Override
    public int hashCode() {
        StringBuilder builder = new StringBuilder();
        builder.append(getIDScreening()).append(getSeatNumber()).append(getRowNumber());
        return Integer.parseInt(builder.toString());
    }
}
