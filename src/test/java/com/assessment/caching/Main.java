package com.assessment.caching;

public class Main {
    public static void main(String[] args) {
        // Initialize DatabaseService
        DatabaseService databaseService = new DatabaseService();

        // Initialize CachingService with max capacity of 3
        CachingService cachingService = new CachingService(3, databaseService);

        // Add entities to the cache
        cachingService.add(new Entity("1"));
        cachingService.add(new Entity("2"));
        cachingService.add(new Entity("3"));

        // Access entities
        System.out.println("Getting entity with ID 1: " + cachingService.get("1")); // From cache
        System.out.println("Getting entity with ID 2: " + cachingService.get("2")); // From cache

        // Add another entity to cause eviction
        cachingService.add(new Entity("4")); // Evicts "3"

        // Verify eviction
        System.out.println("Getting entity with ID 3 (should fetch from DB): " + cachingService.get("3"));

        // Clear the cache
        cachingService.clear();
        System.out.println("Cache cleared!");

        // Access after clearing
        System.out.println("Getting entity with ID 1 after clearing cache: " + cachingService.get("1")); 
    }
}
