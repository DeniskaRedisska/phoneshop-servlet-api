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

import java.math.BigDecimal;
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
        verifyData(cart, productId, quantity);
        rwl.writeLock().lock();
        try {
            Product product = productDao.getProduct(productId);
            Optional<CartItem> optional = getCartItem(cart, product);
            if (optional.isPresent()) {
                setQuantity(product, optional.get(), quantity + optional.get().getQuantity());
            } else {
                cart.getItems().add(new CartItem(product, quantity));
            }
            recalculateCart(cart);
        } finally {
            rwl.writeLock().unlock();
        }
    }


    @Override
    public void update(Cart cart, Long productId, int quantity) throws InvalidArgumentException, OutOfStockException {
        verifyData(cart, productId, quantity);
        rwl.writeLock().lock();
        try {
            Product product = productDao.getProduct(productId);
            Optional<CartItem> optional = getCartItem(cart, product);
            if (optional.isPresent()) {
                setQuantity(product, optional.get(), quantity);
                recalculateCart(cart);
            }
        } finally {
            rwl.writeLock().unlock();
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        verifyNotNull(cart);
        verifyNotNull(productId);
        rwl.writeLock().lock();
        try {
            cart.getItems().removeIf(item -> productId.equals(item.getProduct().getId()));
            recalculateCart(cart);
        } finally {
            rwl.writeLock().unlock();
        }
    }

    private Optional<CartItem> getCartItem(Cart cart, Product product) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findAny();
    }

    private void verifyData(Cart cart, Long productId, int quantity) throws InvalidArgumentException {
        verifyNotNull(cart);
        verifyNotNull(productId);
        if (quantity <= 0) throw new InvalidArgumentException("Invalid quantity");
    }

    private void setQuantity(Product product, CartItem item, int quantity) throws OutOfStockException {
        verifyIsInStock(product, item, quantity);
        item.setQuantity(quantity);
    }

    private void verifyIsInStock(Product product, CartItem item, int quantity) throws OutOfStockException {
        if (product.getStock() < item.getQuantity() + quantity)
            throw new OutOfStockException(product.getStock() - item.getQuantity());
    }

    private void recalculateCart(Cart cart) {
        calculateTotalQuantity(cart);
        calculateTotalPrice(cart);
    }

    private void calculateTotalQuantity(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum());

    }

    private void calculateTotalPrice(Cart cart) {
        cart.setTotalPrice(cart.getItems().stream()
                .map(this::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

    }

    private BigDecimal getPrice(CartItem item) {
        return item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

}
