package com.identlite.api.cache;

import com.identlite.api.model.User;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BookingCache {
    private static final Logger logger = LoggerFactory.getLogger(BookingCache.class);
    private static final long TTL_MILLIS = 60000L;
    private static final int MAX_SIZE = 5;

    private final Map<String, CacheEntry> cache = new LinkedHashMap<String, CacheEntry>(16,
            0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> eldest) {
            boolean shouldRemove = size() > MAX_SIZE;
            if (shouldRemove) {
                logger.info("Кэш переполнен. Удаляется самая старая запись с ключом '{}'",
                        eldest.getKey());
            }
            return shouldRemove;
        }
    };

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
            return Collections.emptyList();
        }

        CacheEntry entry = cache.get(key);
        long age = System.currentTimeMillis() - entry.timestamp;

        if (age > TTL_MILLIS) {
            logger.info("Данные по ключу '{}' устарели ({} мс > {} мс). Удаление...",
                    key,
                    age,
                    TTL_MILLIS);
            cache.remove(key);
            return Collections.emptyList();
        }

        logger.info("Данные по ключу '{}' найдены в кэше (возраст: {} мс)", key, age);
        return entry.data;
    }

    public void putInCache(String key, List<User> users) {
        cache.put(key, new CacheEntry(users));
        logger.info("Данные по ключу '{}' сохранены в кэш ({} пользователей)", key, users.size());
    }

    public boolean contains(String key) {
        if (!cache.containsKey(key)) {
            return false;
        }

        CacheEntry entry = cache.get(key);
        long age = System.currentTimeMillis() - entry.timestamp;
        if (age > TTL_MILLIS) {
            cache.remove(key);
            return false;
        }
        return true;
    }

    public void cleanCache() {
        cache.clear();
        logger.info("Кэш очищен вручную");
    }
}
