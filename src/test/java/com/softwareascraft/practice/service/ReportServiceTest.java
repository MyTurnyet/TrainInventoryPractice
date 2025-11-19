package com.softwareascraft.practice.service;

import com.softwareascraft.practice.enums.*;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.model.RollingStock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportServiceTest {

    private ReportService service = new ReportService();
    private LocomotiveService locoService = new LocomotiveService();
    private RollingStockService rsService = new RollingStockService();

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
    public void testGetSummary() {
        Locomotive l1 = new Locomotive();
        l1.manufacturer = "Test1";
        l1.scale = Scale.HO;
        l1.locomotiveType = LocomotiveType.DIESEL;
        l1.powerType = PowerType.DCC;
        l1.currentValue = new BigDecimal("100.00");
        l1.maintenanceStatus = MaintenanceStatus.OPERATIONAL;
        locoService.create(l1);

        Locomotive l2 = new Locomotive();
        l2.manufacturer = "Test2";
        l2.scale = Scale.N;
        l2.locomotiveType = LocomotiveType.STEAM;
        l2.powerType = PowerType.DC;
        l2.currentValue = new BigDecimal("150.00");
        l2.maintenanceStatus = MaintenanceStatus.OPERATIONAL;
        locoService.create(l2);

        RollingStock rs1 = new RollingStock();
        rs1.manufacturer = "Test3";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.XM;
        rs1.currentValue = new BigDecimal("50.00");
        rs1.maintenanceStatus = MaintenanceStatus.NEEDS_MAINTENANCE;
        rsService.create(rs1);

        Map<String, Object> summary = service.getSummary();

        assertEquals(3, summary.get("totalItems"));
        assertEquals(2, summary.get("totalLocomotives"));
        assertEquals(1, summary.get("totalRollingStock"));

        BigDecimal totalValue = (BigDecimal) summary.get("totalInventoryValue");
        assertEquals(new BigDecimal("300.00"), totalValue);

        Map<Scale, Integer> byScale = (Map<Scale, Integer>) summary.get("itemsByScale");
        assertEquals(2, byScale.get(Scale.HO));
        assertEquals(1, byScale.get(Scale.N));

        Map<MaintenanceStatus, Integer> byStatus = (Map<MaintenanceStatus, Integer>) summary.get("itemsByStatus");
        assertEquals(2, byStatus.get(MaintenanceStatus.OPERATIONAL));
        assertEquals(1, byStatus.get(MaintenanceStatus.NEEDS_MAINTENANCE));
    }
}
