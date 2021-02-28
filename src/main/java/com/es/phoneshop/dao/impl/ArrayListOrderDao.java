package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.GenericArrayListDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exceptions.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public class ArrayListOrderDao extends GenericArrayListDao<Order> implements OrderDao {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private ArrayListOrderDao() {
        super(new ArrayList<>());
    }

    private static class Singleton {
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }

    public static ArrayListOrderDao getInstance() {
        return Singleton.INSTANCE;
    }


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
        super.save(order);
    }

}
