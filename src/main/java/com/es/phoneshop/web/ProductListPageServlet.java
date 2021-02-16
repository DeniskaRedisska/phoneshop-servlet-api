package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortType;
import com.es.phoneshop.factory.DataProviderFactory;
import com.es.phoneshop.service.PropertyService;
import com.es.phoneshop.service.RecentProductsService;
import com.es.phoneshop.service.impl.AppPropertyService;
import com.es.phoneshop.service.impl.DefaultRecentProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Deque;
import java.util.Optional;

import static com.es.phoneshop.service.impl.AppPropertyService.RECENT_COUNT;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;

    private RecentProductsService recentProductsService;

    private PropertyService propertyService;

    private final int skipCount = 0;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.productDao = ArrayListProductDao.getInstance();
        this.propertyService = AppPropertyService.getInstance();
        this.recentProductsService = DefaultRecentProductsService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String query = request.getParameter("query");
        String order = request.getParameter("order");
        String sort = request.getParameter("sort");
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sort).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(order).map(SortType::valueOf).orElse(null)));
        String count = propertyService.getProperties().getProperty(RECENT_COUNT);
        Deque<Long> recentProductIds = recentProductsService
                .getRecentProductIds(DataProviderFactory.getDataProvider(session));
        request.setAttribute("recentProducts",
                recentProductsService.getRecentProducts(Integer.parseInt(count), skipCount, recentProductIds));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
