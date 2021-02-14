package com.es.phoneshop.exceptions;

public class OutOfStockException extends Exception {
    private int quantityAvailable;

    public OutOfStockException(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }
}
