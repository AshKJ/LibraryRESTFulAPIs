package com.example.rest.api.service;

import com.example.rest.api.model.Book;
import com.example.rest.api.repository.BookRepository;
import com.example.rest.api.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
class LibraryTest {

    @Autowired
    private Library library;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void createdBookIsReturned() {

        Book book = TestDataUtil.createBook("100000000001", "Philosopher's Stone", "JK Rowling", 1997, 1000000);
        library.addBook(book);

        for(int i = 0 ; i < 2; i++) {

            Book queriedBook = library.findBookByISBN(book.getIsbn());

            assertNotNull("Book shouldn't be null", queriedBook);
            assertEquals("100000000001", queriedBook.getIsbn()) ;
            assertEquals("Philosopher's Stone", queriedBook.getTitle());
            assertEquals("JK Rowling", queriedBook.getAuthor());
            assertEquals(1997, queriedBook.getPublicationYear());
            assertEquals(1000000, queriedBook.getAvailableCopies());
        }
    }

    @Test
    void deletedBookShouldNotBeAvailable() {
        var isbn = "100000000001";
        Book book = TestDataUtil.createBook(isbn, "Secret", "David Walliams", 2020, 10);

        // the book is added to the library and then removed
        library.addBook(book);
        library.removeBook(book.getIsbn());

        // when the book is queried it is not found
        Throwable exception = assertThrows(NoSuchElementException.class, () -> library.findBookByISBN(isbn));
        assertEquals("No value present", exception.getMessage());

    }

    @Test
    void allBooksByAuthorShouldBeReturned() {
        //when two books by an author is added to the library and then queried, both books should be returned
        Book bookOne = TestDataUtil.createBook("90000001", "The Lord of the Rings One", "JRR Tolkien", 1995, 1000000);
        library.addBook(bookOne);

        Book bookTwo = TestDataUtil.createBook("90000002", "The Lord of the Rings Two", "JRR Tolkien", 1996, 2000000);
        library.addBook(bookTwo);

        var books = library.findBooksByAuthor("JRR Tolkien");
        assertEquals(2, books.size());
    }

    @Test
    void borrowingBookShouldReduceTheCount() {
        Book book = TestDataUtil.createBook("90000003", "The Tempest", "New Author", 1997, 1000);
        library.addBook(book);

        library.borrowBook(book.getIsbn());

        var foundBook = library.findBookByISBN(book.getIsbn());
        assertEquals(999, foundBook.getAvailableCopies());
    }

    @Test
    void bookIsBorrowedAndReturned() {
        Book book = TestDataUtil.createBook("90000004", "School", "Mr X", 1998, 1000);
        library.addBook(book);
        library.borrowBook(book.getIsbn());
        library.returnBook(book.getIsbn());

        var foundBook = library.findBookByISBN(book.getIsbn());
        assertEquals(1000, foundBook.getAvailableCopies());
    }
}
