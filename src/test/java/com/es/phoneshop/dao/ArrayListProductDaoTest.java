package com.es.phoneshop.dao;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.SortField;
import com.es.phoneshop.dao.SortType;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.exceptions.VerificationException;
import com.es.phoneshop.model.product.Product;
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
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
    }

    @Test
    public void testFindProducts() {
        assertFalse(productDao.findProducts(null, null, null).isEmpty());
    }

    @Test
    public void testFindProductsWithQueryAndSorting() {
        assertEquals(3,
                productDao.findProducts("Samsung Galaxy", SortField.price, SortType.asc).size());
        assertEquals(3,
                productDao.findProducts("Samsung II", SortField.price, SortType.asc).size());
        assertEquals(2,
                productDao.findProducts("II", SortField.price, SortType.asc).size());
        assertEquals(3,
                productDao.findProducts("Samsung Galaxy S II", SortField.price, SortType.asc).size());
        assertEquals(3,
                productDao.findProducts("S", SortField.price, SortType.asc).size());
    }

    @Test
    public void testSortingBySearchTerms() {
        assertEquals("Samsung Galaxy S II",
                productDao.findProducts("Samsung II", null, null).get(0).getDescription());
        assertEquals("Samsung Galaxy S II",
                productDao.findProducts("S II", null, null).get(0).getDescription());
        assertEquals("Samsung Galaxy S",
                productDao.findProducts("Samsung S", null, null).get(0).getDescription());

    }

    @Test
    public void testSortingByFieldsAndType() {
        assertEquals("Samsung Galaxy S",
                productDao.findProducts("Samsung S", SortField.price, SortType.asc).get(0).getDescription());
        assertEquals("Samsung Galaxy S III",
                productDao.findProducts("Samsung S", SortField.price, SortType.desc).get(0).getDescription());
        assertEquals("Samsung Galaxy S III",
                productDao.findProducts("Samsung S", SortField.description, SortType.desc).get(0).getDescription());
        assertEquals("Samsung Galaxy S",
                productDao.findProducts("Samsung S", SortField.description, SortType.asc).get(0).getDescription());
    }


    @Test
    public void testFindProductsWithNullPrice() {
        assertFalse(productDao.findProducts(null, null, null).stream()
                .anyMatch(product -> product.getPrice() == null));
    }

    @Test
    public void testFindProductsWithZeroStock() {
        assertFalse(productDao.findProducts(null, null, null).stream()
                .anyMatch(product -> product.getStock() == 0));

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
