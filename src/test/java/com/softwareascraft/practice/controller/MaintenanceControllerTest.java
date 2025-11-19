package com.softwareascraft.practice.controller;

import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.model.MaintenanceLog;
import com.softwareascraft.practice.service.MaintenanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MaintenanceControllerTest {

    @Mock
    private MaintenanceService service;

    @InjectMocks
    private MaintenanceController controller;

    @BeforeEach
    public void setup() {
        // Mocks are initialized by @ExtendWith(MockitoExtension.class)
    }

    @Test
    public void testCreate() {
        MaintenanceLog log = new MaintenanceLog();
        log.inventoryItemId = 1L;
        log.maintenanceDate = LocalDate.now();
        log.description = "Test";
        log.workPerformed = "Cleaning";

        MaintenanceLog saved = new MaintenanceLog();
        saved.id = 1L;
        saved.inventoryItemId = 1L;
        saved.maintenanceDate = LocalDate.now();
        saved.description = "Test";
        saved.workPerformed = "Cleaning";

        when(service.create(any(MaintenanceLog.class))).thenReturn(saved);

        ResponseEntity<MaintenanceLog> response = controller.create(log);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().id);
        verify(service, times(1)).create(any(MaintenanceLog.class));
    }

    @Test
    public void test2() {
        MaintenanceLog log = new MaintenanceLog();
        log.id = 1L;
        log.inventoryItemId = 1L;
        log.maintenanceDate = LocalDate.now();
        log.description = "Test";
        log.workPerformed = "Lubrication";

        when(service.getById(1L)).thenReturn(log);

        ResponseEntity<MaintenanceLog> response = controller.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test", response.getBody().description);
        verify(service, times(1)).getById(1L);
    }

    @Test
    public void testGetByItemId() {
        List<MaintenanceLog> logs = new ArrayList<>();

        MaintenanceLog log1 = new MaintenanceLog();
        log1.id = 1L;
        log1.inventoryItemId = 1L;
        log1.maintenanceDate = LocalDate.now();
        log1.description = "Test1";
        logs.add(log1);

        MaintenanceLog log2 = new MaintenanceLog();
        log2.id = 2L;
        log2.inventoryItemId = 1L;
        log2.maintenanceDate = LocalDate.now();
        log2.description = "Test2";
        logs.add(log2);

        when(service.getByItemId(1L)).thenReturn(logs);

        ResponseEntity<List<MaintenanceLog>> response = controller.getByItemId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).getByItemId(1L);
    }

    @Test
    public void testUpdateStatus() {
        doNothing().when(service).updateItemStatus(eq(1L), any(MaintenanceStatus.class));

        Map<String, String> body = new HashMap<>();
        body.put("status", "IN_MAINTENANCE");

        ResponseEntity<Void> response = controller.updateStatus(1L, body);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).updateItemStatus(1L, MaintenanceStatus.IN_MAINTENANCE);
    }
}
