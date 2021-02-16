package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;

import java.util.Deque;
import java.util.List;

public interface RecentProductsService {

    void addToRecent(Long id, Deque<Long> recentProductIds);

    List<Product> getRecentProducts(int count, Deque<Long> recentProductIds);

    Deque<Long> getRecentProductIds(DataProvider<Deque<Long>> provider);

}
