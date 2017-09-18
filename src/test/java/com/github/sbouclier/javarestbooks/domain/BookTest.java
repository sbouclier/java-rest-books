package com.github.sbouclier.javarestbooks.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

/**
 * Book test
 *
 * @author Stéphane Bouclier
 *
 */
public class BookTest {

    @Test
    public void should_return_to_string() {

        // Given
        final Author author = new Author("John", "Doe");
        final Book book = new Book("isbn", "title", "publisher");
        book.addAuthor(author);

        StringBuilder expectedString = new StringBuilder("Book[id=<null>,isbn=isbn,title=title,description=<null>,");
        expectedString.append("authors=[Author[firstName=John,lastName=Doe]],publisher=publisher]");

        // When
        final String toString = book.toString();

        // Then
        assertThat(toString, is(expectedString.toString()));
    }
}
