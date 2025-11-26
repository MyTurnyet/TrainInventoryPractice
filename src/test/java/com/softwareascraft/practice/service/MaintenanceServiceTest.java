package com.softwareascraft.practice.service;

import com.softwareascraft.practice.dto.request.CreateMaintenanceLogRequest;
import com.softwareascraft.practice.dto.response.MaintenanceLogResponse;
import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.repository.LocomotiveRepository;
import com.softwareascraft.practice.util.IdGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceServiceTest {

    private MaintenanceService service;
    private LocomotiveRepository locomotiveRepository;
    private static final String DATA_DIR = "data/";

    @BeforeEach
    void setUp() {
        IdGenerator.resetIdCounter("locomotive");
        IdGenerator.resetIdCounter("rolling_stock");
        IdGenerator.resetIdCounter("maintenance_log");
        service = new MaintenanceService();
        locomotiveRepository = new LocomotiveRepository();
    }

    @AfterEach
    void tearDown() {
        // Clean up all test files
        deleteFileIfExists(DATA_DIR + "locomotives.json");
        deleteFileIfExists(DATA_DIR + "rolling-stock.json");
        deleteFileIfExists(DATA_DIR + "maintenance-logs.json");

        IdGenerator.resetIdCounter("locomotive");
        IdGenerator.resetIdCounter("rolling_stock");
        IdGenerator.resetIdCounter("maintenance_log");
    }

    @Test
    void testCreateMaintenanceLog() {
        // Create a locomotive first
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = locomotiveRepository.save(locomotive);

        CreateMaintenanceLogRequest request = new CreateMaintenanceLogRequest();
        request.setInventoryItemId(saved.getId());
        request.setMaintenanceDate(LocalDate.now());
        request.setDescription("Routine cleaning");
        request.setWorkPerformed("Cleaned wheels and motor");
        request.setPerformedBy("Owner");
        request.setNotes("Running smoothly");

        MaintenanceLogResponse response = service.createMaintenanceLog(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(saved.getId(), response.getInventoryItemId());
        assertEquals("Routine cleaning", response.getDescription());
    }

    @Test
    void testCreateMaintenanceLog_InventoryItemNotFound() {
        CreateMaintenanceLogRequest request = new CreateMaintenanceLogRequest();
        request.setInventoryItemId(999L);
        request.setMaintenanceDate(LocalDate.now());
        request.setDescription("Test");
        request.setWorkPerformed("Test");
        request.setPerformedBy("Owner");

        assertThrows(ResourceNotFoundException.class, () ->
                service.createMaintenanceLog(request));
    }

    @Test
    void testGetMaintenanceLogById() {
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = locomotiveRepository.save(locomotive);

        CreateMaintenanceLogRequest request = new CreateMaintenanceLogRequest();
        request.setInventoryItemId(saved.getId());
        request.setMaintenanceDate(LocalDate.now());
        request.setDescription("Test");
        request.setWorkPerformed("Test work");
        request.setPerformedBy("Owner");
        request.setNotes("Notes");

        MaintenanceLogResponse created = service.createMaintenanceLog(request);

        MaintenanceLogResponse found = service.getMaintenanceLogById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
    }

    @Test
    void testGetMaintenanceLogById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () ->
                service.getMaintenanceLogById(999L));
    }

    @Test
    void testGetMaintenanceLogsByItemId() {
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = locomotiveRepository.save(locomotive);

        // Create multiple logs for the same item
        createMaintenanceLogForItem(saved.getId(), "Log 1");
        createMaintenanceLogForItem(saved.getId(), "Log 2");
        createMaintenanceLogForItem(saved.getId(), "Log 3");

        List<MaintenanceLogResponse> logs = service.getMaintenanceLogsByItemId(saved.getId());

        assertEquals(3, logs.size());
        assertTrue(logs.stream().allMatch(log -> log.getInventoryItemId().equals(saved.getId())));
    }

    @Test
    void testUpdateMaintenanceStatus() {
        Locomotive locomotive = createTestLocomotive();
        locomotive.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        Locomotive saved = locomotiveRepository.save(locomotive);

        service.updateMaintenanceStatus(saved.getId(), MaintenanceStatus.NEEDS_MAINTENANCE);

        Locomotive updated = locomotiveRepository.findById(saved.getId()).orElseThrow();
        assertEquals(MaintenanceStatus.NEEDS_MAINTENANCE, updated.getMaintenanceStatus());
    }

    @Test
    void testUpdateMaintenanceStatus_ItemNotFound() {
        assertThrows(ResourceNotFoundException.class, () ->
                service.updateMaintenanceStatus(999L, MaintenanceStatus.OPERATIONAL));
    }

    @Test
    void testDeleteMaintenanceLog() {
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = locomotiveRepository.save(locomotive);

        CreateMaintenanceLogRequest request = new CreateMaintenanceLogRequest();
        request.setInventoryItemId(saved.getId());
        request.setMaintenanceDate(LocalDate.now());
        request.setDescription("Test");
        request.setWorkPerformed("Test work");
        request.setPerformedBy("Owner");

        MaintenanceLogResponse created = service.createMaintenanceLog(request);

        service.deleteMaintenanceLog(created.getId());

        assertThrows(ResourceNotFoundException.class, () ->
                service.getMaintenanceLogById(created.getId()));
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

    private void createMaintenanceLogForItem(Long itemId, String description) {
        CreateMaintenanceLogRequest request = new CreateMaintenanceLogRequest();
        request.setInventoryItemId(itemId);
        request.setMaintenanceDate(LocalDate.now());
        request.setDescription(description);
        request.setWorkPerformed("Work performed");
        request.setPerformedBy("Owner");
        request.setNotes("Notes");
        service.createMaintenanceLog(request);
    }

    private void deleteFileIfExists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
