package com.javapractice.practice2.repository;

import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Set<Actor> findActorsByMoviesIs(Movie movie);

    @Query(value = "SELECT m.actors FROM Movie m WHERE m.id = ?1")
    Set<Actor> findActorsByMovieId(int movieId);
}
