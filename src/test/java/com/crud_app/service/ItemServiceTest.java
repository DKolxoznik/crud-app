package com.crud_app.service;

import com.crud_app.model.Item;
import com.crud_app.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item item1;
    private Item item2;
    private Item item3;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        itemId = UUID.randomUUID();

        item1 = new Item();
        item1.setId(itemId);
        item1.setName("Item 1");
        item1.setDescription("Description 1");
        item1.setCreatedAt(LocalDateTime.now().minusDays(2));

        item2 = new Item();
        item2.setId(UUID.randomUUID());
        item2.setName("Item 2");
        item2.setDescription("Description 2");
        item2.setCreatedAt(LocalDateTime.now().minusDays(1));

        item3 = new Item();
        item3.setId(UUID.randomUUID());
        item3.setName("Different Item");
        item3.setDescription("Another description");
        item3.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAllItems() {
        List<Item> items = Arrays.asList(item1, item2, item3);
        when(itemRepository.findAll()).thenReturn(items);

        List<Item> result = itemService.getAllItems();

        assertEquals(3, result.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testGetAllItemsPaginated() {
        Page<Item> page = new PageImpl<>(Arrays.asList(item1, item2));
        when(itemRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Item> result = itemService.getAllItemsPaginated(0, 10, "name", "asc");

        assertEquals(2, result.getContent().size());
        verify(itemRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testGetAllItemsPaginatedDescending() {
        Page<Item> page = new PageImpl<>(Arrays.asList(item3, item2));
        when(itemRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Item> result = itemService.getAllItemsPaginated(0, 10, "createdAt", "desc");

        assertEquals(2, result.getContent().size());
        verify(itemRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testSearchItems() {
        Page<Item> page = new PageImpl<>(Arrays.asList(item1, item2));
        when(itemRepository.searchByKeyword(eq("item"), any(Pageable.class))).thenReturn(page);

        Page<Item> result = itemService.searchItems("item", 0, 10);

        assertEquals(2, result.getContent().size());
        verify(itemRepository, times(1)).searchByKeyword(eq("item"), any(Pageable.class));
    }

    @Test
    void testFindByCreatedAtAfter() {
        Page<Item> page = new PageImpl<>(Arrays.asList(item2, item3));
        when(itemRepository.findByCreatedAtAfter(any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);

        Page<Item> result = itemService.findByCreatedAtAfter("2024-12-10", 0, 10);

        assertEquals(2, result.getContent().size());
        verify(itemRepository, times(1)).findByCreatedAtAfter(any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void testFindByCreatedAtAfterInvalidDate() {
        Page<Item> page = new PageImpl<>(Arrays.asList(item1, item2, item3));
        when(itemRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Item> result = itemService.findByCreatedAtAfter("invalid-date", 0, 10);

        assertEquals(3, result.getContent().size());
        verify(itemRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testGetItemByIdFound() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item1));

        Optional<Item> result = itemService.getItemById(itemId);

        assertTrue(result.isPresent());
        assertEquals("Item 1", result.get().getName());
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void testGetItemByIdNotFound() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        Optional<Item> result = itemService.getItemById(itemId);

        assertFalse(result.isPresent());
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void testSaveItem() {
        when(itemRepository.save(item1)).thenReturn(item1);

        Item result = itemService.saveItem(item1);

        assertEquals("Item 1", result.getName());
        verify(itemRepository, times(1)).save(item1);
    }

    @Test
    void testDeleteItem() {
        doNothing().when(itemRepository).deleteById(itemId);

        itemService.deleteItem(itemId);

        verify(itemRepository, times(1)).deleteById(itemId);
    }

    @Test
    void testSaveNewItemSetsTimestamps() {
        Item newItem = new Item();
        newItem.setName("New Item");
        newItem.setDescription("New Description");

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            savedItem.setId(UUID.randomUUID());
            savedItem.setCreatedAt(LocalDateTime.now());
            return savedItem;
        });

        Item result = itemService.saveItem(newItem);

        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
        verify(itemRepository, times(1)).save(newItem);
    }
}