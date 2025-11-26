package com.softwareascraft.practice.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdGenerator {

    private static final Map<String, Long> idCounters = new HashMap<>();

    private IdGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Generates the next ID for a given entity type
     * @param entityType the type of entity (e.g., "locomotive", "rolling_stock")
     * @return the next available ID
     */
    public static synchronized Long generateId(String entityType) {
        Long currentId = idCounters.getOrDefault(entityType, 0L);
        Long nextId = currentId + 1;
        idCounters.put(entityType, nextId);
        return nextId;
    }

    /**
     * Gets the current maximum ID from a list of entities
     * @param entities list of entities with getId() method
     * @return the maximum ID found, or 0 if list is empty
     */
    public static Long getCurrentMaxId(List<?> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0L;
        }

        long maxId = 0L;
        for (Object entity : entities) {
            try {
                Long id = (Long) entity.getClass().getMethod("getId").invoke(entity);
                if (id != null && id > maxId) {
                    maxId = id;
                }
            } catch (Exception e) {
                // Ignore reflection errors
            }
        }
        return maxId;
    }

    /**
     * Initializes the ID counter based on existing data
     * @param entityType the type of entity
     * @param entities list of existing entities
     */
    public static synchronized void initializeCounter(String entityType, List<?> entities) {
        Long maxId = getCurrentMaxId(entities);
        idCounters.put(entityType, maxId);
    }

    /**
     * Resets the ID counter for a given entity type (for testing purposes)
     * @param entityType the type of entity
     */
    public static synchronized void resetIdCounter(String entityType) {
        idCounters.remove(entityType);
    }
}
