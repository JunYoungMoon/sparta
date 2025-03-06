package com.service.redis.service;

import com.service.redis.dto.ItemDto;
import com.service.redis.dto.OrderDto;
import com.service.redis.entity.h2.Item;
import com.service.redis.entity.h2.Order;
import com.service.redis.repository.h2.H2ItemRepository;
import com.service.redis.repository.h2.H2OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ItemService {
    private final CacheManager cacheManager;
    private final H2ItemRepository h2ItemRepository;
    private final H2OrderRepository h2OrderRepository;
    private final ZSetOperations<String, ItemDto> rankOps;
    private final RedisTemplate<String, OrderDto> orderTemplate;
    private final ListOperations<String, OrderDto> orderOps;

    public ItemService(
            H2ItemRepository h2ItemRepository,
            H2OrderRepository h2OrderRepository,
            RedisTemplate<String, ItemDto> itemRedisTemplate,
            RedisTemplate<String, OrderDto> orderTemplate,
            CacheManager cacheManager
    ) {
        this.h2ItemRepository = h2ItemRepository;
        this.h2OrderRepository = h2OrderRepository;
        this.rankOps = itemRedisTemplate.opsForZSet();
        this.cacheManager = cacheManager;
        this.orderTemplate = orderTemplate;
        this.orderOps = this.orderTemplate.opsForList();
    }

    public void purchase(OrderDto dto) {
        Item item = h2ItemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

//        h2OrderRepository.save(Order.builder()
//                .item(item)
//                .count(1)
//                .build());

//        ItemDto dto = ItemDto.fromEntity(item);
        //incrementScore: 기존 member의 점수 증가, 존재하지 않으면 1로 설정 후 추가.
//        rankOps.incrementScore("soldRanks", dto, 1);
        //add: 새로운 member 추가, 존재하면 점수 덮어쓰기.
//        rankOps.add("soldRanks", dto, 1);

        orderOps.rightPush("orderCache::behind", dto);
        rankOps.incrementScore(
                "soldRanks",
                ItemDto.fromEntity(item),
                1
        );
    }

    @Transactional
    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.SECONDS)
    public void insertOrders() {
        boolean exists = Optional.of(orderTemplate.hasKey("orderCache::behind"))
                .orElse(false);
        if (!exists) {
            log.info("no orders in cache");
            return;
        }
        // 적재된 주문을 처리하기 위해 별도로 이름을 변경하기 위해
        orderTemplate.rename("orderCache::behind", "orderCache::now");

        log.info("saving {} orders to db", orderOps.size("orderCache::now"));

        h2OrderRepository.saveAll(orderOps.range("orderCache::now", 0, -1).stream()
                .map(dto -> Order.builder()
                        .itemId(dto.getItemId())
                        .count(dto.getCount())
                        .build())
                .toList());
        orderTemplate.delete("orderCache::now");
    }

    public List<ItemDto> getMostSold() {
        Set<ItemDto> ranks = rankOps.reverseRange("soldRanks", 0, 9);
        if (ranks == null) return Collections.emptyList();
        return ranks.stream().toList();
    }

    // 이 메서드의 결과는 캐싱이 가능하다.
    // cacheNames : 이 메서드로 인해서 만들어질 캐시를 지칭하는 이름이다.
    // 다른메서드에서 이 캐시를 활용할때도 사용하고
    // 또 config에서 어떤 설정을 할것인지도 cacheNames를 본다.
    // key : 캐시 데이터를 구분하기 위해 활용하는 값 아규먼트의 0번째를 키로 활용 할 것이다.
    // 조회된 아이템은 일단 캐시에 둔다.
    // TTL은 줄이면서 TTI를 설정함(CacheConfig)으로서,
    // 자주 조회되지 않는 아이템들은 캐시에서 빠르게 제거하고
    // 자주 조회되는 아이템은 캐시에 유지하도록 설정
    @Cacheable(cacheNames = "itemCache", key = "args[0]")
    public ItemDto readOne(Long id) {
        return h2ItemRepository.findById(id)
                .map(ItemDto::fromEntity)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // 전체 조회는 일반적인 서비스에서 가장 많이 활용된다.
    @Cacheable(cacheNames = "itemAllCache", key = "getMethodName()")
    public List<ItemDto> readAll() {
        return h2ItemRepository.findAll()
                .stream()
                .map(ItemDto::fromEntity)
                .toList();
    }
    
    // 새로 만든 아이템은 다음 검색에서 등장할 수 있게끔 기존의 전체 조회 캐시 삭제
    // 인지도가 높지 않을 가능성이 있으므로 생성시 추가 캐시 생성은 하지 않는다.
    @CacheEvict(cacheNames = "itemAllCache", key = "#result.id")
    public ItemDto create(ItemDto dto) {
        return ItemDto.fromEntity(h2ItemRepository.save(Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build()));
    }

//    @CachePut(cacheNames = "itemCache", key = "args[0]")
//    // 업데이트를 하니깐 이전의 아이템 정보가 남으면 안되기 때문에 itemAllCache로 시작하는 모든 데이터를 제거하겠다는 의미이다.
//    // key를 CachePut과 같이 하나하나 만들었다고 한다면 하나씩 제거 할수도 있고
//    // readAll 같이 키가 한곳에 관리가 된다고 한다면 allEntries = true로 설정할수도 있다.
//    @CacheEvict(cacheNames = "itemAllCache", allEntries = true /*, key = "'readAll'"*/)

    //개별 애너테이션을 사용할수도 있지만 이렇게 묶어서 사용할수도 있다.
    //또 Transactional을 걸어 DB 실패시에 캐시도 롤백 될수 있게 처리한다.
    @Transactional
    @Caching(
            put = {@CachePut(cacheNames = "itemCache", key = "#id")},
            evict = {@CacheEvict(cacheNames = "itemAllCache", allEntries = true)}
    )
    public ItemDto update(Long id, ItemDto dto) {
        Item item = h2ItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        return ItemDto.fromEntity(h2ItemRepository.save(item));
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "itemCache", key = "#id"),         // 개별 캐시 삭제
                    @CacheEvict(cacheNames = "itemAllCache", allEntries = true) // 전체 목록 캐시 삭제
            }
    )
    public void delete(Long id) {
        h2ItemRepository.deleteById(id);

        // cacheManager를 주입 받아 좀더 디테일 하게 설정도 가능하다.
        // 개별 캐시 삭제
//        Cache itemCache = cacheManager.getCache("itemCache");
//        if (itemCache != null) {
//            itemCache.evict(id);
//        }
//
//        // 전체 목록 캐시 삭제
//        Cache itemAllCache = cacheManager.getCache("itemAllCache");
//        if (itemAllCache != null) {
//            itemAllCache.clear();
//        }
    }

    @Cacheable(
            cacheNames = "itemSearchCache",
            key = "{#query, #pageable.pageNumber, #pageable.pageSize}")
    public Page<ItemDto> searchByName(String query, Pageable pageable) {
        return h2ItemRepository.findAllByNameContains(query, pageable)
                .map(ItemDto::fromEntity);
    }
}
