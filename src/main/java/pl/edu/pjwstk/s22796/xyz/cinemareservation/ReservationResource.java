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

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Contains API methods to manage reservations.
 */
@Path("/reservation")
public class ReservationResource {

    private final EntityManagerFactory emgrFactory;

    public ReservationResource() {
        emgrFactory = Persistence.createEntityManagerFactory("default");
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ReservationInvoice makeReservation(ReservationRequest req) {

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
        // Reserve each seat, converting ReservationRequest.Seat to ReservedSeat
        // Duplicate reservations are prevented by the composite primary key constraint
        // (see entities.ReservedSeatPrimaryKey)
        for(ReservationRequest.Seat seat : req.getSeats()) {
            ReservedSeat seat1 = new ReservedSeat();
            seat1.setReservation(reservation);
            seat1.setScreening(scr);
            seat1.setIDScreening(scr.getID());
            seat1.setSeatNumber(seat.getSeatNumber());
            seat1.setRowNumber(seat.getRowNumber());
            seat1.setTicketType(seat.getTicketType());
            emgr.persist(seat1);
            totalPrice += seat.getTicketType().getPrice();
        }

        trans.commit();
        return new ReservationInvoice(totalPrice, reservation.getExpiration());
    }
}
