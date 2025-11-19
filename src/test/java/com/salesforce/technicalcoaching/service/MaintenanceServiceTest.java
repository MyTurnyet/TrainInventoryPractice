package com.salesforce.technicalcoaching.service;

import com.salesforce.technicalcoaching.enums.LocomotiveType;
import com.salesforce.technicalcoaching.enums.MaintenanceStatus;
import com.salesforce.technicalcoaching.enums.PowerType;
import com.salesforce.technicalcoaching.enums.Scale;
import com.salesforce.technicalcoaching.model.Locomotive;
import com.salesforce.technicalcoaching.model.MaintenanceLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MaintenanceServiceTest {

    private MaintenanceService service = new MaintenanceService();
    private LocomotiveService locoService = new LocomotiveService();

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

        MaintenanceLog result = service.create(log);
        assertNotNull(result);
        assertNotNull(result.id);
    }

    @Test
    public void testGetByItemId() {
        MaintenanceLog log1 = new MaintenanceLog();
        log1.inventoryItemId = 1L;
        log1.maintenanceDate = LocalDate.now();
        log1.description = "Test1";
        service.create(log1);

        MaintenanceLog log2 = new MaintenanceLog();
        log2.inventoryItemId = 1L;
        log2.maintenanceDate = LocalDate.now();
        log2.description = "Test2";
        service.create(log2);

        List<MaintenanceLog> logs = service.getByItemId(1L);
        assertEquals(2, logs.size());
    }

    @Test
    public void testUpdateItemStatus() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.scale = Scale.HO;
        l.locomotiveType = LocomotiveType.DIESEL;
        l.powerType = PowerType.DCC;
        l.maintenanceStatus = MaintenanceStatus.OPERATIONAL;

        Locomotive saved = locoService.create(l);
        service.updateItemStatus(saved.id, MaintenanceStatus.IN_MAINTENANCE);

        Locomotive updated = locoService.getById(saved.id);
        assertEquals(MaintenanceStatus.IN_MAINTENANCE, updated.maintenanceStatus);
    }
}
