package com.crud_app.service;

import com.crud_app.model.Item;
import com.crud_app.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    public List<Item> getAllItems() {
        return repository.findAll();
    }

    public Page<Item> getAllItemsPaginated(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }

    public Page<Item> searchItems(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.searchByKeyword(keyword, pageable);
    }

    public Optional<Item> getItemById(UUID id) {
        return repository.findById(id);
    }

    public Item saveItem(Item item) {
        return repository.save(item);
    }

    public void deleteItem(UUID id) {
        repository.deleteById(id);
    }

    public Page<Item> findByCreatedAtAfter(String dateFrom, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            // Пробуем разные форматы даты
            LocalDate date = LocalDate.parse(dateFrom);
            LocalDateTime startOfDay = date.atStartOfDay();

            return repository.findByCreatedAtAfter(startOfDay, pageable);
        } catch (Exception e) {
            return repository.findAll(pageable);
        }
    }
}