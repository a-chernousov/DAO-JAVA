package com.example.dao.BD;

import java.util.List;

public class ProductList {
    private DAO dao;

    // Конструктор, принимающий реализацию DAO
    public ProductList(DAO dao) {
        this.dao = dao;
    }

    // Метод для добавления продукта
    public void addProduct(Product product) {
        // Логика добавления продукта через DAO
    }

    // Метод для обновления продукта
    public void updateProduct(Product product) {
        dao.updateProduct(product);
    }

    // Метод для удаления продукта
    public void deleteProduct(Product product) {
        dao.deleteProduct(product);
    }

    // Метод для получения всех продуктов
    public List<Product> getAllProducts() {
        return dao.selectAllProducts();
    }

    // Метод для поиска продукта по имени
    public Product findProductByName(String name) {
        List<Product> products = dao.selectAllProducts();
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
}