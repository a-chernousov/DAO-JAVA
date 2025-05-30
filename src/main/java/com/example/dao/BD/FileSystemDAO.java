package com.example.dao.BD;

import com.example.dao.product.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class FileSystemDAO implements DAO {
    protected String productsFilePath = "D:/VSTU/JavaProg/DAO.txt"; // Файл для продуктов
    protected String categoriesFilePath = "D:/VSTU/JavaProg/categories.txt"; // Файл для категорий

    @Override
    public void createTable() {
        // Создаём файлы, если они не существуют
        createFileIfNotExists(productsFilePath);
        createFileIfNotExists(categoriesFilePath);
    }

    private void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Ошибка при создании файла: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Product> readProduct() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(productsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    double count = Double.parseDouble(parts[1]);
                    String categoryName = parts[2];
                    products.add(new Product(name, count, categoryName));
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return products;
    }

    @Override
    public void updateProduct(Product product) {
        List<Product> products = readProduct();
        boolean productExists = false;

        // Ищем продукт в списке и обновляем его количество
        for (Product p : products) {
            if (p.getName().equals(product.getName())) {
                p.setCount(product.getCount());
                p.setCategoryName(product.getCategoryName());
                productExists = true;
                break;
            }
        }

        // Если продукт не найден, добавляем его
        if (!productExists) {
            products.add(product);
        }

        // Записываем обновлённый список обратно в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsFilePath))) {
            for (Product p : products) {
                writer.write(p.getName() + "," + p.getCount() + "," + p.getCategoryName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }

        // Обновляем категории
        updateCategories();
    }

    @Override
    public void deleteProduct(Product product) {
        List<Product> products = readProduct();
        products.removeIf(p -> p.getName().equals(product.getName()));

        // Записываем обновлённый список обратно в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsFilePath))) {
            for (Product p : products) {
                writer.write(p.getName() + "," + p.getCount() + "," + p.getCategoryName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }

        // Обновляем категории
        updateCategories();
    }

    @Override
    public List<String> selectAllCategories() {
        List<String> categories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(categoriesFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                categories.add(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла категорий: " + e.getMessage());
        }
        return categories;
    }

    private void updateCategories() {
        List<Product> products = readProduct();
        List<String> categories = new ArrayList<>();

        // Собираем уникальные категории из списка продуктов
        for (Product product : products) {
            String category = product.getCategoryName();
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }

        // Записываем категории в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(categoriesFilePath))) {
            for (String category : categories) {
                writer.write(category);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи категорий в файл: " + e.getMessage());
        }
    }
}