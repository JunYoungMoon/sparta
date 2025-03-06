package com.service.redis.repository.redis;

import com.service.redis.entity.redis.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, String> {

}
