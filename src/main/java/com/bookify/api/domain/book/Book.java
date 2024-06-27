package com.bookify.api.domain.book;

import com.bookify.api.domain.author.Author;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String language;
    private Integer downloadQuantity;


    @ManyToOne(cascade = CascadeType.ALL)
    private Author author;

    public Book(BookDTO bookDTO) {
        this.title = bookDTO.title();
        this.language = bookDTO.language().getFirst();
        this.downloadQuantity = bookDTO.downloadQuantity();
        this.author = new Author(bookDTO.authors().getFirst());
    }

    @Override
    public String toString() {
        return  "--------------------- BOOK ---------------------" +
                "\nTitle:               " + title +
                "\nAuthor:              " + author +
                "\nLanguage:            " + language +
                "\nNumber of Downloads: " + downloadQuantity +
                "\n-----------------------------------------------\n";
    }
}
