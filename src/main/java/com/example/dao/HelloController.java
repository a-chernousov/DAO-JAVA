package com.example.dao;

import com.example.dao.BD.*;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

//TODO
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

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    @FXML
    public void initialize() {
//        databaseConnection.createTable();
//        databaseConnection.ConnectToBD();
        DAO postgresDAO = new PostgresDAO();
        this.productList = new ProductList(postgresDAO);
        refreshTable();

        // Отключаем автоматическую сортировку при изменении данных
        productTable.setSortPolicy(param -> {
            return true;
        });

        // Устанавливаем cellFactory для колонки с действиями
        actionsColumn.setCellFactory(param -> new ButtonCell(this));

        // Загружаем категории в ComboBox
        categoryComboBox.getItems().addAll(databaseConnection.selectAllCategories());
    }

    @FXML
    protected void addProduct() {
        String productName = productNameField.getText();
        String categoryName = categoryComboBox.getValue();

        if (productName != null && !productName.trim().isEmpty() && categoryName != null) {
            Product product = new Product(productName, 1, categoryName);
            productList.addProduct(product);
            refreshTable();
            productNameField.clear();
        }
    }

    @FXML
    protected void increaseQuantity(Product product) {
        product.setCount(product.getCount() + 1);
        productList.updateProduct(product);
        refreshTable();
    }

    @FXML
    protected void decreaseQuantity(Product product) {
        if (product.getCount() > 1) {
            product.setCount(product.getCount() - 1);
            productList.updateProduct(product);
        } else {
            productList.deleteProduct(product);
        }
        refreshTable();
    }

    private void refreshTable() {
        productTable.getItems().clear();
        productTable.getItems().addAll(productList.getAllProducts());
    }

    private int getSelectedIndex() {
        return productTable.getSelectionModel().getSelectedIndex();
    }

    private void restoreSelection(int index) {
        if (index >= 0 && index < productTable.getItems().size()) {
            productTable.getSelectionModel().select(index);
        }
    }
}