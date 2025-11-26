package com.softwareascraft.practice.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.MaintenanceLog;
import com.softwareascraft.practice.util.IdGenerator;
import com.softwareascraft.practice.util.JsonFileManager;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository for MaintenanceLog entities (ANTI-PATTERN for teaching purposes)
 * - Hard-coded file path
 * - Direct calls to static utility methods
 * - No interface abstraction
 * - Cannot be easily mocked or substituted
 */
public class MaintenanceLogRepository {

    // Hard-coded file name (ANTI-PATTERN)
    private static final String FILE_NAME = "maintenance-logs.json";
    private static final String ENTITY_TYPE = "maintenance_log";

    public MaintenanceLogRepository() {
        // Initialize ID counter from existing data
        List<MaintenanceLog> existing = findAll();
        IdGenerator.initializeCounter(ENTITY_TYPE, existing);
    }

    public MaintenanceLog save(MaintenanceLog log) {
        List<MaintenanceLog> logs = findAll();

        // Generate new ID using static utility (ANTI-PATTERN)
        Long newId = IdGenerator.generateId(ENTITY_TYPE);
        log.setId(newId);

        logs.add(log);

        // Call static utility method directly (ANTI-PATTERN)
        JsonFileManager.writeToFile(FILE_NAME, logs);

        return log;
    }

    public Optional<MaintenanceLog> findById(Long id) {
        List<MaintenanceLog> logs = findAll();
        return logs.stream()
                .filter(log -> log.getId().equals(id))
                .findFirst();
    }

    public List<MaintenanceLog> findAll() {
        // Call static utility method directly (ANTI-PATTERN)
        return JsonFileManager.readFromFile(FILE_NAME, new TypeReference<List<MaintenanceLog>>() {});
    }

    public MaintenanceLog update(Long id, MaintenanceLog updatedLog) {
        List<MaintenanceLog> logs = findAll();

        boolean found = false;
        for (int i = 0; i < logs.size(); i++) {
            if (logs.get(i).getId().equals(id)) {
                updatedLog.setId(id);
                logs.set(i, updatedLog);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ResourceNotFoundException("MaintenanceLog", id);
        }

        // Call static utility method directly (ANTI-PATTERN)
        JsonFileManager.writeToFile(FILE_NAME, logs);

        return updatedLog;
    }

    public void deleteById(Long id) {
        List<MaintenanceLog> logs = findAll();

        boolean removed = logs.removeIf(log -> log.getId().equals(id));

        if (!removed) {
            throw new ResourceNotFoundException("MaintenanceLog", id);
        }

        // Call static utility method directly (ANTI-PATTERN)
        JsonFileManager.writeToFile(FILE_NAME, logs);
    }

    public List<MaintenanceLog> findByInventoryItemId(Long itemId) {
        return findAll().stream()
                .filter(log -> log.getInventoryItemId().equals(itemId))
                .collect(Collectors.toList());
    }

    public List<MaintenanceLog> findByInventoryItemIdOrderByMaintenanceDateDesc(Long itemId) {
        return findAll().stream()
                .filter(log -> log.getInventoryItemId().equals(itemId))
                .sorted(Comparator.comparing(MaintenanceLog::getMaintenanceDate).reversed())
                .collect(Collectors.toList());
    }

    public List<MaintenanceLog> findByMaintenanceDateBetween(LocalDate start, LocalDate end) {
        return findAll().stream()
                .filter(log -> !log.getMaintenanceDate().isBefore(start) &&
                              !log.getMaintenanceDate().isAfter(end))
                .collect(Collectors.toList());
    }
}
