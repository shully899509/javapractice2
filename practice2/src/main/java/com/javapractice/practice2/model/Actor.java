package com.javapractice.practice2.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "actors")
public class Actor implements Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}/*CascadeType.ALL*/)
    @JoinTable(name = "actor_movie", joinColumns = {@JoinColumn(name = "actor_id")}, inverseJoinColumns = {@JoinColumn(name = "movie_id")})
    private Set<Movie> movies = new HashSet<>();

    @JsonIgnore
    public Set<Movie> getMovies() {
        return movies;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
        movie.getActors().add(this);
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
        movie.getActors().remove(this);
    }

    public Actor() {

    }

    public Actor(String name) {
        this.name = name;
    }

    public Actor(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Actor(int id, String name, Set<Movie> movies) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                //", movies=" + movies +
                '}';
    }
}
