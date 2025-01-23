package com.assessment.caching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class CachingService {

    private static final Logger logger = LoggerFactory.getLogger(CachingService.class);

    private final int maxCapacity;
    private final Map<String, Entity> cache;
    private final DatabaseService databaseService;

    public CachingService(int maxCapacity, DatabaseService databaseService) {
        this.maxCapacity = maxCapacity;
        this.databaseService = databaseService;

        this.cache = new LinkedHashMap<>(maxCapacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Entity> eldest) {
                if (size() > maxCapacity) {
                    logger.info("Evicting entity: {}", eldest.getValue());
                    databaseService.persist(eldest.getValue());
                    return true;
                }
                return false;
            }
        };

        logger.info("CachingService initialized with max capacity of {}", maxCapacity);
    }

    public void add(Entity entity) {
        if (entity == null || entity.getId() == null) {
            logger.error("Cannot add null entity or entity with null ID to the cache.");
            throw new IllegalArgumentException("Entity or Entity ID cannot be null.");
        }
        cache.put(entity.getId(), entity);
        logger.info("Entity added to cache: {}", entity);
    }

    public Entity get(String id) {
        if (id == null) {
            logger.error("Cannot fetch entity with null ID.");
            throw new IllegalArgumentException("ID cannot be null.");
        }
        if (cache.containsKey(id)) {
            logger.info("Entity fetched from cache: {}", id);
            return cache.get(id);
        }

        logger.info("Entity not found in cache. Fetching from database: {}", id);
        Entity entity = databaseService.fetch(id);
        if (entity != null) {
            add(entity); // Add it back to the cache
        }
        return entity;
    }

    public void remove(Entity entity) {
        if (entity == null || entity.getId() == null) {
            logger.error("Cannot remove null entity or entity with null ID from the cache.");
            throw new IllegalArgumentException("Entity or Entity ID cannot be null.");
        }
        cache.remove(entity.getId());
        databaseService.delete(entity);
        logger.info("Entity removed from cache and database: {}", entity);
    }

    public void removeAll() {
        cache.clear();
        databaseService.clearAll();
        logger.info("Cache and database cleared.");
    }

    public void clear() {
        cache.clear();
        logger.info("Cache cleared.");
    }
}
