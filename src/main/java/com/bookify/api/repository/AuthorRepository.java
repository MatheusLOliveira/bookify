package com.bookify.api.repository;

import com.bookify.api.domain.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByName(String name);

    @Query("SELECT a FROM Book b JOIN b.author a WHERE a.birthYear <= :year and a.deathYear >= :year")
    List<Author> listLivingAuthorInAYear(int year);

}
