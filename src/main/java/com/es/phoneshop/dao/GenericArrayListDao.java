package com.es.phoneshop.dao;

import com.es.phoneshop.exceptions.ItemNotFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public abstract class GenericArrayListDao<T extends ObjectWithUniqueId> implements Serializable {

    protected List<T> items;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private long maxId;

    public long getMaxId() {
        return maxId;
    }

    public GenericArrayListDao(List<T> list) {
        items = list;
    }

    protected void save(T value) {
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

    public void delete(Long id) {
        verifyNotNull(id);
        writeLock.lock();
        try {
            items.removeIf(product -> id.equals(product.getId()));
        } finally {
            writeLock.unlock();
        }
    }

}
