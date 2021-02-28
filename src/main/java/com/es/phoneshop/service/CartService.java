package com.es.phoneshop.service;

import com.es.phoneshop.exceptions.InvalidArgumentException;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;

public interface CartService {
    Cart getCart(DataProvider<Cart> provider);

    void add(Cart cart, Long productId, int quantity) throws OutOfStockException, InvalidArgumentException;

    void update(Cart cart,Long productId,int quantity) throws InvalidArgumentException, OutOfStockException;

    void delete(Cart cart,Long productId);
}
