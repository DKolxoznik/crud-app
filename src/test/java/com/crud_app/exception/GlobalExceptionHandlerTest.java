package com.crud_app.exception;

import com.crud_app.controller.ItemController;
import com.crud_app.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ItemController.class})
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    @WithMockUser(roles = "USER")
    void returns404PageWhenItemNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(itemService.getItemByIdOrThrow(id))
                .thenThrow(new ResourceNotFoundException("Запись с ID " + id + " не найдена"));

        mockMvc.perform(get("/items/edit/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("status", 404))
                .andExpect(model().attribute("title", "Запись не найдена"));
    }
}
