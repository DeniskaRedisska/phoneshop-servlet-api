package com.es.phoneshop.web;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.factory.DataProviderFactory;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import com.es.phoneshop.utils.ValidationUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CheckoutPageServlet extends HttpServlet {

    private CartService cartService;

    private OrderService orderService;

    private final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = getCart(request);
        Order order = orderService.getOrder(cart);
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        if (request.getAttribute("order") == null) {
            request.setAttribute("order", order);
        }
        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = getCart(request);
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();

        setRequestParam(request, "firstName", errors,
                order::setFirstName, ValidationUtil.getDefaultParamPredicate());
        setRequestParam(request, "lastName", errors,
                order::setLastName, ValidationUtil.getDefaultParamPredicate());
        setRequestParam(request, "phoneNumber", errors,
                order::setPhoneNumber, ValidationUtil.getPhonePredicate());
        setRequestParam(request, "deliveryAddress", errors,
                order::setDeliveryAddress, ValidationUtil.getDefaultParamPredicate());
        setRequestParam(request, "deliveryDate", errors,
                (s -> order.setDeliveryDate(parseDate(s))), ValidationUtil.getDatePredicate());
        setRequestParam(request, "paymentMethod", errors,
                (s -> order.setPaymentMethod(PaymentMethod.valueOf(s))), ValidationUtil.getPaymentMethodPredicate());


        handleError(request, response, errors, order);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             Map<String, String> errors, Order order)
            throws IOException, ServletException {
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/overview");
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            doGet(request, response);
        }
    }


    private void setRequestParam(HttpServletRequest request, String paramName, Map<String, String> errors,
                                 Consumer<String> consumer, Predicate<String> predicate) {
        String parameter = request.getParameter(paramName);
        if (predicate.test(parameter)) {
            consumer.accept(parameter);
        } else {
            errors.put(paramName, "Invalid input");
        }
    }


    private Cart getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return cartService.getCart(DataProviderFactory.getDataProvider(session));
    }

    //todo mb parse util
    private LocalDate parseDate(String date) {
        return LocalDate.parse(date);
    }

}
