package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.model.Item;
import com.es.phoneshop.exceptions.ItemNotFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public abstract class AbstractGenericArrayListDao<T extends Item> implements GenericDao<T>,Serializable {

    private List<T> items;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private long maxId;

    public long getMaxId() {
        return maxId;
    }

    public AbstractGenericArrayListDao(List<T> list) {
        items = list;
    }

    public void save(T value) {
        verifyNotNull(value);
        writeLock.lock();
        try {
            if (value.getId() != null) {
                updateItems(value);
            } else {
                addItems(value);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void addItems(T value) {
        value.setId(maxId++);
        items.add(value);
    }

    private void updateItems(T value) {
        items.stream()
                .filter(p -> value.getId().equals(p.getId()))
                .findAny()
                .ifPresentOrElse(
                        val -> items.set(items.indexOf(val), value),
                        () -> items.add(value)
               );
    }


    public T get(Long id) throws ItemNotFoundException {
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

    public void delete(Long id) {
        verifyNotNull(id);
        writeLock.lock();
        try {
            items.removeIf(item -> id.equals(item.getId()));
        } finally {
            writeLock.unlock();
        }
    }

    public List<T> getItems() {
        return items;
    }

    public Lock getWriteLock() {
        return writeLock;
    }

    public Lock getReadLock() {
        return readLock;
    }
}
