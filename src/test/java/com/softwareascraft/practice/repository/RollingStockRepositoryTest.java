package com.softwareascraft.practice.repository;

import com.softwareascraft.practice.enums.AARType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.RollingStock;
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

class RollingStockRepositoryTest {

    private RollingStockRepository repository;
    private static final String DATA_DIR = "data/";
    private static final String TEST_FILE = "rolling-stock.json";

    @BeforeEach
    void setUp() {
        IdGenerator.resetIdCounter("rolling_stock");
        repository = new RollingStockRepository();
    }

    @AfterEach
    void tearDown() {
        File dataFile = new File(DATA_DIR + TEST_FILE);
        if (dataFile.exists()) {
            dataFile.delete();
        }
        IdGenerator.resetIdCounter("rolling_stock");
    }

    @Test
    void testSaveRollingStock() {
        RollingStock rollingStock = createTestRollingStock();

        RollingStock saved = repository.save(rollingStock);

        assertNotNull(saved.getId());
        assertEquals("Walthers", saved.getManufacturer());
        assertEquals(Scale.HO, saved.getScale());
    }

    @Test
    void testFindById() {
        RollingStock rollingStock = createTestRollingStock();
        RollingStock saved = repository.save(rollingStock);

        Optional<RollingStock> found = repository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void testFindById_NotFound() {
        Optional<RollingStock> found = repository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        repository.save(createTestRollingStock());
        repository.save(createTestRollingStock());

        List<RollingStock> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void testUpdate() {
        RollingStock rollingStock = createTestRollingStock();
        RollingStock saved = repository.save(rollingStock);

        saved.setManufacturer("Updated Manufacturer");
        saved.setCarType("Updated Type");

        RollingStock updated = repository.update(saved.getId(), saved);

        assertEquals("Updated Manufacturer", updated.getManufacturer());
        assertEquals("Updated Type", updated.getCarType());
    }

    @Test
    void testUpdate_NotFound() {
        RollingStock rollingStock = createTestRollingStock();

        assertThrows(ResourceNotFoundException.class, () ->
                repository.update(999L, rollingStock));
    }

    @Test
    void testDeleteById() {
        RollingStock rollingStock = createTestRollingStock();
        RollingStock saved = repository.save(rollingStock);

        repository.deleteById(saved.getId());

        Optional<RollingStock> found = repository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () ->
                repository.deleteById(999L));
    }

    @Test
    void testFindByAarType() {
        RollingStock rs1 = createTestRollingStock();
        rs1.setAarType(AARType.XM);
        repository.save(rs1);

        RollingStock rs2 = createTestRollingStock();
        rs2.setAarType(AARType.FC);
        repository.save(rs2);

        List<RollingStock> result = repository.findByAarType(AARType.XM);

        assertEquals(1, result.size());
        assertEquals(AARType.XM, result.get(0).getAarType());
    }

    private RollingStock createTestRollingStock() {
        RollingStock rollingStock = new RollingStock();
        rollingStock.setManufacturer("Walthers");
        rollingStock.setModelNumber("910-2961");
        rollingStock.setScale(Scale.HO);
        rollingStock.setRoadName("Santa Fe");
        rollingStock.setColor("Silver/Red");
        rollingStock.setDescription("50' Reefer");
        rollingStock.setPurchasePrice(new BigDecimal("34.99"));
        rollingStock.setPurchaseDate(LocalDate.of(2024, 2, 10));
        rollingStock.setCurrentValue(new BigDecimal("34.99"));
        rollingStock.setNotes("Metal wheels");
        rollingStock.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        rollingStock.setAarType(AARType.RB);
        rollingStock.setCarType("Refrigerator Car");
        rollingStock.setRoadNumber("23456");
        rollingStock.setCapacity("50 foot");
        return rollingStock;
    }
}
