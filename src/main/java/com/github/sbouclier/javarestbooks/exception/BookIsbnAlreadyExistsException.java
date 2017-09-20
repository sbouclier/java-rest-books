package com.github.sbouclier.javarestbooks.exception;

/**
 * BookIsbnAlreadyExists exception
 *
 * @author Stéphane Bouclier
 *
 */
public class BookIsbnAlreadyExistsException extends RuntimeException {

    public BookIsbnAlreadyExistsException(String isbn) {
        super("book already exists for ISBN: '" + isbn + "'");
    }
}
