# 📋 CRUD Приложение для управления записями

## 🎯 Обзор проекта

![Simulation Preview](src\main\resources\static\Crud_app_preview.gif)

**CRUD приложение** - это полнофункциональное веб-приложение на Spring Boot для управления записями с возможностью создания, чтения, обновления и удаления данных. Приложение включает пагинацию, поиск, фильтрацию и сортировку с современным веб-интерфейсом.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen)
![Build](https://img.shields.io/badge/build-Maven-blue)


## 📋 Содержание

- [✨ Особенности](#-особенности)
- [🏗️ Архитектура](#️-архитектура)
- [🚀 Быстрый старт](#-быстрый-старт)
- [📦 Зависимости](#-зависимости)
- [🗂️ Структура проекта](#️-структура-проекта)
- [⚙️ Конфигурация](#️-конфигурация)
- [🧪 Тестирование](#-тестирование)
- [🐳 Docker](#-docker)
- [📊 База данных](#-база-данных)
- [🔗 API Эндпоинты](#-api-эндпоинты)


## ✨ Особенности

### ✅ Основной функционал
- **Полный CRUD**: Создание, чтение, обновление, удаление записей
- **Валидация данных**: Проверка вводимых данных на стороне сервера
- **Автоматические метки времени**: Автоматическое отслеживание времени создания и обновления

### 🔍 Расширенные возможности
- **Интеллектуальный поиск**: Поиск по названию и описанию (без учета регистра)
- **Фильтрация по дате**: Поиск записей созданных после определенной даты
- **Гибкая сортировка**: Сортировка по всем полям в обоих направлениях
- **Адаптивная пагинация**: Настраиваемое количество записей на странице (5, 10, 20, 50)

### 🎨 Пользовательский интерфейс
- **Современный дизайн**: Bootstrap 5 для чистого и адаптивного интерфейса
- **Интуитивная навигация**: Простая и понятная структура страниц
- **Информативные сообщения**: Уведомления об успешных операциях и ошибках
- **Визуальные подсказки**: Подсветка обновленных записей

### 🔧 Технические возможности
- **Автоматическое тестирование**: Полный набор юнит и интеграционных тестов
- **Контейнеризация**: Готовность к развертыванию через Docker
- **Несколько профилей**: Раздельные конфигурации для разработки, тестирования и продакшена
- **Готовая база данных**: Автоматическое создание тестовых данных при первом запуске

## 🏗️ Архитектура

### Паттерн MVC
Приложение построено по классической архитектуре MVC (Model-View-Controller):

- **Model**: Сущность `Item` с полями и бизнес-логикой
- **View**: Thymeleaf шаблоны с Bootstrap 5
- **Controller**: `ItemController` для обработки HTTP запросов

### Сущность Item
```java
@Entity
@Table(name = "items")
public class Item {
    private UUID id;                    // Уникальный идентификатор
    private String name;                // Название (3-50 символов)
    private String description;         // Описание (до 255 символов)
    private LocalDateTime createdAt;    // Дата создания (автоматически)
    private LocalDateTime updatedAt;    // Дата обновления (автоматически)
}
```

## 🚀 Быстрый старт

### Предварительные требования

- **Java 21** или выше
- **Maven 3.6+**
- **Git** (опционально, для клонирования)

### Установка и запуск

#### Способ 1: Через Maven Wrapper

```bash
# 1. Клонировать репозиторий (если есть Git)
git clone https://github.com/DKolxoznik/crud-app
cd crud-app

# 2. Собрать проект
mvnw.cmd clean package

# 3. Запустить приложение
mvnw.cmd spring-boot:run

# Или запустить собранный JAR файл
java -jar target/crud-app-0.0.1-SNAPSHOT.jar
```

#### Способ 2: Через IntelliJ IDEA

1. Откройте проект в IntelliJ IDEA
2. Дождитесь индексации и загрузки зависимостей
3. Найдите класс `CrudAppApplication`
4. Нажмите правой кнопкой → `Run CrudAppApplication`

### Проверка работоспособности

После успешного запуска откройте браузер и перейдите по адресам:

- 🌐 **Главная страница**: [http://localhost:8080/items](http://localhost:8080/items)
- 🗄️ **Консоль H2**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- ➕ **Создание записи**: [http://localhost:8080/items/new](http://localhost:8080/items/new)

**Данные для входа в H2 Console:**
- URL: `jdbc:h2:file:./data/cruddb`
- Username: `sa`
- Password: (оставить пустым)

## 📦 Зависимости

### Основные зависимости (pom.xml)

```xml
<dependencies>
    
    <!-- Основные зависимости -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Базы данных -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
    </dependency>
    
    <!-- Утилиты -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <!-- Тестирование -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 🗂️ Структура проекта

```
crud-app/
├── .mvn/                          # Maven Wrapper файлы
├── data/                          # Директория для данных H2 (автосоздается)
├── docker-data/                   # Данные Docker томов
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/crud_app/
│   │   │       ├── config/               # Конфигурационные классы
│   │   │       │   └── DataInitializer.java
│   │   │       ├── controller/           # Контроллеры
│   │   │       │   └── ItemController.java
│   │   │       ├── model/                # Модели/сущности
│   │   │       │   └── Item.java
│   │   │       ├── repository/           # Репозитории
│   │   │       │   └── ItemRepository.java
│   │   │       ├── service/              # Сервисный слой
│   │   │       │   └── ItemService.java
│   │   │       └── CrudAppApplication.java  # Главный класс приложения
│   │   └── resources/
│   │       ├── static/                   # Статические файлы (CSS, JS, изображения)
│   │       ├── templates/                # Thymeleaf шаблоны
│   │       │   └── items/
│   │       │       ├── form.html         # Форма создания/редактирования
│   │       │       └── list.html         # Список всех записей
│   │       ├── application.properties    # Основная конфигурация
│   │       └── application-docker.properties # Конфигурация для Docker
│   └── test/
│       ├── java/
│       │   └── com/crud_app/
│       │       ├── controller/           # Тесты контроллеров
│       │       │   └── ItemControllerTest.java
│       │       ├── model/                # Тесты моделей
│       │       │   └── ItemTest.java
│       │       ├── repository/           # Тесты репозиториев
│       │       │   └── ItemRepositoryTest.java
│       │       └── service/              # Тесты сервисов
│       │           └── CrudAppApplicationTests.java
│       └── resources/
│           └── application-test.properties # Конфигурация для тестов
├── docker-build.bat                # Скрипт сборки Docker образа (Windows)
├── docker-compose.yml              # Конфигурация Docker Compose
├── docker-run.bat                  # Скрипт запуска Docker контейнера (Windows)
├── Dockerfile                      # Конфигурация Docker
├── mvnw.cmd                        # Maven Wrapper для Windows
└── pom.xml                         # Конфигурация Maven
```

## ⚙️ Конфигурация

### Профили приложения

#### 1. **Профиль по умолчанию** (`application.properties`)
```properties
# ============ H2 ============

spring.datasource.url=jdbc:h2:file:./data/cruddb;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.trace=false
spring.h2.console.settings.web-allow-others=false

# ============ App ============

server.port=8080

spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML
spring.thymeleaf.encoding=UTF-8

app.page-size.default=10
app.page-size.options=5,10,20,50

spring.mvc.format.date=yyyy-MM-dd
spring.mvc.format.date-time=yyyy-MM-dd HH:mm:ss
```

#### 2. **Тестовый профиль** (`application-test.properties`)
```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=false

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false

spring.jpa.defer-datasource-initialization=true
```

#### 3. **Docker профиль** (`application-docker.properties`)
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:h2:file:/app/data/cruddb;DB_CLOSE_ON_EXIT=FALSE}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:sa}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}

spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:update}
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

spring.h2.console.enabled=${SPRING_H2_CONSOLE_ENABLED:false}
spring.h2.console.path=/h2-console

server.port=8080

spring.thymeleaf.cache=true
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

spring.main.banner-mode=off

logging.level.root=INFO
logging.level.com.crud_app=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

## 🧪 Тестирование

### Запуск тестов

```bash
# Все тесты
mvnw.cmd test

# Конкретный тестовый класс
mvnw.cmd test -Dtest=ItemControllerTest
```

### Виды тестов

#### 1. **Юнит-тесты моделей** (`ItemTest.java`)
- Тестирование бизнес-логики сущности Item
- Проверка валидации данных
- Тестирование методов форматирования

#### 2. **Тесты репозитория** (`ItemRepositoryTest.java`)

- Тестирование методов репозитория
- Использование тестовой БД H2 в памяти


#### 3. **Тесты контроллера** (`ItemControllerTest.java`)

- Тестирование HTTP эндпоинтов
- Мокирование сервисного слоя
- Проверка возвращаемых представлений


#### 4. **Интеграционные тесты** (`CrudAppApplicationTests.java`)

- Полноценное тестирование приложения
- Проверка загрузки контекста


## 🐳 Docker

### Сборка и запуск

#### Способ 1: Через скрипты (Windows)

```bash
# Сборка Docker образа
docker-build.bat

# Запуск контейнера
docker-run.bat

# Или использовать Docker Compose
docker-compose up --build
```

#### Способ 2: Через команды

```bash
# Сборка образа
docker build -t crud-app .

# Запуск контейнера
docker run -p 8080:8080 -v ./docker-data:/app/data crud-app

# Или через Docker Compose
docker-compose up
```

### Docker Compose конфигурация

```yaml
version: '3.8'

services:
  crud-app:
    build: .
    container_name: crud-app
    environment:
      SPRING_DATASOURCE_URL: jdbc:h2:file:/app/data/cruddb;DB_CLOSE_ON_EXIT=FALSE
      SPRING_DATASOURCE_USERNAME: sa
      SPRING_DATASOURCE_PASSWORD:
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SPRING_H2_CONSOLE_ENABLED: false

      SPRING_PROFILES_ACTIVE: docker
    ports:
      - "8080:8080"
    volumes:
      - ./docker-data:/app/data
    restart: unless-stopped
```

### Dockerfile

```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
VOLUME /app/data
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 📊 База данных

### H2 Database
- **Тип**: Встроенная файловая база данных
- **Сохранение**: Данные сохраняются между запусками
- **Автоматическая миграция**: DDL операции выполняются автоматически
- **Тестовые данные**: 25 записей создаются при первом запуске

### Схема базы данных
```sql
CREATE TABLE items (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
```


## 🔗 API Эндпоинты

### Веб-интерфейс

| Метод | URL | Описание | Параметры |
|-------|-----|----------|-----------|
| `GET` | `/items` | Список всех записей | `page`, `size`, `sort`, `dir`, `keyword`, `dateFrom` |
| `GET` | `/items/new` | Форма создания записи | - |
| `POST` | `/items` | Создание новой записи | `name`, `description` |
| `GET` | `/items/edit/{id}` | Форма редактирования записи | `id` (UUID) |
| `POST` | `/items/update/{id}` | Обновление записи | `id` (UUID), `name`, `description` |
| `GET` | `/items/delete/{id}` | Удаление записи | `id` (UUID) |

### Параметры запросов

#### Параметры пагинации
```bash
# Базовый запрос с пагинацией
GET /items?page=0&size=10

# Страница 2, 20 записей на странице
GET /items?page=1&size=20
```

#### Параметры сортировки
```bash
# Сортировка по названию (по возрастанию)
GET /items?sort=name&dir=asc

# Сортировка по дате создания (по убыванию)
GET /items?sort=createdAt&dir=desc
```

#### Параметры поиска
```bash
# Поиск по ключевому слову
GET /items?keyword=продукты

# Фильтрация по дате создания
GET /items?dateFrom=2024-01-01
```

### Примеры запросов

```bash
# Все записи, отсортированные по дате создания (новые сначала)
GET /items?page=0&size=10&sort=createdAt&dir=desc

# Поиск записей со словом "тест" на второй странице
GET /items?keyword=тест&page=1&size=5

# Записи созданные после 1 января 2024, отсортированные по названию
GET /items?dateFrom=2024-01-01&sort=name&dir=asc&page=0&size=20
```