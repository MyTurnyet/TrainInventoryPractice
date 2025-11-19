package com.salesforce.technicalcoaching.repository;

import com.salesforce.technicalcoaching.enums.AARType;
import com.salesforce.technicalcoaching.enums.Scale;
import com.salesforce.technicalcoaching.model.RollingStock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RollingStockRepositoryTest {

    private RollingStockRepository repo = new RollingStockRepository();

    @BeforeEach
    public void setup() {
        File f = new File("data/rolling-stock.json");
        if (f.exists()) {
            f.delete();
        }
    }

    @AfterEach
    public void cleanup() {
        File f = new File("data/rolling-stock.json");
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    public void test1() {
        RollingStock rs = new RollingStock();
        rs.manufacturer = "Walthers";
        rs.scale = Scale.HO;
        rs.aarType = AARType.XM;
        rs.carType = "Box Car";

        RollingStock saved = repo.save(rs);
        assertNotNull(saved.id);

        List<RollingStock> list = repo.findAll();
        assertEquals(1, list.size());
    }

    @Test
    public void testSaveMultiple() {
        RollingStock rs1 = new RollingStock();
        rs1.manufacturer = "Athearn";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.FC;
        rs1.carType = "Flat Car";

        RollingStock rs2 = new RollingStock();
        rs2.manufacturer = "Walthers";
        rs2.scale = Scale.N;
        rs2.aarType = AARType.TA;
        rs2.carType = "Tank Car";

        repo.save(rs1);
        repo.save(rs2);

        List<RollingStock> all = repo.findAll();
        assertEquals(2, all.size());
        assertTrue(all.get(0).id < all.get(1).id);
    }

    @Test
    public void testFindByAarType() {
        // duplicate code from previous test
        RollingStock rs1 = new RollingStock();
        rs1.manufacturer = "Test1";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.XM;
        rs1.carType = "Box Car";
        repo.save(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.manufacturer = "Test2";
        rs2.scale = Scale.N;
        rs2.aarType = AARType.FC;
        rs2.carType = "Flat Car";
        repo.save(rs2);

        RollingStock rs3 = new RollingStock();
        rs3.manufacturer = "Test3";
        rs3.scale = Scale.HO;
        rs3.aarType = AARType.XM;
        rs3.carType = "Box Car";
        repo.save(rs3);

        List<RollingStock> xmList = repo.findByAarType(AARType.XM);
        assertEquals(2, xmList.size());
    }
}
