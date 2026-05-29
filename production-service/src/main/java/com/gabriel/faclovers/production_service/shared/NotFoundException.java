package com.gabriel.faclovers.production_service.shared;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
