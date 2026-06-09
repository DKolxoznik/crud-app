package com.crud_app.config;

import com.crud_app.model.Item;
import com.crud_app.repository.ItemRepository;
import com.crud_app.security.Role;
import com.crud_app.security.UserRepository;
import com.crud_app.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Random;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final UserService userService;

    @Bean
    @Profile("!test")
    public CommandLineRunner initDatabase(ItemRepository repository) {
        return args -> {
            initUsers();
            initItems(repository);
        };
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            userService.createUser("admin", "admin123", Set.of(Role.ROLE_ADMIN));
            userService.createUser("user", "user123", Set.of(Role.ROLE_USER));
            System.out.println("👤 Созданы пользователи: admin/admin123 (ADMIN), user/user123 (USER)");
        }
    }

    private void initItems(ItemRepository repository) {
        long count = repository.count();
        System.out.println("📊 Найдено записей в базе: " + count);

        if (count == 0) {
            System.out.println("🔄 Создаём тестовые данные...");

            String[] names = {
                    "Купить продукты", "Позвонить маме", "Заплатить за квартиру",
                    "Сходить в спортзал", "Прочитать книгу", "Написать код",
                    "Подготовить отчёт", "Убраться в комнате", "Починить компьютер",
                    "Записаться к врачу", "Купить подарок", "Выучить английский"
            };

            String[] descriptions = {
                    "Важное дело на неделе", "Не забыть сделать", "Срочная задача",
                    "Для саморазвития", "Рабочий вопрос", "Личные дела",
                    "Семейные вопросы", "Образование", "Здоровье", "Финансы"
            };

            Random random = new Random();

            for (int i = 1; i <= 25; i++) {
                Item item = Item.builder()
                        .name(names[random.nextInt(names.length)] + " #" + i)
                        .description(descriptions[random.nextInt(descriptions.length)])
                        .build();

                repository.save(item);

                if (random.nextBoolean()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    Item updatedItem = Item.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .description(item.getDescription() + " (обновлено)")
                            .createdAt(item.getCreatedAt())
                            .build();
                    repository.save(updatedItem);
                }
            }

            System.out.println("✅ Добавлено 25 тестовых записей");
            System.out.println("🌐 Приложение доступно: http://localhost:8080/items");
        }
    }
}
