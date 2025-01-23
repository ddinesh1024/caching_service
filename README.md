# Caching Service Project

## Overview
This project is a simple **Caching Service** built using Java. It provides an efficient way to manage frequently accessed data in memory while ensuring older data is removed automatically when the cache becomes full. The system also integrates with a mock database for persisting data that has been evicted from the cache.

---

## Features
- **LRU (Least Recently Used) Eviction**:
  - Automatically removes the least-used data when the cache is full.
- **Configurable Cache Size**:
  - Define the maximum number of items the cache can hold.
- **APIs for Managing Data**:
  - Add, retrieve, remove, and clear cached data.
- **Database Integration**:
  - Simulated database for saving and retrieving evicted data.
- **Structured Logging**:
  - Logs all operations for easy debugging.

---

## How It Works
1. **Adding Data**:
   - Data is added to the cache using the `add` method. If the cache is full, the least recently used item is evicted and saved to the database.
2. **Retrieving Data**:
   - Data is retrieved from the cache if it exists. If not, it is fetched from the database (if available) and added back to the cache.
3. **Eviction Policy**:
   - When the cache is full, the oldest unused data is removed automatically.
4. **Clearing Data**:
   - You can clear all data from the cache or remove specific items.
---

## Quick Setup

### Prerequisites
- **Java 11** or higher
- **Maven 3.6** or higher

### Steps
1. Clone the repository:
   ```bash
   git clone 
   cd caching-service
This Code implements a caching service using java
