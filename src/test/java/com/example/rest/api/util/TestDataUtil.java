package com.example.rest.api.util;

import com.example.rest.api.model.Book;

public class TestDataUtil {

    public static String getDefaultBookContent() {
        return "{\"isbn\":\"ISBN001\", \"author\":\"JK Rowling\", \"title\":\"Harry Potter and the Philosopher’s Stone\", \"publicationYear\":2020, \"availableCopies\":1000000}";
    }

    public static Book createBook(String isbn, String title, String author, int publicationYear, int availableCopies){
        return new Book(isbn, title, author, publicationYear, availableCopies);
    }

    public static Book getDefaultBook(){
        return createBook("ISBN001", "Harry Potter and the Philosopher’s Stone", "JK Rowling", 2020, 1000000);
    }
}
