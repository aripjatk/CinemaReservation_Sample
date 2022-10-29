package pl.edu.pjwstk.s22796.xyz.cinemareservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.Reservation;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.ReservedSeat;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.Screening;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata.ReservationInvoice;

import java.time.ZoneOffset;

@Path("/reservation")
public class ReservationResource {

    private final EntityManager emgr;

    public ReservationResource() {
        emgr = Persistence.createEntityManagerFactory("default")
                .createEntityManager();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ReservationInvoice makeReservation(String name, String surname, ReservedSeat... seats) {
        // Make sure all seats are for the same screening
        Screening screening = null;
        for(ReservedSeat seat : seats) {
            if(screening == null)
                screening = seat.getScreening();
            else
                if(screening.getID() != seat.getScreening().getID())
                    throw new IllegalArgumentException("Seats must be for the same screening");
        }
        if(screening == null)
            throw new IllegalArgumentException("No screening specified");

        Reservation reservation = new Reservation();
        reservation.setName(name);
        reservation.setSurname(surname);
        reservation.setExpiration(screening.getDateAndTime().toInstant(ZoneOffset.UTC));
        double totalPrice = 0D;
        for(ReservedSeat seat : seats) {
            seat.setReservation(reservation);
            emgr.persist(seat);
            totalPrice += seat.getTicketType().getPrice();
        }
        emgr.persist(reservation);
        return new ReservationInvoice(totalPrice, reservation.getExpiration());
    }
}
