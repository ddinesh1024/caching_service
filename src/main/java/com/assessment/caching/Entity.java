package com.assessment.caching;

public class Entity {
    private final String id;

    public Entity(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Entity ID cannot be null or empty.");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Entity{id='" + id + "'}";
    }
}
