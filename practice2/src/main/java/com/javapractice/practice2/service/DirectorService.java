package com.javapractice.practice2.service;

import com.javapractice.practice2.model.Director;
import com.javapractice.practice2.model.EntityNotFoundException;
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

    public List<Director> getDirectors(){
        return directorRepository.findAll();
    }

    public Director getDirectorById(int id){
        Optional<Director> director = directorRepository.findById(id);
        if (director.isPresent()) {
            return director.get();
        } else {
            throw new EntityNotFoundException("Director with ID " + id + " not found.");
        }
    }

    private Optional<Director> findDirectorById(int id){
        return directorRepository.findById(id);
    }

    public List<Movie> getMoviesByDirector(Director director){
        return movieRepository.findMoviesByDirector(director);
    }

    @Transactional
    public Director insertDirector(Director director){
        return directorRepository.save(director);
    }

    @Transactional
    public void deleteDirector(Director director){
        directorRepository.delete(director);
    }

    @Transactional
    public Director updateDirector(Director director, Director updatedDirector){
        if (findDirectorById(director.getId()).isPresent()){
            return directorRepository.save(new Director(director.getId(), updatedDirector.getName(), updatedDirector.getMovies()));
        } else {
            throw new EntityNotFoundException("Person with ID " + director.getId() + " not found.");
        }
    }
}
