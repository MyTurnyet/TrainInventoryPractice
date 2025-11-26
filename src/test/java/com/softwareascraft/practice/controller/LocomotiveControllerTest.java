package com.softwareascraft.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softwareascraft.practice.dto.request.CreateLocomotiveRequest;
import com.softwareascraft.practice.dto.request.UpdateLocomotiveRequest;
import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;
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

/**
 * Controller test with real service and repository (ANTI-PATTERN for teaching purposes)
 * - Cannot easily mock the service (created with 'new' in controller)
 * - Full integration test touching file system
 * - Slow execution
 * - Hard to test edge cases
 */
@SpringBootTest
@AutoConfigureMockMvc
class LocomotiveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private static final String DATA_DIR = "data/";
    private static final String TEST_FILE = "locomotives.json";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        IdGenerator.resetIdCounter("locomotive");
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
    void testCreateLocomotive() throws Exception {
        CreateLocomotiveRequest request = createLocomotiveRequest();

        mockMvc.perform(post("/api/locomotives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.manufacturer").value("Athearn"))
                .andExpect(jsonPath("$.scale").value("HO"));
    }

    @Test
    void testGetLocomotiveById() throws Exception {
        CreateLocomotiveRequest request = createLocomotiveRequest();

        // Create first
        String createResponse = mockMvc.perform(post("/api/locomotives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        // Then get
        mockMvc.perform(get("/api/locomotives/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.manufacturer").value("Athearn"));
    }

    @Test
    void testGetLocomotiveById_NotFound() throws Exception {
        mockMvc.perform(get("/api/locomotives/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllLocomotives() throws Exception {
        CreateLocomotiveRequest request1 = createLocomotiveRequest();
        CreateLocomotiveRequest request2 = createLocomotiveRequest();

        mockMvc.perform(post("/api/locomotives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));

        mockMvc.perform(post("/api/locomotives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)));

        mockMvc.perform(get("/api/locomotives"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testUpdateLocomotive() throws Exception {
        CreateLocomotiveRequest createRequest = createLocomotiveRequest();

        String createResponse = mockMvc.perform(post("/api/locomotives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

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

        mockMvc.perform(put("/api/locomotives/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturer").value("Updated Manufacturer"))
                .andExpect(jsonPath("$.scale").value("N"));
    }

    @Test
    void testDeleteLocomotive() throws Exception {
        CreateLocomotiveRequest request = createLocomotiveRequest();

        String createResponse = mockMvc.perform(post("/api/locomotives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(delete("/api/locomotives/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/locomotives/" + id))
                .andExpect(status().isNotFound());
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
