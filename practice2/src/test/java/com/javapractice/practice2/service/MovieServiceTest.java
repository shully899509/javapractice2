package com.javapractice.practice2.service;

import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Rollback
@SpringBootTest
@ExtendWith(SpringExtension.class)
class MovieServiceTest {
    private final MovieService movieService;

    @Autowired
    MovieServiceTest(MovieService movieService) {
        this.movieService = movieService;
    }

    @Test
    public void getActorsOfMovieTest(){
        Movie movie1 = new Movie("movie1");

        Actor actor1 = new Actor("actor1");
        Actor actor2 = new Actor("actor2");
        movie1.addActor(actor1); movie1.addActor(actor2);

        movieService.insertMovie(movie1);
        List<Actor> actors = movieService.getActorsByMovie(movie1);

        assertEquals(movie1.getActors(), actors);
    }
}