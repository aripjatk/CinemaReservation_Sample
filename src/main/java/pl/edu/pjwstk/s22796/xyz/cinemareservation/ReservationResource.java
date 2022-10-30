package pl.edu.pjwstk.s22796.xyz.cinemareservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.Reservation;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.ReservedSeat;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.Screening;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata.ReservationInvoice;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata.ReservationRequest;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata.SeatingAvailability;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Contains API methods to manage reservations.
 */
@Path("/reservation")
public class ReservationResource {

    private final EntityManagerFactory emgrFactory;

    public ReservationResource() {
        emgrFactory = Persistence.createEntityManagerFactory("default");
    }

    public static boolean validateName(String name, boolean lastName) {
        if(name.length() < 3)
            return false;
        // !(isUpperCase()) is used instead of (isLowerCase) because the latter
        // would also return false if the character is not a letter at all
        if(!(Character.isUpperCase(name.charAt(0))))
            return false;
        for(int i=1;i<name.length();i++) {
            char ch = name.charAt(i);
            if(Character.isLetter(ch)) {
                if(Character.isUpperCase(ch))
                    return false;
            } else {
                if(ch == '-') {
                    if(!lastName)
                        return false;
                    else {
                        lastName = false; // lastName can only have one hyphen
                        if(!(Character.isUpperCase(name.charAt(++i))))
                            return false;
                    }
                } else
                    return false;
            }
        }
        return true;
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ReservationInvoice makeReservation(ReservationRequest req) {

        if(req.getSeats().isEmpty())
            throw new IllegalArgumentException("Reservation must be for at least one seat");

        if(!(validateName(req.getName(), false) && validateName(req.getSurname(), true)))
            throw new IllegalArgumentException("Invalid name or surname");

        // Start transaction to avoid partial reservations e.g. in case one seat is taken
        EntityManager emgr = emgrFactory.createEntityManager();
        EntityTransaction trans = emgr.getTransaction();
        trans.begin();

        // Make sure all seats are for the same screening
        int screeningID = 0;
        for(ReservationRequest.Seat seat : req.getSeats()) {
            if(screeningID == 0)
                screeningID = seat.getScreeningID();
            else
                if(screeningID != seat.getScreeningID())
                    throw new IllegalArgumentException("Seats must be for the same screening");
        }
        if(screeningID == 0)
            throw new IllegalArgumentException("No screening specified");

        // Prepare query for looking up screening for reservation request
        CriteriaBuilder cb = emgr.getCriteriaBuilder();
        CriteriaQuery<Screening> query = cb.createQuery(Screening.class);
        Root<Screening> screeningRoot = query.from(Screening.class);
        query.where(cb.equal(screeningRoot.get("IDScreening"), screeningID));
        Screening scr = emgr.createQuery(query).getSingleResult();

        // Initialise reservation object with info from POST request
        // then save it, so it can be used as foreign key for ReservedSeats
        Reservation reservation = new Reservation();
        reservation.setName(req.getName());
        reservation.setSurname(req.getSurname());
        reservation.setExpiration(Instant.now().plus(24, ChronoUnit.HOURS));
        emgr.persist(reservation);

        double totalPrice = 0D;
        ArrayList<ReservedSeat> seatsReservedThisSession = new ArrayList<>();

        // Reserve each seat, converting ReservationRequest.Seat to ReservedSeat
        // Duplicate reservations are prevented by the composite primary key constraint
        // (see entities.ReservedSeatPrimaryKey)
        for(ReservationRequest.Seat seat : req.getSeats()) {

            ReservedSeat seat1 = new ReservedSeat();
            seat1.setReservation(reservation);
            seat1.setScreening(scr);
            seat1.setIDScreening(scr.getID());
            seat1.setTicketType(seat.getTicketType());

            // Validate row number
            if(scr.getRoom().getNumRows() < seat.getRowNumber())
                throw new IllegalArgumentException("Row number " + seat.getRowNumber() + " does not exist in the room");
            else if(seat.getRowNumber() <= 0)
                throw new IllegalArgumentException(seat.getRowNumber() + " is not a valid row number");
            else
                seat1.setRowNumber(seat.getRowNumber());

            // Validate seat number
            if(seat.getSeatNumber() > 26 || seat.getSeatNumber() <= 0)
                throw new IllegalArgumentException(seat.getSeatNumber() + " is not a valid seat number");
            else if(scr.getRoom().getSeatsPerRow() < seat.getSeatNumber())
                throw new IllegalArgumentException("Seat " + seat.getRowNumber() + SeatingAvailability.seat(seat.getSeatNumber())
                        + " does not exist in the room");
            else
                seat1.setSeatNumber(seat.getSeatNumber());

            emgr.persist(seat1);
            seatsReservedThisSession.add(seat1);
            totalPrice += seat.getTicketType().getPrice();
        }

        // Check if there are any empty seats left over
        seatsReservedThisSession.stream().mapToInt(ReservedSeat::getRowNumber)
                        .distinct().forEach(row -> {
                            int[] seatNumbers =
                                    seatsReservedThisSession.stream().filter(seat -> seat.getRowNumber() == row)
                                    .mapToInt(ReservedSeat::getSeatNumber).sorted().toArray();
                            if(seatNumbers.length >= 2)
                                for(int i=1;i<seatNumbers.length;i++)
                                    if(seatNumbers[i] != (seatNumbers[i-1]+1))
                                        throw new IllegalArgumentException("There cannot be a single place left over " +
                                                "in a row between two already reserved places");
                });

        trans.commit();
        return new ReservationInvoice(totalPrice, reservation.getExpiration());
    }
}
