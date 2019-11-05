package com.javapractice.practice2.service;

import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.EntityNotFoundException;
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
        if (movie.isPresent()){
            return movie.get();
        } else {
            throw new EntityNotFoundException("Movie with ID " + id + " not found.");
        }
    }

    private Optional<Movie> findMovieById(int id){
        return movieRepository.findById(id);
    }


    public List<Actor> getActorsByMovie(Movie movie){
        return actorRepository.findActorsByMoviesIs(movie);
    }

    @Transactional
    public Movie insertMovie(Movie movie){
        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Movie movie){
        movieRepository.delete(movie);
    }

    @Transactional
    public Movie updateMovie(Movie movie, Movie updatedMovie){
        if (findMovieById(movie.getId()).isPresent()){
            return movieRepository.save(new Movie(movie.getId(), updatedMovie.getName(), updatedMovie.getDirector(), updatedMovie.getActors()));
        } else {
            throw new EntityNotFoundException("Movie with ID " + movie.getId() + " not found.");
        }
    }
}
