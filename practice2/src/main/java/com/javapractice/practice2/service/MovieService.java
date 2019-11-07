package com.javapractice.practice2.service;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.Director;
import com.javapractice.practice2.model.Movie;
import com.javapractice.practice2.repository.ActorRepository;
import com.javapractice.practice2.repository.DirectorRepository;
import com.javapractice.practice2.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            return movie.get();
        } else {
            throw new EntityNotFoundException("Movie with ID " + id + " not found.");
        }
    }

    private Optional<Movie> findMovieById(int id) {
        return movieRepository.findById(id);
    }


    public List<Actor> getActorsByMovie(Movie movie) {
        return actorRepository.findActorsByMoviesIs(movie);
    }

    public List<Actor> getActorsByMovieId(int movieId) {
        return actorRepository.findActorsByMovieId(movieId);
    }

    @Transactional
    public Movie insertMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }

    @Transactional
    public Movie updateMovie(int id, Movie updatedMovie) {
        if (movieRepository.existsById(id)) {
            return movieRepository.save(new Movie(id, updatedMovie.getName(), updatedMovie.getDirector(), updatedMovie.getActors()));
        } else {
            throw new EntityNotFoundException("Movie with ID " + id + " not found.");
        }
    }

    @Transactional
    public void addActorToMovie(int movieId, int actorId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        Optional<Actor> actor = actorRepository.findById(actorId);
        if (movie.isEmpty()) {
            throw new EntityNotFoundException("Movie with ID " + movieId + " not found.");
        } else if (actor.isEmpty()) {
            throw new EntityNotFoundException("Actor with ID " + actorId + " not found.");
        } else {
            movie.get().addActor(actor.get());
            movieRepository.save(movie.get());
            actorRepository.save(actor.get());
        }
    }

    @Transactional
    public void removeActorFromMovie(int movieId, int actorId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        Optional<Actor> actor = actorRepository.findById(actorId);
        if (movie.isEmpty()) {
            throw new EntityNotFoundException("Movie with ID " + movieId + " not found.");
        } else if (actor.isEmpty()) {
            throw new EntityNotFoundException("Actor with ID " + actorId + " not found.");
        } else {
            movie.get().removeActor(actor.get());
            movieRepository.save(movie.get());
            actorRepository.save(actor.get());
        }
    }

    @Transactional
    public void updateMovieDirector(int movieId, int directorId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        Optional<Director> director = directorRepository.findById(directorId);
        if (movie.isEmpty()) {
            throw new EntityNotFoundException("Movie with ID " + movieId + " not found.");
        } else if (director.isEmpty()) {
            throw new EntityNotFoundException("Director with ID " + directorId + " not found.");
        } else {
            movie.get().setDirector(director.get());
            movieRepository.save(movie.get());
        }
    }
}
