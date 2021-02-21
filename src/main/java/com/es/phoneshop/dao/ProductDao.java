package com.es.phoneshop.dao;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortType;
import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);

    List<Product> findProducts(String query, SortField sortField, SortType sortType);

    void save(Product product);

    void delete(Long id);
}
