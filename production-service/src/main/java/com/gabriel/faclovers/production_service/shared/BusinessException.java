package com.gabriel.faclovers.production_service.shared;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
