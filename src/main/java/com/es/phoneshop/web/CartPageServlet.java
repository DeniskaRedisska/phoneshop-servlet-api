package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
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
import java.util.*;
import java.util.List;

public class CartPageServlet extends HttpServlet {
    private ProductDao productDao;

    private CartService cartService;

    private final String SUCCESS_MSG = "Products were successfully updated";


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.productDao = ArrayListProductDao.getInstance();
        this.cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setAttribute("cart", cartService.getCart(DataProviderFactory.getDataProvider(session)));
        request.getRequestDispatcher("/WEB-INF/pages/cartPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String[] quantities = request.getParameterValues("quantity");
        String[] productIds = request.getParameterValues("productId");

        if (quantities.length != productIds.length) {
            throw new InvalidArgumentException("Different array sizes");
        }

        List<String> quantityList = Arrays.asList(quantities);
        List<String> productIdList = Arrays.asList(productIds);

        Iterator<String> quantityIterator = quantityList.iterator();
        Iterator<String> productIterator = productIdList.iterator();

        Cart cart = cartService.getCart(DataProviderFactory.getDataProvider(session));
        Map<Long, String> errors = new HashMap<>();

        while (quantityIterator.hasNext() && productIterator.hasNext()) {
            updateCart(quantityIterator, productIterator, cart, request, errors);
        }

        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart" + "?message=" + SUCCESS_MSG);
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private void updateCart(Iterator<String> quantityIterator, Iterator<String> productIterator,
                            Cart cart, HttpServletRequest request, Map<Long, String> errors) {
        Long id = Long.valueOf(productIterator.next());
        try {
            int quantity = getQuantity(quantityIterator.next(), request);
            cartService.update(cart, id, quantity);
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
