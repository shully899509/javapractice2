package com.javapractice.practice2.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "actors")
public class Actor implements Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(cascade =
            {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "Actor_Movie",
            joinColumns = {
                    @JoinColumn(
                            name = "actor_id",
                            referencedColumnName = "id"
                    )
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "movie_id",
                            referencedColumnName = "id"
                    )
            }
    )
    private List<Movie> movies = new ArrayList<>();

    public List<Movie> getMovies(){
        return movies;
    }

    public void addMovie(Movie movie){
        movie.addActorToMovie(this);
        movies.add(movie);
    }

    public void removeMovie(Movie movie){
        movie.removeActorFromMovie(this);
        movies.remove(movie);
    }

    public void addMovieToActor(Movie movie){
        movies.add(movie);
    }
    public void removeMovieFromActor(Movie movie){
        movies.remove(movie);
    }

    public Actor(String name) {
        this.name = name;
    }

    public Actor(int id, String name, List<Movie> movies) {
        this.name = name;
        this.movies = movies;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


}
