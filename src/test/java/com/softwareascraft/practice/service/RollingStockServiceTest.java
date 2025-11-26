package com.softwareascraft.practice.service;

import com.softwareascraft.practice.dto.request.CreateRollingStockRequest;
import com.softwareascraft.practice.dto.request.UpdateRollingStockRequest;
import com.softwareascraft.practice.dto.response.RollingStockResponse;
import com.softwareascraft.practice.enums.AARType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.util.IdGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Service test with real repository and file I/O (ANTI-PATTERN for teaching purposes)
 * - Uses real repository instead of mocks
 * - Slow execution due to file operations
 * - Hard to isolate failures
 * - Requires file system cleanup
 */
class RollingStockServiceTest {

    private RollingStockService service;
    private static final String DATA_DIR = "data/";
    private static final String TEST_FILE = "rolling-stock.json";

    @BeforeEach
    void setUp() {
        IdGenerator.resetIdCounter("rolling_stock");
        service = new RollingStockService();
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
    void testCreateRollingStock() {
        CreateRollingStockRequest request = createRollingStockRequest();

        RollingStockResponse response = service.createRollingStock(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Walthers", response.getManufacturer());
        assertEquals(Scale.HO, response.getScale());
        assertEquals(AARType.RB, response.getAarType());
    }

    @Test
    void testGetRollingStockById() {
        CreateRollingStockRequest request = createRollingStockRequest();
        RollingStockResponse created = service.createRollingStock(request);

        RollingStockResponse found = service.getRollingStockById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("Walthers", found.getManufacturer());
    }

    @Test
    void testGetRollingStockById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () ->
                service.getRollingStockById(999L));
    }

    @Test
    void testGetAllRollingStock() {
        service.createRollingStock(createRollingStockRequest());
        service.createRollingStock(createRollingStockRequest());

        List<RollingStockResponse> all = service.getAllRollingStock();

        assertEquals(2, all.size());
    }

    @Test
    void testUpdateRollingStock() {
        CreateRollingStockRequest createRequest = createRollingStockRequest();
        RollingStockResponse created = service.createRollingStock(createRequest);

        UpdateRollingStockRequest updateRequest = new UpdateRollingStockRequest();
        updateRequest.setManufacturer("Updated Manufacturer");
        updateRequest.setModelNumber("NEW-MODEL");
        updateRequest.setScale(Scale.N);
        updateRequest.setRoadName("Updated Road");
        updateRequest.setColor("Red");
        updateRequest.setDescription("Updated Description");
        updateRequest.setPurchasePrice(new BigDecimal("49.99"));
        updateRequest.setPurchaseDate(LocalDate.now());
        updateRequest.setCurrentValue(new BigDecimal("49.99"));
        updateRequest.setNotes("Updated notes");
        updateRequest.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        updateRequest.setAarType(AARType.XM);
        updateRequest.setCarType("Box Car");
        updateRequest.setRoadNumber("8888");
        updateRequest.setCapacity("40 foot");

        RollingStockResponse updated = service.updateRollingStock(created.getId(), updateRequest);

        assertEquals("Updated Manufacturer", updated.getManufacturer());
        assertEquals("NEW-MODEL", updated.getModelNumber());
        assertEquals(Scale.N, updated.getScale());
    }

    @Test
    void testUpdateRollingStock_NotFound() {
        UpdateRollingStockRequest request = new UpdateRollingStockRequest();
        request.setManufacturer("Test");

        assertThrows(ResourceNotFoundException.class, () ->
                service.updateRollingStock(999L, request));
    }

    @Test
    void testDeleteRollingStock() {
        CreateRollingStockRequest request = createRollingStockRequest();
        RollingStockResponse created = service.createRollingStock(request);

        service.deleteRollingStock(created.getId());

        assertThrows(ResourceNotFoundException.class, () ->
                service.getRollingStockById(created.getId()));
    }

    @Test
    void testGetRollingStockByManufacturer() {
        CreateRollingStockRequest request1 = createRollingStockRequest();
        request1.setManufacturer("Walthers");
        service.createRollingStock(request1);

        CreateRollingStockRequest request2 = createRollingStockRequest();
        request2.setManufacturer("Athearn");
        service.createRollingStock(request2);

        List<RollingStockResponse> result = service.getRollingStockByManufacturer("Walthers");

        assertEquals(1, result.size());
        assertEquals("Walthers", result.get(0).getManufacturer());
    }

    private CreateRollingStockRequest createRollingStockRequest() {
        CreateRollingStockRequest request = new CreateRollingStockRequest();
        request.setManufacturer("Walthers");
        request.setModelNumber("910-2961");
        request.setScale(Scale.HO);
        request.setRoadName("Santa Fe");
        request.setColor("Silver/Red");
        request.setDescription("50' Reefer");
        request.setPurchasePrice(new BigDecimal("34.99"));
        request.setPurchaseDate(LocalDate.of(2024, 2, 10));
        request.setCurrentValue(new BigDecimal("34.99"));
        request.setNotes("Metal wheels");
        request.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        request.setAarType(AARType.RB);
        request.setCarType("Refrigerator Car");
        request.setRoadNumber("23456");
        request.setCapacity("50 foot");
        return request;
    }
}
