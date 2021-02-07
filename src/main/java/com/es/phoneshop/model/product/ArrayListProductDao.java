package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exceptions.ProductNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.es.phoneshop.model.product.VerifyUtil.verifyNotNull;

public class ArrayListProductDao implements ProductDao {

    private final List<Product> products;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private long maxId;

    private ArrayListProductDao() {
        products = new ArrayList<>();
    }

    protected ArrayListProductDao(List<Product> list) {
        products = list;
    }

    private static class Singleton {
        private static final ArrayListProductDao INSTANCE = new ArrayListProductDao();
    }

    public static ArrayListProductDao getInstance() {
        return Singleton.INSTANCE;
    }

    public long getMaxId() {
        return maxId;
    }

    @Override
    public Product getProduct(Long id) {
        verifyNotNull(id);
        readLock.lock();
        try {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(()->new ProductNotFoundException(id));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortType sortType) {
        readLock.lock();
        try {
            return products.stream()
                    .filter(product ->
                                    query == null ||
                                    query.equals("") ||
                                    search(query, product))
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> product.getPrice() != null)
                    .sorted((p1, p2) -> relevanceSort(query, p1, p2))
                    .sorted(getSortComparator(sortField, sortType))
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    private boolean search(String query, Product product) {
        return Arrays.stream(query.split("\\s+"))
                .anyMatch(word -> product.getDescription().toLowerCase()
                        .contains(word.toLowerCase()));
    }


    private int relevanceSort(String query, Product p1, Product p2) {
        if (query == null || query.equals("")) return 0;
        return Long.compare(getNumberOfCollisions(query, p2), getNumberOfCollisions(query, p1));
    }

    private long getNumberOfCollisions(String query, Product product) {
        return Arrays.stream(query.split("\\s+"))
                .filter(word -> product.getDescription().toLowerCase()
                        .contains(word.toLowerCase()))
                .count();
    }

    private Comparator<Product> getSortComparator(SortField field, SortType type) {
        if (field == null) return (o1, o2) -> 0;
        return Comparator.comparing(
                product -> {
                    if (field == SortField.price) return product.getPrice();
                    else return product.getDescription();
                },
                getOrderTypeComparator(type)
        );
    }

    private Comparator<Comparable> getOrderTypeComparator(SortType type) {
        if (type == null) return (o1, o2) -> 0;
        if (type == SortType.asc) return Comparator.naturalOrder();
        else return Comparator.reverseOrder();
    }


    @Override
    public void save(Product product) {
        verifyNotNull(product);
        writeLock.lock();
        try {
            if (product.getId() != null) {
                updateProducts(product);
            } else {
                addProduct(product);
            }
        } finally {
            writeLock.unlock();
        }
    }


    private void addProduct(Product product) {
        product.setId(maxId++);
        products.add(product);
    }

    private void updateProducts(Product product) {
        products.stream()
                .filter(p -> product.getId().equals(p.getId()))
                .findAny()
                .ifPresentOrElse(
                        val -> products.set(products.indexOf(val), product),
                        () -> products.add(product)
                );
    }

    @Override
    public void delete(Long id) {
        verifyNotNull(id);
        writeLock.lock();
        try {
            products.removeIf(product -> id.equals(product.getId()));
        } finally {
            writeLock.unlock();
        }
    }
}
