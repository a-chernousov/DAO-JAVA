package com.example.dao.product;

import com.example.dao.BD.DAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductListTest {

    @Mock
    private DAO dao; // Мок DAO

    private ProductList productList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productList = new ProductList(dao);
    }

    @Test
    void addOrUpdateProduct_ShouldCallUpdateProductInDao() {
        Product product = new Product("Laptop", 5.0, "Electronics");

        productList.addOrUpdateProduct(product);

        verify(dao, times(1)).updateProduct(product);
    }

    @Test
    void deleteProduct_ShouldCallDeleteProductInDao() {
        Product product = new Product("Phone", 10.0, "Electronics");

        productList.deleteProduct(product);

        verify(dao, times(1)).deleteProduct(product);
    }

    @Test
    void getAllProducts_ShouldReturnAllProductsFromDao() {
        List<Product> mockProducts = Arrays.asList(
                new Product("Laptop", 5.0, "Electronics"),
                new Product("Phone", 10.0, "Electronics")
        );
        when(dao.selectAllProducts()).thenReturn(mockProducts); // Задаём поведение мока

        List<Product> result = productList.getAllProducts();

        assertEquals(2, result.size()); // Проверяем, что вернулось 2 продукта
        assertEquals("Laptop", result.get(0).getName());
        verify(dao, times(1)).selectAllProducts();
    }

    @Test
    void findProductByName_WhenProductExists_ShouldReturnProduct() {
        List<Product> mockProducts = Arrays.asList(
                new Product("Laptop", 5.0, "Electronics"),
                new Product("Phone", 10.0, "Electronics")
        );
        when(dao.selectAllProducts()).thenReturn(mockProducts);

        Product foundProduct = productList.findProductByName("Phone");

        assertNotNull(foundProduct); // Проверяем, что продукт найден
        assertEquals("Phone", foundProduct.getName());
        assertEquals(10.0, foundProduct.getCount());
    }

    @Test
    void findProductByName_WhenProductNotExists_ShouldReturnNull() {
        List<Product> mockProducts = Arrays.asList(
                new Product("Laptop", 5.0, "Electronics"),
                new Product("Phone", 10.0, "Electronics")
        );
        when(dao.selectAllProducts()).thenReturn(mockProducts);

        Product foundProduct = productList.findProductByName("Tablet");

        assertNull(foundProduct); // Проверяем, что продукт не найден (null)
    }

    @Test
    void getAllCategories_ShouldReturnAllCategoriesFromDao() {
        List<String> mockCategories = Arrays.asList("Electronics", "Clothing");
        when(dao.selectAllCategories()).thenReturn(mockCategories);

        List<String> result = productList.getAllCategories();

        assertEquals(2, result.size()); // Проверяем, что вернулось 2 категории
        assertTrue(result.contains("Electronics"));
        verify(dao, times(1)).selectAllCategories();
    }
}