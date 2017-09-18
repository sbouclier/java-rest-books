package com.github.sbouclier.javarestbooks.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Book entity
 *
 * @author Stéphane Bouclier
 *
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "uk_book_isbn", columnNames = "isbn") })
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String isbn;

    @NotBlank
    private String title;

    private String description;

    @ElementCollection
    @NotEmpty
    private Set<Author> authors;

    @NotBlank
    private String publisher;

    // ----------------
    // - CONSTRUCTORS -
    // ----------------

    private Book() {
        // Default constructor for Jackson
    }

    public Book(String isbn, String title, Set<Author> authors, String publisher) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
    }

    public Book(String isbn, String title, String publisher) {
        this(isbn, title, new HashSet<>(), publisher);
    }

    // -----------
    // - METHODS -
    // -----------

    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    // -------------
    // - TO STRING -
    // -------------

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("isbn", isbn)
                .append("title", title)
                .append("description", description)
                .append("authors", authors)
                .append("publisher", publisher)
                .toString();
    }

    // -------------------
    // - SETTERS/GETTERS -
    // -------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
