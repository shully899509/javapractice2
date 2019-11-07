package com.javapractice.practice2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "directors")
public class Director implements Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}/*CascadeType.ALL*/, mappedBy = "director"/*, orphanRemoval = true*/)
    private Set<Movie> movies = new HashSet<>();

    @JsonIgnore
    public Set<Movie> getMovies() {
        return new HashSet<>(movies);
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
        movie.setDirector(this);
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
        movie.setDirector(null);
    }

    public Director() {
    }

    public Director(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Director(int id, String name, Set<Movie> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }

    public Director(String name) {
        this.name = name;
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

    /*@Override
    public String toString() {
        return "Director{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", movies=" + movies +
                '}';
    }*/
}
