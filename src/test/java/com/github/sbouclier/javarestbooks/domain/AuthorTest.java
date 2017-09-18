package com.github.sbouclier.javarestbooks.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Author test
 *
 * @author Stéphane Bouclier
 *
 */
public class AuthorTest {

    @Test
    public void should_return_to_string() {

        // Given
        final Author author = new Author("John", "Doe");

        // When
        final String toString = author.toString();

        // Then
        assertThat(toString, is("Author[firstName=John,lastName=Doe]"));
    }
}
