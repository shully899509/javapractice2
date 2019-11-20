package com.javapractice.practice2.service;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.Director;
import com.javapractice.practice2.model.Movie;
import com.javapractice.practice2.repository.ActorRepository;
import com.javapractice.practice2.repository.DirectorRepository;
import com.javapractice.practice2.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, ActorRepository actorRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
    }

    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(int id) {
        return movieRepository.getOne(id);
    }

    @Transactional
    public Movie insertMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovieById(int id) {
        try {
            movieRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Movie with ID " + id + " not found.");
        }
    }

    @Transactional
    public Movie updateMovie(int id, Movie updatedMovie) {
        if (movieRepository.existsById(id)) {
            return movieRepository.save(Movie.builder(updatedMovie).withId(id).build());
        } else {
            throw new EntityNotFoundException("Movie with ID " + id + " not found.");
        }
    }

    public Set<Actor> getActorsByMovie(Movie movie) {
        return actorRepository.findActorsByMoviesIs(movie);
    }

    public Set<Actor> getActorsByMovieId(int movieId) {
        return actorRepository.findActorsByMovieId(movieId);
    }

    @Transactional
    public Movie addActorToMovie(int movieId, int actorId) {
        Movie movie = movieRepository.getOne(movieId);
        Actor actor = actorRepository.getOne(actorId);
        validateActorInMovie(movie, actor);
        movie.addActor(actor);
        movieRepository.save(movie);
        actorRepository.save(actor);
        return movie;
    }

    private void validateActorInMovie(Movie movie, Actor actor) {
        if (movie.getActors().contains(actor)) {
            throw new EntityNotFoundException("Movie with ID " + movie.getId() + " already contains Actor with ID " + actor.getId());
        }
    }

    @Transactional
    public Movie removeActorFromMovie(int movieId, int actorId) {
        Movie movie = movieRepository.getOne(movieId);
        Actor actor = actorRepository.getOne(actorId);
        validateActorNotInMovie(movie, actor);
        movie.removeActor(actor);
        movieRepository.save(movie);
        actorRepository.save(actor);
        return movie;

    }

    private void validateActorNotInMovie(Movie movie, Actor actor) {
        if (!movie.getActors().contains(actor)) {
            throw new EntityNotFoundException("Movie with ID " + movie.getId() + " does not contain Actor with ID " + actor.getId());
        }
    }

    @Transactional
    public Director getMovieDirector(int movieId) {
        return movieRepository.findDirectorByMovieId(movieId);
    }

    @Transactional
    public Movie updateMovieDirector(int movieId, int directorId) {
        Movie movie = movieRepository.getOne(movieId);
        Director director = directorRepository.getOne(directorId);
        movie = Movie.builder(movie).withDirector(director).build();
        return movieRepository.save(movie);
    }
}
