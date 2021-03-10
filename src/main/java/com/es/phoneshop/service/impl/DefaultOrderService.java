package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.es.phoneshop.utils.VerifyUtil.verifyNotNull;

public class DefaultOrderService implements OrderService {

    private OrderDao orderDao = ArrayListOrderDao.getInstance();

    private DefaultOrderService(){

    }

    private static class Singleton {
        private static final DefaultOrderService INSTANCE = new DefaultOrderService();
    }

    public static DefaultOrderService getInstance() {
        return DefaultOrderService.Singleton.INSTANCE;
    }

    @Override
    public Order getOrder(Cart cart) {
        verifyNotNull(cart);
        Order order = new Order();
        order.setItems(cart.getItems().stream()
                .map(CartItem::new).collect(Collectors.toList()));
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setSubTotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getDeliveryCost().add(order.getSubTotal()));
        return order;
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        verifyNotNull(order);
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }
}
