package com.es.phoneshop.dao;

import com.es.phoneshop.enums.SearchMode;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortType;
import com.es.phoneshop.exceptions.ItemNotFoundException;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao extends GenericDao<Product> {
    List<Product> findProducts(String query, SortField sortField, SortType sortType);

    List<Product> findProductWithAdvancedSearch(String description, BigDecimal maxPrice, BigDecimal minPrice, SearchMode searchMode);
}
