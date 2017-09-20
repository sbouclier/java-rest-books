package com.github.sbouclier.javarestbooks.controller;

import com.github.sbouclier.javarestbooks.exception.BookIsbnAlreadyExistsException;
import com.github.sbouclier.javarestbooks.exception.BookNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error")
public class BookControllerAdvice {

    @ResponseBody
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors bookNotFoundExceptionHandler(BookNotFoundException ex) {
        return new VndErrors("error", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(BookIsbnAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    VndErrors bookIsbnAlreadyExistsExceptionHandler(BookIsbnAlreadyExistsException ex) {
        return new VndErrors("error", ex.getMessage());
    }
}
