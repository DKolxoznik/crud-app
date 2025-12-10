package com.crud_app.controller;

import com.crud_app.model.Item;
import com.crud_app.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // ГЛАВНАЯ СТРАНИЦА
    // GET http://localhost:8080/items?page=0&size=10&sort=name&dir=asc&keyword=&dateFrom=
    @GetMapping
    public String showAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String dir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String dateFrom,
            Model model) {

        Page<Item> itemsPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            itemsPage = itemService.searchItems(keyword.trim(), page, size);
        } else {
            itemsPage = itemService.getAllItemsPaginated(page, size, sort, dir);
        }

        model.addAttribute("itemsPage", itemsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sort);
        model.addAttribute("sortDir", dir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("dateFrom", dateFrom);

        model.addAttribute("totalPages", itemsPage.getTotalPages());
        model.addAttribute("totalItems", itemsPage.getTotalElements());

        return "items/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("item", new Item());
        return "items/form";
    }

    @PostMapping
    public String createItem(@ModelAttribute Item item) {
        itemService.saveItem(item);
        return "redirect:/items";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Item item = itemService.getItemById(id)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        model.addAttribute("item", item);
        return "items/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable UUID id) {
        itemService.deleteItem(id);
        return "redirect:/items";
    }
}