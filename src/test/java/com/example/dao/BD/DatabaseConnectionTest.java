package com.example.dao.BD;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnectionTest {

    private static Connection connection;
    private static DatabaseConnection databaseConnection;

    @BeforeAll
    public static void setup() throws SQLException {
        // Устанавливаем соединение с тестовой базой данных
        String url = "jdbc:postgresql://localhost:5432/test"; // Используйте тестовую БД
        String user = "postgres";
        String password = "1111";
        connection = (Connection) DriverManager.getConnection(url, user, password);

        // Инициализируем DAO
        databaseConnection = new DatabaseConnection();

        // Создаем тестовую таблицу
        databaseConnection.createTable();
    }

    @AfterAll
    public static void tearDown() throws SQLException, IOException {
        // Закрываем соединение после всех тестов
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testDatabaseInitialization() throws SQLException {
        // Проверяем, что соединение установлено
        Assertions.assertFalse(connection.isClosed(), "Соединение с базой данных должно быть открыто.");

        // Создаем тестовую таблицу
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS test_table (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(50) NOT NULL)";
            statement.execute(sql);
        }

        // Проверяем, что таблица создана
        try (Statement statement = connection.createStatement();
             var resultSet = statement.executeQuery(
                     "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'test_table')"
             )) {
            Assertions.assertTrue(resultSet.next(), "Таблица должна существовать.");
            Assertions.assertTrue(resultSet.getBoolean(1), "Таблица 'test_table' должна быть создана.");
        }
    }

    @Test
    public void testConnectionClose() throws SQLException {
        // Закрываем соединение
        connection.close();

        // Проверяем, что соединение закрыто
        Assertions.assertTrue(connection.isClosed(), "Соединение с базой данных должно быть закрыто.");
    }
}