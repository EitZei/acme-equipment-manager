package com.acme.repository;

public class NonUniqueItemException extends IllegalStateException {
    private static final long serialVersionUID = 3125212207566556382L;

    public NonUniqueItemException(String message) {
        super(message);
    }
}
