package com.es.phoneshop.model.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<CartItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public String toString(){
        return items.toString();
    }
}
