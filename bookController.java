package com.example.library.controller;


import java.util.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.model.Book;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class bookController {


    List<Book> books = new ArrayList<>();

 
    @PostMapping
    public String addBook(@Valid @RequestBody Book book) {
    	books.add(book);
    	return "Book added successfully";
    }


    @GetMapping
    public List<Book> getAllBooks() {
           return books;
    }


    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) {

        for (Book book : books) {
             if (book.getBookId() == id) {
                return book;
            }
        }

        return null;
    }

  
    @PutMapping("/{id}")
    public String updateAvailability(
            @PathVariable int id,
            @RequestParam boolean available) {

        for (Book book : books) {
        	if (book.getBookId() == id) {
            book.setAvailable(available);
              return "Book availability updated successfully";
            }
        }

        return "Book not found";
    }

   
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id) {

        boolean removed = books.removeIf(
                book ->book.getBookId() == id);

        if (removed) {
            return "Book deleted successfully";
        }

        return "Book not found";
    }
}
