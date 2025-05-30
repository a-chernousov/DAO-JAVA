package com.example.dao.BD;

import com.example.dao.product.Product;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PostgresDAOTest {
    private static PostgresDAO postgresDAO;
    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException {
        // Используем H2 in-memory базу для тестов
        String testUrl = "jdbc:postgresql://localhost:5432/Products";
        String testUser = "postgres";
        String testPassword = "1111";

        // Инициализируем DAO с тестовой базой
        postgresDAO = new PostgresDAO() {
            @Override
            public void createTable() {
                this.url = testUrl;
                this.user = testUser;
                this.password = testPassword;
                super.createTable();
                createCategoriesTable();
            }
        };

        connection = DriverManager.getConnection(testUrl, testUser, testPassword);
        postgresDAO.createTable(); // Создаем таблицы один раз перед всеми тестами
    }

    private static void createCategoriesTable() {
        try (var statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Categories (" +
                    "id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL)");

            // Добавляем тестовые категории
            statement.execute("INSERT INTO Categories (name) VALUES ('Electronics')");
            statement.execute("INSERT INTO Categories (name) VALUES ('Food')");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create categories table", e);
        }
    }

    @AfterEach
    void cleanUp() throws SQLException {
        // Очищаем таблицу продуктов после каждого теста
        try (var statement = connection.createStatement()) {
            statement.execute("DELETE FROM ProductsList");
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    @DisplayName("Добавление нового продукта")
    void testAddNewProduct() {
        Product newProduct = new Product("Laptop", 1500.0, "Красота");

        // Проверяем, что продукта нет в базе
        assertTrue(postgresDAO.readProduct().isEmpty());

        // Добавляем продукт
        postgresDAO.updateProduct(newProduct);

        // Проверяем, что продукт добавился
        List<Product> products = postgresDAO.readProduct();
        assertEquals(1, products.size());

        Product savedProduct = products.get(0);
        assertEquals(newProduct.getName(), savedProduct.getName());
        assertEquals(newProduct.getCount(), savedProduct.getCount());
        assertEquals(newProduct.getCategoryName(), savedProduct.getCategoryName());
    }

    @Test
    @DisplayName("Обновление существующего продукта")
    void testUpdateExistingProduct() {
        // Добавляем начальный продукт
        Product initialProduct = new Product("Apple", 10.0, "Food");
        postgresDAO.updateProduct(initialProduct);

        // Обновляем продукт
        Product updatedProduct = new Product("Apple", 15.0, "Food");
        postgresDAO.updateProduct(updatedProduct);

        // Проверяем обновление
        List<Product> products = postgresDAO.readProduct();
        assertEquals(1, products.size());
        assertEquals(updatedProduct.getCount(), products.get(0).getCount());
    }

    @Test
    @DisplayName("Удаление продукта")
    void testDeleteProduct() {
        // Добавляем продукт
        Product product = new Product("Phone", 999.99, "Electronics");
        postgresDAO.updateProduct(product);

        // Проверяем, что продукт добавился
        assertEquals(1, postgresDAO.readProduct().size());

        // Удаляем продукт
        postgresDAO.deleteProduct(product);

        // Проверяем, что продукт удалился
        assertTrue(postgresDAO.readProduct().isEmpty());
    }

    @Test
    @DisplayName("Попытка добавить продукт с несуществующей категорией")
    void testAddProductWithInvalidCategory() {
        Product invalidProduct = new Product("Test", 1.0, "NonExistingCategory");

        postgresDAO.updateProduct(invalidProduct);

        // Продукт не должен добавиться
        assertTrue(postgresDAO.readProduct().isEmpty());
    }

    @Test
    @DisplayName("Добавление нескольких продуктов")
    void testAddMultipleProducts() {
        Product product1 = new Product("Keyboard", 50.0, "Electronics");
        Product product2 = new Product("Bread", 2.5, "Food");

        postgresDAO.updateProduct(product1);
        postgresDAO.updateProduct(product2);

        List<Product> products = postgresDAO.readProduct();
        assertEquals(2, products.size());
    }

    @Test
    @DisplayName("Удаление несуществующего продукта")
    void testDeleteNonExistingProduct() {
        Product nonExisting = new Product("NonExisting", 0.0, "Electronics");

        // Не должно быть исключений
        assertDoesNotThrow(() -> postgresDAO.deleteProduct(nonExisting));
    }
}