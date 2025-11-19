package com.salesforce.technicalcoaching.repository;

import com.salesforce.technicalcoaching.model.MaintenanceLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MaintenanceLogRepositoryTest {

    private MaintenanceLogRepository repo = new MaintenanceLogRepository();

    @BeforeEach
    public void setup() {
        File f = new File("data/maintenance-logs.json");
        if (f.exists()) {
            f.delete();
        }
    }

    @AfterEach
    public void cleanup() {
        File f = new File("data/maintenance-logs.json");
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    public void testSave() {
        MaintenanceLog log = new MaintenanceLog();
        log.inventoryItemId = 1L;
        log.maintenanceDate = LocalDate.now();
        log.description = "Test";
        log.workPerformed = "Cleaning";

        MaintenanceLog saved = repo.save(log);
        assertNotNull(saved.id);
        assertNotNull(saved.createdDate);
    }

    @Test
    public void testFindByItem() {
        // poor variable names
        MaintenanceLog l1 = new MaintenanceLog();
        l1.inventoryItemId = 1L;
        l1.maintenanceDate = LocalDate.now();
        l1.description = "Test1";
        repo.save(l1);

        MaintenanceLog l2 = new MaintenanceLog();
        l2.inventoryItemId = 1L;
        l2.maintenanceDate = LocalDate.now();
        l2.description = "Test2";
        repo.save(l2);

        MaintenanceLog l3 = new MaintenanceLog();
        l3.inventoryItemId = 2L;
        l3.maintenanceDate = LocalDate.now();
        l3.description = "Test3";
        repo.save(l3);

        List<MaintenanceLog> logs = repo.findByItem(1L);
        assertEquals(2, logs.size());
    }
}
