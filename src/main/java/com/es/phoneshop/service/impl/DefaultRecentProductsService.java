package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.DataProvider;
import com.es.phoneshop.service.RecentProductsService;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public class DefaultRecentProductsService implements RecentProductsService {

    private static final String RECENT_SESSION_ATTR = DefaultRecentProductsService.class.getName() + ".recentProducts";

    private final ProductDao dao;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private final Lock readLock = readWriteLock.readLock();
    private final int skipCount = 1;

    private DefaultRecentProductsService() {
        dao = ArrayListProductDao.getInstance();
    }

    private static class Singleton {
        private static final DefaultRecentProductsService INSTANCE = new DefaultRecentProductsService();
    }

    public static DefaultRecentProductsService getInstance() {
        return Singleton.INSTANCE;
    }


    @Override
    public void addToRecent(Long id, Deque<Long> recentProductIds) {
        verifyNotNull(id);
        writeLock.lock();
        try {
            recentProductIds.stream()
                    .filter(id::equals)
                    .findAny()
                    .ifPresentOrElse(
                            val -> addValToTop(val, recentProductIds),
                            () -> recentProductIds.addFirst(id)
                    );
        } finally {
            writeLock.unlock();
        }
    }

    private void addValToTop(Long val, Deque<Long> recentProductIds) {
        recentProductIds.remove(val);
        recentProductIds.addFirst(val);
    }

    @Override
    public List<Product> getRecentProducts(int count, Deque<Long> recentProductIds) {
        readLock.lock();
        try {
            return recentProductIds.stream()
                    .skip(skipCount)
                    .limit(count)
                    .map(dao::getProduct)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Deque<Long> getRecentProductIds(DataProvider<Deque<Long>> provider) {
        verifyNotNull(provider);
        return provider.getAttribute(RECENT_SESSION_ATTR, new ArrayDeque<>());
    }
}
