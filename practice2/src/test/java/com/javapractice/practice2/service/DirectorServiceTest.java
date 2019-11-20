package com.javapractice.practice2.service;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Director;
import com.javapractice.practice2.model.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Rollback
@SpringBootTest
@ExtendWith(SpringExtension.class)
class DirectorServiceTest {

    private final DirectorService directorService;
    private final MovieService movieService;
    private Director director;
    private Movie movie;

    @Autowired
    DirectorServiceTest(DirectorService directorService, MovieService movieService) {
        this.directorService = directorService;
        this.movieService = movieService;
    }

    private Director createDirector(Director.DirectorBuilder builder, String director12) {
        return builder.withName(director12).build();
    }

    private Movie createMovie(String movie2) {
        return Movie.builder().withName(movie2).build();
    }

    @BeforeEach
    public void setup() {
        director = directorService.insertDirector(createDirector(Director.builder(), "director12"));
        movie = movieService.insertMovie(createMovie("movie1"));
    }

    @Test
    @DisplayName("Check insert Director and getAll")
    void getDirectorsTest() {
        assertEquals(2, directorService.getDirectors().size());
    }

    @Test
    @DisplayName("Check insert Director and getDirectorById")
    void insertDirectorTest() {
        assertEquals(director, directorService.getDirectorById(director.getId()));
    }

    @Test
    @DisplayName("Check delete Director after insert")
    void deleteDirectorTest() {
        int directorId = director.getId();
        directorService.deleteDirectorById(directorId);
        Exception exception = Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> directorService.getDirectorById(directorId));
        assertEquals("Unable to find com.javapractice.practice2.model.Director with id "
                + directorId + "; nested exception is javax.persistence.EntityNotFoundException: Unable to find com.javapractice.practice2.model.Director with id "
                + directorId, exception.getMessage());
    }

    @Test
    @DisplayName("Check delete Director throws exception not found")
    void deleteDirectorFail() {
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> directorService.deleteDirectorById(-1));
        assertEquals("Director with ID -1 not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Check update Director")
    void updateDirectorNameTest() {
        director = createDirector(Director.builder(director), "updatedDirectorName");
        director = directorService.updateDirector(director.getId(), director);
        assertEquals("updatedDirectorName", director.getName());
    }

    @Test
    @DisplayName("Check update Director throws exception not found")
    void updateDirectorFail() {
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> directorService.updateDirector(-1, createDirector(Director.builder(), "updatedDirectorFail")));
        assertEquals("Director with ID -1 not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Check insert director with movies and getMoviesByDirectorId")
    void getDirectorMoviesTest() {
        Movie movie1 = movieService.insertMovie(createMovie("movie1"));
        Movie movie2 = movieService.insertMovie(createMovie("movie2"));
        directorService.addMovieToDirector(director.getId(), movie1.getId());
        directorService.addMovieToDirector(director.getId(), movie2.getId());
        Set<Movie> movies = new HashSet<>(director.getMovies());
        assertEquals(movies, directorService.getMoviesByDirectorId(director.getId()));
    }

    @Test
    @DisplayName("Check addMovieToDirector")
    void addMovieToDirectorTest() {
        director = directorService.addMovieToDirector(director.getId(), movie.getId());
        assertEquals(director.getMovies(), directorService.getMoviesByDirectorId(director.getId()));
    }

    @Test
    @DisplayName("Check addMovieToDirector fail no Movie")
    void addMovieToDirectorFailNoMovie() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> directorService.addMovieToDirector(director.getId(), -1));
        assertEquals("Unable to find com.javapractice.practice2.model.Movie with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check addMovieToDirector fail no Director")
    void addMovieToDirectorFailNoDirector() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> directorService.addMovieToDirector(-1, movie.getId()));
        assertEquals("Unable to find com.javapractice.practice2.model.Director with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check addMovieToDirector fail is linked")
    void addMovieToDirectorFailIsLinked() {
        director = directorService.addMovieToDirector(director.getId(), movie.getId());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> directorService.addMovieToDirector(director.getId(), movie.getId()));
        assertEquals("Director with ID " + director.getId() + " already contains Movie with ID " + movie.getId(), exception.getMessage());
    }

    @Test
    @DisplayName("Check removeMovieFromDirector")
    void removeMovieFromDirectorTest() {
        Movie movie2 = movieService.insertMovie(createMovie("Movie2"));
        director = directorService.addMovieToDirector(director.getId(), movie.getId());
        director = directorService.addMovieToDirector(director.getId(), movie2.getId());
        assertEquals(director.getMovies(), directorService.getMoviesByDirectorId(director.getId()));
        director = directorService.removeMovieFromDirector(director.getId(), movie2.getId());
        assertEquals(director.getMovies(), directorService.getMoviesByDirectorId(director.getId()));
    }

    @Test
    @DisplayName("Check removeMovieFromDirector fail no Movie")
    void removeMovieFromDirectorFailNoMovie() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> directorService.removeMovieFromDirector(director.getId(), -1));
        assertEquals("Unable to find com.javapractice.practice2.model.Movie with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check removeMovieFromDirector fail no Director")
    void removeMovieFromDirectorFailNoDirector() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> directorService.removeMovieFromDirector(-1, movie.getId()));
        assertEquals("Unable to find com.javapractice.practice2.model.Director with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check removeMovieFromDirector no link")
    void removeMovieFromDirectorFailNoLink() {
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> directorService.removeMovieFromDirector(director.getId(), movie.getId()));
        assertEquals("Director with ID " + director.getId() + " does not contain Movie with ID " + movie.getId(), exception.getMessage());
    }
}