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
        return actorService.findActorById(id).orElse(null);
    }

    @PostMapping
    public Actor create(@RequestBody Actor actor) {
        return actorService.insertActor(actor);
    }

    @PatchMapping("/{id}")
    public Actor update(@PathVariable("id") int id, @RequestBody Actor actor) {
        return actorService.updateActor(id, actor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        actorService.deleteActor(id);
    }

    @GetMapping("/{id}/movies")
    public List<Movie> getMovies(@PathVariable("id") int id) {
        return actorService.getMoviesByActor(actorService.getActorById(id));
    }

    @GetMapping("/{actorId}/movies/{movieId}")
    public Movie getActorMovieById(@PathVariable("actorId") int actorId, @PathVariable("movieId") int movieId) {
        Movie movie = movieService.getMovieById(movieId);
        if (actorService.getMoviesByActorID(actorId).contains(movie)) {
            return movie;
        } else {
            throw new EntityNotFoundException("Actor " + actorId + " is not in Movie " + movieId);
        }
    }

    @PatchMapping("/{actorId}/movies/{movieId}")
    public void addMovieToActor(@PathVariable("actorId") int actordId, @PathVariable("movieId") int movieId) {
        actorService.addMovieToActor(actordId, movieId);
    }

    @DeleteMapping("/{actorId}/movies/{movieId}")
    public void removeMovieFromActor(@PathVariable("actorId") int actordId, @PathVariable("movieId") int movieId) {
        actorService.removeMovieFromActor(actordId, movieId);
    }
}
