package com.example.dao.BD;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    public String url = "jdbc:postgresql://localhost:5432/Products"; // URL базы данных
    public String user = "postgres"; // Имя пользователя
    public String password = "1111"; // Пароль

    public void ConnectToBD() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Соединение с базой данных установлено!");
            try (var statement = connection.createStatement();
                 var resultSet = statement.executeQuery("SELECT version()")) {
                if (resultSet.next()) {
                    System.out.println("Версия PostgreSQL: " + resultSet.getString(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS ProductsList (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "count DOUBLE PRECISION NOT NULL)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица создана или уже существует.");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    public void insertProduct(String name, double count) {
        String sql = "INSERT INTO ProductsList (name, count) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, count);
            preparedStatement.executeUpdate();
            System.out.println("Данные успешно добавлены.");
        } catch (SQLException e) {
            System.err.println("Ошибка при вставке данных: " + e.getMessage());
        }
    }

    public List<Product> selectAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM ProductsList";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double count = resultSet.getDouble("count");
                products.add(new Product(name, count));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выборке данных: " + e.getMessage());
        }
        return products;
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE ProductsList SET count = ? WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, product.getCount());
            preparedStatement.setString(2, product.getName());
            preparedStatement.executeUpdate();
            System.out.println("Данные успешно обновлены.");
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении данных: " + e.getMessage());
        }
    }

    public void deleteProduct(Product product) {
        String sql = "DELETE FROM ProductsList WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.executeUpdate();
            System.out.println("Данные успешно удалены.");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении данных: " + e.getMessage());
        }
    }

    public void dropTable() {
        String sql = "DROP TABLE IF EXISTS ProductsList";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица удалена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }
}