package com.javapractice.practice2.repository;

import com.javapractice.practice2.model.Actor;
import com.javapractice.practice2.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findMoviesByDirector_Id(int directorId);

    List<Movie> findMoviesByActorsIs(Actor actor);

    @Query(value = "SELECT a.movies FROM Actor a WHERE a.id = ?1")
    List<Movie> findMoviesByActorId(int actorId);

    @Query(value = "SELECT M.* FROM MOVIES M LEFT OUTER JOIN ACTOR_MOVIE AM ON M.ID = AM.MOVIE_ID WHERE AM.ACTOR_ID = ?1",
            nativeQuery = true)
    List<Movie> findMoviesByActorIdNative(int actorId);
}
