package com.crud_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ГЛАВНАЯ ТОЧКА ВХОДА
@SpringBootApplication
public class CrudAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrudAppApplication.class, args);
        System.out.println("Приложение запущено! Откройте: http://localhost:8080/items");
    }
}