package com.projects.ecommerce.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {

        super("Something went wrong");

    }

    public UnauthorizedException(String obj) {

        super(obj + " does not belong to user");

    }

}
