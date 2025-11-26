package com.softwareascraft.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softwareascraft.practice.dto.request.CreateMaintenanceLogRequest;
import com.softwareascraft.practice.dto.request.UpdateMaintenanceStatusRequest;
import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.repository.LocomotiveRepository;
import com.softwareascraft.practice.util.IdGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MaintenanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private LocomotiveRepository locomotiveRepository;
    private static final String DATA_DIR = "data/";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        IdGenerator.resetIdCounter("locomotive");
        IdGenerator.resetIdCounter("maintenance_log");
        locomotiveRepository = new LocomotiveRepository();
    }

    @AfterEach
    void tearDown() {
        deleteFileIfExists(DATA_DIR + "locomotives.json");
        deleteFileIfExists(DATA_DIR + "rolling-stock.json");
        deleteFileIfExists(DATA_DIR + "maintenance-logs.json");
        IdGenerator.resetIdCounter("locomotive");
        IdGenerator.resetIdCounter("rolling_stock");
        IdGenerator.resetIdCounter("maintenance_log");
    }

    @Test
    void testCreateMaintenanceLog() throws Exception {
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = locomotiveRepository.save(locomotive);

        CreateMaintenanceLogRequest request = new CreateMaintenanceLogRequest();
        request.setInventoryItemId(saved.getId());
        request.setMaintenanceDate(LocalDate.now());
        request.setDescription("Routine cleaning");
        request.setWorkPerformed("Cleaned wheels and motor");
        request.setPerformedBy("Owner");
        request.setNotes("Running smoothly");

        mockMvc.perform(post("/api/maintenance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.inventoryItemId").value(saved.getId()))
                .andExpect(jsonPath("$.description").value("Routine cleaning"));
    }

    @Test
    void testCreateMaintenanceLog_ItemNotFound() throws Exception {
        CreateMaintenanceLogRequest request = new CreateMaintenanceLogRequest();
        request.setInventoryItemId(999L);
        request.setMaintenanceDate(LocalDate.now());
        request.setDescription("Test");
        request.setWorkPerformed("Test work");
        request.setPerformedBy("Owner");

        mockMvc.perform(post("/api/maintenance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMaintenanceLogById() throws Exception {
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = locomotiveRepository.save(locomotive);

        CreateMaintenanceLogRequest request = new CreateMaintenanceLogRequest();
        request.setInventoryItemId(saved.getId());
        request.setMaintenanceDate(LocalDate.now());
        request.setDescription("Test");
        request.setWorkPerformed("Test work");
        request.setPerformedBy("Owner");
        request.setNotes("Notes");

        String createResponse = mockMvc.perform(post("/api/maintenance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/maintenance/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value("Test"));
    }

    @Test
    void testGetMaintenanceLogsByItemId() throws Exception {
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = locomotiveRepository.save(locomotive);

        // Create multiple logs
        for (int i = 0; i < 3; i++) {
            CreateMaintenanceLogRequest request = new CreateMaintenanceLogRequest();
            request.setInventoryItemId(saved.getId());
            request.setMaintenanceDate(LocalDate.now());
            request.setDescription("Log " + i);
            request.setWorkPerformed("Work " + i);
            request.setPerformedBy("Owner");

            mockMvc.perform(post("/api/maintenance")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }

        mockMvc.perform(get("/api/maintenance/item/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testUpdateMaintenanceStatus() throws Exception {
        Locomotive locomotive = createTestLocomotive();
        locomotive.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        Locomotive saved = locomotiveRepository.save(locomotive);

        UpdateMaintenanceStatusRequest request = new UpdateMaintenanceStatusRequest();
        request.setMaintenanceStatus(MaintenanceStatus.NEEDS_MAINTENANCE);

        mockMvc.perform(put("/api/maintenance/status/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteMaintenanceLog() throws Exception {
        Locomotive locomotive = createTestLocomotive();
        Locomotive saved = locomotiveRepository.save(locomotive);

        CreateMaintenanceLogRequest request = new CreateMaintenanceLogRequest();
        request.setInventoryItemId(saved.getId());
        request.setMaintenanceDate(LocalDate.now());
        request.setDescription("Test");
        request.setWorkPerformed("Test work");
        request.setPerformedBy("Owner");

        String createResponse = mockMvc.perform(post("/api/maintenance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(delete("/api/maintenance/" + id))
                .andExpect(status().isNoContent());
    }

    private Locomotive createTestLocomotive() {
        Locomotive locomotive = new Locomotive();
        locomotive.setManufacturer("Athearn");
        locomotive.setModelNumber("RTR-87901");
        locomotive.setScale(Scale.HO);
        locomotive.setRoadName("Union Pacific");
        locomotive.setColor("Yellow/Gray");
        locomotive.setDescription("SD70M Locomotive");
        locomotive.setPurchasePrice(new BigDecimal("189.99"));
        locomotive.setPurchaseDate(LocalDate.of(2024, 1, 15));
        locomotive.setCurrentValue(new BigDecimal("189.99"));
        locomotive.setNotes("DCC Ready");
        locomotive.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        locomotive.setLocomotiveType(LocomotiveType.DIESEL);
        locomotive.setPowerType(PowerType.DCC_SOUND);
        locomotive.setRoadNumber("4141");
        return locomotive;
    }

    private void deleteFileIfExists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
