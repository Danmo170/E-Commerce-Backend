package com.projects.ecommerce.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {

        super("Something went wrong");

    }

    public BadRequestException(String message) {

        super(message);

    }

}
