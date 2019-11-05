package com.javapractice.practice2.service;

import com.javapractice.practice2.model.Director;
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
class DirectorServiceTest {

    private final DirectorService directorService;
    private final MovieService movieService;

    @Autowired
    DirectorServiceTest(DirectorService directorService, MovieService movieService) {
        this.directorService = directorService;
        this.movieService = movieService;
    }

    @Test
    void getDirectorsTest() {
        Director panaMea = new Director("Pana Mea");
        Director insertedDirector = directorService.insertDirector(panaMea);
        assertEquals(1, directorService.getDirectors().size());
    }

    @Test
    void insertDirectorTest(){
        Director panaMea = new Director("Pana Mea");
        Director insertedDirector = directorService.insertDirector(panaMea);
        assertEquals(panaMea, directorService.getDirectorById(insertedDirector.getId()));
    }

    @Test
    void insertDirectorWithMovies(){
        Director panaMea = new Director("Pana Mea");
        Movie movie1 = new Movie("A");
        Movie movie2 = new Movie("B");
        panaMea.addMovie(movie1);
        panaMea.addMovie(movie2);

        Director insertedDirector = directorService.insertDirector(panaMea);

        assertEquals(panaMea, directorService.getDirectorById(insertedDirector.getId()));
        assertEquals(2, movieService.getMovies().size());
        assertEquals(panaMea.getMovies(), directorService.getMoviesByDirector(insertedDirector));
    }
}