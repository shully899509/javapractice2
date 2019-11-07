package com.javapractice.practice2.service;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Director;
import com.javapractice.practice2.model.Movie;
import com.javapractice.practice2.repository.DirectorRepository;
import com.javapractice.practice2.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Director> director = directorRepository.findById(id);
        if (director.isPresent()) {
            return director.get();
        } else {
            throw new EntityNotFoundException("Director with ID " + id + " not found.");
        }
    }

    public Optional<Director> findDirectorById(int id) {
        return directorRepository.findById(id);
    }

    public List<Movie> getMoviesByDirectorId(int directorId) {
        return movieRepository.findMoviesByDirectorId(directorId);
    }

    @Transactional
    public Director insertDirector(Director director) {
        return directorRepository.save(director);
    }

    @Transactional
    public void deleteDirector(int directorId) {
        directorRepository.deleteById(directorId);
    }

    @Transactional
    public Director updateDirector(int id, Director updatedDirector) {
        if (directorRepository.existsById(id)) {
            return directorRepository.save(new Director(id, updatedDirector.getName(), updatedDirector.getMovies()));
        } else {
            throw new EntityNotFoundException("Person with ID " + id + " not found.");
        }
    }

    @Transactional
    public void addMovieToDirector(int directorId, int movieId) {
        Optional<Director> director = directorRepository.findById(directorId);
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (director.isEmpty()) {
            throw new EntityNotFoundException("Director with ID " + directorId + " not found.");
        } else if (movie.isEmpty()) {
            throw new EntityNotFoundException("Movie with ID " + movieId + " not found.");
        } else {
            director.get().addMovie(movie.get());
            directorRepository.save(director.get());
            movieRepository.save(movie.get());
        }
    }

    @Transactional
    public void removeMovieFromDirector(int directorId, int movieId) {
        Optional<Director> director = directorRepository.findById(directorId);
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (director.isEmpty()) {
            throw new EntityNotFoundException("Director with ID " + directorId + " not found.");
        } else if (movie.isEmpty()) {
            throw new EntityNotFoundException("Movie with ID " + movieId + " not found.");
        } else {
            director.get().removeMovie(movie.get());
            directorRepository.save(director.get());
            movieRepository.save(movie.get());
        }
    }
}
