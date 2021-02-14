package com.es.phoneshop.cart;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.es.phoneshop.utils.VerifyUtil.verifyInStock;

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
    public Cart getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        synchronized (session) {
            Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                session.setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        rwl.writeLock().lock();
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
