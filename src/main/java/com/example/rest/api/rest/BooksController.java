package com.example.rest.api.rest;

import com.example.rest.api.model.Book;
import com.example.rest.api.service.Library;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Library Service" , description = "API to work against library books")
public class BooksController {

    private final Library library;

    public BooksController(Library library) {
        this.library = library;
    }

    @Operation(summary = "Adds the book to the library")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBook(final Book book) {
        library.addBook(book);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Removes the book from the library")
    @DeleteMapping (value = "/{isbn}")
    public ResponseEntity<?> removeBook(final @PathVariable String isbn) {
        try {
            library.removeBook(isbn);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Finds a book against given ISBN number")
    @GetMapping(value = "/{isbn}")
    public ResponseEntity<Book> findBookByISBN(@PathVariable String isbn) {
        try {
            return new ResponseEntity<>(library.findBookByISBN(isbn), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Finds all books for given author")
    @GetMapping(value = "/author/{author}")
    public ResponseEntity<List<Book>> findBooksByAuthor(@PathVariable String author) {
        List<Book> books = library.findBooksByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(summary = "Reserves a book for given ISBN number")
    @PutMapping(value = "/borrow/{isbn}")
    public ResponseEntity<Book> borrowBook(@PathVariable String isbn) {
        try {
            return new ResponseEntity<>(library.borrowBook(isbn), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Returns a book for given ISBN number to the library")
    @PutMapping(value = "/return/{isbn}")
    public ResponseEntity<Book> returnBook(@PathVariable String isbn) {
        try {
            return new ResponseEntity<>(library.returnBook(isbn), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
