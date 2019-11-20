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

@Api("Actors API")
@RestController
@RequestMapping("actors")
public class ActorController {
    private final ActorService actorService;
    private final MovieService movieService;

    @Autowired
    public ActorController(ActorService actorService, MovieService movieService) {
        this.actorService = actorService;
        this.movieService = movieService;
    }

    @GetMapping
    public List<Actor> getActors() {
        return actorService.getActors();
    }

    @GetMapping("/{id}")
    public Actor getById(@PathVariable("id") int id) {
        return actorService.getActorById(id);
    }

    @PostMapping
    public Actor create(@RequestBody Actor actor) {
        return actorService.insertActor(actor);
    }

    @PatchMapping("/{id}")
    public Actor update(@PathVariable("id") int id, @RequestBody Actor actor) {
        return actorService.updateActor(actor, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        actorService.deleteActorById(id);
    }

    @GetMapping("/{id}/movies")
    public Set<Movie> getMovies(@PathVariable("id") int id) {
        return actorService.getMoviesByActorId(id);
    }

    @GetMapping("/{actor-id}/movies/{movie-id}")
    public Movie getActorMovieById(@PathVariable("actor-id") int actorId, @PathVariable("movie-id") int movieId) {
        Movie movie = movieService.getMovieById(movieId);
        if (actorService.getMoviesByActorId(actorId).contains(movie)) {
            return movie;
        } else {
            throw new EntityNotFoundException("Actor " + actorId + " is not in Movie " + movieId);
        }
    }

    @PatchMapping("/{actor-id}/movies/{movie-id}")
    public void addMovieToActor(@PathVariable("actor-id") int actordId, @PathVariable("movie-id") int movieId) {
        actorService.addMovieToActor(actordId, movieId);
    }

    @DeleteMapping("/{actor-id}/movies/{movie-id}")
    public void removeMovieFromActor(@PathVariable("actor-id") int actordId, @PathVariable("movie-id") int movieId) {
        actorService.removeMovieFromActor(actordId, movieId);
    }
}
