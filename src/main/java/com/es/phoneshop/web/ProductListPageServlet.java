package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.SortField;
import com.es.phoneshop.dao.SortType;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String order = request.getParameter("order");
        String sort = request.getParameter("sort");
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sort).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(order).map(SortType::valueOf).orElse(null)));
        request.setAttribute("recentProducts",productDao.getRecentProducts(3,request));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
