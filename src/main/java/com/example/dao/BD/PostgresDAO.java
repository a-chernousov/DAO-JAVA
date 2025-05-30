package com.example.dao.BD;

import com.example.dao.product.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresDAO implements DAO {
    protected String url = "jdbc:postgresql://localhost:5432/Products";
    protected String user = "postgres";
    protected String password = "1111";

    @Override
    public void createTable() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            String createProductsTable = "CREATE TABLE IF NOT EXISTS ProductsList (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "count DOUBLE PRECISION NOT NULL, " +
                    "category_id INT REFERENCES Categories(id))";
            statement.execute(createProductsTable);
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }
    @Override
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

    @Override
    public List<Product> readProduct() {
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

    @Override
    public void updateProduct(Product product) {
        // Проверяем, существует ли продукт в базе данных
        String selectSql = "SELECT id FROM ProductsList WHERE name = ?";
        String insertSql = "INSERT INTO ProductsList (name, count, category_id) VALUES (?, ?, ?)";
        String updateSql = "UPDATE ProductsList SET count = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStatement = connection.prepareStatement(selectSql);
             PreparedStatement insertStatement = connection.prepareStatement(insertSql);
             PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {

            // Проверяем, существует ли продукт
            selectStatement.setString(1, product.getName());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Продукт существует, обновляем количество
                int productId = resultSet.getInt("id");

                updateStatement.setDouble(1, product.getCount()); // Используем текущее количество из объекта Product
                updateStatement.setInt(2, productId);
                updateStatement.executeUpdate();
            } else {
                // Продукт не существует, добавляем новый продукт с текущим количеством
                int categoryId = getCategoryIdByName(product.getCategoryName());
                if (categoryId != -1) {
                    insertStatement.setString(1, product.getName());
                    insertStatement.setDouble(2, product.getCount()); // Используем текущее количество из объекта Product
                    insertStatement.setInt(3, categoryId);
                    insertStatement.executeUpdate();
                } else {
                    System.err.println("Категория не найдена: " + product.getCategoryName());
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении/добавлении продукта: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(Product product) {
            String sql = "DELETE FROM ProductsList";

            try (Connection connection = DriverManager.getConnection(url, user, password);
                 Statement statement = connection.createStatement()) {
                statement.execute(sql);
                System.out.println("Все данные удалены.");

                // Сбрасываем последовательность
                resetSequence();
            } catch (SQLException e) {
                System.err.println("Ошибка при удалении данных: " + e.getMessage());
            }
        }

    private int getCategoryIdByName(String categoryName) {
        String sql = "SELECT id FROM Categories WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, categoryName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении ID категории: " + e.getMessage());
        }
        return -1; // Если категория не найдена
    }
    public void resetSequence() {
        String sql = "ALTER SEQUENCE productslist_id_seq RESTART WITH 1";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Последовательность сброшена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при сбросе последовательности: " + e.getMessage());
        }
    }
}