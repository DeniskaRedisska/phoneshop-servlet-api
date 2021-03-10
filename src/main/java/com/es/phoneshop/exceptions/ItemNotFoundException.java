package com.es.phoneshop.exceptions;

public class ItemNotFoundException extends RuntimeException {
    private final Long id;

    public ItemNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
