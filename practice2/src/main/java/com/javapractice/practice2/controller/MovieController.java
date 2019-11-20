package com.javapractice.practice2.controller;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.Movie;
import com.javapractice.practice2.service.ActorService;
import com.javapractice.practice2.service.MovieService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Api("Movies API")
@RestController
@RequestMapping("movies")
public class MovieController {
    private final MovieService movieService;
    private final ActorService actorService;

    @Autowired
    public MovieController(MovieService movieService, ActorService actorService) {
        this.movieService = movieService;
        this.actorService = actorService;
    }

    @GetMapping
    public List<Movie> getAll() {
        /*Movie movie = new Movie("movie");
        Actor actor1 = new Actor("actor1");
        Actor actor2 = new Actor("actor2");
        movie.addActor(actor1);
        movie.addActor(actor2);
        movie = movieService.insertMovie(movie);
        movieService.removeActorFromMovie(movie.getId(), actor1.getId());*/

        return movieService.getMovies();
    }

    @GetMapping("/{id}")
    public Movie getById(@PathVariable("id") int id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public Movie create(@RequestBody Movie movie) {
        return movieService.insertMovie(movie);
    }

    @PatchMapping("/{id}")
    public Movie update(@PathVariable("id") int id, @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        movieService.deleteMovieById(id);
    }

    @GetMapping("/{id}/actors")
    public Set<Actor> getActors(@PathVariable("id") int id) {
        return movieService.getActorsByMovie(movieService.getMovieById(id));
    }

    @GetMapping("/{movie-id}/actors/{actor-id}")
    public Actor getMovieActorById(@PathVariable("movie-id") int movieId, @PathVariable("actor-id") int actorId) {
        Actor actor = actorService.getActorById(actorId);
        if (movieService.getActorsByMovieId(movieId).contains(actor)) {
            return actor;
        } else {
            throw new EntityNotFoundException("Actor " + actorId + " is not in Movie " + movieId);
        }
    }

    @PatchMapping("/{movie-id}/actors/{actor-id}")
    public void addActorToMovie(@PathVariable("movie-id") int movieId, @PathVariable("actor-id") int actorId) {
        movieService.addActorToMovie(movieId, actorId);
    }

    @DeleteMapping("/{movie-id}/actors/{actor-id}")
    public void removeActorFromMovie(@PathVariable("movie-id") int movieId, @PathVariable("actor-id") int actorId) {
        movieService.removeActorFromMovie(movieId, actorId);
    }

    @PatchMapping("/{movie-id}/director/{director-id}")
    public Movie updateMovieDirector(@PathVariable("movie-id") int movieId, @PathVariable("director-id") int directorId) {
        return movieService.updateMovieDirector(movieId, directorId);
    }
}
