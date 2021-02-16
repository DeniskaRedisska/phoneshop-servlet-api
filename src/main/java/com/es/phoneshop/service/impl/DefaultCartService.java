package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exceptions.InvalidArgumentException;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.DataProvider;

import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public class DefaultCartService implements CartService {

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private ProductDao productDao;

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
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException, InvalidArgumentException {
        verifyNotNull(cart);
        verifyNotNull(productId);
        verifyAboveZero(quantity);
        rwl.writeLock().lock();
        try {
            Product product = productDao.getProduct(productId);
            Optional<CartItem> optional = cart.getItems().stream()
                    .filter(item -> item.getProduct().getCode().equals(product.getCode()))
                    .findAny();
            if (optional.isPresent()) {
                incrementQuantity(product, optional.get(), quantity);
            } else {
                cart.getItems().add(new CartItem(product, quantity));
            }
        } finally {
            rwl.writeLock().unlock();
        }
    }

    private void incrementQuantity(Product product, CartItem item, int quantity) throws OutOfStockException {
        verifyIsInStock(product, item, quantity);
        item.incrementQuantity(quantity);
    }

    private void verifyIsInStock(Product product, CartItem item, int quantity) throws OutOfStockException {
        if (product.getStock() < item.getQuantity() + quantity)
            throw new OutOfStockException(product.getStock() - item.getQuantity());
    }

    private void verifyAboveZero(int quantity) throws InvalidArgumentException {
        if (quantity <= 0) throw new InvalidArgumentException("Invalid quantity");
    }

}
