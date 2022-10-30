package pl.edu.pjwstk.s22796.xyz.cinemareservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.Movie;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.Room;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.entities.Screening;

import java.time.LocalDateTime;

@ApplicationPath("/cinema")
public class ScreeningsApplication extends Application {

    private static boolean initDone = false;

    /**
     * Initialises the database with sample data.
     * Does nothing if it has already been run, or if the database is not empty.
     */
    public static void initDB() {
        if(initDone) return;
        EntityManager emgr = Persistence.createEntityManagerFactory("default").createEntityManager();

        // only initialise if no data is present
        // (only the Screening table is checked for performance reasons)
        CriteriaBuilder cb = emgr.getCriteriaBuilder();
        CriteriaQuery<Screening> screeningQuery = cb.createQuery(Screening.class);
        // SELECT * FROM Screening
        if(!(emgr.createQuery(screeningQuery.select(screeningQuery.from(Screening.class)))
                .getResultList().isEmpty()))
            return;

        EntityTransaction trans = emgr.getTransaction();
        trans.begin();
        Movie m1 = new Movie("Avatar 2");
        Movie m2 = new Movie("X-Men");
        Movie m3 = new Movie("The Room");
        Movie[] sampleMovies = new Movie[] {m1, m2, m3};
        for(Movie m : sampleMovies)
            emgr.persist(m);
        Room r1 = new Room(new char[] {'1'}, 8, 8);
        Room r2 = new Room(new char[] {'2'}, 8, 6);
        Room r3 = new Room(new char[] {'3'}, 6, 6);
        Room[] samplerooms = new Room[] {r1, r2, r3};
        for(Room r: samplerooms)
            emgr.persist(r);
        Screening s1 = new Screening(m1, r1, LocalDateTime.now().plusDays(1));
        Screening s2 = new Screening(m3, r1, LocalDateTime.now().plusHours(12));
        Screening s3 = new Screening(m2, r2, LocalDateTime.now().plusHours(18));
        Screening s4 = new Screening(m1, r2, LocalDateTime.now().plusMinutes(30));
        Screening s5 = new Screening(m3, r3, LocalDateTime.now().plusHours(20));
        Screening s6 = new Screening(m1, r3, LocalDateTime.now().plusHours(3));
        Screening[] sampleScreenings = new Screening[] {s1, s2, s3, s4, s5, s6};
        for(Screening s : sampleScreenings) {
            s.setDateAndTime(s.getDateAndTime().minusNanos(s.getDateAndTime().getNano()));
            emgr.persist(s);
        }
        trans.commit();
        initDone = true;
    }
    public ScreeningsApplication() {
    }
}
