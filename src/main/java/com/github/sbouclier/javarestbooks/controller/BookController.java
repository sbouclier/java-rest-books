package com.github.sbouclier.javarestbooks.controller;

import com.github.sbouclier.javarestbooks.domain.Book;
import com.github.sbouclier.javarestbooks.exception.BookIsbnAlreadyExistsException;
import com.github.sbouclier.javarestbooks.exception.BookNotFoundException;
import com.github.sbouclier.javarestbooks.repository.BookRepository;
import io.swagger.annotations.*;
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
 */
@RestController
@RequestMapping(value = "/api/books")
@Api(value = "Operations to interact with books collection")
public class BookController {

    private static final int MAX_PAGE_SIZE = 50;

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @ApiOperation(value = "Create a book", notes = "${bookController.createBook.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book successfully created",
                    responseHeaders = @ResponseHeader(
                            name = "Location",
                            description = "The resulting URI of the book creation",
                            response = String.class)),
            @ApiResponse(code = 409, message = "Book with same ISBN already exists")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> createBook(
            @ApiParam(name = "book", value = "Book to create", required = true) @Valid @RequestBody Book book,
            UriComponentsBuilder ucBuilder) {
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new BookIsbnAlreadyExistsException(book.getIsbn());
        }
        bookRepository.save(book);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/books/{isbn}").buildAndExpand(book.getIsbn()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Retrieve a book", notes = "${bookController.getBook.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book successfully retrieved"),
            @ApiResponse(code = 404, message = "Book does not exist")
    })
    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBook(@PathVariable("isbn") String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @ApiOperation(value = "Retrieve all paginated books", notes = "${bookController.getAllBooks.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All books are retrieved",
                    responseHeaders = @ResponseHeader(
                            name = "X-Total-Count",
                            description = "Total books number",
                            response = Long.class)),
            @ApiResponse(code = 204, message = "There is no book"),
            @ApiResponse(code = 206, message = "Books are retrieved and there are others",
                    responseHeaders = {
                            @ResponseHeader(
                                    name = "X-Total-Count",
                                    description = "Total books number",
                                    response = Long.class),
                            @ResponseHeader(
                                    name = "first",
                                    description = "First page URI",
                                    response = String.class),
                            @ResponseHeader(
                                    name = "last",
                                    description = "Last page URI",
                                    response = String.class),
                            @ResponseHeader(
                                    name = "next",
                                    description = "Next page URI",
                                    response = String.class),
                            @ResponseHeader(
                                    name = "prev",
                                    description = "Previous page URI",
                                    response = String.class)})
    })
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @PageableDefault(size = MAX_PAGE_SIZE) Pageable pageable,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order) {
        final PageRequest pr = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc".equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort)
        );

        Page<Book> booksPage = bookRepository.findAll(pr);

        if (booksPage.getContent().isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            long totalBooks = booksPage.getTotalElements();
            int nbPageBooks = booksPage.getNumberOfElements();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(totalBooks));

            if (nbPageBooks < totalBooks) {
                headers.add("first", buildPageUri(PageRequest.of(0, booksPage.getSize())));
                headers.add("last", buildPageUri(PageRequest.of(booksPage.getTotalPages() - 1, booksPage.getSize())));

                if (booksPage.hasNext()) {
                    headers.add("next", buildPageUri(booksPage.nextPageable()));
                }

                if (booksPage.hasPrevious()) {
                    headers.add("prev", buildPageUri(booksPage.previousPageable()));
                }

                return new ResponseEntity<>(booksPage.getContent(), headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity(booksPage.getContent(), headers, HttpStatus.OK);
            }
        }
    }

    @ApiOperation(value = "Update a book", notes = "${bookController.updateBook.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book successfully updated"),
            @ApiResponse(code = 404, message = "Book does not exist")
    })
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

    @ApiOperation(value = "Update a book's description", notes = "${bookController.updateBookDescription.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book's description successfully updated"),
            @ApiResponse(code = 404, message = "Book does not exist")
    })
    @PatchMapping("/{isbn}")
    public ResponseEntity<Book> updateBookDescription(@PathVariable("isbn") String isbn, @RequestBody String description) {
        return bookRepository.findByIsbn(isbn)
                .map(book -> {
                    book.setDescription(description);
                    bookRepository.save(book);

                    return new ResponseEntity<>(book, HttpStatus.OK);
                })
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @ApiOperation(value = "Delete a book", notes = "${bookController.deleteBook.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Book successfully deleted"),
            @ApiResponse(code = 404, message = "Book does not exist")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{isbn}")
    public ResponseEntity<?> deleteBook(@PathVariable("isbn") String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(book -> {
                    bookRepository.delete(book);
                    return new ResponseEntity(HttpStatus.NO_CONTENT);
                })
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    private String buildPageUri(Pageable page) {
        return fromUriString("/api/books")
                .query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize())
                .toUriString();
    }

}
