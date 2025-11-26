package com.softwareascraft.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softwareascraft.practice.dto.request.CreateRollingStockRequest;
import com.softwareascraft.practice.dto.request.UpdateRollingStockRequest;
import com.softwareascraft.practice.enums.AARType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
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
class RollingStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private static final String DATA_DIR = "data/";
    private static final String TEST_FILE = "rolling-stock.json";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        IdGenerator.resetIdCounter("rolling_stock");
    }

    @AfterEach
    void tearDown() {
        File dataFile = new File(DATA_DIR + TEST_FILE);
        if (dataFile.exists()) {
            dataFile.delete();
        }
        IdGenerator.resetIdCounter("rolling_stock");
    }

    @Test
    void testCreateRollingStock() throws Exception {
        CreateRollingStockRequest request = createRollingStockRequest();

        mockMvc.perform(post("/api/rolling-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.manufacturer").value("Walthers"))
                .andExpect(jsonPath("$.scale").value("HO"));
    }

    @Test
    void testGetRollingStockById() throws Exception {
        CreateRollingStockRequest request = createRollingStockRequest();

        String createResponse = mockMvc.perform(post("/api/rolling-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/rolling-stock/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.manufacturer").value("Walthers"));
    }

    @Test
    void testGetRollingStockById_NotFound() throws Exception {
        mockMvc.perform(get("/api/rolling-stock/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllRollingStock() throws Exception {
        CreateRollingStockRequest request1 = createRollingStockRequest();
        CreateRollingStockRequest request2 = createRollingStockRequest();

        mockMvc.perform(post("/api/rolling-stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));

        mockMvc.perform(post("/api/rolling-stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)));

        mockMvc.perform(get("/api/rolling-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testUpdateRollingStock() throws Exception {
        CreateRollingStockRequest createRequest = createRollingStockRequest();

        String createResponse = mockMvc.perform(post("/api/rolling-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        UpdateRollingStockRequest updateRequest = new UpdateRollingStockRequest();
        updateRequest.setManufacturer("Updated Manufacturer");
        updateRequest.setModelNumber("NEW-MODEL");
        updateRequest.setScale(Scale.N);
        updateRequest.setRoadName("Updated Road");
        updateRequest.setColor("Red");
        updateRequest.setDescription("Updated Description");
        updateRequest.setPurchasePrice(new BigDecimal("49.99"));
        updateRequest.setPurchaseDate(LocalDate.now());
        updateRequest.setCurrentValue(new BigDecimal("49.99"));
        updateRequest.setNotes("Updated notes");
        updateRequest.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        updateRequest.setAarType(AARType.XM);
        updateRequest.setCarType("Box Car");
        updateRequest.setRoadNumber("8888");
        updateRequest.setCapacity("40 foot");

        mockMvc.perform(put("/api/rolling-stock/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturer").value("Updated Manufacturer"))
                .andExpect(jsonPath("$.scale").value("N"));
    }

    @Test
    void testDeleteRollingStock() throws Exception {
        CreateRollingStockRequest request = createRollingStockRequest();

        String createResponse = mockMvc.perform(post("/api/rolling-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(delete("/api/rolling-stock/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/rolling-stock/" + id))
                .andExpect(status().isNotFound());
    }

    private CreateRollingStockRequest createRollingStockRequest() {
        CreateRollingStockRequest request = new CreateRollingStockRequest();
        request.setManufacturer("Walthers");
        request.setModelNumber("910-2961");
        request.setScale(Scale.HO);
        request.setRoadName("Santa Fe");
        request.setColor("Silver/Red");
        request.setDescription("50' Reefer");
        request.setPurchasePrice(new BigDecimal("34.99"));
        request.setPurchaseDate(LocalDate.of(2024, 2, 10));
        request.setCurrentValue(new BigDecimal("34.99"));
        request.setNotes("Metal wheels");
        request.setMaintenanceStatus(MaintenanceStatus.OPERATIONAL);
        request.setAarType(AARType.RB);
        request.setCarType("Refrigerator Car");
        request.setRoadNumber("23456");
        request.setCapacity("50 foot");
        return request;
    }
}
