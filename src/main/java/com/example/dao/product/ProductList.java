package com.example.dao.product;

import com.example.dao.BD.DAO;

import java.util.List;

public class ProductList {
    private DAO dao;

    // Конструктор, принимающий реализацию DAO
    public ProductList(DAO dao) {
        this.dao = dao;
    }

    // Метод для добавления или обновления продукта
    public void addOrUpdateProduct(Product product) {
        dao.updateProduct(product); // Используем updateProduct для добавления или обновления
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

    // Метод для получения всех категорий
    public List<String> getAllCategories() {
        return dao.selectAllCategories();
    }
}