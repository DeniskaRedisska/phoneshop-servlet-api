package com.es.phoneshop.exceptions;

public class OrderNotFoundException extends RuntimeException {
    String msg = "Order was not found";

    public OrderNotFoundException() {
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
