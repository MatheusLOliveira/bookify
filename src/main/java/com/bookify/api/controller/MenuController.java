package com.bookify.api.controller;

import com.bookify.api.domain.author.Author;
import com.bookify.api.domain.book.Book;
import com.bookify.api.domain.book.BookDTO;
import com.bookify.api.repository.AuthorRepository;
import com.bookify.api.repository.BookRepository;
import com.bookify.api.service.ApiConsumption;
import com.bookify.api.service.DataConverter;
import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Controller
public class MenuController {
    private final String API_URL = "https://gutendex.com/";

    Scanner scanner = new Scanner(System.in);

    ApiConsumption apiConsumption = new ApiConsumption();
    DataConverter dataConverter = new DataConverter();
    AuthorRepository authorRepository;
    BookRepository bookRepository;

    public MenuController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void showMenu() {
        int option = -1;
        var menu = """
                    ***********************************************
                    *                                             *
                    *              Choose an option:              *
                    *                                             *
                    *   1- Search book by title                   *
                    *   2- List registered books                  *
                    *   3- List registered authors                *
                    *   4- List authors alive in a specific year  *
                    *   5- List books in a specific language      *
                    *                                             *
                    *   0- Exit                                   *
                    *                                             *
                    ***********************************************
                    """;

        while (option != 0) {
            System.out.println(menu);
            System.out.print("Enter option: ");
            option = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            switch (option) {
                case 0:
                    break;
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listAuthorsAliveInASpecificYear();
                    break;
                case 5:
                    listBooksInASpecificLanguage();
                    break;
                default:
                    System.out.println("Please enter a valid option!!!\n");
                    break;
            }
        }

    }

    private void searchBookByTitle() {
        System.out.println("Enter a title of Book: ");
        String bookName = scanner.nextLine();
        String searchUrl = API_URL.concat("/books/?search=" + bookName.replace(" ", "+").toLowerCase().trim());
        String json = apiConsumption.getData(searchUrl);
        String jsonBook = dataConverter.extractObjectFromJson(json, "results");
        List<BookDTO> booksDTO = dataConverter.getList(jsonBook, BookDTO.class);

        if (!booksDTO.isEmpty()) {
            Book books = new Book(booksDTO.getFirst());

            Author author = authorRepository.findByName(books.getAuthor().getName());
            if (author != null) {
                books.setAuthor(null);
                bookRepository.save(books);
                books.setAuthor(author);
            }
            books = bookRepository.save(books);
            System.out.println(books);
        } else {
            System.out.println("\nBook not found!!!\n");
        }
    }

    private void listRegisteredBooks() {
        List<Book> registeredBooks = bookRepository.findAll();
        registeredBooks.forEach(System.out::println);
    }

    private void listRegisteredAuthors() {
        List<Author> registeredAuthors = authorRepository.findAll();
        registeredAuthors.forEach(System.out::println);
    }

    private void listAuthorsAliveInASpecificYear() {
        try {
            System.out.println("Enter a year: ");
            var year = scanner.nextInt();

            List<Author> authors = authorRepository.listLivingAuthorInAYear(year);
            if (!authors.isEmpty()) {
                authors.forEach(System.out::println);
            } else {
                System.out.println("No author found alive in this year.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid entry. Please enter a number.");
            scanner.nextLine();
        }
    }

    private void listBooksInASpecificLanguage() {
        String languageMenu =
                """
                    **********************************
                    *                                *
                    *       Choose a language:       *
                    *                                *
                    *   en - English                 *
                    *   pt - portuguese              *
                    *   fr - French                  *
                    *   es - Spanish                 *
                    *   ru - Rush                    *
                    *   it - Italian                 *
                    *   de - German                  *
                    *   zh - Chinese                 *
                    *   ja - Japanese                *
                    *                                *
                    **********************************
                    """;

        System.out.println(languageMenu);
        System.out.print("Select a language: ");
        var language = scanner.nextLine();

        List<Book> books = bookRepository.findByLanguage(language);
        if (!books.isEmpty()) {
            books.forEach(System.out::println);
        } else {
            System.out.println("No book found in this language.");
        }
    }

}
