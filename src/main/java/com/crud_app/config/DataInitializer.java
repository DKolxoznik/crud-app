package com.crud_app.config;

import com.crud_app.model.Item;
import com.crud_app.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    @Profile("!test")
    public CommandLineRunner initDatabase(ItemRepository repository) {
        return args -> {
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
                    Item item = new Item();
                    item.setName(names[random.nextInt(names.length)] + " #" + i);
                    item.setDescription(descriptions[random.nextInt(descriptions.length)]);

                    repository.save(item);

                    if (random.nextBoolean()) {
                        try {
                            Thread.sleep(1); // Минимальная задержка
                        } catch (InterruptedException e) {
                        }
                        item.setDescription(item.getDescription() + " (обновлено)");
                        repository.save(item);
                    }
                }

                System.out.println("✅ Добавлено 25 тестовых записей");
                System.out.println("🌐 Приложение доступно: http://localhost:8080/items");
                System.out.println("🗄️ Консоль базы данных: http://localhost:8080/h2-console");
            }
        };
    }
}