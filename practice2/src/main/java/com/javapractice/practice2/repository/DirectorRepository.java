package com.javapractice.practice2.repository;

import com.javapractice.practice2.model.Director;
import com.javapractice.practice2.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectorRepository extends JpaRepository<Director, Integer> {

}
