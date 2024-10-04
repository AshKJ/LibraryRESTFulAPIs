# A set of RESTful APIs based on Spring Boot for library management system

## Introduction
A simple Spring Boot application providing RESTful APIs to add, remove, find, borrow and return books to the library.

## Endpoint details

The books are identified by ISBN number and`{isbn}` is the entity identifier 

The application provides below endpoints

- `PUT` at `/api/v1/books` which 
  - Saves a new book with the posted book data in the library
  - Return `201 Created` if the book is successfully created

- `GET` at `/api/v1/books/{isbn}` which
  - Returns all books in the library for given `{isbn}`
  - Returns empty list if books for given `{isbn}` doesn't exist
  
- `DELETE` at `/api/v1/books/{isbn}` which should:
  - Delete the book from the library
  - Return `200 Success` if the book is successfully deleted
  - Return `404 Not Found` if the book with given ISBN doesn't exist

- `GET` at `/api/v1/books/author/{author}` which
  - Returns all books in the library for given `{author}` name in case insensitive manner
  - Returns empty list if books for given `{author}` doesn't exist
  
- `POST` at `/api/v1/books/borrow/{isbn}` which 
  - Saves a new book with the posted book data in the library
  - Return `200 Success` if the book is successfully borrowed from the library
  - Return `404 Not Found` if the book with given ISBN doesn't exist

- `POST` at `/api/v1/books/RETURN/{isbn}` which 
  - Saves a new book with the posted book data in the library
  - Return `200 Success` if the book is successfully returned to the library
  - Return `404 Not Found` if the book with given ISBN doesn't exist
  
The API should be protected with JWT token. A set of endpoints, filters need to be added for JWT functionality and spring security should be configured accordingly. after identifying the user, the rate limiting can be applied against the user.

## To run the application
As the project is maven based simply run the following command in the root directory of the project `mvn spring-boot:run` which will start the app on default port 8080

The project can be built running all the tests with the command `mvn clean install`


