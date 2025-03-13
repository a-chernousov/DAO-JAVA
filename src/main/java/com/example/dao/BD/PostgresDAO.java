package com.example.dao.BD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresDAO implements DAO {
    private String url = "jdbc:postgresql://localhost:5432/Products";
    private String user = "postgres";
    private String password = "1111";

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

    @Override
    public void updateProduct(Product product) {
        String sql = "UPDATE ProductsList SET count = ? WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, product.getCount());
            preparedStatement.setString(2, product.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении данных: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(Product product) {
        String sql = "DELETE FROM ProductsList WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении данных: " + e.getMessage());
        }
    }
}