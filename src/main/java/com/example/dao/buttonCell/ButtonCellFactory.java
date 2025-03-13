package com.example.dao.buttonCell;

import com.example.dao.product.Product;
import com.example.dao.HelloController;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ButtonCellFactory implements Callback<TableColumn<Product, Void>, TableCell<Product, Void>> {

    private HelloController controller;

    // Статический метод для создания экземпляра фабрики
    public static ButtonCellFactory create() {
        return new ButtonCellFactory();
    }

    public void setController(HelloController controller) {
        this.controller = controller;
    }

    @Override
    public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
        return new TableCell<>() {
            private final Button decreaseButton = new Button("-");
            private final Button increaseButton = new Button("+");
            private final HBox hbox = new HBox(decreaseButton, increaseButton);


            {
                decreaseButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    controller.decreaseQuantity(product);
                });

                increaseButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    controller.increaseQuantity(product);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                }
            }
        };
    }
}