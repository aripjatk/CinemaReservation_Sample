package pl.edu.pjwstk.s22796.xyz.cinemareservation.models;

/**
 * Stores information about availability of seats in a screening, which is calculated
 * on the fly by the server on request, and therefore not stored in the database.
 * Each seat is marked with a digit indicating the row, followed by a letter
 * indicating the 'column'. For example, the third seat from the left in the 2nd row is 2C.
 */
public class SeatingAvailability {
    private final int numRows;
    private final int seatsPerRow;
    private final boolean[][] availability;

    public SeatingAvailability(int rows, int seatsPerRow, boolean defaultValue) {
        if(rows <= 0)
            throw new IllegalArgumentException("rows must be greater than 0");
        if(seatsPerRow <= 0 || seatsPerRow > 25)
            throw new IllegalArgumentException("seatsPerRow must be between 1 and 25");
        numRows = rows;
        this.seatsPerRow = seatsPerRow;
        availability = new boolean[numRows][seatsPerRow];
        for(int i=0;i<rows;i++)
            for(int j=0;j<seatsPerRow;j++)
                availability[i][j] = defaultValue;
    }

    public void markAvailable(int row, int seat) {
        availability[row-1][seat-1] = true;
    }

    public void markUnavailable(int row, int seat) {
        availability[row-1][seat-1] = false;
    }

    /**
     * Converts a seat's number in a row into its letter representation
     * (first seat = A, second seat = B, etc.)
     * @param num Seat number (starts at 1)
     * @return Seat letter
     */
    private char seat(int num) {
        return (char)((int)'A'+num-1);
    }

    /* Result: a String showing available seats divided by row.
    * Consecutive seats are abbreviated as (row)-(first seat)-(last seat)
    * Example: there are 3 rows of 3 seats each. Seats 1B and 3A are taken.
    * In such a situation, this function will return "1A,1C,2A-C,3B-C"
    */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<numRows;i++) {
            builder.append(i+1);
            int consecutiveFreeSeats = 0;
            int firstFreeSeat = 0;
            for(int j=0;j<seatsPerRow;j++) {
                if(availability[i][j]) {
                    if(consecutiveFreeSeats == 0)
                        firstFreeSeat = j;
                    consecutiveFreeSeats++;
                } else { // next seat unavailable
                    if(consecutiveFreeSeats == 0)
                        continue;
                    if(consecutiveFreeSeats == 1) // previous seat was available, this one is not
                        builder.append(seat(j));
                    else // more than one previous seat was available
                        builder.append(seat(firstFreeSeat+1))
                                .append('-').append(seat(j+2));
                    builder.append(",").append(i+1);
                    consecutiveFreeSeats = 0;
                }
                if(Character.getNumericValue(builder.charAt(builder.length()-1)) == i)
                // if the builder ends with the current row number
                    builder.deleteCharAt(builder.length()-1);
            }

            if(consecutiveFreeSeats == 1)
                builder.append(seat(seatsPerRow));
            else if(consecutiveFreeSeats > 1)
                builder.append(seat(firstFreeSeat+1)).append('-').append(seat(seatsPerRow));

            if(Character.getNumericValue(builder.charAt(builder.length()-1)) == i+1)
                builder.deleteCharAt(builder.length()-1);
            builder.append(",");
        }
        if(builder.charAt(builder.length()-1) == ',')
            builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
}
