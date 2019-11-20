package com.javapractice.practice2.service;

import com.javapractice.practice2.exceptions.EntityNotFoundException;
import com.javapractice.practice2.model.Actor;
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
class ActorServiceTest {
    private final ActorService actorService;
    private final MovieService movieService;
    private Actor actor;
    private Movie movie;

    @Autowired
    ActorServiceTest(ActorService actorService, MovieService movieService) {
        this.actorService = actorService;
        this.movieService = movieService;
    }

    @BeforeEach
    public void setup() {
        actor = actorService.insertActor(Actor.builder().withName("actor").build());
        movie = movieService.insertMovie(Movie.builder().withName("movie1").build());
    }

    @Test
    @DisplayName("Check insert Actor and getAll")
    void getActorsTest() {
        assertEquals(2, actorService.getActors().size());
    }

    @Test
    @DisplayName("Check insert Actor and getActorById")
    void insertActorTest() {
        assertEquals(actor, actorService.getActorById(actor.getId()));
    }

    @Test
    @DisplayName("Check delete Actor after insert")
    void deleteActorTest() {
        int actorId = actor.getId();
        actorService.deleteActorById(actorId);
        Exception exception = Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> actorService.getActorById(actorId));
        assertEquals("Unable to find com.javapractice.practice2.model.Actor with id "
                + actorId + "; nested exception is javax.persistence.EntityNotFoundException: Unable to find com.javapractice.practice2.model.Actor with id "
                + actorId, exception.getMessage());
    }

    @Test
    @DisplayName("Check delete Actor throws exception not found")
    void deleteActorFail() {
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> actorService.deleteActorById(-1));
        assertEquals("Actor with ID -1 not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Check update Actor")
    void updateActorNameTest() {
        actor = Actor.builder(actor).withName("updatedActorName").build();
        actor = actorService.updateActor(actor, actor.getId());
        assertEquals("updatedActorName", actor.getName());
    }

    @Test
    @DisplayName("Check update Actor throws exception not found")
    void updateActorFail() {
        Actor updatedActor = Actor.builder().withName("actorName").build();
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> actorService.updateActor(updatedActor, -1));
        assertEquals("Actor with ID -1 not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Check getMoviesByActor")
    void getActorMoviesTest() {
        Movie movie2 = movieService.insertMovie(Movie.builder().withName("movie2").build());
        actor = actorService.addMovieToActor(actor.getId(), movie.getId());
        actor = actorService.addMovieToActor(actor.getId(), movie2.getId());
        assertEquals(new HashSet<>(actor.getMovies()), actorService.getMoviesByActor(actor));
    }

    @Test
    @DisplayName("Check addMovieToActor")
    void addMovieToActorTest() {
        actor = actorService.addMovieToActor(actor.getId(), movie.getId());
        assertEquals(actor.getMovies(), actorService.getMoviesByActor(actor));
    }

    @Test
    @DisplayName("Check addMovieToActor fail no Movie")
    void addMovieToActorFailNoMovie() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> actorService.addMovieToActor(actor.getId(), -1));
        assertEquals("Unable to find com.javapractice.practice2.model.Movie with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check addMovieToActor fail no Actor")
    void addMovieToActorFailNoActor() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> actorService.addMovieToActor(-1, movie.getId()));
        assertEquals("Unable to find com.javapractice.practice2.model.Actor with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check addMovieToActor fail Movie is in Actor")
    public void addMovieToActorFailMovieIsInActor() {
        actor = actorService.addMovieToActor(actor.getId(), movie.getId());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> actorService.addMovieToActor(actor.getId(), movie.getId()));
        assertEquals("Actor with ID " + actor.getId() + " already contains Movie with ID " + movie.getId(), exception.getMessage());
    }

    @Test
    @DisplayName("Check removeMovieFromActor")
    void removeMovieFromActorTest() {
        Movie movie2 = movieService.insertMovie(Movie.builder().withName("movie2").build());
        actor = actorService.addMovieToActor(actor.getId(), movie.getId());
        actor = actorService.addMovieToActor(actor.getId(), movie2.getId());
        actor = actorService.removeMovieFromActor(actor.getId(), movie2.getId());
        assertEquals(actor.getMovies(), actorService.getMoviesByActor(actor));
    }

    @Test
    @DisplayName("Check removeMovieFromActor fail no Movie")
    public void removeMovieFromActorFailNoActor() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> actorService.removeMovieFromActor(actor.getId(), -1));
        assertEquals("Unable to find com.javapractice.practice2.model.Movie with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check removeMovieFromActor fail no Actor")
    public void removeMovieFromActorFailNoMovie() {
        Exception exception = Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> actorService.removeMovieFromActor(-1, movie.getId()));
        assertEquals("Unable to find com.javapractice.practice2.model.Actor with id -1", exception.getMessage());
    }

    @Test
    @DisplayName("Check removeMovieFromActor fail Movie is not in Actor")
    public void removeMovieFromActorFailMovieIsNotIn() {
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> actorService.removeMovieFromActor(actor.getId(), movie.getId()));
        assertEquals("Actor with ID " + actor.getId() + " does not contain Movie with ID " + movie.getId(), exception.getMessage());
    }
}