package com.es.phoneshop.utils;

import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.exceptions.VerificationException;
import com.es.phoneshop.model.product.Product;

public class VerifyUtil {
    public static void verifyNotNull(Object o) {
        if (o == null) throw new VerificationException("Null reference!");
    }

    public static void verifyInStock(Product product, int quantity) throws OutOfStockException {
        if (product.getStock() < quantity) throw new OutOfStockException(product.getStock());
    }
}
