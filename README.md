## Описание
Приложение работает как лист покупков, в котором можно написать продукт, выбрать категорию продуктов и выбрать количество


## Создание таблиц в базе данных

### Таблица `Categories` (Категории)

```sql
CREATE TABLE Categories (
    id SERIAL PRIMARY KEY,         -- Уникальный идентификатор категории
    name VARCHAR(50) NOT NULL,     -- Название категории
    order_index INT                -- Поле для сортировки категорий
);
```

### Таблица `ProductsList` (Товары)

```sql
CREATE TABLE ProductsList (
    id SERIAL PRIMARY KEY,         -- Уникальный идентификатор товара
    name VARCHAR(50) NOT NULL,     -- Название товара
    count DOUBLE PRECISION NOT NULL, -- Количество товара
    category_id INT REFERENCES Categories(id) -- Ссылка на категорию
);
```

## Заполнение таблицы `Categories` (Категории)

```sql
INSERT INTO Categories (name, order_index) VALUES
('Еда', 1),           -- Категория "Еда" будет первой
('Электроника', 2),  
('Одежда', 3),       
('Книги', 4),         
('Игрушки', 5),       
('Мебель', 6),       
('Спорт', 7),        
('Красота', 8),      
('Автотовары', 9),    
('Другое', 10);       
```


## Дополнительный функционал
База данных - postgresql, так же можно изменить работу программы на использование с *.txt 
для этого в HelloController.java надо изменить код с 
```
public HelloController() {
        // Используем PostgresDAO
        DAO postgresDAO = new PostgresDAO();
        this.productList = new ProductList(postgresDAO);
        // Используем FileSystemDAO вместо PostgresDAO
//        DAO fileSystemDAO = new FileSystemDAO();
//        this.productList = new ProductList(fileSystemDAO);
    }
```
на 
```
    public HelloController() {
        // Используем PostgresDAO
//        DAO postgresDAO = new PostgresDAO();
//        this.productList = new ProductList(postgresDAO);
        // Используем FileSystemDAO вместо PostgresDAO
        DAO fileSystemDAO = new FileSystemDAO();
        this.productList = new ProductList(fileSystemDAO);
    }
```

![image](https://github.com/user-attachments/assets/7d2390fa-2bb9-418d-b03f-894d4adb1d8f)

Так же надо указать *txt файлы в FileSystemDAO, в которых будут находиться категории и продукты
9 и 10 строка кода
```
    private final String productsFilePath = "D:/VSTU/JavaProg/DAO.txt";; // Файл для продуктов
    private final String categoriesFilePath = "D:/VSTU/JavaProg/categories.txt"; // Файл для категорий
```

Файл с категориями - 
[categories.txt](https://github.com/user-attachments/files/20452475/categories.txt)
15436



## Архитектура
![dia](https://github.com/user-attachments/assets/464f0383-5f55-4c4b-b7f5-38f6ef082670)


## Результат
![image](https://github.com/user-attachments/assets/78d92081-f5ed-4265-8d32-74aadcc45eda)

