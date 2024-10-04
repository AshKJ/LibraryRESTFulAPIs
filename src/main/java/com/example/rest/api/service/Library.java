package com.example.rest.api.service;

import com.example.rest.api.model.Book;
import com.example.rest.api.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
@Transactional
public class Library {

    @Value("${max.cache.entries:1000000}")
    private int maxCacheEntries;

    private final BookRepository bookRepository;

    private final Object lock = new Object();

    public Library(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    Map<String, Book> bookCache = new LinkedHashMap<>(maxCacheEntries, .75F, true) {
        @Override
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > maxCacheEntries;
        }
    };

    @PostConstruct
    public void init() {
        bookCache = Collections.synchronizedMap(bookCache);
    }

    public void addBook(final Book book) {
        bookRepository.save(book);
        bookCache.put(book.getIsbn(), book);
    }

    public void removeBook(String isbn) {
        bookRepository.delete(findBookByISBN(isbn));
        bookCache.remove(isbn);
    }

    public Book findBookByISBN(String isbn) {
        if (bookCache.containsKey(isbn)) {
            return bookCache.get(isbn);
        }
        Book book = bookRepository.findById(isbn).orElseThrow();
        bookCache.put(isbn, book);
        return book;
    }

    public List<Book> findBooksByAuthor(String author) {
        List<Book> books = bookCache.values().stream().filter(book -> book.getAuthor().equals(author)).toList();
        if (!books.isEmpty()) {
            return books;
        }
        books = bookRepository.findByAuthorIgnoreCase(author);
        books.forEach(book -> bookCache.put(book.getIsbn(), book));
        return books;
    }

    public Book borrowBook(String isbn) {
        return handleBook(isbn, book -> book.setAvailableCopies(book.getAvailableCopies() - 1));
    }

    public Book returnBook(String isbn) {
        return handleBook(isbn, book -> book.setAvailableCopies(book.getAvailableCopies() + 1));
    }

    private Book handleBook(String isbn, Consumer<Book> bookConsumer) {
        Book book = findBookByISBN(isbn);
        synchronized (lock) {
            bookConsumer.accept(book);
            bookRepository.saveAndFlush(book);
            bookCache.put(isbn, book);
        }
        return book;
    }
}
