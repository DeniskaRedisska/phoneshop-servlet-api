package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortType;
import com.es.phoneshop.exceptions.ItemNotFoundException;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractGenericArrayListDao<Product> implements ProductDao, Serializable {

    private ArrayListProductDao() {
        super(new ArrayList<>());
    }

    protected ArrayListProductDao(List<Product> list) {
        super(list);
    }

    private static class Singleton {
        private static final ArrayListProductDao INSTANCE = new ArrayListProductDao();
    }

    public static ArrayListProductDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public Product get(Long id) {
        try {
            return super.get(id);
        } catch (ItemNotFoundException exc) {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortType sortType) {
        getReadLock().lock();
        try {
            String[] queryWords = (query != null && !query.equals("")) ? query.split("\\s+") : null;
            return getItems().stream()
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> product.getPrice() != null)
                    .map(product -> Map.entry(product, getNumberOfCollisions(queryWords, product)))
                    .filter(entry -> entry.getValue() > 0)
                    .sorted(getSortComparator(sortField, sortType))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } finally {
            getReadLock().unlock();
        }
    }

    private long getNumberOfCollisions(String[] queryWords, Product product) {
        if (queryWords == null) return 1;
        return Arrays.stream(queryWords).filter(word -> product.getDescription().toLowerCase()
                .contains(word.toLowerCase()))
                .count();
    }

    private Comparator<Map.Entry<Product, Long>> getSortComparator(SortField field, SortType type) {
        if (field == null || type == null) return this::relevanceSort;
        return Comparator.comparing(
                keyExtractor(field),
                getOrderTypeComparator(type)
        );
    }

    private Function<Map.Entry<Product, Long>, Comparable> keyExtractor(SortField field) {
        if (field == SortField.price) return product -> product.getKey().getPrice();
        return product -> product.getKey().getDescription();
    }

    private Comparator<Comparable> getOrderTypeComparator(SortType type) {
        if (type == SortType.asc) return Comparator.naturalOrder();
        else return Comparator.reverseOrder();
    }

    private int relevanceSort(Map.Entry<Product, Long> p1, Map.Entry<Product, Long> p2) {
        return Long.compare(p2.getValue(), p1.getValue());
    }

}
