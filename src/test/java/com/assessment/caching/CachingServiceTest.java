package com.assessment.caching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CachingServiceTest {

    private CachingService cachingService;
    private DatabaseService databaseService;

    @BeforeEach
    void setUp() {
        databaseService = new DatabaseService();
        cachingService = new CachingService(3, databaseService); // Max capacity 3
    }

    @Test
    void testAddAndGet() {
        Entity e1 = new Entity("1");
        cachingService.add(e1);

        assertEquals(e1, cachingService.get("1"), "Entity should be retrieved from cache");
    }

    @Test
    void testEvictionPolicy() {
        Entity e1 = new Entity("1");
        Entity e2 = new Entity("2");
        Entity e3 = new Entity("3");
        Entity e4 = new Entity("4");

        cachingService.add(e1);
        cachingService.add(e2);
        cachingService.add(e3);
        cachingService.add(e4); // Evicts e1

        assertNull(cachingService.get("1"), "Entity e1 should be evicted");
        assertNotNull(databaseService.fetch("1"), "Evicted entity should persist in database");
    }

    @Test
    void testRemoveEntity() {
        Entity e1 = new Entity("1");
        cachingService.add(e1);
        cachingService.remove(e1);

        assertNull(cachingService.get("1"), "Entity should be removed from cache and database");
    }

    @Test
    void testRemoveAll() {
        cachingService.add(new Entity("1"));
        cachingService.add(new Entity("2"));
        cachingService.removeAll();

        assertNull(cachingService.get("1"), "Cache should be empty after removeAll");
        assertNull(databaseService.fetch("1"), "Database should be empty after removeAll");
    }

    @Test
    void testClearCache() {
        cachingService.add(new Entity("1"));
        cachingService.clear();

        assertNull(cachingService.get("1"), "Cache should be empty after clear");
        assertNotNull(databaseService.fetch("1"), "Database should still contain the entity");
    }
}
