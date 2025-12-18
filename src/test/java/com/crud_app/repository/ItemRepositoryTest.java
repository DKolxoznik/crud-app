package com.crud_app.repository;

import com.crud_app.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    private Item testItem1;
    private Item testItem2;
    private Item testItem3;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();

        now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        testItem1 = new Item();
        testItem1.setName("Test Item 1");
        testItem1.setDescription("Description for item 1");
        testItem1.setCreatedAt(now.minusDays(2));

        testItem2 = new Item();
        testItem2.setName("Test Item 2");
        testItem2.setDescription("Another description");
        testItem2.setCreatedAt(now.minusDays(1).minusHours(6)); // 1.5 дня назад

        testItem3 = new Item();
        testItem3.setName("Different Item");
        testItem3.setDescription("Third description");
        testItem3.setCreatedAt(now.minusHours(1)); // 1 час назад

        itemRepository.saveAll(List.of(testItem1, testItem2, testItem3));

        // Очищаем контекст, чтобы получить свежие данные из базы
        itemRepository.flush();
    }

    @Test
    void testFindAll() {
        List<Item> items = itemRepository.findAll();

        assertEquals(3, items.size());
    }

    @Test
    void testFindById() {
        Optional<Item> foundItem = itemRepository.findById(testItem1.getId());

        assertTrue(foundItem.isPresent());
        assertEquals(testItem1.getName(), foundItem.get().getName());
    }

    @Test
    void testSaveItem() {
        Item newItem = new Item();
        newItem.setName("New Test Item");
        newItem.setDescription("New Description");

        Item savedItem = itemRepository.save(newItem);

        assertNotNull(savedItem.getId());
        assertEquals("New Test Item", savedItem.getName());
        assertEquals(4, itemRepository.count());
    }

    @Test
    void testDeleteById() {
        itemRepository.deleteById(testItem1.getId());

        assertEquals(2, itemRepository.count());
        assertFalse(itemRepository.existsById(testItem1.getId()));
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Item> result = itemRepository.findByNameContainingIgnoreCase("test", pageable);

        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
                .allMatch(item -> item.getName().toLowerCase().contains("test")));
    }

    @Test
    void testSearchByKeyword() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Item> resultByName = itemRepository.searchByKeyword("item", pageable);

        Page<Item> resultByDescription = itemRepository.searchByKeyword("description", pageable);

        assertEquals(3, resultByName.getTotalElements());
        assertEquals(3, resultByDescription.getTotalElements());
    }

    @Test
    void testFindByCreatedAtGreaterThanEqual() {
        LocalDateTime filterDate = now.minusDays(2);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Item> result = itemRepository.findByCreatedAtGreaterThanEqual(filterDate, pageable);

        assertEquals(3, result.getTotalElements());
    }

    @Test
    void testPagination() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("name").ascending());

        Page<Item> page = itemRepository.findAll(pageable);

        assertEquals(2, page.getContent().size());
        assertEquals(3, page.getTotalElements());
        assertEquals(2, page.getTotalPages());

        List<Item> items = page.getContent();
        assertTrue(items.get(0).getName().compareTo(items.get(1).getName()) < 0);
    }

    @Test
    void testUpdateItem() {
        String newName = "Updated Name";
        testItem1.setName(newName);

        Item updatedItem = itemRepository.save(testItem1);

        assertEquals(newName, updatedItem.getName());
        assertNotNull(updatedItem.getUpdatedAt());
    }
}