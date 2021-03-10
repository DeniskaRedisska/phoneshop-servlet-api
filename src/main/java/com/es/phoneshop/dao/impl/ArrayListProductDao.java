package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SearchMode;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortType;
import com.es.phoneshop.exceptions.ItemNotFoundException;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.io.Serializable;
import java.math.BigDecimal;
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

    @Override
    public List<Product> findProductWithAdvancedSearch(String description, BigDecimal minPrice, BigDecimal maxPrice,
                                                       SearchMode searchMode) {
        getReadLock().lock();
        try {
            String[] queryWords = (description != null && !description.equals("")) ? description.split("\\s+") : null;
            return getItems().stream()
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> product.getPrice() != null)
                    .map(product -> Map.entry(product, getNumberOfCollisions(queryWords, product)))
                    .filter(productLongEntry -> getFilter(productLongEntry, searchMode, queryWords))
                    .filter(productLongEntry -> getPriceFilter(productLongEntry, minPrice, maxPrice))
                    .sorted(getSortComparator(null, null))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } finally {
            getReadLock().unlock();
        }
    }

    private boolean getPriceFilter(Map.Entry<Product, Long> productLongEntry, BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null && maxPrice == null) return true;
        BigDecimal price = productLongEntry.getKey().getPrice();
        return price.compareTo(minPrice) > 0 &&
                price.compareTo(maxPrice) < 0;
    }

    private boolean getFilter(Map.Entry<Product, Long> productLongEntry, SearchMode mode, String[] queryWords) {
        if (mode == SearchMode.ANY_WORD) return productLongEntry.getValue() > 0;
        else return productLongEntry.getValue() == queryWords.length;
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
