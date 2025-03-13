package com.example.dao.BD;

import java.util.List;
public interface DAO {

    void createTable();
    List<Product> selectAllProducts();
    void updateProduct(Product product);
    void deleteProduct(Product product);

}


