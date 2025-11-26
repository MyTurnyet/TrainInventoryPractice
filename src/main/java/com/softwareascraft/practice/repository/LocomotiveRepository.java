package com.softwareascraft.practice.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.util.IdGenerator;
import com.softwareascraft.practice.util.JsonFileManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository for Locomotive entities (ANTI-PATTERN for teaching purposes)
 * - Hard-coded file path
 * - Direct calls to static utility methods
 * - No interface abstraction
 * - Cannot be easily mocked or substituted
 */
public class LocomotiveRepository {

    // Hard-coded file name (ANTI-PATTERN)
    private static final String FILE_NAME = "locomotives.json";
    private static final String ENTITY_TYPE = "locomotive";

    public LocomotiveRepository() {
        // Initialize ID counter from existing data
        List<Locomotive> existing = findAll();
        IdGenerator.initializeCounter(ENTITY_TYPE, existing);
    }

    public Locomotive save(Locomotive locomotive) {
        List<Locomotive> locomotives = findAll();

        // Generate new ID using static utility (ANTI-PATTERN)
        Long newId = IdGenerator.generateId(ENTITY_TYPE);
        locomotive.setId(newId);

        locomotives.add(locomotive);

        // Call static utility method directly (ANTI-PATTERN)
        JsonFileManager.writeToFile(FILE_NAME, locomotives);

        return locomotive;
    }

    public Optional<Locomotive> findById(Long id) {
        List<Locomotive> locomotives = findAll();
        return locomotives.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst();
    }

    public List<Locomotive> findAll() {
        // Call static utility method directly (ANTI-PATTERN)
        return JsonFileManager.readFromFile(FILE_NAME, new TypeReference<List<Locomotive>>() {});
    }

    public Locomotive update(Long id, Locomotive updatedLocomotive) {
        List<Locomotive> locomotives = findAll();

        boolean found = false;
        for (int i = 0; i < locomotives.size(); i++) {
            if (locomotives.get(i).getId().equals(id)) {
                updatedLocomotive.setId(id);
                locomotives.set(i, updatedLocomotive);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ResourceNotFoundException("Locomotive", id);
        }

        // Call static utility method directly (ANTI-PATTERN)
        JsonFileManager.writeToFile(FILE_NAME, locomotives);

        return updatedLocomotive;
    }

    public void deleteById(Long id) {
        List<Locomotive> locomotives = findAll();

        boolean removed = locomotives.removeIf(l -> l.getId().equals(id));

        if (!removed) {
            throw new ResourceNotFoundException("Locomotive", id);
        }

        // Call static utility method directly (ANTI-PATTERN)
        JsonFileManager.writeToFile(FILE_NAME, locomotives);
    }

    public List<Locomotive> findByManufacturer(String manufacturer) {
        return findAll().stream()
                .filter(l -> l.getManufacturer() != null &&
                           l.getManufacturer().equalsIgnoreCase(manufacturer))
                .collect(Collectors.toList());
    }

    public List<Locomotive> findByScale(Scale scale) {
        return findAll().stream()
                .filter(l -> l.getScale() == scale)
                .collect(Collectors.toList());
    }

    public List<Locomotive> findByMaintenanceStatus(MaintenanceStatus status) {
        return findAll().stream()
                .filter(l -> l.getMaintenanceStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Locomotive> findByRoadName(String roadName) {
        return findAll().stream()
                .filter(l -> l.getRoadName() != null &&
                           l.getRoadName().equalsIgnoreCase(roadName))
                .collect(Collectors.toList());
    }
}
