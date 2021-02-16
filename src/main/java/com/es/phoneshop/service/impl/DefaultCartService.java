package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.DataProvider;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.es.phoneshop.utils.VerifyUtil.verifyInStock;
import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public class DefaultCartService implements CartService {

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private final ProductDao productDao;

    private final ReadWriteLock rwl = new ReentrantReadWriteLock();

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class Singleton {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public Cart getCart(DataProvider<Cart> dataProvider) {
        verifyNotNull(dataProvider);
        return dataProvider.getAttribute(CART_SESSION_ATTRIBUTE, new Cart());
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        rwl.writeLock().lock();
        verifyNotNull(cart);
        verifyNotNull(productId);
        Product product = productDao.getProduct(productId);
        verifyInStock(product, quantity);
        cart.getItems().stream()
                .filter(item -> item.getProduct().getCode().equals(product.getCode()))
                .findAny()
                .ifPresentOrElse(
                        item -> item.incrementQuantity(quantity),
                        () -> cart.getItems().add(new CartItem(product, quantity))
                );
        rwl.writeLock().unlock();
    }

}
