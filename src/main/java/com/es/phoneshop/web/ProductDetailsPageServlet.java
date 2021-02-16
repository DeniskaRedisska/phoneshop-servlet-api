package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exceptions.InvalidArgumentException;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.factory.DataProviderFactory;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.PropertyService;
import com.es.phoneshop.service.RecentProductsService;
import com.es.phoneshop.service.impl.AppPropertyService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultRecentProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Deque;

import static com.es.phoneshop.service.impl.AppPropertyService.RECENT_COUNT;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductDao productDao;

    private CartService cartService;

    private RecentProductsService recentProductsService;

    private PropertyService propertyService;

    private final int skipCount = 1;

    private final String SUCCESS_MSG = "Product added to cart";

    private final String OUT_OF_STOCK_MSG = "Product is out of stock, available: ";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.productDao = ArrayListProductDao.getInstance();
        this.cartService = DefaultCartService.getInstance();
        this.recentProductsService = DefaultRecentProductsService.getInstance();
        this.propertyService = AppPropertyService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long id = parseProductId(request);
        request.setAttribute("product", productDao.getProduct(id));
        request.setAttribute("cart", cartService.getCart(DataProviderFactory.getDataProvider(session)));
        setRecentProducts(request, session, id);
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
    }

    private void setRecentProducts(HttpServletRequest request, HttpSession session, Long id) {
        Deque<Long> recentProductIds = recentProductsService.getRecentProductIds(DataProviderFactory.getDataProvider(session));
        recentProductsService.addToRecent(id, recentProductIds);
        String count = propertyService.getProperties().getProperty(RECENT_COUNT);
        request.setAttribute("recentProducts",
                recentProductsService.getRecentProducts(Integer.parseInt(count), skipCount, recentProductIds));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long productId = parseProductId(request);
        String quantityString = request.getParameter("quantity");
        int quantity;
        try {
            quantity = getQuantity(quantityString, request);
        } catch (ParseException e) {
            sendErrorMessage(request, response, "Not a number");
            return;
        }
        Cart cart = cartService.getCart(DataProviderFactory.getDataProvider(session));
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            sendErrorMessage(request, response, OUT_OF_STOCK_MSG + e.getQuantityAvailable());
            return;
        } catch (InvalidArgumentException e) {
            sendErrorMessage(request, response, e.getMessage());
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=" + SUCCESS_MSG);
    }


    private long parseProductId(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(1));
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        return numberFormat.parse(quantityString).intValue();
    }

    private void sendErrorMessage(HttpServletRequest request, HttpServletResponse response, String msg) throws ServletException, IOException {
        request.setAttribute("error", msg);
        doGet(request, response);
    }

}
