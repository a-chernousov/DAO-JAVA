package com.example.dao.buttonCell;

import com.example.dao.HelloController;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import com.example.dao.product.Product;

public class ButtonCell extends TableCell<Product, Void> {
    private final Button decreaseButton = new Button("-");
    private final Button increaseButton = new Button("+");
    private final HBox hbox = new HBox(decreaseButton, increaseButton);

    private final HelloController controller;

    public ButtonCell(HelloController controller) {
        this.controller = controller;

        hbox.setSpacing(10);

        // Кнопка "-" уменьшает количество
        decreaseButton.setOnAction(event -> {
            Product product = getTableView().getItems().get(getIndex());
            controller.decreaseQuantity(product); // Вызываем decreaseQuantity
        });

        // Кнопка "+" увеличивает количество
        increaseButton.setOnAction(event -> {
            Product product = getTableView().getItems().get(getIndex());
            controller.increaseQuantity(product); // Вызываем increaseQuantity
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
}