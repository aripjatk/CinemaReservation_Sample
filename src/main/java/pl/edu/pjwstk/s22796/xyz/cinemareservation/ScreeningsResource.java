package pl.edu.pjwstk.s22796.xyz.cinemareservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.models.Movie;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.models.Screening;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.models.ScreeningInformation;

import java.time.LocalDateTime;
import java.util.List;

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
        // Create database query
        CriteriaBuilder cb = emgr.getCriteriaBuilder();
        CriteriaQuery<ScreeningInformation> query = cb.createQuery(ScreeningInformation.class);
        Root<Screening> screeningRoot = query.from(Screening.class);

        // Parse start and end time
        LocalDateTime start = LocalDateTime.of(year, month, day, startHour, startMinute);
        LocalDateTime end = LocalDateTime.of(year, month, day, endHour, endMinute);

        // SELECT movie, dateAndTime, IDScreening FROM Screening WHERE dateAndTime BETWEEN (start) AND (end)
        query.where(cb.between(screeningRoot.get("dateAndTime"), start, end));
        Selection<Movie> movieSelection = screeningRoot.get("movie");
        Selection<LocalDateTime> dateTimeSelection = screeningRoot.get("dateAndTime");
        Selection<Integer> idSelection = screeningRoot.get("IDScreening");
        return emgr.createQuery(query.multiselect(movieSelection, dateTimeSelection, idSelection))
                .getResultList();
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

        Screening scr = emgr.createQuery(query.select(screeningRoot))
                .getResultList().stream().findFirst().orElseThrow();
        scr.calculateSeatingAvailability();
        return scr;
    }
}
