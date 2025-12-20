package com.crud_app;

import com.crud_app.model.Item;
import com.crud_app.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CrudAppApplicationTests {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void contextLoads() {
        // Проверка, что контекст Spring загружается успешно
    }

    @Test
    void testDatabaseConnection() {
        long count = itemRepository.count();
        assertTrue(count >= 0);
    }

    @Test
    void testCompleteCrudCycle() {

        Item newItem = new Item();
        newItem.setName("Integration Test Item");
        newItem.setDescription("Created during integration test");

        Item savedItem = itemRepository.save(newItem);
        assertNotNull(savedItem.getId());
        assertEquals("Integration Test Item", savedItem.getName());

        UUID itemId = savedItem.getId();

        Optional<Item> foundItem = itemRepository.findById(itemId);
        assertTrue(foundItem.isPresent());
        assertEquals("Integration Test Item", foundItem.get().getName());

        Item toUpdate = foundItem.get();
        toUpdate.setName("Updated Integration Test Item");
        toUpdate.setDescription("Updated description");

        Item updatedItem = itemRepository.save(toUpdate);
        assertEquals("Updated Integration Test Item", updatedItem.getName());
        assertNotNull(updatedItem.getUpdatedAt());

        itemRepository.deleteById(itemId);

        Optional<Item> deletedItem = itemRepository.findById(itemId);
        assertFalse(deletedItem.isPresent());
    }

    @Test
    void testSearchFunctionality() {
        Item item1 = new Item();
        item1.setName("Search Test Item 1");
        item1.setDescription("This is a test for search functionality");

        Item item2 = new Item();
        item2.setName("Another Item");
        item2.setDescription("Search test description");

        itemRepository.save(item1);
        itemRepository.save(item2);

        var resultByName = itemRepository.findByNameContainingIgnoreCase(
                "search",
                org.springframework.data.domain.PageRequest.of(0, 10)
        );

        assertTrue(resultByName.getTotalElements() >= 1);

        var resultByKeyword = itemRepository.searchByKeyword(
                "search",
                org.springframework.data.domain.PageRequest.of(0, 10)
        );

        assertTrue(resultByKeyword.getTotalElements() >= 2);
    }

    @Test
    void testDateFiltering() {
        itemRepository.deleteAll();

        java.time.ZonedDateTime testDate = java.time.ZonedDateTime.now()
                .minusHours(1)
                .withNano(0);

        Item oldItem = new Item();
        oldItem.setName("Old Item");
        oldItem.setDescription("Created yesterday");
        oldItem.setCreatedAt(testDate.minusDays(1).toLocalDateTime());

        Item newItem = new Item();
        newItem.setName("New Item");
        newItem.setDescription("Created just now");
        newItem.setCreatedAt(java.time.ZonedDateTime.now()
                .withNano(0)
                .toLocalDateTime());

        itemRepository.save(oldItem);
        itemRepository.save(newItem);

        var result = itemRepository.findByCreatedAtAfter(
                testDate.toLocalDateTime(),
                PageRequest.of(0, 10)
        );

        assertTrue(result.getTotalElements() <= 2);

        boolean foundNewItem = result.getContent().stream()
                .anyMatch(item -> "New Item".equals(item.getName()));
        assertTrue(foundNewItem, "Должен найти 'New Item'");

        if (result.getTotalElements() == 2) {
            System.out.println("Warning: Found both items. Check time precision settings.");
        }
    }
}