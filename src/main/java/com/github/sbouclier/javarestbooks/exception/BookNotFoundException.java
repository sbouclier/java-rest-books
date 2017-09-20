package com.github.sbouclier.javarestbooks.exception;

/**
 * BookNotFound exception
 *
 * @author Stéphane Bouclier
 *
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String isbn) {
        super("could not find book with ISBN: '" + isbn + "'");
    }
}
