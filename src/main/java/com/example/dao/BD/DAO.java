package com.example.dao.BD;

import com.example.dao.product.Product;

import java.util.List;
public interface DAO {

    void createTable();
    List<Product> selectAllProducts();
    void updateProduct(Product product);
    void deleteProduct(Product product);
    List<String> selectAllCategories();
}


