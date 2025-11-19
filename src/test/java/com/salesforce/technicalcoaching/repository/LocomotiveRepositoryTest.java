package com.salesforce.technicalcoaching.repository;

import com.salesforce.technicalcoaching.enums.LocomotiveType;
import com.salesforce.technicalcoaching.enums.PowerType;
import com.salesforce.technicalcoaching.enums.Scale;
import com.salesforce.technicalcoaching.model.Locomotive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocomotiveRepositoryTest {

    private LocomotiveRepository repo = new LocomotiveRepository();

    @BeforeEach
    public void setup() {
        // delete the file before each test
        File f = new File("data/locomotives.json");
        if (f.exists()) {
            f.delete();
        }
    }

    @AfterEach
    public void cleanup() {
        // delete the file after each test
        File f = new File("data/locomotives.json");
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    public void testSaveAndFindAll() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.modelNumber = "123";
        l.scale = Scale.HO;
        l.locomotiveType = LocomotiveType.DIESEL;
        l.powerType = PowerType.DCC;

        Locomotive saved = repo.save(l);
        assertNotNull(saved.id);

        List<Locomotive> list = repo.findAll();
        assertEquals(1, list.size());
        assertEquals("Test", list.get(0).manufacturer);
    }

    @Test
    public void test2() {
        Locomotive l1 = new Locomotive();
        l1.manufacturer = "Athearn";
        l1.scale = Scale.HO;
        l1.locomotiveType = LocomotiveType.STEAM;
        l1.powerType = PowerType.DC;

        Locomotive l2 = new Locomotive();
        l2.manufacturer = "Walthers";
        l2.scale = Scale.N;
        l2.locomotiveType = LocomotiveType.DIESEL;
        l2.powerType = PowerType.DCC_SOUND;

        repo.save(l1);
        repo.save(l2);

        List<Locomotive> all = repo.findAll();
        assertEquals(2, all.size());
    }

    @Test
    public void testFindById() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.scale = Scale.O;
        l.locomotiveType = LocomotiveType.ELECTRIC;
        l.powerType = PowerType.DC;

        Locomotive saved = repo.save(l);
        Long id = saved.id;

        Locomotive found = repo.findById(id);
        assertNotNull(found);
        assertEquals("Test", found.manufacturer);
    }

    @Test
    public void testUpdate() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Original";
        l.scale = Scale.G;
        l.locomotiveType = LocomotiveType.DIESEL;
        l.powerType = PowerType.DCC;

        Locomotive saved = repo.save(l);

        saved.manufacturer = "Updated";
        Locomotive updated = repo.update(saved.id, saved);

        assertEquals("Updated", updated.manufacturer);
    }

    @Test
    public void testDelete() {
        Locomotive l = new Locomotive();
        l.manufacturer = "ToDelete";
        l.scale = Scale.HO;
        l.locomotiveType = LocomotiveType.STEAM;
        l.powerType = PowerType.BATTERY;

        Locomotive saved = repo.save(l);
        repo.deleteById(saved.id);

        List<Locomotive> all = repo.findAll();
        assertEquals(0, all.size());
    }

    @Test
    public void testFindByScale() {
        Locomotive l1 = new Locomotive();
        l1.manufacturer = "Test1";
        l1.scale = Scale.HO;
        l1.locomotiveType = LocomotiveType.DIESEL;
        l1.powerType = PowerType.DC;
        repo.save(l1);

        Locomotive l2 = new Locomotive();
        l2.manufacturer = "Test2";
        l2.scale = Scale.N;
        l2.locomotiveType = LocomotiveType.STEAM;
        l2.powerType = PowerType.DCC;
        repo.save(l2);

        Locomotive l3 = new Locomotive();
        l3.manufacturer = "Test3";
        l3.scale = Scale.HO;
        l3.locomotiveType = LocomotiveType.ELECTRIC;
        l3.powerType = PowerType.DCC_SOUND;
        repo.save(l3);

        List<Locomotive> hoList = repo.findByScale(Scale.HO);
        assertEquals(2, hoList.size());
    }
}
