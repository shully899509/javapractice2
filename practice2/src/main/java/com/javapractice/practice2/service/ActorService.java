package com.javapractice.practice2.service;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.Movie;
import com.javapractice.practice2.repository.ActorRepository;
import com.javapractice.practice2.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Actor> actor = actorRepository.findById(id);
        if (actor.isPresent()) {
            return actor.get();
        } else {
            throw new EntityNotFoundException("Actor with ID " + id + " not found.");
        }
    }

    public Optional<Actor> findActorById(int id) {
        return actorRepository.findById(id);
    }

    public List<Movie> getMoviesByActor(Actor actor) {
        return movieRepository.findMoviesByActorsIs(actor);
    }

    public List<Movie> getMoviesByActorID(int actorId) {
        return movieRepository.findMoviesByActorId(actorId);
    }

    @Transactional
    public Actor insertActor(Actor actor) {
        return actorRepository.save(actor);
    }

    @Transactional
    public void deleteActor(int actorId) {
        actorRepository.deleteById(actorId);
    }

    @Transactional
    public Actor updateActor(int id, Actor updatedActor) {
        if (actorRepository.existsById(id)) {
            return actorRepository.save(new Actor(id, updatedActor.getName(), updatedActor.getMovies()));
        } else {
            throw new EntityNotFoundException("Actor with ID " + id + " not found.");
        }
    }

    @Transactional
    public void addMovieToActor(int actorId, int movieId) {
        Optional<Actor> actor = actorRepository.findById(actorId);
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (actor.isEmpty()) {
            throw new EntityNotFoundException("Actor with ID " + actorId + " not found.");
        } else if (movie.isEmpty()) {
            throw new EntityNotFoundException("Movie with ID " + movieId + " not found.");
        } else {
            actor.get().addMovie(movie.get());
            actorRepository.save(actor.get());
            movieRepository.save(movie.get());
        }
    }

    @Transactional
    public void removeMovieFromActor(int actorId, int movieId) {
        Optional<Actor> actor = actorRepository.findById(actorId);
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (actor.isEmpty()) {
            throw new EntityNotFoundException("Actor with ID " + actorId + " not found.");
        } else if (movie.isEmpty()) {
            throw new EntityNotFoundException("Movie with ID " + movieId + " not found.");
        } else {
            actor.get().removeMovie(movie.get());
            actorRepository.save(actor.get());
            movieRepository.save(movie.get());
        }
    }
}
