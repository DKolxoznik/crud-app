package com.crud_app.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testItemCreation() {
        UUID id = UUID.randomUUID();
        String name = "Test Item";
        String description = "Test Description";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusHours(1);

        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setCreatedAt(createdAt);
        item.setUpdatedAt(updatedAt);

        assertEquals(id, item.getId());
        assertEquals(name, item.getName());
        assertEquals(description, item.getDescription());
        assertEquals(createdAt, item.getCreatedAt());
        assertEquals(updatedAt, item.getUpdatedAt());
    }

    @Test
    void testFormattedCreatedAt() {
        Item item = new Item();
        LocalDateTime dateTime = LocalDateTime.of(2024, 12, 12, 14, 30, 45);
        item.setCreatedAt(dateTime);

        String formatted = item.getFormattedCreatedAt();

        assertEquals("2024-12-12 | 14:30:45", formatted);
    }

    @Test
    void testFormattedUpdatedAt() {
        Item item = new Item();
        LocalDateTime dateTime = LocalDateTime.of(2024, 12, 12, 15, 45, 30);
        item.setUpdatedAt(dateTime);

        String formatted = item.getFormattedUpdatedAt();

        assertEquals("2024-12-12 | 15:45:30", formatted);
    }

    @Test
    void testIsUpdatedWhenUpdated() {
        Item item = new Item();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = createdAt.plusHours(1);
        item.setCreatedAt(createdAt);
        item.setUpdatedAt(updatedAt);

        assertTrue(item.isUpdated());
    }

    @Test
    void testIsUpdatedWhenNotUpdated() {
        Item item = new Item();
        LocalDateTime createdAt = LocalDateTime.now();
        item.setCreatedAt(createdAt);
        item.setUpdatedAt(null);

        assertFalse(item.isUpdated());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        Item item1 = new Item();
        item1.setId(id);
        item1.setName("Item 1");

        Item item2 = new Item();
        item2.setId(id);
        item2.setName("Item 2");

        Item item3 = new Item();
        item3.setId(UUID.randomUUID());
        item3.setName("Item 1");

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
        assertEquals(item1.hashCode(), item2.hashCode());
        assertNotEquals(item1.hashCode(), item3.hashCode());
    }

    @Test
    void testToString() {
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Item item = new Item();
        item.setId(id);
        item.setName("Test Item");

        String toString = item.toString();

        assertTrue(toString.contains("Test Item"));
        assertTrue(toString.contains(id.toString()));
    }
}