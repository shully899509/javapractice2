package com.persons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("SELECT new Person(id, name) FROM Person WHERE LOWER(name) = LOWER(:name)")
    public List<Person> findPersonsByName(@Param("name") String name);
}
