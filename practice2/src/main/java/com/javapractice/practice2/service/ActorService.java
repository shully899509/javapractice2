package com.javapractice.practice2.service;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.Movie;
import com.javapractice.practice2.repository.ActorRepository;
import com.javapractice.practice2.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ActorService {
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    public List<Actor> getActors() {
        return actorRepository.findAll();
    }

    public Actor getActorById(int id) {
        return actorRepository.getOne(id);
    }

    public Set<Movie> getMoviesByActor(Actor actor) {
        return movieRepository.findMoviesByActorsIs(actor);
    }

    public Set<Movie> getMoviesByActorID(int actorId) {
        return movieRepository.findMoviesByActorId(actorId);
    }

    @Transactional
    public Actor insertActor(Actor actor) {
        return actorRepository.save(actor);
    }

    @Transactional
    public void deleteActorById(int id) {
        try {
            actorRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Actor with ID " + id + " not found.");
        }
    }

    @Transactional
    public Actor updateActor(Actor updatedActor, int id) {
        if (actorRepository.existsById(id)) {
            return actorRepository.save(Actor.builder(updatedActor).withId(id).build());
        } else {
            throw new EntityNotFoundException("Actor with ID " + id + " not found.");
        }
    }

    @Transactional
    public Actor addMovieToActor(int actorId, int movieId) {
        Actor actor = actorRepository.getOne(actorId);
        Movie movie = movieRepository.getOne(movieId);
        validateMovieInActor(actor, movie);
        actor.addMovie(movie);
        actorRepository.save(actor);
        movieRepository.save(movie);
        return actor;
    }

    private void validateMovieInActor(Actor actor, Movie movie) {
        if (actor.getMovies().contains(movie)) {
            throw new EntityNotFoundException("Actor with ID " + actor.getId() + " already contains Movie with ID " + movie.getId());
        }
    }

    @Transactional
    public Actor removeMovieFromActor(int actorId, int movieId) {
        Actor actor = actorRepository.getOne(actorId);
        Movie movie = movieRepository.getOne(movieId);
        validateMovieNotInActor(actor, movie);
        actor.removeMovie(movie);
        actorRepository.save(actor);
        movieRepository.save(movie);
        return actor;
    }

    private void validateMovieNotInActor(Actor actor, Movie movie) {
        if (!actor.getMovies().contains(movie)) {
            throw new EntityNotFoundException("Actor with ID " + actor.getId() + " does not contain Movie with ID " + movie.getId());
        }
    }
}
