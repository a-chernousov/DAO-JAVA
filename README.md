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

## Архитектура

![dia](https://github.com/user-attachments/assets/464f0383-5f55-4c4b-b7f5-38f6ef082670)


## Результат

![image](https://github.com/user-attachments/assets/78d92081-f5ed-4265-8d32-74aadcc45eda)

