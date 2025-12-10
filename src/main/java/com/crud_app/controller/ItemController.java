package com.crud_app.controller;

import com.crud_app.model.Item;
import com.crud_app.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // Показать ВСЕ записи
    // GET http://localhost:8080/items
    @GetMapping
    public String showAllItems(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "items/list";
    }

    // Показать форму для создания
    // GET http://localhost:8080/items/new
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("item", new Item());
        return "items/form";
    }

    // СОЗДАТЬ новую запись
    // POST http://localhost:8080/items
    @PostMapping
    public String createItem(@ModelAttribute Item item) {
        itemService.saveItem(item);
        return "redirect:/items";
    }

    // Показать форму для РЕДАКТИРОВАНИЯ
    // GET http://localhost:8080/items/edit/123
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Item item = itemService.getItemById(id)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        model.addAttribute("item", item);
        return "items/form";
    }

    // УДАЛИТЬ запись
    // GET http://localhost:8080/items/delete/123
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable UUID id) {
        itemService.deleteItem(id);
        return "redirect:/items";
    }
}