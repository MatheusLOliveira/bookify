package com.bookify.api.repository;

import com.bookify.api.domain.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByLanguage(String language);

}
