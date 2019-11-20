package com.javapractice.practice2.service;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Director;
import com.javapractice.practice2.model.Movie;
import com.javapractice.practice2.repository.DirectorRepository;
import com.javapractice.practice2.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public DirectorService(DirectorRepository directorRepository, MovieRepository movieRepository) {
        this.directorRepository = directorRepository;
        this.movieRepository = movieRepository;
    }

    public List<Director> getDirectors() {
        return directorRepository.findAll();
    }

    public Director getDirectorById(int id) {
        return directorRepository.getOne(id);
    }

    @Transactional
    public Director insertDirector(Director director) {
        return directorRepository.save(director);
    }

    @Transactional
    public void deleteDirectorById(int id) {
        try {
            directorRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Director with ID " + id + " not found.");
        }
    }

    @Transactional
    public Director updateDirector(int id, Director updatedDirector) {
        if (directorRepository.existsById(id)) {
            return directorRepository.save(updatedDirector.builder(updatedDirector).withId(id).build());
        } else {
            throw new EntityNotFoundException("Director with ID " + id + " not found.");
        }
    }

    public Set<Movie> getMoviesByDirectorId(int directorId) {
        return movieRepository.findMoviesByDirectorId(directorId);
    }

    @Transactional
    public Director addMovieToDirector(int directorId, int movieId) {
        Director director = directorRepository.getOne(directorId);
        Movie movie = movieRepository.getOne(movieId);
        validateMovieInDirector(director, movie);
        director.addMovie(movie);
        directorRepository.save(director);
        movieRepository.save(movie);
        return director;

    }

    private void validateMovieInDirector(Director director, Movie movie) {
        if (director.getMovies().contains(movie)) {
            throw new EntityNotFoundException("Director with ID " + director.getId() + " already contains Movie with ID " + movie.getId());
        }
    }

    @Transactional
    public Director removeMovieFromDirector(int directorId, int movieId) {
        Director director = directorRepository.getOne(directorId);
        Movie movie = movieRepository.getOne(movieId);
        validateMovieNotInDirector(director, movie);
        director.removeMovie(movie);
        directorRepository.save(director);
        movieRepository.save(movie);
        return director;
    }

    private void validateMovieNotInDirector(Director director, Movie movie) {
        if (!director.getMovies().contains(movie)) {
            throw new EntityNotFoundException("Director with ID " + director.getId() + " does not contain Movie with ID " + movie.getId());
        }
    }
}
