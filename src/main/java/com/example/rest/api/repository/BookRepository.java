package com.example.rest.api.repository;

import com.example.rest.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<Book, String> {


    List<Book> findByAuthorIgnoreCase(String author);

    @Modifying
    @Query(value = "update Book bk set bk.availableCopies = :availableCopies where bk.isbn = :isbn")
    void updateAvailableCopies(@Param("availableCopies") int availableCopies, @Param("isbn") String isbn);

}
