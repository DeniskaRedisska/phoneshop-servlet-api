package com.es.phoneshop.web;

import com.es.phoneshop.factory.DataProviderFactory;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DeleteItemServlet extends HttpServlet {

    private CartService cartService;

    private final String SUCCESS_MSG = "Products were successfully updated";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.cartService = DefaultCartService.getInstance();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long id = parseProductId(request);
        Cart cart = cartService.getCart(DataProviderFactory.getDataProvider(session));
        cartService.delete(cart, id);
        response.sendRedirect(request.getContextPath() + "/cart?message=" + SUCCESS_MSG);
    }

    private long parseProductId(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(1));
    }
}
