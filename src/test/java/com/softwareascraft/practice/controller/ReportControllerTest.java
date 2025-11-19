package com.softwareascraft.practice.controller;

import com.softwareascraft.practice.enums.AARType;
import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.model.RollingStock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportControllerTest {

    private ReportController controller = new ReportController();
    private LocomotiveController locoController = new LocomotiveController();
    private RollingStockController rsController = new RollingStockController();

    @BeforeEach
    public void setup() {
        File f = new File("data/locomotives.json");
        if (f.exists()) {
            f.delete();
        }
        File f2 = new File("data/rolling-stock.json");
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
        File f2 = new File("data/rolling-stock.json");
        if (f2.exists()) {
            f2.delete();
        }
    }

    @Test
    public void test1() {
        Locomotive l1 = new Locomotive();
        l1.manufacturer = "Test1";
        l1.scale = Scale.HO;
        l1.locomotiveType = LocomotiveType.DIESEL;
        l1.powerType = PowerType.DCC;
        locoController.create(l1);

        Locomotive l2 = new Locomotive();
        l2.manufacturer = "Test2";
        l2.scale = Scale.N;
        l2.locomotiveType = LocomotiveType.STEAM;
        l2.powerType = PowerType.DC;
        locoController.create(l2);

        RollingStock rs1 = new RollingStock();
        rs1.manufacturer = "Test3";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.XM;
        rsController.create(rs1);

        ResponseEntity<Map<String, Object>> response = controller.getSummary();
        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> body = response.getBody();
        assertEquals(3, body.get("totalItems"));
        assertEquals(2, body.get("totalLocomotives"));
        assertEquals(1, body.get("totalRollingStock"));
    }
}
