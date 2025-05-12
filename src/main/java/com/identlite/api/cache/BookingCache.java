package com.identlite.api.cache;

import com.identlite.api.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BookingCache {
    private static final Logger logger = LoggerFactory.getLogger(BookingCache.class);
    private static final long TTL_MILLIS = 60000L;

    private final Map<String, CacheEntry> cache = new HashMap<>();

    private static class CacheEntry {
        List<User> data;
        long timestamp;

        CacheEntry(List<User> data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }
    }

    public List<User> getFromCache(String key) {
        if (!cache.containsKey(key)) {
            logger.info("Ключ '{}' не найден в кэше", key);
            return null;
        }

        CacheEntry entry = cache.get(key);
        long age = System.currentTimeMillis() - entry.timestamp;

        if (age > TTL_MILLIS) {
            logger.info("Данные по ключу '{}' устарели ({} мс > {} мс). Удаление...",
                    key,
                    age,
                    TTL_MILLIS);
            cache.remove(key);
            return null;
        }

        logger.info("Данные по ключу '{}' найдены в кэше (возраст: {} мс)", key, age);
        return entry.data;
    }

    public void putInCache(String key, List<User> users) {
        cache.put(key, new CacheEntry(users));
        logger.info("Данные по ключу '{}' сохранены в кэш ({} пользователей)", key, users.size());
    }

    public boolean contains(String key) {
        return getFromCache(key) != null;
    }

    public void cleanCache() {
        cache.clear();
        logger.info("Кэш очищен вручную");
    }
}
