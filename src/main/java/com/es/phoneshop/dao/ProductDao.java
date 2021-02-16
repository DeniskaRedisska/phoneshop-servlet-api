package com.es.phoneshop.dao;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortType;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Deque;
import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);

    List<Product> findProducts(String query, SortField sortField, SortType sortType);

    void save(Product product);

    void delete(Long id);

    void addToRecent(Long id, Deque<Long> recent);

    List<Product> getRecentProducts(int count, HttpServletRequest request);

    Deque<Long> getRecentProductIds(HttpServletRequest request);
}
