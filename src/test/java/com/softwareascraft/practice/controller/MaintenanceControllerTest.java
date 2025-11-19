package com.softwareascraft.practice.controller;

import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.model.MaintenanceLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MaintenanceControllerTest {

    private MaintenanceController controller = new MaintenanceController();
    private LocomotiveController locoController = new LocomotiveController();

    @BeforeEach
    public void setup() {
        File f = new File("data/maintenance-logs.json");
        if (f.exists()) {
            f.delete();
        }
        File f2 = new File("data/locomotives.json");
        if (f2.exists()) {
            f2.delete();
        }
    }

    @AfterEach
    public void cleanup() {
        File f = new File("data/maintenance-logs.json");
        if (f.exists()) {
            f.delete();
        }
        File f2 = new File("data/locomotives.json");
        if (f2.exists()) {
            f2.delete();
        }
    }

    @Test
    public void testCreate() {
        MaintenanceLog log = new MaintenanceLog();
        log.inventoryItemId = 1L;
        log.maintenanceDate = LocalDate.now();
        log.description = "Test";
        log.workPerformed = "Cleaning";

        ResponseEntity<MaintenanceLog> response = controller.create(log);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().id);
    }

    @Test
    public void test2() {
        MaintenanceLog log = new MaintenanceLog();
        log.inventoryItemId = 1L;
        log.maintenanceDate = LocalDate.now();
        log.description = "Test";
        log.workPerformed = "Lubrication";

        ResponseEntity<MaintenanceLog> createResponse = controller.create(log);
        Long id = createResponse.getBody().id;

        ResponseEntity<MaintenanceLog> getResponse = controller.getById(id);
        assertEquals(200, getResponse.getStatusCodeValue());
        assertEquals("Test", getResponse.getBody().description);
    }

    @Test
    public void testGetByItemId() {
        MaintenanceLog log1 = new MaintenanceLog();
        log1.inventoryItemId = 1L;
        log1.maintenanceDate = LocalDate.now();
        log1.description = "Test1";
        controller.create(log1);

        MaintenanceLog log2 = new MaintenanceLog();
        log2.inventoryItemId = 1L;
        log2.maintenanceDate = LocalDate.now();
        log2.description = "Test2";
        controller.create(log2);

        ResponseEntity<List<MaintenanceLog>> response = controller.getByItemId(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testUpdateStatus() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.scale = Scale.HO;
        l.locomotiveType = LocomotiveType.DIESEL;
        l.powerType = PowerType.DCC;

        ResponseEntity<Locomotive> locoResponse = locoController.create(l);
        Long id = locoResponse.getBody().id;

        Map<String, String> body = new HashMap<>();
        body.put("status", "IN_MAINTENANCE");

        ResponseEntity<Void> response = controller.updateStatus(id, body);
        assertEquals(200, response.getStatusCodeValue());
    }
}
