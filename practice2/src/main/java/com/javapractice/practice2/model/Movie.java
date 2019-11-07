package com.javapractice.practice2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne
    private Director director;

    @ManyToMany(mappedBy = "movies")
    private Set<Actor> actors = new HashSet<>();

    @JsonIgnore
    public Set<Actor> getActors() {
        return new HashSet<>(actors);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
        actor.getMovies().add(this);
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
        actor.getMovies().remove(this);
    }

    public Movie() {
    }

    public Movie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Movie(int id, String name, Director director, Set<Actor> actors) {
        this.id = id;
        this.name = name;
        this.director = director;
        this.actors = actors;
    }

    public Movie(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", director=" + director +
                ", actors=" + actors +
                '}';
    }
}
