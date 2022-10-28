package pl.edu.pjwstk.s22796.xyz.cinemareservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.models.Screening;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Path("/screenings")
public class ScreeningsResource {

    private final EntityManagerFactory emf;

    public ScreeningsResource() {
        emf = Persistence.createEntityManagerFactory("default");
    }

    @GET
    @Path("/list/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Screening> getScreeningsInInterval(@PathParam("start") long startLong,
                                                   @PathParam("end") long endLong) {
        // Create SQL query criteria
        EntityManager emgr = emf.createEntityManager();
        CriteriaBuilder cb = emgr.getCriteriaBuilder();
        CriteriaQuery<Screening> query = cb.createQuery(Screening.class);
        Root<Screening> screeningRoot = query.from(Screening.class);

        // Parse start and end time
        Instant startInstant = Instant.ofEpochSecond(startLong);
        LocalDateTime start = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
        Instant endInstant = Instant.ofEpochSecond(endLong);
        LocalDateTime end = LocalDateTime.ofInstant(endInstant, ZoneId.systemDefault());

        query.where(cb.between(screeningRoot.get("dateAndTime"), start, end));
        return emgr.createQuery(query.select(screeningRoot)).getResultList();
    }
}
