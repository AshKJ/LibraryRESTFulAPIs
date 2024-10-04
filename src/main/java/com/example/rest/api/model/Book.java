package com.example.rest.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Book {

    @Id
    @Column
    private String isbn;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private int publicationYear;

    @Column
    private int availableCopies;

    //@Version
    //private int version;

    public Book() {
    }

    public Book(String isbn, String title, String author, int publicationYear, int availableCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
    }

    @Override
    public String toString() {
        return "Book [" + "ISBN = " + isbn + ", Author = " + author + ", Title = "
            + title + ", Publication Year = " + publicationYear + ", Available Copies = " + availableCopies + "]";
    }

}
