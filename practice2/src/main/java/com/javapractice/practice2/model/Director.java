package com.javapractice.practice2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "directors")
public class Director implements Person {
    private int id;
    private String name;
    private Set<Movie> movies = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "director")
    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = new HashSet<>(movies);
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

    private Director(int id, String name, Set<Movie> movies) {
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
        return "Director{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Director director = (Director) o;
        return id == director.id &&
                Objects.equals(name, director.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static DirectorBuilder builder() {
        return new DirectorBuilder();
    }

    public static DirectorBuilder builder(Director director) {
        return new DirectorBuilder(director);
    }

    public static class DirectorBuilder {
        private int id;
        private String name;
        private Set<Movie> movies = new HashSet<>();

        private DirectorBuilder() {
        }

        private DirectorBuilder(Director director) {
            this.id = director.getId();
            this.name = director.getName();
            this.movies = new HashSet<>(director.getMovies());
        }

        public DirectorBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public DirectorBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DirectorBuilder withMovies(Set<Movie> movies) {
            this.movies = movies;
            return this;
        }

        public Director build() {
            return new Director(id, name, movies);
        }
    }
}
