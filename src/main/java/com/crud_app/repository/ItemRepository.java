package com.crud_app.repository;

import com.crud_app.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    Page<Item> findByCreatedAtAfter(LocalDateTime dateFrom, Pageable pageable);

    Page<Item> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE " +
            "LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Item> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Item> findByCreatedAtGreaterThanEqual(LocalDateTime date, Pageable pageable);
}



