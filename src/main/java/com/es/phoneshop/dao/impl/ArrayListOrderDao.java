package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exceptions.ItemNotFoundException;
import com.es.phoneshop.exceptions.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public class ArrayListOrderDao extends GenericDao<Order> implements OrderDao, Serializable {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private ArrayListOrderDao() {
        super(new ArrayList<>());
    }

    protected ArrayListOrderDao(List<Order> list) {
        super(list);
    }

    private static class Singleton {
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }

    public static ArrayListOrderDao getInstance() {
        return Singleton.INSTANCE;
    }


    @Override
    public Order getOrder(Long id) throws ItemNotFoundException {
        return get(id);
    }

    @Override
    public Order getOrderBySecureId(String id) {
        verifyNotNull(id);
        readLock.lock();
        try {
            return items.stream()
                    .filter(p -> id.equals(p.getSecureId()))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            readLock.unlock();
        }
    }


    @Override
    public void saveOrder(Order order) {
        save(order);
    }

}
