package com.softwareascraft.practice.controller;

import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.Locomotive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocomotiveControllerTest {

    private LocomotiveController controller = new LocomotiveController();

    @BeforeEach
    public void setup() {
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
    public void test1() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.scale = Scale.HO;
        l.locomotiveType = LocomotiveType.DIESEL;
        l.powerType = PowerType.DCC;

        ResponseEntity<Locomotive> response = controller.create(l);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().id);
    }

    @Test
    public void testGetById() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.scale = Scale.N;
        l.locomotiveType = LocomotiveType.STEAM;
        l.powerType = PowerType.DC;

        ResponseEntity<Locomotive> createResponse = controller.create(l);
        Long id = createResponse.getBody().id;

        ResponseEntity<Map<String, Object>> getResponse = controller.getById(id);
        assertEquals(200, getResponse.getStatusCodeValue());
        assertNotNull(getResponse.getBody());
    }

    @Test
    public void testGetAll() {
        Locomotive l1 = new Locomotive();
        l1.manufacturer = "Test1";
        l1.scale = Scale.HO;
        l1.locomotiveType = LocomotiveType.DIESEL;
        l1.powerType = PowerType.DCC;
        controller.create(l1);

        Locomotive l2 = new Locomotive();
        l2.manufacturer = "Test2";
        l2.scale = Scale.N;
        l2.locomotiveType = LocomotiveType.STEAM;
        l2.powerType = PowerType.DC;
        controller.create(l2);

        ResponseEntity<List<Locomotive>> response = controller.getAll();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testUpdate() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Original";
        l.scale = Scale.O;
        l.locomotiveType = LocomotiveType.ELECTRIC;
        l.powerType = PowerType.BATTERY;

        ResponseEntity<Locomotive> createResponse = controller.create(l);
        Long id = createResponse.getBody().id;

        Locomotive updated = new Locomotive();
        updated.manufacturer = "Updated";
        updated.scale = Scale.O;
        updated.locomotiveType = LocomotiveType.ELECTRIC;
        updated.powerType = PowerType.BATTERY;

        ResponseEntity<Locomotive> updateResponse = controller.update(id, updated);
        assertEquals(200, updateResponse.getStatusCodeValue());
        assertEquals("Updated", updateResponse.getBody().manufacturer);
    }

    @Test
    public void testSearch() {
        Locomotive l1 = new Locomotive();
        l1.manufacturer = "Athearn";
        l1.scale = Scale.HO;
        l1.locomotiveType = LocomotiveType.DIESEL;
        l1.powerType = PowerType.DCC;
        controller.create(l1);

        Locomotive l2 = new Locomotive();
        l2.manufacturer = "Walthers";
        l2.scale = Scale.N;
        l2.locomotiveType = LocomotiveType.STEAM;
        l2.powerType = PowerType.DC;
        controller.create(l2);

        ResponseEntity<Map<String, Object>> response = controller.search("Athearn", null, null);
        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> body = response.getBody();
        List<Locomotive> results = (List<Locomotive>) body.get("results");
        assertEquals(1, results.size());
    }

    @Test
    public void testUpdateStatus() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.scale = Scale.G;
        l.locomotiveType = LocomotiveType.DIESEL;
        l.powerType = PowerType.DCC;

        ResponseEntity<Locomotive> createResponse = controller.create(l);
        Long id = createResponse.getBody().id;

        Map<String, String> body = new HashMap<>();
        body.put("status", "NEEDS_MAINTENANCE");

        ResponseEntity<Void> updateResponse = controller.updateStatus(id, body);
        assertEquals(200, updateResponse.getStatusCodeValue());
    }
}
