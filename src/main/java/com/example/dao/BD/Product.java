package com.example.dao.BD;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    private SimpleStringProperty name;
    private SimpleDoubleProperty count;

    public Product(String name, double count) {
        this.name = new SimpleStringProperty(name);
        this.count = new SimpleDoubleProperty(count);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getCount() {
        return count.get();
    }

    public void setCount(double count) {
        this.count.set(count);
    }
}