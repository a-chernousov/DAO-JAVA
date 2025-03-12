package com.example.dao.BD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductListTest {

    private ProductList productList;


    @BeforeEach
    void setUp(){
        productList = new ProductList();
    }

    @Nested
    class add{
        @Test
        void addProduct(){
            productList.add(new Product("Хлеб", 1));

        }

        @Test
        void addEmptyProduct(){
            try {
                productList.add(new Product("", 0));
                fail("Expected thrown test");
            } catch (IllegalArgumentException e){
                assertEquals("Product name cannot be null or empty", e.getMessage());
            }
        }

        @Test
        void addNegativeOrZeroCount(){
            try {
                productList.add(new Product("Молоко", -0));
                fail("Expected thrown test");
            } catch (IllegalArgumentException e){
                assertEquals("Product count must be greater than 0", e.getMessage());
            }
        }

    }

    @Test
    public void delete() {
        Product product = new Product("Пиво", 10);
        productList.delete(product);
        assertEquals(0, productList.getProducts().size(), "Список должен содержать один продукт");

    }

    @Test
    public void update(){
        productList.update(new Product("Мороженное", 2));
        productList.update(new Product("Мороженное", 0));
    }
}