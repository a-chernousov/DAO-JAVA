package com.example.dao;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.example.dao.BD.Product;
import com.example.dao.BD.DatabaseConnection;

public class HelloController {
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TextField productNameField;

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    @FXML
    public void initialize() {
        databaseConnection.createTable();
        databaseConnection.ConnectToBD();
        refreshTable();

        // Устанавливаем cellFactory для колонки с действиями
        TableColumn<Product, Void> actionsColumn = (TableColumn<Product, Void>) productTable.getColumns().get(2);
        actionsColumn.setCellFactory(param -> new ButtonCell(this));
    }

    @FXML
    protected void addProduct() {
        String productName = productNameField.getText();
        if (productName != null && !productName.trim().isEmpty()) {
            databaseConnection.insertProduct(productName, 1);
            refreshTable();
            productNameField.clear();
        }
    }

    @FXML
    protected void increaseQuantity(Product product) {
        product.setCount(product.getCount() + 1);
        databaseConnection.updateProduct(product);
        refreshTable();
    }

    @FXML
    protected void decreaseQuantity(Product product) {
        if (product.getCount() > 1) {
            product.setCount(product.getCount() - 1);
            databaseConnection.updateProduct(product);
        } else {
            databaseConnection.deleteProduct(product);
        }
        refreshTable();
    }

    private void refreshTable() {
        productTable.getItems().clear();
        productTable.getItems().addAll(databaseConnection.selectAllProducts());
    }
}