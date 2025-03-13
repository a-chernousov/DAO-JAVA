package com.example.dao.BD;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection implements DAO{
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

    public boolean isTableExists(String tableName) {
        String sql = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tableName.toLowerCase());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при проверке существования таблицы: " + e.getMessage());
        }
        return false;
    }

    public void createTable() {
        // Создаем таблицу Categories, если она не существует
        String createCategoriesTable = "CREATE TABLE Categories (\n" +
                "    id SERIAL PRIMARY KEY,         -- Уникальный идентификатор категории\n" +
                "    name VARCHAR(50) NOT NULL,     -- Название категории\n" +
                "    order_index INT                -- Поле для сортировки категорий\n" +
                ");";

        // Создаем таблицу ProductsList, если она не существует
        String createProductsTable = "CREATE TABLE IF NOT EXISTS ProductsList (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "count DOUBLE PRECISION NOT NULL, " +
                "category_id INT REFERENCES Categories(id))";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            // Создаем таблицу Categories
            if (!isTableExists("categories")) {
                System.out.println("Выполнение запроса: " + createCategoriesTable);
                statement.execute(createCategoriesTable);
                System.out.println("Таблица Categories создана.");
            }

            // Создаем таблицу ProductsList
            if (!isTableExists("productslist")) {
                System.out.println("Выполнение запроса: " + createProductsTable);
                statement.execute(createProductsTable);
                System.out.println("Таблица ProductsList создана.");
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблиц: " + e.getMessage());
        }
    }

    public void insertProduct(String name, double count, int categoryId) {
        String sql = "INSERT INTO ProductsList (name, count, category_id) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, count);
            preparedStatement.setInt(3, categoryId);
            preparedStatement.executeUpdate();
            System.out.println("Данные успешно добавлены.");
        } catch (SQLException e) {
            System.err.println("Ошибка при вставке данных: " + e.getMessage());
        }
    }

    public List<Product> selectAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.name, p.count, c.name AS category_name " +
                "FROM ProductsList p " +
                "JOIN Categories c ON p.category_id = c.id";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double count = resultSet.getDouble("count");
                String categoryName = resultSet.getString("category_name");
                products.add(new Product(name, count, categoryName));
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

    public List<String> selectAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT name FROM Categories";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                categories.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выборке категорий: " + e.getMessage());
        }
        return categories;
    }

    public void insertCategory(String name) {
        String sql = "INSERT INTO Categories (name) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            System.out.println("Категория успешно добавлена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении категории: " + e.getMessage());
        }
    }

    public int getCategoryIdByName(String name) {
        String sql = "SELECT id FROM Categories WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении ID категории: " + e.getMessage());
        }
        return -1; // Если категория не найдена
    }
}