package com.example.dao;

import com.example.dao.BD.FileSystemDAO;
import com.example.dao.product.ProductList;
import com.example.dao.buttonCell.ButtonCell;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.example.dao.product.Product;
import com.example.dao.BD.DAO;
import com.example.dao.BD.PostgresDAO;

public class HelloController {
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TextField productNameField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TableColumn<Product, Void> actionsColumn;

    private ProductList productList;

    public HelloController() {
        // Используем PostgresDAO
        DAO postgresDAO = new PostgresDAO();
        this.productList = new ProductList(postgresDAO);
        // Используем FileSystemDAO вместо PostgresDAO
//        DAO fileSystemDAO = new FileSystemDAO();
//        this.productList = new ProductList(fileSystemDAO);
    }

    @FXML
    public void initialize() {
        // Загружаем категории в ComboBox
        categoryComboBox.getItems().addAll(productList.getAllCategories());

        // Настройка таблицы
        productTable.setSortPolicy(param -> true); // Отключаем автоматическую сортировку
        actionsColumn.setCellFactory(param -> new ButtonCell(this));

        // Обновляем таблицу при запуске
        refreshTable();
    }

    @FXML
    public void addOrUpdateProduct() {
        String productName = productNameField.getText();
        String categoryName = categoryComboBox.getValue();

        if (productName != null && !productName.trim().isEmpty() && categoryName != null) {
            Product product = new Product(productName, 1, categoryName);
            productList.addOrUpdateProduct(product); // Используем addOrUpdateProduct
            refreshTable();
            productNameField.clear();
        }
    }

    @FXML
    public void increaseQuantity(Product product) {
        product.setCount(product.getCount() + 1);
        productList.addOrUpdateProduct(product); // Используем addOrUpdateProduct для обновления
        refreshTable();
    }

    @FXML
    public void decreaseQuantity(Product product) {
        if (product.getCount() > 1) {
            product.setCount(product.getCount() - 1);
            productList.addOrUpdateProduct(product); // Используем addOrUpdateProduct для обновления
        } else {
            productList.deleteProduct(product); // Удаляем продукт, если количество <= 1
        }
        refreshTable();
    }

    private void refreshTable() {
        productTable.getItems().clear();
        productTable.getItems().addAll(productList.getAllProducts());
    }
}