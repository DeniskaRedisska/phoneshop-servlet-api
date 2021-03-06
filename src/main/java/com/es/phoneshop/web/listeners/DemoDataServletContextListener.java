package com.es.phoneshop.web.listeners;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceInfo;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Currency;

public class DemoDataServletContextListener implements ServletContextListener, Serializable {

    private ProductDao productDao;

    public DemoDataServletContextListener() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        boolean insertDemoData = Boolean.parseBoolean(
                servletContextEvent.getServletContext().getInitParameter("insertDemoData"));
        if (insertDemoData) {
            initProducts();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    protected void initProducts() {
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
        setPriceHistory();
    }

    private void setPriceHistory() {
        productDao.get(0L).setPriceHistory(new ArrayList<>() {{
            add(new PriceInfo(LocalDateTime.of(2020, Month.JUNE, 12, 0, 0).toLocalDate(), new BigDecimal(80)));
            add(new PriceInfo(LocalDateTime.of(2020, Month.AUGUST, 9, 0, 0).toLocalDate(), new BigDecimal(90)));
            add(new PriceInfo(LocalDateTime.of(2021, Month.FEBRUARY, 2, 0, 0).toLocalDate(), new BigDecimal(100)));
        }});
        productDao.get(2L).setPriceHistory(new ArrayList<>() {{
            add(new PriceInfo(LocalDateTime.of(2020, Month.JUNE, 12, 0, 0).toLocalDate(), new BigDecimal(250)));
            add(new PriceInfo(LocalDateTime.of(2020, Month.AUGUST, 9, 0, 0).toLocalDate(), new BigDecimal(280)));
            add(new PriceInfo(LocalDateTime.of(2021, Month.FEBRUARY, 2, 0, 0).toLocalDate(), new BigDecimal(300)));
        }});
        productDao.get(3L).setPriceHistory(new ArrayList<>() {{
            add(new PriceInfo(LocalDateTime.of(2020, Month.JUNE, 12, 0, 0).toLocalDate(), new BigDecimal(100)));
            add(new PriceInfo(LocalDateTime.of(2020, Month.AUGUST, 9, 0, 0).toLocalDate(), new BigDecimal(150)));
            add(new PriceInfo(LocalDateTime.of(2021, Month.FEBRUARY, 2, 0, 0).toLocalDate(), new BigDecimal(200)));
        }});
        productDao.get(4L).setPriceHistory(new ArrayList<>() {{
            add(new PriceInfo(LocalDateTime.of(2020, Month.JUNE, 12, 0, 0).toLocalDate(), new BigDecimal(1500)));
            add(new PriceInfo(LocalDateTime.of(2020, Month.AUGUST, 9, 0, 0).toLocalDate(), new BigDecimal(1200)));
            add(new PriceInfo(LocalDateTime.of(2021, Month.FEBRUARY, 2, 0, 0).toLocalDate(), new BigDecimal(1000)));
        }});
        productDao.get(5L).setPriceHistory(new ArrayList<>() {{
            add(new PriceInfo(LocalDateTime.of(2020, Month.JUNE, 12, 0, 0).toLocalDate(), new BigDecimal(400)));
            add(new PriceInfo(LocalDateTime.of(2020, Month.AUGUST, 9, 0, 0).toLocalDate(), new BigDecimal(350)));
            add(new PriceInfo(LocalDateTime.of(2021, Month.FEBRUARY, 2, 0, 0).toLocalDate(), new BigDecimal(320)));
        }});
    }
}
