package com.es.phoneshop.model.product.exceptions;

import com.es.phoneshop.model.product.Product;

public class OutOfStockException extends Exception {
    private Product product;
    private int quantityWanted;
    private int quantityAvailable;


    public OutOfStockException(Product product, int quantityWanted, int quantityAvailable) {
        this.product = product;
        this.quantityWanted = quantityWanted;
        this.quantityAvailable = quantityAvailable;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantityWanted() {
        return quantityWanted;
    }
}
