package com.es.phoneshop.dao;

import com.es.phoneshop.exceptions.ItemNotFoundException;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public abstract class GenericDao<T extends DataObject> {

    protected List<T> items;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private long maxId;

    public long getMaxId() {
        return maxId;
    }

    public GenericDao(List<T> list) {
        items = list;
    }

    protected void save(T value) {
        verifyNotNull(value);
        writeLock.lock();
        try {
            if (value.getId() != null) {
                updateProducts(value);
            } else {
                addProduct(value);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void addProduct(T value) {
        value.setId(maxId++);
        items.add(value);
    }

    private void updateProducts(T value) {
        items.stream()
                .filter(p -> value.getId().equals(p.getId()))
                .findAny()
                .ifPresentOrElse(
                        val -> items.set(items.indexOf(val), value),
                        () -> items.add(value)
                );
    }


    protected T get(Long id) throws ItemNotFoundException {
        verifyNotNull(id);
        readLock.lock();
        try {
            return items.stream()
                    .filter(p -> id.equals(p.getId()))
                    .findAny()
                    .orElseThrow(() -> new ItemNotFoundException(id));
        } finally {
            readLock.unlock();
        }
    }
}
