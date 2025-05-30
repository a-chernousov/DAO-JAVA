package com.example.dao.BD;

import com.example.dao.product.Product;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemDAOTest {
    private static TestableFileSystemDAO fileSystemDAO;
    private static Path tempProductsFile;
    private static Path tempCategoriesFile;

    private static class TestableFileSystemDAO extends FileSystemDAO {
        public void setProductsFilePath(String path) {
            this.productsFilePath = path;
        }
        public void setCategoriesFilePath(String path) {
            this.categoriesFilePath = path;
        }
    }

    @BeforeAll
    static void setUp() throws IOException {
        // Создаем временные файлы для тестов
        tempProductsFile = Files.createTempFile("products", ".txt");
        tempCategoriesFile = Files.createTempFile("categories", ".txt");

        // Инициализируем DAO с путями к временным файлам
        fileSystemDAO = new TestableFileSystemDAO();
        fileSystemDAO.setProductsFilePath(tempProductsFile.toString());
        fileSystemDAO.setCategoriesFilePath(tempCategoriesFile.toString());

        fileSystemDAO.createTable();
    }

    @AfterEach
    void cleanUp() throws IOException {
        // Очищаем файлы после каждого теста
        Files.write(tempProductsFile, new byte[0]);
        Files.write(tempCategoriesFile, new byte[0]);
    }

    @AfterAll
    static void tearDown() throws IOException {
        // Удаляем временные файлы после всех тестов
        Files.deleteIfExists(tempProductsFile);
        Files.deleteIfExists(tempCategoriesFile);
    }

    @Test
    void testCreateTable() {
        assertTrue(Files.exists(tempProductsFile));
        assertTrue(Files.exists(tempCategoriesFile));
    }

    // Остальные тестовые методы остаются без изменений
    @Test
    void testReadEmptyProductFile() {
        List<Product> products = fileSystemDAO.readProduct();
        assertTrue(products.isEmpty());
    }

    @Test
    void testUpdateAndReadProduct() {
        Product product = new Product("TestProduct", 10.5, "TestCategory");
        fileSystemDAO.updateProduct(product);

        List<Product> products = fileSystemDAO.readProduct();
        assertEquals(1, products.size());
        assertEquals(product.getName(), products.get(0).getName());
        assertEquals(product.getCount(), products.get(0).getCount());
        assertEquals(product.getCategoryName(), products.get(0).getCategoryName());
    }

    @Test
    void testUpdateExistingProduct() {
        Product product1 = new Product("TestProduct", 10.5, "TestCategory");
        Product product2 = new Product("TestProduct", 20.0, "UpdatedCategory");

        fileSystemDAO.updateProduct(product1);
        fileSystemDAO.updateProduct(product2);

        List<Product> products = fileSystemDAO.readProduct();
        assertEquals(1, products.size());
        assertEquals(product2.getCount(), products.get(0).getCount());
        assertEquals(product2.getCategoryName(), products.get(0).getCategoryName());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product("TestProduct", 10.5, "TestCategory");
        fileSystemDAO.updateProduct(product);

        assertEquals(1, fileSystemDAO.readProduct().size());

        fileSystemDAO.deleteProduct(product);
        assertTrue(fileSystemDAO.readProduct().isEmpty());
    }

    @Test
    void testSelectAllCategories() {
        Product product1 = new Product("Product1", 1.0, "Category1");
        Product product2 = new Product("Product2", 2.0, "Category2");
        Product product3 = new Product("Product3", 3.0, "Category1"); // Дублирующая категория

        fileSystemDAO.updateProduct(product1);
        fileSystemDAO.updateProduct(product2);
        fileSystemDAO.updateProduct(product3);

        List<String> categories = fileSystemDAO.selectAllCategories();
        assertEquals(2, categories.size());
        assertTrue(categories.contains("Category1"));
        assertTrue(categories.contains("Category2"));
    }

    @Test
    void testUpdateCategoriesAfterDeletion() {
        Product product1 = new Product("Product1", 1.0, "Category1");
        Product product2 = new Product("Product2", 2.0, "Category2");

        fileSystemDAO.updateProduct(product1);
        fileSystemDAO.updateProduct(product2);

        assertEquals(2, fileSystemDAO.selectAllCategories().size());

        fileSystemDAO.deleteProduct(product2);
        List<String> categories = fileSystemDAO.selectAllCategories();
        assertEquals(1, categories.size());
        assertEquals("Category1", categories.get(0));
    }
}