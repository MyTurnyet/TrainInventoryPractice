package com.softwareascraft.practice.repository;

import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.MaintenanceLog;
import com.softwareascraft.practice.util.IdGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Repository test with real file I/O (ANTI-PATTERN for teaching purposes)
 * - Uses real JSON files
 * - Slow execution
 * - Tests can interfere with each other
 * - Requires file system cleanup
 */
class MaintenanceLogRepositoryTest {

    private MaintenanceLogRepository repository;
    private static final String DATA_DIR = "data/";
    private static final String TEST_FILE = "maintenance-logs.json";

    @BeforeEach
    void setUp() {
        IdGenerator.resetIdCounter("maintenance_log");
        repository = new MaintenanceLogRepository();
    }

    @AfterEach
    void tearDown() {
        File dataFile = new File(DATA_DIR + TEST_FILE);
        if (dataFile.exists()) {
            dataFile.delete();
        }
        IdGenerator.resetIdCounter("maintenance_log");
    }

    @Test
    void testSaveMaintenanceLog() {
        MaintenanceLog log = createTestMaintenanceLog(1L);

        MaintenanceLog saved = repository.save(log);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getInventoryItemId());
        assertEquals("Routine cleaning", saved.getDescription());
    }

    @Test
    void testFindById() {
        MaintenanceLog log = createTestMaintenanceLog(1L);
        MaintenanceLog saved = repository.save(log);

        Optional<MaintenanceLog> found = repository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void testFindById_NotFound() {
        Optional<MaintenanceLog> found = repository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        repository.save(createTestMaintenanceLog(1L));
        repository.save(createTestMaintenanceLog(2L));

        List<MaintenanceLog> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void testUpdate() {
        MaintenanceLog log = createTestMaintenanceLog(1L);
        MaintenanceLog saved = repository.save(log);

        saved.setDescription("Updated description");
        saved.setWorkPerformed("Updated work");

        MaintenanceLog updated = repository.update(saved.getId(), saved);

        assertEquals("Updated description", updated.getDescription());
        assertEquals("Updated work", updated.getWorkPerformed());
    }

    @Test
    void testUpdate_NotFound() {
        MaintenanceLog log = createTestMaintenanceLog(1L);

        assertThrows(ResourceNotFoundException.class, () ->
                repository.update(999L, log));
    }

    @Test
    void testDeleteById() {
        MaintenanceLog log = createTestMaintenanceLog(1L);
        MaintenanceLog saved = repository.save(log);

        repository.deleteById(saved.getId());

        Optional<MaintenanceLog> found = repository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () ->
                repository.deleteById(999L));
    }

    @Test
    void testFindByInventoryItemId() {
        repository.save(createTestMaintenanceLog(1L));
        repository.save(createTestMaintenanceLog(1L));
        repository.save(createTestMaintenanceLog(2L));

        List<MaintenanceLog> result = repository.findByInventoryItemId(1L);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(log -> log.getInventoryItemId().equals(1L)));
    }

    @Test
    void testFindByInventoryItemIdOrderByMaintenanceDateDesc() {
        MaintenanceLog log1 = createTestMaintenanceLog(1L);
        log1.setMaintenanceDate(LocalDate.of(2024, 1, 1));
        repository.save(log1);

        MaintenanceLog log2 = createTestMaintenanceLog(1L);
        log2.setMaintenanceDate(LocalDate.of(2024, 3, 1));
        repository.save(log2);

        List<MaintenanceLog> result = repository.findByInventoryItemIdOrderByMaintenanceDateDesc(1L);

        assertEquals(2, result.size());
        assertEquals(LocalDate.of(2024, 3, 1), result.get(0).getMaintenanceDate());
        assertEquals(LocalDate.of(2024, 1, 1), result.get(1).getMaintenanceDate());
    }

    @Test
    void testFindByMaintenanceDateBetween() {
        MaintenanceLog log1 = createTestMaintenanceLog(1L);
        log1.setMaintenanceDate(LocalDate.of(2024, 1, 15));
        repository.save(log1);

        MaintenanceLog log2 = createTestMaintenanceLog(2L);
        log2.setMaintenanceDate(LocalDate.of(2024, 2, 15));
        repository.save(log2);

        MaintenanceLog log3 = createTestMaintenanceLog(3L);
        log3.setMaintenanceDate(LocalDate.of(2024, 3, 15));
        repository.save(log3);

        List<MaintenanceLog> result = repository.findByMaintenanceDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 28)
        );

        assertEquals(2, result.size());
    }

    private MaintenanceLog createTestMaintenanceLog(Long inventoryItemId) {
        MaintenanceLog log = new MaintenanceLog();
        log.setInventoryItemId(inventoryItemId);
        log.setMaintenanceDate(LocalDate.now());
        log.setDescription("Routine cleaning");
        log.setWorkPerformed("Cleaned wheels and motor, lubricated gears");
        log.setPerformedBy("Owner");
        log.setNotes("Running smoothly");
        return log;
    }
}
