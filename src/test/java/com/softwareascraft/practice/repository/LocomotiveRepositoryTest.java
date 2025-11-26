package com.softwareascraft.practice.repository;

import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.util.IdGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
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
class LocomotiveRepositoryTest {

    private LocomotiveRepository repository;
    private static final String DATA_DIR = "data/";
    private static final String TEST_FILE = "locomotives.json";

    @BeforeEach
    void setUp() {
        // Reset ID counter for each test
        IdGenerator.resetIdCounter("locomotive");
        repository = new LocomotiveRepository();
    }

    @AfterEach
    void tearDown() {
        // Clean up test data files
        File dataFile = new File(DATA_DIR + TEST_FILE);
        if (dataFile.exists()) {
            dataFile.delete();
        }
        IdGenerator.resetIdCounter("locomotive");
    }

    @Test
    void testSaveLocomotive() {
        Locomotive locomotive = createTestLocomotive();

        Locomotive saved = repository.save(locomotive);

        assertNotNull(saved.getId());
        assertEquals("Athearn", saved.getManufacturer());
        assertEquals(Scale.HO, saved.getScale());
    }

    @Test
    void testFindById() {
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = repository.save(locomotive);

        Optional<Locomotive> found = repository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Athearn", found.get().getManufacturer());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Locomotive> found = repository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        repository.save(createTestLocomotive());
        repository.save(createTestLocomotive());

        List<Locomotive> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void testUpdate() {
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = repository.save(locomotive);

        saved.setManufacturer("Updated Manufacturer");
        saved.setRoadName("Updated Road");

        Locomotive updated = repository.update(saved.getId(), saved);

        assertEquals("Updated Manufacturer", updated.getManufacturer());
        assertEquals("Updated Road", updated.getRoadName());
    }

    @Test
    void testUpdate_NotFound() {
        Locomotive locomotive = createTestLocomotive();

        assertThrows(ResourceNotFoundException.class, () ->
                repository.update(999L, locomotive));
    }

    @Test
    void testDeleteById() {
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = repository.save(locomotive);

        repository.deleteById(saved.getId());

        Optional<Locomotive> found = repository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () ->
                repository.deleteById(999L));
    }

    @Test
    void testFindByManufacturer() {
        Locomotive loco1 = createTestLocomotive();
        loco1.setManufacturer("Athearn");
        repository.save(loco1);

        Locomotive loco2 = createTestLocomotive();
        loco2.setManufacturer("Walthers");
        repository.save(loco2);

        List<Locomotive> result = repository.findByManufacturer("Athearn");

        assertEquals(1, result.size());
        assertEquals("Athearn", result.get(0).getManufacturer());
    }

    @Test
    void testFindByScale() {
        Locomotive loco1 = createTestLocomotive();
        loco1.setScale(Scale.HO);
        repository.save(loco1);

        Locomotive loco2 = createTestLocomotive();
        loco2.setScale(Scale.N);
        repository.save(loco2);

        List<Locomotive> result = repository.findByScale(Scale.HO);

        assertEquals(1, result.size());
        assertEquals(Scale.HO, result.get(0).getScale());
    }

    @Test
    void testFindByMaintenanceStatus() {
        Locomotive loco1 = createTestLocomotive();
        loco1.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        repository.save(loco1);

        Locomotive loco2 = createTestLocomotive();
        loco2.setMaintenanceStatus(MaintenanceStatus.NEEDS_MAINTENANCE);
        repository.save(loco2);

        List<Locomotive> result = repository.findByMaintenanceStatus(MaintenanceStatus.OPERATIONAL);

        assertEquals(1, result.size());
        assertEquals(MaintenanceStatus.OPERATIONAL, result.get(0).getMaintenanceStatus());
    }

    private Locomotive createTestLocomotive() {
        Locomotive locomotive = new Locomotive();
        locomotive.setManufacturer("Athearn");
        locomotive.setModelNumber("RTR-87901");
        locomotive.setScale(Scale.HO);
        locomotive.setRoadName("Union Pacific");
        locomotive.setColor("Yellow/Gray");
        locomotive.setDescription("SD70M Locomotive");
        locomotive.setPurchasePrice(new BigDecimal("189.99"));
        locomotive.setPurchaseDate(LocalDate.of(2024, 1, 15));
        locomotive.setCurrentValue(new BigDecimal("189.99"));
        locomotive.setNotes("DCC Ready");
        locomotive.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        locomotive.setLocomotiveType(LocomotiveType.DIESEL);
        locomotive.setPowerType(PowerType.DCC_SOUND);
        locomotive.setRoadNumber("4141");
        return locomotive;
    }
}
