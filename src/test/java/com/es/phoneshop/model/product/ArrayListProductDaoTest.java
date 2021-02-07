package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.product.exceptions.VerificationException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArrayListProductDaoTest {
    private ArrayListProductDao productDao;
    private Currency usd;

    @Before
    public void setup() {
        List<Product> testList = new ArrayList<>();
        productDao = new ArrayListProductDao(testList);
        setTestData();
        usd = Currency.getInstance("USD");
    }

    private void setTestData() {
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
    }

    @Test
    public void testFindProducts() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testFindProductsWithNullPrice() {
        if (productDao.findProducts().stream()
                .anyMatch(product -> product.getPrice() == null))
            fail("Shouldn't contain products with null price");
    }

    @Test
    public void testFindProductsWithZeroStock() {
        if (productDao.findProducts().stream()
                .anyMatch(product -> product.getStock() == 0))
            fail("Shouldn't contain products with zero stock");
    }

    @Test
    public void testSaveProduct() {
        Product product = new Product("new", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        assertNotNull(product.getId());
        try {
            assertEquals(product, productDao.getProduct(product.getId()));
        } catch (Exception e) {
            fail("Unexpected exception in getProduct method");
        }

    }

    @Test
    public void testUpdateProduct() {
        try {
            Product product = productDao.getProduct(0L);
            assertEquals("sgs", product.getCode());
            productDao.save((new Product(0L, "updated", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg")));
            product = productDao.getProduct(0L);
            assertEquals("updated", product.getCode());
        } catch (Exception e) {
            fail("Unexpected exception in getProduct method");
        }
    }

    @Test
    public void testTrySaveNull() {
        assertThrows(
                VerificationException.class,
                () -> productDao.save(null)
        );
    }

    @Test
    public void testSaveProductWithExistingUniqueId() {
        long id = productDao.getMaxId() + 5L;
        Product product = new Product(id, "new", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        try {
            Product savedProduct = productDao.getProduct(id);
            assertEquals(product, savedProduct);
        } catch (Exception e) {
            fail("Unexpected exception in getProduct method");
        }

    }

    @Test
    public void testSuccessfulDeleteProduct() {
        try {
            Product product = productDao.getProduct(2L);
            assertNotNull(product);
        } catch (Exception e) {
            fail("Unexpected exception in getProduct method");
        }
        productDao.delete(2L);
        assertThrows(
                ProductNotFoundException.class,
                () -> productDao.getProduct(2L)
        );
    }

    @Test
    public void testDeleteNonExistingProduct() {
        long id = productDao.getMaxId() + 10L;
        assertThrows(
                ProductNotFoundException.class,
                () -> productDao.getProduct(id)
        );
        try {
            productDao.delete(id);
        } catch (Exception e) {
            fail("Unexpected exception");
        }

    }

    @Test
    public void testDeleteNull() {
        assertThrows(
                VerificationException.class,
                () -> productDao.delete(null)
        );
    }

    @Test
    public void testGetProduct() {
        try {
            Product product = productDao.getProduct(2L);
            assertNotNull(product);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testUnsuccessfulGetProduct() {
        assertThrows(
                ProductNotFoundException.class,
                () -> productDao.getProduct(productDao.getMaxId() + 5L)
        );
    }

    @Test
    public void testGetNull() {
        assertThrows(
                VerificationException.class,
                () -> productDao.getProduct(null)
        );
    }
}
