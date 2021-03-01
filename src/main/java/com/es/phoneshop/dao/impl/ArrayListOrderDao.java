package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exceptions.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;

import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public class ArrayListOrderDao extends AbstractGenericArrayListDao<Order> implements OrderDao {

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
        getReadLock().lock();
        try {
            return getItems().stream()
                    .filter(p -> id.equals(p.getSecureId()))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            getReadLock().unlock();
        }
    }

}
