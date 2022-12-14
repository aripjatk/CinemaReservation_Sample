package pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata;

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

    @SuppressWarnings("unused")
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
    public static char seat(int num) {
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
                if(availability[i][j]) { // this seat is available
                    if(consecutiveFreeSeats == 0)
                        firstFreeSeat = j;
                    consecutiveFreeSeats++;
                } else { // this seat is unavailable...
                    if(consecutiveFreeSeats == 0) // ... and the one before it wasn't either
                        continue;
                    if(consecutiveFreeSeats == 1) // ... but the one before it was
                        builder.append(seat(j));
                    else // ... but more than one before it was
                        builder.append(seat(firstFreeSeat+1))
                                .append('-').append(seat(j));
                    builder.append(",").append(i+1);
                    consecutiveFreeSeats = 0;
                }
                // if the builder ends with the current row number, remove it
                if(Character.getNumericValue(builder.charAt(builder.length()-1)) == i)
                    builder.deleteCharAt(builder.length()-1);
            }

            if(consecutiveFreeSeats == 1) // if the last seat in a row is available
                builder.append(seat(seatsPerRow));
            else if(consecutiveFreeSeats > 1) // if the last several seats in a row are available
                builder.append(seat(firstFreeSeat+1)).append('-').append(seat(seatsPerRow));

            // if the builder ends with the current row number, remove it
            if(Character.getNumericValue(builder.charAt(builder.length()-1)) == i+1)
                builder.deleteCharAt(builder.length()-1);
            builder.append(",");
        }
        // remove trailing comma
        if(builder.charAt(builder.length()-1) == ',')
            builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
}
