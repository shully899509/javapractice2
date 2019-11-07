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
        return directorService.findDirectorById(id).orElse(null);
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
        directorService.deleteDirector(id);
    }

    @GetMapping("/{id}/movies")
    public List<Movie> getMoviesByDirectorId(@PathVariable("id") int id) {
        return directorService.getMoviesByDirectorId(id);
    }

    @GetMapping("/{directorId}/movies/{movieId}")
    public Movie getDirectorMovieById(@PathVariable("directorId") int directorId, @PathVariable("movieId") int movieId) {
        Movie movie = movieService.getMovieById(movieId);
        if (directorService.getMoviesByDirectorId(directorId).contains(movie)) {
            return movie;
        } else {
            throw new EntityNotFoundException("Movie " + movieId + " is not in Director " + directorId);
        }
    }

    @PatchMapping("/{directorId}/movies/{movieId}")
    public void addMovieToDirector(@PathVariable("directorId") int directorId, @PathVariable("movieId") int movieId) {
        directorService.addMovieToDirector(directorId, movieId);
    }

    @DeleteMapping("/{directorId}/movies/{movieId}")
    public void removeMovieFromDirector(@PathVariable("directorId") int directorId, @PathVariable("movieId") int movieId) {
        directorService.removeMovieFromDirector(directorId, movieId);
    }
}
