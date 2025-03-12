package com.example.dao;

import com.example.dao.BD.Product;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class ButtonCell extends TableCell<Product, Void> {
    private final Button decreaseButton = new Button("-");
    private final Button increaseButton = new Button("+");
    private final HBox hbox = new HBox(decreaseButton, increaseButton);

    private final HelloController controller;

    public ButtonCell(HelloController controller) {
        this.controller = controller;

        // Устанавливаем расстояние между кнопками
        hbox.setSpacing(10); // 10 пикселей между кнопками

        // Устанавливаем отступы внутри кнопок
        decreaseButton.setPadding(new Insets(5, 10, 5, 10)); // Отступы внутри кнопки "-"
        increaseButton.setPadding(new Insets(5, 10, 5, 10)); // Отступы внутри кнопки "+"

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
}