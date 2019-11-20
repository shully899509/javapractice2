package com.javapractice.practice2.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "actors")
public class Actor implements Person {
    private int id;
    private String name;
    private Set<Movie> movies = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "actor_movie", joinColumns = {@JoinColumn(name = "actor_id")}, inverseJoinColumns = {@JoinColumn(name = "movie_id")})
    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = new HashSet<>(movies);
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
        movie.getActors().add(this);
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
        movie.getActors().removeIf(m -> m.getId() == this.id);
    }

    public Actor() {
    }

    private Actor(int id, String name, Set<Movie> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return id == actor.id &&
                Objects.equals(name, actor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static ActorBuilder builder() {
        return new ActorBuilder();
    }

    public static ActorBuilder builder(Actor actor) {
        return new ActorBuilder(actor);
    }

    public static class ActorBuilder {
        private int id;
        private String name;
        private Set<Movie> movies = new HashSet<>();

        private ActorBuilder() {
        }

        private ActorBuilder(Actor actor) {
            this.id = actor.getId();
            this.name = actor.getName();
            this.movies = new HashSet<>(actor.getMovies());
        }

        public ActorBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public ActorBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ActorBuilder withMovies(Set<Movie> movies) {
            this.movies = movies;
            return this;
        }

        public Actor build() {
            return new Actor(id, name, movies);
        }
    }
}
