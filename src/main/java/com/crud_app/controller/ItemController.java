package com.crud_app.controller;

import com.crud_app.model.Item;
import com.crud_app.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public String showAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String dir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String dateFrom,
            Model model) {

        List<String> validSortFields = Arrays.asList("name", "description", "createdAt", "updatedAt");
        if (!validSortFields.contains(sort)) {
            sort = "createdAt";
        }

        if (!dir.equalsIgnoreCase("asc") && !dir.equalsIgnoreCase("desc")) {
            dir = "desc";
        }

        Page<Item> itemsPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            itemsPage = itemService.searchItems(keyword.trim(), page, size);
        } else if (dateFrom != null && !dateFrom.trim().isEmpty()) {
            itemsPage = itemService.findByCreatedAtAfter(dateFrom.trim(), page, size);
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
    public String createItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        itemService.saveItem(item);
        redirectAttributes.addFlashAttribute("success", "Запись успешно создана");
        return "redirect:/items";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Item item = itemService.getItemByIdOrThrow(id);
        model.addAttribute("item", item);
        return "items/form";
    }

    @PostMapping("/update/{id}")
    public String updateItem(@PathVariable UUID id,
                             @ModelAttribute Item item,
                             RedirectAttributes redirectAttributes) {
        Item existing = itemService.getItemByIdOrThrow(id);
        existing.setName(item.getName());
        existing.setDescription(item.getDescription());
        itemService.saveItem(existing);
        redirectAttributes.addFlashAttribute("success", "Запись успешно обновлена");
        return "redirect:/items";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteItem(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        itemService.getItemByIdOrThrow(id);
        itemService.deleteItem(id);
        redirectAttributes.addFlashAttribute("success", "Запись удалена");
        return "redirect:/items";
    }
}
