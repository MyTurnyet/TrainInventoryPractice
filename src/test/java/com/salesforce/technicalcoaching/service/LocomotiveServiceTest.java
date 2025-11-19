package com.salesforce.technicalcoaching.service;

import com.salesforce.technicalcoaching.enums.LocomotiveType;
import com.salesforce.technicalcoaching.enums.MaintenanceStatus;
import com.salesforce.technicalcoaching.enums.PowerType;
import com.salesforce.technicalcoaching.enums.Scale;
import com.salesforce.technicalcoaching.model.Locomotive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocomotiveServiceTest {

    // tight coupling
    private LocomotiveService service = new LocomotiveService();

    @BeforeEach
    public void setup() {
        // cleanup files
        File f = new File("data/locomotives.json");
        if (f.exists()) {
            f.delete();
        }
        File f2 = new File("data/maintenance-logs.json");
        if (f2.exists()) {
            f2.delete();
        }
    }

    @AfterEach
    public void cleanup() {
        File f = new File("data/locomotives.json");
        if (f.exists()) {
            f.delete();
        }
        File f2 = new File("data/maintenance-logs.json");
        if (f2.exists()) {
            f2.delete();
        }
    }

    @Test
    public void testCreate() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.scale = Scale.HO;
        l.locomotiveType = LocomotiveType.DIESEL;
        l.powerType = PowerType.DCC;

        Locomotive result = service.create(l);
        assertNotNull(result);
        assertNotNull(result.id);
    }

    @Test
    public void test2() {
        // vague name, testing multiple things
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.scale = Scale.HO;
        l.locomotiveType = LocomotiveType.STEAM;
        l.powerType = PowerType.DC;

        Locomotive saved = service.create(l);
        Locomotive found = service.getById(saved.id);

        assertNotNull(found);
        assertEquals("Test", found.manufacturer);
    }

    @Test
    public void testGetAll() {
        Locomotive l1 = new Locomotive();
        l1.manufacturer = "Test1";
        l1.scale = Scale.HO;
        l1.locomotiveType = LocomotiveType.DIESEL;
        l1.powerType = PowerType.DCC;
        service.create(l1);

        Locomotive l2 = new Locomotive();
        l2.manufacturer = "Test2";
        l2.scale = Scale.N;
        l2.locomotiveType = LocomotiveType.STEAM;
        l2.powerType = PowerType.DC;
        service.create(l2);

        List<Locomotive> all = service.getAll();
        assertEquals(2, all.size());
    }

    @Test
    public void testSearch() {
        // duplicate setup
        Locomotive l1 = new Locomotive();
        l1.manufacturer = "Athearn";
        l1.scale = Scale.HO;
        l1.locomotiveType = LocomotiveType.DIESEL;
        l1.powerType = PowerType.DCC;
        l1.maintenanceStatus = MaintenanceStatus.OPERATIONAL;
        service.create(l1);

        Locomotive l2 = new Locomotive();
        l2.manufacturer = "Walthers";
        l2.scale = Scale.HO;
        l2.locomotiveType = LocomotiveType.STEAM;
        l2.powerType = PowerType.DC;
        l2.maintenanceStatus = MaintenanceStatus.NEEDS_MAINTENANCE;
        service.create(l2);

        Map<String, Object> result = service.search("Athearn", null, null);
        List<Locomotive> list = (List<Locomotive>) result.get("results");
        assertEquals(1, list.size());
        assertEquals("Athearn", list.get(0).manufacturer);
    }

    @Test
    public void testUpdateStatus() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.scale = Scale.O;
        l.locomotiveType = LocomotiveType.ELECTRIC;
        l.powerType = PowerType.BATTERY;
        l.maintenanceStatus = MaintenanceStatus.OPERATIONAL;

        Locomotive saved = service.create(l);
        service.updateStatus(saved.id, MaintenanceStatus.NEEDS_MAINTENANCE);

        Locomotive updated = service.getById(saved.id);
        assertEquals(MaintenanceStatus.NEEDS_MAINTENANCE, updated.maintenanceStatus);
    }
}
