package com.crud_app.controller;

import com.crud_app.model.Item;
import com.crud_app.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private Item item1;
    private Item item2;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        itemId = UUID.randomUUID();

        item1 = new Item();
        item1.setId(itemId);
        item1.setName("Test Item 1");
        item1.setDescription("Description 1");
        item1.setCreatedAt(LocalDateTime.now().minusDays(2));

        item2 = new Item();
        item2.setId(UUID.randomUUID());
        item2.setName("Test Item 2");
        item2.setDescription("Description 2");
        item2.setCreatedAt(LocalDateTime.now().minusDays(1));
    }

    @Test
    void testShowAllItems() throws Exception {
        Page<Item> page = new PageImpl<>(Arrays.asList(item1, item2));
        when(itemService.getAllItemsPaginated(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(page);

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("items/list"))
                .andExpect(model().attributeExists("itemsPage"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("pageSize", 10))
                .andExpect(model().attribute("totalItems", 2L));

        verify(itemService, times(1))
                .getAllItemsPaginated(0, 10, "createdAt", "desc");
    }

    @Test
    void testShowAllItemsWithSearch() throws Exception {
        Page<Item> page = new PageImpl<>(Arrays.asList(item1));
        when(itemService.searchItems(eq("test"), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/items")
                        .param("keyword", "test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("itemsPage"))
                .andExpect(model().attribute("keyword", "test"));

        verify(itemService, times(1)).searchItems("test", 0, 10);
    }

    @Test
    void testShowAllItemsWithDateFilter() throws Exception {
        Page<Item> page = new PageImpl<>(Arrays.asList(item2));
        when(itemService.findByCreatedAtAfter(eq("2024-12-10"), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/items")
                        .param("dateFrom", "2024-12-10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("itemsPage"))
                .andExpect(model().attribute("dateFrom", "2024-12-10"));

        verify(itemService, times(1)).findByCreatedAtAfter("2024-12-10", 0, 10);
    }

    @Test
    void testShowAllItemsWithSorting() throws Exception {
        Page<Item> page = new PageImpl<>(Arrays.asList(item1, item2));
        when(itemService.getAllItemsPaginated(0, 10, "name", "asc"))
                .thenReturn(page);

        mockMvc.perform(get("/items")
                        .param("sort", "name")
                        .param("dir", "asc"))
                .andExpect(status().isOk());

        verify(itemService, times(1)).getAllItemsPaginated(0, 10, "name", "asc");
    }

    @Test
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/items/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("items/form"))
                .andExpect(model().attributeExists("item"))
                .andExpect(model().attribute("item", hasProperty("id", nullValue())));
    }

    @Test
    void testCreateItem() throws Exception {
        when(itemService.saveItem(any(Item.class))).thenReturn(item1);

        mockMvc.perform(post("/items")
                        .param("name", "New Item")
                        .param("description", "New Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"));

        verify(itemService, times(1)).saveItem(any(Item.class));
    }

    @Test
    void testShowEditForm() throws Exception {
        when(itemService.getItemById(itemId)).thenReturn(Optional.of(item1));

        mockMvc.perform(get("/items/edit/{id}", itemId))
                .andExpect(status().isOk())
                .andExpect(view().name("items/form"))
                .andExpect(model().attributeExists("item"))
                .andExpect(model().attribute("item", hasProperty("name", is("Test Item 1"))));

        verify(itemService, times(1)).getItemById(itemId);
    }

    @Test
    void testShowEditFormNotFound() throws Exception {
        when(itemService.getItemById(itemId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/items/edit/{id}", itemId))
                .andExpect(status().is3xxRedirection()) // Ожидаем редирект
                .andExpect(redirectedUrl("/items"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Запись с ID " + itemId + " не найдена"));

        verify(itemService, times(1)).getItemById(itemId);
    }

    @Test
    void testDeleteItem() throws Exception {
        mockMvc.perform(get("/items/delete/{id}", itemId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"));

        verify(itemService, times(1)).deleteItem(itemId);
    }

    @Test
    void testShowAllItemsWithPagination() throws Exception {
        Page<Item> page = new PageImpl<>(Arrays.asList(item1));
        when(itemService.getAllItemsPaginated(2, 5, "createdAt", "desc"))
                .thenReturn(page);

        mockMvc.perform(get("/items")
                        .param("page", "2")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentPage", 2))
                .andExpect(model().attribute("pageSize", 5));

        verify(itemService, times(1)).getAllItemsPaginated(2, 5, "createdAt", "desc");
    }

    @Test
    void testInvalidSortParameter() throws Exception {
        Page<Item> page = new PageImpl<>(Arrays.asList(item1, item2));
        when(itemService.getAllItemsPaginated(0, 10, "createdAt", "desc"))
                .thenReturn(page);

        mockMvc.perform(get("/items")
                        .param("sort", "invalidField"))
                .andExpect(status().isOk());

        verify(itemService, times(1)).getAllItemsPaginated(0, 10, "createdAt", "desc");
    }
}