package com.salesforce.technicalcoaching.controller;

import com.salesforce.technicalcoaching.enums.AARType;
import com.salesforce.technicalcoaching.enums.Scale;
import com.salesforce.technicalcoaching.model.RollingStock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RollingStockControllerTest {

    private RollingStockController controller = new RollingStockController();

    @BeforeEach
    public void setup() {
        File f = new File("data/rolling-stock.json");
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
        File f = new File("data/rolling-stock.json");
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
        RollingStock rs = new RollingStock();
        rs.manufacturer = "Walthers";
        rs.scale = Scale.HO;
        rs.aarType = AARType.XM;
        rs.carType = "Box Car";

        ResponseEntity<RollingStock> response = controller.create(rs);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().id);
    }

    @Test
    public void test2() {
        // poor name
        RollingStock rs = new RollingStock();
        rs.manufacturer = "Test";
        rs.scale = Scale.N;
        rs.aarType = AARType.FC;
        rs.carType = "Flat Car";

        ResponseEntity<RollingStock> createResponse = controller.create(rs);
        Long id = createResponse.getBody().id;

        ResponseEntity<Map<String, Object>> getResponse = controller.getById(id);
        assertEquals(200, getResponse.getStatusCodeValue());
    }

    @Test
    public void testGetAll() {
        RollingStock rs1 = new RollingStock();
        rs1.manufacturer = "Test1";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.XM;
        controller.create(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.manufacturer = "Test2";
        rs2.scale = Scale.N;
        rs2.aarType = AARType.FC;
        controller.create(rs2);

        ResponseEntity<List<RollingStock>> response = controller.getAll();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetByAarType() {
        RollingStock rs1 = new RollingStock();
        rs1.manufacturer = "Test1";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.XM;
        controller.create(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.manufacturer = "Test2";
        rs2.scale = Scale.N;
        rs2.aarType = AARType.XM;
        controller.create(rs2);

        RollingStock rs3 = new RollingStock();
        rs3.manufacturer = "Test3";
        rs3.scale = Scale.HO;
        rs3.aarType = AARType.FC;
        controller.create(rs3);

        ResponseEntity<List<RollingStock>> response = controller.getByAarType(AARType.XM);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }
}
