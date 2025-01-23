package com.assessment.caching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private final Map<String, Entity> database = new HashMap<>();

    public void persist(Entity entity) {
        database.put(entity.getId(), entity);
        logger.info("Persisted entity to database: {}", entity);
    }

    public Entity fetch(String id) {
        logger.info("Fetching entity from database with ID: {}", id);
        return database.get(id);
    }

    public void delete(Entity entity) {
        database.remove(entity.getId());
        logger.info("Deleted entity from database: {}", entity);
    }

    public void clearAll() {
        database.clear();
        logger.info("Cleared all entities from the database.");
    }
}
