package com.github.sbouclier.javarestbooks.controller;

import com.github.sbouclier.javarestbooks.domain.Book;
import com.github.sbouclier.javarestbooks.exception.BookIsbnAlreadyExistsException;
import com.github.sbouclier.javarestbooks.exception.BookNotFoundException;
import com.github.sbouclier.javarestbooks.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

/**
 * Book controller
 *
 * @author Stéphane Bouclier
 *
 */
@RestController
@RequestMapping(value = "/api/books")
public class BookController {

    private static final int MAX_PAGE_SIZE = 50;

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book, UriComponentsBuilder ucBuilder) {
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new BookIsbnAlreadyExistsException(book.getIsbn());
        }
        bookRepository.save(book);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/books/{isbn}").buildAndExpand(book.getIsbn()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBook(@PathVariable("isbn") String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<Book> updateBook(@PathVariable("isbn") String isbn, @Valid @RequestBody Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(bookToUpdate -> {
                    bookToUpdate.setIsbn(book.getIsbn());
                    bookToUpdate.setTitle(book.getTitle());
                    bookToUpdate.setDescription(book.getDescription());
                    bookToUpdate.setAuthors(book.getAuthors());
                    bookToUpdate.setPublisher(book.getPublisher());
                    bookRepository.save(bookToUpdate);

                    return new ResponseEntity<>(bookToUpdate, HttpStatus.OK);
                })
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

}
