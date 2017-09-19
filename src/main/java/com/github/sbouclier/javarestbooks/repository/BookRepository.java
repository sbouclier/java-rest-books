package com.github.sbouclier.javarestbooks.repository;

import com.github.sbouclier.javarestbooks.domain.Book;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Book repository
 *
 * @author Stéphane Bouclier
 *
 */
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
}
