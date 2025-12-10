package com.crud_app.service;

import com.crud_app.model.Item;
import com.crud_app.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Page<Item> getAllItemsPaginated(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return itemRepository.findAll(pageable);
    }

    public Page<Item> searchItems(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public Optional<Item> getItemById(UUID id) {
        return itemRepository.findById(id);
    }

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(UUID id) {
        itemRepository.deleteById(id);
    }
}