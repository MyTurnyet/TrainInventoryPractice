package com.salesforce.technicalcoaching.service;

import com.salesforce.technicalcoaching.enums.AARType;
import com.salesforce.technicalcoaching.enums.Scale;
import com.salesforce.technicalcoaching.model.RollingStock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RollingStockServiceTest {

    private RollingStockService service = new RollingStockService();

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
    public void test1() {
        RollingStock rs = new RollingStock();
        rs.manufacturer = "Walthers";
        rs.scale = Scale.HO;
        rs.aarType = AARType.XM;
        rs.carType = "Box Car";

        RollingStock result = service.create(rs);
        assertNotNull(result);
        assertNotNull(result.id);
    }

    @Test
    public void testGetById() {
        RollingStock rs = new RollingStock();
        rs.manufacturer = "Test";
        rs.scale = Scale.N;
        rs.aarType = AARType.FC;
        rs.carType = "Flat Car";

        RollingStock saved = service.create(rs);
        RollingStock found = service.getById(saved.id);

        assertNotNull(found);
        assertEquals("Test", found.manufacturer);
    }

    @Test
    public void testSearch() {
        RollingStock rs1 = new RollingStock();
        rs1.manufacturer = "Athearn";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.XM;
        service.create(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.manufacturer = "Walthers";
        rs2.scale = Scale.N;
        rs2.aarType = AARType.FC;
        service.create(rs2);

        Map<String, Object> result = service.search(null, Scale.HO, null);
        List<RollingStock> list = (List<RollingStock>) result.get("results");
        assertEquals(1, list.size());
    }

    @Test
    public void testGetByAarType() {
        // duplicate setup code
        RollingStock rs1 = new RollingStock();
        rs1.manufacturer = "Test1";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.XM;
        service.create(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.manufacturer = "Test2";
        rs2.scale = Scale.N;
        rs2.aarType = AARType.XM;
        service.create(rs2);

        RollingStock rs3 = new RollingStock();
        rs3.manufacturer = "Test3";
        rs3.scale = Scale.HO;
        rs3.aarType = AARType.FC;
        service.create(rs3);

        List<RollingStock> xmList = service.getByAarType(AARType.XM);
        assertEquals(2, xmList.size());
    }
}
