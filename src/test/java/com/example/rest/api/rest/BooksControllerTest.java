package com.example.rest.api.rest;

import com.example.rest.api.model.Book;
import com.example.rest.api.service.Library;
import com.example.rest.api.util.TestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BooksControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected Library library;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        Mockito.reset(library);
    }

    @Test
    void shouldAddBook() throws Exception {
        doNothing().when(library).addBook(any());

        // when book data is posted it should be successfully created
        mockMvc.perform(post("/api/v1/books")
                .content(TestDataUtil.getDefaultBookContent())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    void shouldDeleteBook() throws Exception {

        doNothing().when(library).removeBook(any());

        // when a request is made to delete a book, the request should be successfully processed
        mockMvc.perform(delete("/api/v1/books/ISBN001")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void shouldGetBookByISBN() throws Exception {

        Book book = TestDataUtil.getDefaultBook();
        when(library.findBookByISBN("ISBN001")).thenReturn(book);

        MvcResult result = mockMvc.perform(get("/api/v1/books/ISBN001")).andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        Book respBook = objectMapper.readValue(json, Book.class);

        assertNotNull(respBook);
        assertEquals("ISBN001", respBook.getIsbn());
    }

    @Test
    void shouldGetBookByAuthor() throws Exception {

        Book book = TestDataUtil.getDefaultBook();
        when(library.findBooksByAuthor("JK Rowling")).thenReturn(List.of(book));


        mockMvc.perform(get("/api/v1/books/author/JK Rowling").accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].isbn", is("ISBN001")));
    }


    @Test
    void shouldBeAbleToBorrowAndReturnBook() throws Exception {

        String bookContent = TestDataUtil.getDefaultBookContent();

        Book book = TestDataUtil.getDefaultBook();
        when(library.borrowBook(any())).thenReturn(book);

        // then both borrow and return should be successful
        mockMvc.perform(put("/api/v1/books/borrow/ISBN001")
                .content(bookContent)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(put("/api/v1/books/return/ISBN001")
                .content(bookContent)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
            .andExpect(status().isOk());
    }


}
