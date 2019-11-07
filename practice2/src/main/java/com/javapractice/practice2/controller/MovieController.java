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
    public List<Movie> getActors() {
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
        movieService.deleteMovie(id);
    }

    @GetMapping("/{id}/actors")
    public List<Actor> getActors(@PathVariable("id") int id) {
        return movieService.getActorsByMovie(movieService.getMovieById(id));
    }

    @GetMapping("/{movieId}/actors/{actorId}")
    public Actor getMovieActorById(@PathVariable("movieId") int movieId, @PathVariable("actorId") int actorId) {
        Actor actor = actorService.getActorById(actorId);
        if (movieService.getActorsByMovieId(movieId).contains(actor)) {
            return actor;
        } else {
            throw new EntityNotFoundException("Actor " + actorId + " is not in Movie " + movieId);
        }
    }

    @PatchMapping("/{movieId}/actors/{actorId}")
    public void addActorToMovie(@PathVariable("movieId") int movieId, @PathVariable("actorId") int actorId) {
        movieService.addActorToMovie(movieId, actorId);
    }

    @DeleteMapping("/{movieId}/actors/{actorId}")
    public void removeActorFromMovie(@PathVariable("movieId") int movieId, @PathVariable("actorId") int actorId) {
        movieService.removeActorFromMovie(movieId, actorId);
    }

    @PatchMapping("/{movieId}/director/{directorId}")
    public void updateMovieDirector(@PathVariable("movieId") int movieId, @PathVariable("directorId") int directorId) {
        movieService.updateMovieDirector(movieId, directorId);
    }
}
