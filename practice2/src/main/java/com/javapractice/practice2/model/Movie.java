package com.javapractice.practice2.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne
    private Director director;

    @ManyToMany(mappedBy = "movies", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Actor> actors = new ArrayList<>();

    public List<Actor> getActors() {
        return new ArrayList<>(actors);
    }

    public void addActor(Actor actor) {
        actor.addMovieToActor(this);
        actors.add(actor);
    }

    public void removeActor(Actor actor) {
        actor.removeMovieFromActor(this);
        actors.remove(actor);
    }

    public void addActorToMovie(Actor actor){
        actors.add(actor);
    }
    public void removeActorFromMovie(Actor actor){
        actors.remove(actor);
    }


    public Movie() {
    }

    public Movie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Movie(int id, String name, Director director, List<Actor> actors) {
        this.id = id;
        this.name = name;
        this.director = director;
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

    public Director getDirector(){
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
