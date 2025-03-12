package com.example.dao;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
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
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TableColumn<Product, Void> actionsColumn;

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    @FXML
    public void initialize() {
        databaseConnection.createTable();
        databaseConnection.ConnectToBD();
        refreshTable();

        // Отключаем автоматическую сортировку при изменении данных
        productTable.setSortPolicy(param -> {
            // Сортировка будет происходить только при нажатии на заголовок колонки
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
            int categoryId = databaseConnection.getCategoryIdByName(categoryName);
            if (categoryId != -1) {
                databaseConnection.insertProduct(productName, 1, categoryId);
                refreshTable();
                productNameField.clear();
            } else {
                System.err.println("Категория не найдена!");
            }
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
        // Сохраняем текущее выделение
        int selectedIndex = getSelectedIndex();

        // Обновляем данные в таблице
        productTable.getItems().clear();
        productTable.getItems().addAll(databaseConnection.selectAllProducts());

        // Восстанавливаем выделение
        restoreSelection(selectedIndex);
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