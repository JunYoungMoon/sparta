package com.service.redis.repository.h2;

import com.service.redis.entity.h2.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface H2ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByNameContains(String name, Pageable pageable);
}
