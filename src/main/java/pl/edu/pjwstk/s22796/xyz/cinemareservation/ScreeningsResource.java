package pl.edu.pjwstk.s22796.xyz.cinemareservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.Movie;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.Screening;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata.ScreeningInformation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains API methods to view screenings.
 */
@Path("/screenings")
public class ScreeningsResource {

    private final EntityManager emgr;

    public ScreeningsResource() {
        emgr = Persistence.createEntityManagerFactory("default")
                .createEntityManager();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ScreeningInformation> getScreeningsInInterval(@QueryParam("month") int month, @QueryParam("year") int year,
                                                   @QueryParam("day") int day, @QueryParam("hStart") int startHour,
                                                   @QueryParam("hEnd") int endHour,
                                                   @QueryParam("mStart") int startMinute,
                                                   @QueryParam("mEnd") int endMinute) {
        ScreeningsApplication.initDB(); // add sample data if run first time
        // Create database query
        CriteriaBuilder cb = emgr.getCriteriaBuilder();
        CriteriaQuery<ScreeningInformation> query = cb.createQuery(ScreeningInformation.class);
        Root<Screening> screeningRoot = query.from(Screening.class);

        LocalDateTime start = LocalDateTime.of(year, month, day, startHour, startMinute);
        LocalDateTime end = LocalDateTime.of(year, month, day, endHour, endMinute);

        // SELECT movie, dateAndTime, IDScreening FROM Screening
        // WHERE dateAndTime BETWEEN (start) AND (end)
        query.where(cb.between(screeningRoot.get("dateAndTime"), start, end));
        Selection<Movie> movieSelection = screeningRoot.get("movie");
        Selection<LocalDateTime> dateTimeSelection = screeningRoot.get("dateAndTime");
        Selection<Integer> idSelection = screeningRoot.get("IDScreening");
        
        List<ScreeningInformation> result = emgr.createQuery(query.multiselect(movieSelection, dateTimeSelection, idSelection))
                .getResultList();
        ArrayList<ScreeningInformation> sortedResult = new ArrayList<>(result);
        Collections.sort(sortedResult);
        return sortedResult;
    }

    @GET
    @Path("/details/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Screening getScreeningDetails(@PathParam("id") int idScreening) {
        // Create criteria for database query
        CriteriaBuilder cb = emgr.getCriteriaBuilder();
        CriteriaQuery<Screening> query = cb.createQuery(Screening.class);
        Root<Screening> screeningRoot = query.from(Screening.class);

        // SELECT * FROM Screening WHERE IDScreening = (idScreening)
        query.where(cb.equal(screeningRoot.get("IDScreening"), idScreening));

        Screening scr = emgr.createQuery(query.select(screeningRoot)).getSingleResult();
        scr.calculateSeatingAvailability();
        return scr;
    }
}
