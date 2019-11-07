package com.javapractice.practice2;

import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.Director;
import com.javapractice.practice2.model.Movie;
import com.javapractice.practice2.service.ActorService;
import com.javapractice.practice2.service.DirectorService;
import com.javapractice.practice2.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoData {
    private final DirectorService directorService;
    private final MovieService movieService;
    private final ActorService actorService;

    @Autowired
    public DemoData(DirectorService directorService, MovieService movieService, ActorService actorService) {
        this.directorService = directorService;
        this.movieService = movieService;
        this.actorService = actorService;
    }

    public void run(){
        Director director1 = new Director(1, "Director1");
        directorService.insertDirector(director1);

        Actor actor1 = new Actor(1, "actor1");
        actorService.insertActor(actor1);

        Movie movie1 = new Movie(1,"movie1");
        Movie movie2 = new Movie(2,"movie2");

        movieService.insertMovie(movie1);
        movieService.insertMovie(movie2);

        directorService.addMovieToDirector(1, 1);
        actorService.addMovieToActor(1, 1);
    }
}
