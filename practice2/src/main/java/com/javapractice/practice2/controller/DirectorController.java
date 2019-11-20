package com.javapractice.practice2.controller;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Director;
import com.javapractice.practice2.model.Movie;
import com.javapractice.practice2.service.DirectorService;
import com.javapractice.practice2.service.MovieService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Api(tags = "Services API")
@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;
    private final MovieService movieService;

    @Autowired
    public DirectorController(DirectorService directorService, MovieService movieService) {
        this.directorService = directorService;
        this.movieService = movieService;
    }

    @GetMapping
    public List<Director> getAll() {
        return directorService.getDirectors();
    }

    @GetMapping("/{id}")
    public Director getById(@PathVariable("id") int id) {
        return directorService.getDirectorById(id);
    }

    @PostMapping()
    public Director create(@RequestBody Director director) {
        return directorService.insertDirector(director);
    }

    @PatchMapping("/{id}")
    public Director update(@PathVariable("id") int id, @RequestBody Director director) {
        return directorService.updateDirector(id, director);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        directorService.deleteDirectorById(id);
    }

    @GetMapping("/{id}/movies")
    public Set<Movie> getMoviesByDirectorId(@PathVariable("id") int id) {
        return directorService.getMoviesByDirectorId(id);
    }

    @GetMapping("/{director-id}/movies/{movie-id}")
    public Movie getDirectorMovieById(@PathVariable("director-id") int directorId, @PathVariable("movie-id") int movieId) {
        Movie movie = movieService.getMovieById(movieId);
        if (directorService.getMoviesByDirectorId(directorId).contains(movie)) {
            return movie;
        } else {
            throw new EntityNotFoundException("Movie " + movieId + " is not in Director " + directorId);
        }
    }

    @PatchMapping("/{director-id}/movies/{movie-id}")
    public void addMovieToDirector(@PathVariable("director-id") int directorId, @PathVariable("movie-id") int movieId) {
        directorService.addMovieToDirector(directorId, movieId);
    }

    @DeleteMapping("/{director-id}/movies/{movie-id}")
    public void removeMovieFromDirector(@PathVariable("director-id") int directorId, @PathVariable("movie-id") int movieId) {
        directorService.removeMovieFromDirector(directorId, movieId);
    }
}
