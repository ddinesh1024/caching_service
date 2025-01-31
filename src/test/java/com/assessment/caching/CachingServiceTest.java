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

    @Test
    void testGetEntityFromDatabaseIfNotInCache() {
        Entity entity = new Entity("2", "DatabaseEntity");
        when(databaseService.fetch("2")).thenReturn(entity);

        Entity result = cachingService.get("2");

        assertNotNull(result);
        assertEquals(entity, result);
        verify(databaseService, times(1)).fetch("2"); // Ensure DB fetch is called
    }

    

    @Test
    void testAddNullEntityThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cachingService.add(null));
        assertEquals("Entity or Entity ID cannot be null.", exception.getMessage());
    }

    @Test
    void testGetWithNullIdThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cachingService.get(null));
        assertEquals("ID cannot be null.", exception.getMessage());
    }

    @Test
    void testRemoveNullEntityThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cachingService.remove(null));
        assertEquals("Entity or Entity ID cannot be null.", exception.getMessage());
    }
}
