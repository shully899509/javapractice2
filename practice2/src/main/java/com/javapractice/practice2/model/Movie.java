package com.javapractice.practice2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "movies")
public class Movie {
    private int id;
    private String name;
    private Director director;
    private Set<Actor> actors = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "movies")
    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = new HashSet<>(actors);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
        actor.getMovies().add(this);
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
        actor.getMovies().removeIf(a -> a.getId() == this.id);
    }

    public Movie() {
    }

    private Movie(int id, String name, Director director, Set<Actor> actors) {
        this.id = id;
        this.name = name;
        this.director = director;
        this.actors = actors;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id &&
                Objects.equals(name, movie.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static MovieBuilder builder(){
        return new MovieBuilder();
    }

    public static MovieBuilder builder(Movie movie){
        return new MovieBuilder(movie);
    }

    public static class MovieBuilder {
        private int id;
        private String name;
        private Director director;
        private Set<Actor> actors = new HashSet<>();

        private MovieBuilder() {
        }

        private MovieBuilder(Movie movie) {
            this.id = movie.getId();
            this.name = movie.getName();
            this.director = movie.getDirector();
            this.actors = new HashSet<>(movie.getActors());
        }

        public MovieBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public MovieBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public MovieBuilder withDirector(Director director) {
            this.director = director;
            return this;
        }

        public MovieBuilder withMovies(Set<Actor> actors) {
            this.actors = actors;
            return this;
        }

        public Movie build() {
            return new Movie(id, name, director, actors);
        }
    }
}
