package com.softwareascraft.practice.service;

import com.softwareascraft.practice.dto.request.CreateLocomotiveRequest;
import com.softwareascraft.practice.dto.request.UpdateLocomotiveRequest;
import com.softwareascraft.practice.dto.response.LocomotiveResponse;
import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.util.IdGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocomotiveServiceTest {

    private LocomotiveService service;
    private static final String DATA_DIR = "data/";
    private static final String TEST_FILE = "locomotives.json";

    @BeforeEach
    void setUp() {
        IdGenerator.resetIdCounter("locomotive");
        service = new LocomotiveService();
    }

    @AfterEach
    void tearDown() {
        File dataFile = new File(DATA_DIR + TEST_FILE);
        if (dataFile.exists()) {
            dataFile.delete();
        }
        IdGenerator.resetIdCounter("locomotive");
    }

    @Test
    void testCreateLocomotive() {
        CreateLocomotiveRequest request = createLocomotiveRequest();

        LocomotiveResponse response = service.createLocomotive(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Athearn", response.getManufacturer());
        assertEquals(Scale.HO, response.getScale());
        assertEquals(LocomotiveType.DIESEL, response.getLocomotiveType());
    }

    @Test
    void testGetLocomotiveById() {
        CreateLocomotiveRequest request = createLocomotiveRequest();
        LocomotiveResponse created = service.createLocomotive(request);

        LocomotiveResponse found = service.getLocomotiveById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("Athearn", found.getManufacturer());
    }

    @Test
    void testGetLocomotiveById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () ->
                service.getLocomotiveById(999L));
    }

    @Test
    void testGetAllLocomotives() {
        service.createLocomotive(createLocomotiveRequest());
        service.createLocomotive(createLocomotiveRequest());

        List<LocomotiveResponse> all = service.getAllLocomotives();

        assertEquals(2, all.size());
    }

    @Test
    void testUpdateLocomotive() {
        CreateLocomotiveRequest createRequest = createLocomotiveRequest();
        LocomotiveResponse created = service.createLocomotive(createRequest);

        UpdateLocomotiveRequest updateRequest = new UpdateLocomotiveRequest();
        updateRequest.setManufacturer("Updated Manufacturer");
        updateRequest.setModelNumber("NEW-MODEL");
        updateRequest.setScale(Scale.N);
        updateRequest.setRoadName("Updated Road");
        updateRequest.setColor("Blue");
        updateRequest.setDescription("Updated Description");
        updateRequest.setPurchasePrice(new BigDecimal("299.99"));
        updateRequest.setPurchaseDate(LocalDate.now());
        updateRequest.setCurrentValue(new BigDecimal("299.99"));
        updateRequest.setNotes("Updated notes");
        updateRequest.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        updateRequest.setLocomotiveType(LocomotiveType.ELECTRIC);
        updateRequest.setPowerType(PowerType.DCC);
        updateRequest.setRoadNumber("9999");

        LocomotiveResponse updated = service.updateLocomotive(created.getId(), updateRequest);

        assertEquals("Updated Manufacturer", updated.getManufacturer());
        assertEquals("NEW-MODEL", updated.getModelNumber());
        assertEquals(Scale.N, updated.getScale());
    }

    @Test
    void testUpdateLocomotive_NotFound() {
        UpdateLocomotiveRequest request = new UpdateLocomotiveRequest();
        request.setManufacturer("Test");

        assertThrows(ResourceNotFoundException.class, () ->
                service.updateLocomotive(999L, request));
    }

    @Test
    void testDeleteLocomotive() {
        CreateLocomotiveRequest request = createLocomotiveRequest();
        LocomotiveResponse created = service.createLocomotive(request);

        service.deleteLocomotive(created.getId());

        assertThrows(ResourceNotFoundException.class, () ->
                service.getLocomotiveById(created.getId()));
    }

    @Test
    void testGetLocomotivesByManufacturer() {
        CreateLocomotiveRequest request1 = createLocomotiveRequest();
        request1.setManufacturer("Athearn");
        service.createLocomotive(request1);

        CreateLocomotiveRequest request2 = createLocomotiveRequest();
        request2.setManufacturer("Walthers");
        service.createLocomotive(request2);

        List<LocomotiveResponse> result = service.getLocomotivesByManufacturer("Athearn");

        assertEquals(1, result.size());
        assertEquals("Athearn", result.get(0).getManufacturer());
    }

    private CreateLocomotiveRequest createLocomotiveRequest() {
        CreateLocomotiveRequest request = new CreateLocomotiveRequest();
        request.setManufacturer("Athearn");
        request.setModelNumber("RTR-87901");
        request.setScale(Scale.HO);
        request.setRoadName("Union Pacific");
        request.setColor("Yellow/Gray");
        request.setDescription("SD70M Locomotive");
        request.setPurchasePrice(new BigDecimal("189.99"));
        request.setPurchaseDate(LocalDate.of(2024, 1, 15));
        request.setCurrentValue(new BigDecimal("189.99"));
        request.setNotes("DCC Ready");
        request.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        request.setLocomotiveType(LocomotiveType.DIESEL);
        request.setPowerType(PowerType.DCC_SOUND);
        request.setRoadNumber("4141");
        return request;
    }
}
