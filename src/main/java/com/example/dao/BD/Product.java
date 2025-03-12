package com.example.dao.BD;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    private SimpleStringProperty name;
    private SimpleDoubleProperty count;
    private SimpleStringProperty categoryName;  // Новое поле для категории

    public Product(String name, double count, String categoryName) {
        this.name = new SimpleStringProperty(name);
        this.count = new SimpleDoubleProperty(count);
        this.categoryName = new SimpleStringProperty(categoryName);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public double getCount() {
        return count.get();
    }

    public void setCount(double count) {
        this.count.set(count);
    }

    public SimpleDoubleProperty countProperty() {
        return count;
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.set(categoryName);
    }

    public SimpleStringProperty categoryNameProperty() {
        return categoryName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name=" + name.get() +
                ", count=" + count.get() +
                ", categoryName=" + categoryName.get() +
                '}';
    }
}