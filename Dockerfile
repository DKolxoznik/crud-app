# Этап сборки (build stage)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Копируем файлы для зависимостей (кешируем этот слой)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Этап запуска (runtime stage)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Копируем собранный JAR из этапа сборки
COPY --from=build /app/target/*.jar app.jar

# Создаём папку для базы данных H2 ДО создания пользователя
RUN mkdir -p /app/data && chmod 755 /app/data

# Создаём пользователя для безопасности
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Открываем порт
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]