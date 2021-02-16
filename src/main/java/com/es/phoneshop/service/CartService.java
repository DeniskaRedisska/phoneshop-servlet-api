package com.es.phoneshop.service;

import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;

public interface CartService {
    Cart getCart(DataProvider<Cart> provider);

    void add(Cart cart, Long productId, int quantity) throws OutOfStockException;

}
