package com.javapractice.practice2.service;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Actor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Rollback
@SpringBootTest
@ExtendWith(SpringExtension.class)
class MovieServiceTest {
    private final MovieService movieService;
    private final ActorService actorService;
    private final DirectorService directorService;
    private Movie movie;
    private Actor actor;

    @Autowired
    MovieServiceTest(MovieService movieService, ActorService actorService, DirectorService directorService) {
        this.movieService = movieService;
        this.actorService = actorService;
        this.directorService = directorService;
    }

    @BeforeEach
    public void setup() {
        Director director = directorService.insertDirector(Director.builder().withName("director1").build());
        movie = movieService.insertMovie(Movie.builder().withName("movie").withDirector(director).build());
        actor = actorService.insertActor(createActor("actor1"));
    }

    @Test
    @DisplayName("Check insert Movie and getAll")
    void getMoviesTest() {
        assertEquals(3, movieService.getMovies().size());
    }

    @Test
    @DisplayName("Check insert Movie and getMovieById")
    void insertMovieTest() {
        assertEquals(movie, movieService.getMovieById(movie.getId()));
    }

    @Test
    @DisplayName("Check delete Movie after insert")
    void deleteMovieTest() {
        int movieId = movie.getId();
        movieService.deleteMovieById(movieId);
        Exception exception = Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> movieService.getMovieById(movieId));
        assertEquals("Unable to find com.javapractice.practice2.model.Movie with id "
                + movieId + "; nested exception is javax.persistence.EntityNotFoundException: Unable to find com.javapractice.practice2.model.Movie with id "
                + movieId, exception.getMessage());
    }

    @Test
    @DisplayName("Check delete Movie throws exception not found")
    void deleteMovieFail() {
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> movieService.deleteMovieById(-1));
        assertEquals("Movie with ID -1 not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Check update Movie")
    void updateMovieNameTest() {
        movie = Movie.builder(movie).withName("updatedMovieName").build();
        movie = movieService.updateMovie(movie.getId(), movie);
        assertEquals("updatedMovieName", movie.getName());
    }

    @Test
    @DisplayName("Check update Movie throws exception not found")
    void updateMovieFail() {
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> movieService.updateMovie(-1, Movie.builder().withName("updatedMovie").build()));
        assertEquals("Movie with ID -1 not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Check getActorsByMovie")
    public void getMovieActorsTest() {
        Actor actor2 = actorService.insertActor(createActor("actor2"));
        movie = movieService.addActorToMovie(movie.getId(), actor.getId());
        movie = movieService.addActorToMovie(movie.getId(), actor2.getId());
        assertEquals(new HashSet<>(movie.getActors()), movieService.getActorsByMovieId(movie.getId()));
    }

    @Test
    @DisplayName("Check addActorToMovie")
    public void addActorToMovieTest() {
        movie = movieService.addActorToMovie(movie.getId(), actor.getId());
        assertEquals(movie.getActors(), movieService.getActorsByMovie(movie));
    }

    @Test
    @DisplayName("Check addActorToMovie fail no Actor")
    public void addActorToMovieFailNoActor() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> movieService.addActorToMovie(movie.getId(), -1));
        assertEquals("Unable to find com.javapractice.practice2.model.Actor with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check addActorToMovie fail no Movie")
    public void addActorToMovieFailNoMovie() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> movieService.addActorToMovie(-1, actor.getId()));
        assertEquals("Unable to find com.javapractice.practice2.model.Movie with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check addActorToMovie fail Actor is in Movie")
    public void addActorToMovieFailActorIsInMovie() {
        movie = movieService.addActorToMovie(movie.getId(), actor.getId());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> movieService.addActorToMovie(movie.getId(), actor.getId()));
        assertEquals("Movie with ID " + movie.getId() + " already contains Actor with ID " + actor.getId(), exception.getMessage());
    }

    @Test
    @DisplayName("Check removeActorFromMovie")
    public void removeActorFromMovieTest() {
        Actor actor2 = actorService.insertActor(createActor("actor2"));
        movie = movieService.addActorToMovie(movie.getId(), actor.getId());
        movie = movieService.addActorToMovie(movie.getId(), actor2.getId());
        movie = movieService.removeActorFromMovie(movie.getId(), actor.getId());
        assertEquals(movie.getActors(), movieService.getActorsByMovieId(movie.getId()));
    }

    @Test
    @DisplayName("Check removeActorFromMovie fail no Actor")
    public void removeActorFromMovieFailNoActor() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> movieService.removeActorFromMovie(movie.getId(), -1));
        assertEquals("Unable to find com.javapractice.practice2.model.Actor with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check addActorToMovie fail no Movie")
    public void removeActorFromMovieFailNoMovie() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> movieService.removeActorFromMovie(-1, actor.getId()));
        assertEquals("Unable to find com.javapractice.practice2.model.Movie with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check addActorToMovie fail Actor is not in Movie")
    public void removeActorFromMovieFailActorIsNotInMovie() {
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> movieService.removeActorFromMovie(movie.getId(), actor.getId()));
        assertEquals("Movie with ID " + movie.getId() + " does not contain Actor with ID " + actor.getId(), exception.getMessage());
    }

    private Actor createActor(String actor2) {
        return Actor.builder().withName(actor2).build();
    }

    @Test
    @DisplayName("Check getDirectorOfMovie")
    public void getDirectorOfMovieTest() {
        assertEquals(movie.getDirector(), movieService.getMovieDirector(movie.getId()));
    }

    @Test
    @DisplayName("Check update movie director")
    public void updateMovieDirectorTest() {
        Director director1 = directorService.insertDirector(Director.builder().withName("directorUpdated").build());
        movie = movieService.updateMovieDirector(movie.getId(), director1.getId());
        assertEquals(movie.getDirector(), movieService.getMovieDirector(movie.getId()));
    }

    @Test
    @DisplayName("Check update movie director fail no Movie")
    public void updateMovieDirectorFailNoMovie() {
        Director director1 = directorService.insertDirector(Director.builder().withName("directorUpdated").build());
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> movieService.updateMovieDirector(-1, director1.getId()));
        assertEquals("Unable to find com.javapractice.practice2.model.Movie with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check update movie director fail no Director")
    public void updateMovieDirectorFailNoDirector() {
        Exception exception = Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> movieService.updateMovieDirector(movie.getId(), -1));
        assertEquals("Unable to find com.javapractice.practice2.model.Director with id -1; nested exception is javax.persistence.EntityNotFoundException: Unable to find com.javapractice.practice2.model.Director with id -1", exception.getMessage());
    }
}