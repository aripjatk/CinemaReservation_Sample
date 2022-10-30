package pl.edu.pjwstk.s22796.xyz.cinemareservation.entities;

import jakarta.persistence.*;

@Entity
@SuppressWarnings("unused")
public class Movie {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "IDMovie")
    private int idMovie;
    @Column(nullable = false)
    private String title;

    public Movie() {}

    public Movie(String title) {
        this.title = title;
    }

    public int getID() {
        return idMovie;
    }

    public void setID(int ID) {
        idMovie = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
