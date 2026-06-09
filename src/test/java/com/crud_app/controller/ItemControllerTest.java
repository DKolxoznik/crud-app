package com.crud_app.controller;

import com.crud_app.exception.GlobalExceptionHandler;
import com.crud_app.exception.ResourceNotFoundException;
import com.crud_app.model.Item;
import com.crud_app.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
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
    @WithMockUser(roles = "USER")
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
    @WithMockUser(roles = "USER")
    void testShowAllItemsWithSearch() throws Exception {
        Page<Item> page = new PageImpl<>(Arrays.asList(item1));
        when(itemService.searchItems(eq("test"), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/items")
                        .param("keyword", "test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("keyword", "test"));

        verify(itemService, times(1)).searchItems("test", 0, 10);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/items/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("items/form"))
                .andExpect(model().attribute("item", hasProperty("id", nullValue())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateItem() throws Exception {
        when(itemService.saveItem(any(Item.class))).thenReturn(item1);

        mockMvc.perform(post("/items")
                        .with(csrf())
                        .param("name", "New Item")
                        .param("description", "New Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"));

        verify(itemService, times(1)).saveItem(any(Item.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testShowEditForm() throws Exception {
        when(itemService.getItemByIdOrThrow(itemId)).thenReturn(item1);

        mockMvc.perform(get("/items/edit/{id}", itemId))
                .andExpect(status().isOk())
                .andExpect(view().name("items/form"))
                .andExpect(model().attribute("item", hasProperty("name", is("Test Item 1"))));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testShowEditFormNotFound() throws Exception {
        when(itemService.getItemByIdOrThrow(itemId))
                .thenThrow(new ResourceNotFoundException("Запись с ID " + itemId + " не найдена"));

        mockMvc.perform(get("/items/edit/{id}", itemId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("status", 404));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateItem() throws Exception {
        when(itemService.getItemByIdOrThrow(itemId)).thenReturn(item1);
        when(itemService.saveItem(any(Item.class))).thenReturn(item1);

        mockMvc.perform(post("/items/update/{id}", itemId)
                        .with(csrf())
                        .param("name", "Updated Name")
                        .param("description", "Updated Desc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"));

        verify(itemService).saveItem(any(Item.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteItem() throws Exception {
        when(itemService.getItemByIdOrThrow(itemId)).thenReturn(item1);

        mockMvc.perform(get("/items/delete/{id}", itemId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"));

        verify(itemService, times(1)).deleteItem(itemId);
    }

    @Test
    @WithMockUser(roles = "USER")
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
    }
}
