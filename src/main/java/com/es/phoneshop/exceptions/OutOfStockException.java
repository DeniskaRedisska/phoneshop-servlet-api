package com.es.phoneshop.exceptions;

public class OutOfStockException extends Exception {
    private final int quantityAvailable;

    private final String OUT_OF_STOCK_MSG = "Product is out of stock, available: ";

    public OutOfStockException(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    @Override
    public String getMessage() {
        return OUT_OF_STOCK_MSG + quantityAvailable;
    }
}
