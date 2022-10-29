package pl.edu.pjwstk.s22796.xyz.cinemareservation.models;

import java.time.LocalDateTime;

/**
 * Class representing shortened version of information about a screening
 * (no seat availability or room number) returned by GET /screenings/list.
 */
@SuppressWarnings("unused")
public class ScreeningInformation {
    private Movie movie;
    private LocalDateTime dateAndTime;
    private Integer id;

    public ScreeningInformation(Movie movie, LocalDateTime dateTime, Integer id) {
        this.movie = movie;
        dateAndTime = dateTime;
        this.setId(id);
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
