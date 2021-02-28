package com.es.phoneshop.web.cart;

import com.es.phoneshop.exceptions.InvalidArgumentException;
import com.es.phoneshop.exceptions.OutOfStockException;
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class AddToCartServlet extends HttpServlet {

    private CartService cartService;

    private final String SUCCESS_MSG = "Product added to cart";

    private final String ERROR_MSG = "Error occurred adding product to cart";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long id = Long.valueOf(request.getParameter("productId"));
        String quantityString = request.getParameter("quantity");
        String backPath = request.getParameter("backPath");
        Map<Long, String> errors = new HashMap<>();
        addProduct(request, session, id, quantityString, errors);
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/" + backPath + "?message=" + SUCCESS_MSG);
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("errorMsg", ERROR_MSG);
            request.getRequestDispatcher("/" + backPath).forward(request, response);
        }
    }

    private void addProduct(HttpServletRequest request, HttpSession session, Long id,
                            String quantityString, Map<Long, String> errors) {
        try {
            int quantity = getQuantity(quantityString, request);
            Cart cart = cartService.getCart(DataProviderFactory.getDataProvider(session));
            cartService.add(cart, id, quantity);
        } catch (InvalidArgumentException | OutOfStockException e) {
            errors.put(id, e.getMessage());
        } catch (ParseException e) {
            errors.put(id, "Not a number");
        }
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        return numberFormat.parse(quantityString).intValue();
    }
}
