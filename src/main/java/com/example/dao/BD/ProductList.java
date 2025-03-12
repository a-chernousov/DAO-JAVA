package com.example.dao.BD;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ProductList {
    private static final Logger logger = Logger.getLogger(ProductList.class.getName());
    private List<Product> products;

    public ProductList() {
        products = new ArrayList<>(); // Инициализация в конструкторе
    }

    public void add(Product product) {
        // Проверка на null
        Objects.requireNonNull(product, "Product cannot be null");

        // Проверка названия продукта
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            String errorMessage = "Product name cannot be null or empty";
            logger.severe(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Проверка количества продукта
        if (product.getCount() <= 0) {
            String errorMessage = "Product count must be greater than 0";
            logger.severe(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        

        // Добавление продукта
        products.add(product);
        logger.info("Product added: {}" +  product.toString());
    }

    public void update(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        for (Product p : products) {
            if (p.getName().equals(product.getName())) {
                if (product.getCount() == 0) {
                    delete(product);
                } else {
                    p.setCount(product.getCount());
                    logger.info("Product updated: " + p);
                }
                return;
            }
        }

        products.add(product);
        logger.info("Product added: " + product);
    }


    public void delete(Product product1) {
        products.removeIf(product2 -> {
            boolean match = Objects.equals(product2.getName(), product1.getName());
            if (match) {
                logger.info("Product deleted: " + product2.toString());
            }
            return match;
        });
    }

    public List<Product> getProducts() {
        return products;
    }



}
