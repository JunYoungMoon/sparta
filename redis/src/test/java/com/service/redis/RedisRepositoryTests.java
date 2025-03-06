package com.service.redis;

import com.service.redis.entity.redis.Item;
import com.service.redis.repository.redis.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisRepositoryTests {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void createTest() {
        Item item = Item.builder()
                .name("keyboard")
                .description("Very Expensive Keyboard")
                .price(100000)
                .build();

        itemRepository.save(item);
    }

    @Test
    public void readOneTest() {
        Item item = itemRepository.findById("28e73600-0c5c-4532-9495-6a8b2e79f3ba")
                .orElseThrow();
        System.out.println(item.getDescription());
    }

    @Test
    public void updateTest() {
        Item item = itemRepository.findById("28e73600-0c5c-4532-9495-6a8b2e79f3ba")
                .orElseThrow();

        item.setDescription("On sale!!!");
        Item save = itemRepository.save(item);
        System.out.println(save.getDescription());
    }

    @Test
    public void deleteTest() {
        itemRepository.deleteById("28e73600-0c5c-4532-9495-6a8b2e79f3ba");
    }
}
