package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.SearchMode;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class AdvancedSearchServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("products", Collections.emptyList());
        request.setAttribute("searchModes", Arrays.asList(SearchMode.values()));
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("description");
        String searchMode = request.getParameter("searchMode");

        Map<String, String> errors = new HashMap<>();

        checkPrice(request, errors);

        handleErrors(errors, request, searchMode, response, description, productDao);
    }

    private void checkPrice(HttpServletRequest request, Map<String, String> errors) {
        String firstParam = request.getParameter("maxPrice");
        String secondParam = request.getParameter("minPrice");
        if (!isValid(firstParam, secondParam)) errors.put("maxPrice", "Invalid input");
    }

    private void handleErrors(Map<String, String> errors, HttpServletRequest request, String mode,
                              HttpServletResponse response, String description, ProductDao productDao) throws ServletException, IOException {
        if (errors.isEmpty()) {
            request.setAttribute("products", productDao.findProductWithAdvancedSearch(description,
                    parseBigDecimal(request, "minPrice"),
                    parseBigDecimal(request, "maxPrice"), SearchMode.valueOf(mode)));
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("products", Collections.emptyList());
        }
        request.setAttribute("searchModes", Arrays.asList(SearchMode.values()));
        response.sendRedirect(request.getContextPath() + "/advancedSearch");
    }

    private BigDecimal parseBigDecimal(HttpServletRequest request, String str) {
        String parameter = request.getParameter(str);
        if (parameter.equals("")) return null;
        return BigDecimal.valueOf(Integer.parseInt(parameter));

    }

    private boolean isValid(String max, String min) {
        if (max.equals("") && min.equals("")) return true;
        try {
            BigDecimal maxPrice = BigDecimal.valueOf(Integer.parseInt(max));
            BigDecimal minPrice = BigDecimal.valueOf(Integer.parseInt(min));

            return maxPrice.signum() >= 0 && minPrice.signum() >= 0 && maxPrice.compareTo(minPrice) >= 0;
        } catch (NumberFormatException exc) {
            return false;
        }
    }

}
