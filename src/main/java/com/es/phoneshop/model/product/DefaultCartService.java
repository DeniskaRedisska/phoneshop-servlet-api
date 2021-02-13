package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exceptions.OutOfStockException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class DefaultCartService implements CartService {

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private final ProductDao productDao;

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
        synchronized (session) {//todo think about it
            Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                session.setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (quantity > product.getStock()) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        cart.getItems().stream()
                .filter(item -> item.getProduct().getCode().equals(product.getCode()))
                .findAny()
                .ifPresentOrElse(
                        item -> item.incrementQuantity(quantity),
                        () -> cart.getItems().add(new CartItem(product, quantity))
                );
    }
}
