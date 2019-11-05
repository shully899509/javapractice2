package com.javapractice.practice2.service;

import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.EntityNotFoundException;
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

    @Transactional
    public Actor insertActor(Actor actor) {
        return actorRepository.save(actor);
    }

    @Transactional
    public void deleteActor(Actor actor) {
        actorRepository.delete(actor);
    }

    @Transactional
    public Actor updateActor(Actor actor, Actor updatedActor) {
        if (findActorById(actor.getId()).isPresent()) {
            return actorRepository.save(new Actor(actor.getId(), updatedActor.getName(), updatedActor.getMovies()));
        } else {
            throw new EntityNotFoundException("Person with ID " + actor.getId() + " not found.");
        }
    }
}
