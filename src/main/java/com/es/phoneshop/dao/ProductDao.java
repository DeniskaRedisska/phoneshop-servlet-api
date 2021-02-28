package com.es.phoneshop.dao;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortType;
import com.es.phoneshop.exceptions.ItemNotFoundException;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;

    List<Product> findProducts(String query, SortField sortField, SortType sortType);

    void saveProduct(Product product);

    void deleteProduct(Long id);
}
