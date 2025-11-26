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

public class MaintenanceLogRepository {

    private static final String FILE_NAME = "maintenance-logs.json";
    private static final String ENTITY_TYPE = "maintenance_log";

    public MaintenanceLogRepository() {
        List<MaintenanceLog> existing = findAll();
        IdGenerator.initializeCounter(ENTITY_TYPE, existing);
    }

    public MaintenanceLog save(MaintenanceLog log) {
        List<MaintenanceLog> logs = findAll();

        Long newId = IdGenerator.generateId(ENTITY_TYPE);
        log.setId(newId);

        logs.add(log);

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

        JsonFileManager.writeToFile(FILE_NAME, logs);

        return updatedLog;
    }

    public void deleteById(Long id) {
        List<MaintenanceLog> logs = findAll();

        boolean removed = logs.removeIf(log -> log.getId().equals(id));

        if (!removed) {
            throw new ResourceNotFoundException("MaintenanceLog", id);
        }

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
