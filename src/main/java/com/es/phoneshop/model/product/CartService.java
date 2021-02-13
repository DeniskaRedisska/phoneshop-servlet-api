package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exceptions.OutOfStockException;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);

    void add(Cart cart, Long productId, int quantity) throws OutOfStockException;
}
