package com.softwareascraft.practice.controller;

import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.service.LocomotiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

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
public class LocomotiveControllerTest {

    @Mock
    private LocomotiveService service;

    @InjectMocks
    private LocomotiveController controller;

    @BeforeEach
    public void setup() {
        // Mocks are initialized by @ExtendWith(MockitoExtension.class)
    }

    @Test
    public void test1() {
        Locomotive l = new Locomotive();
        l.manufacturer = "Test";
        l.scale = Scale.HO;
        l.locomotiveType = LocomotiveType.DIESEL;
        l.powerType = PowerType.DCC;

        Locomotive saved = new Locomotive();
        saved.id = 1L;
        saved.manufacturer = "Test";
        saved.scale = Scale.HO;
        saved.locomotiveType = LocomotiveType.DIESEL;
        saved.powerType = PowerType.DCC;

        when(service.create(any(Locomotive.class))).thenReturn(saved);

        ResponseEntity<Locomotive> response = controller.create(l);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().id);
        verify(service, times(1)).create(any(Locomotive.class));
    }

    @Test
    public void testGetById() {
        Map<String, Object> locoData = new HashMap<>();
        locoData.put("id", 1L);
        locoData.put("manufacturer", "Test");
        locoData.put("scale", Scale.N);

        when(service.getWithLogs(1L)).thenReturn(locoData);

        ResponseEntity<Map<String, Object>> response = controller.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(service, times(1)).getWithLogs(1L);
    }

    @Test
    public void testGetAll() {
        List<Locomotive> locos = new ArrayList<>();

        Locomotive l1 = new Locomotive();
        l1.id = 1L;
        l1.manufacturer = "Test1";
        locos.add(l1);

        Locomotive l2 = new Locomotive();
        l2.id = 2L;
        l2.manufacturer = "Test2";
        locos.add(l2);

        when(service.getAll()).thenReturn(locos);

        ResponseEntity<List<Locomotive>> response = controller.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).getAll();
    }

    @Test
    public void testUpdate() {
        Locomotive updated = new Locomotive();
        updated.id = 1L;
        updated.manufacturer = "Updated";
        updated.scale = Scale.O;
        updated.locomotiveType = LocomotiveType.ELECTRIC;
        updated.powerType = PowerType.BATTERY;

        when(service.update(eq(1L), any(Locomotive.class))).thenReturn(updated);

        Locomotive input = new Locomotive();
        input.manufacturer = "Updated";
        input.scale = Scale.O;
        input.locomotiveType = LocomotiveType.ELECTRIC;
        input.powerType = PowerType.BATTERY;

        ResponseEntity<Locomotive> response = controller.update(1L, input);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated", response.getBody().manufacturer);
        verify(service, times(1)).update(eq(1L), any(Locomotive.class));
    }

    @Test
    public void testSearch() {
        Map<String, Object> searchResult = new HashMap<>();
        List<Locomotive> results = new ArrayList<>();

        Locomotive l1 = new Locomotive();
        l1.id = 1L;
        l1.manufacturer = "Athearn";
        results.add(l1);

        searchResult.put("results", results);
        searchResult.put("total", 1);

        when(service.search("Athearn", null, null)).thenReturn(searchResult);

        ResponseEntity<Map<String, Object>> response = controller.search("Athearn", null, null);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        List<Locomotive> resultList = (List<Locomotive>) body.get("results");
        assertEquals(1, resultList.size());
        verify(service, times(1)).search("Athearn", null, null);
    }

    @Test
    public void testUpdateStatus() {
        doNothing().when(service).updateStatus(eq(1L), any(MaintenanceStatus.class));

        Map<String, String> body = new HashMap<>();
        body.put("status", "NEEDS_MAINTENANCE");

        ResponseEntity<Void> response = controller.updateStatus(1L, body);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).updateStatus(1L, MaintenanceStatus.NEEDS_MAINTENANCE);
    }

    @Test
    public void testDelete() {
        doNothing().when(service).delete(1L);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).delete(1L);
    }

    @Test
    public void testGetByIdReturnsNotFoundWhenNull() {
        when(service.getWithLogs(999L)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = controller.getById(999L);

        assertEquals(404, response.getStatusCodeValue());
        verify(service, times(1)).getWithLogs(999L);
    }

    @Test
    public void testUpdateReturnsNotFoundWhenNull() {
        when(service.update(eq(999L), any(Locomotive.class))).thenReturn(null);

        Locomotive input = new Locomotive();
        input.manufacturer = "Test";

        ResponseEntity<Locomotive> response = controller.update(999L, input);

        assertEquals(404, response.getStatusCodeValue());
        verify(service, times(1)).update(eq(999L), any(Locomotive.class));
    }

    @Test
    public void testSearchWithMultipleParameters() {
        Map<String, Object> searchResult = new HashMap<>();
        List<Locomotive> results = new ArrayList<>();

        Locomotive l1 = new Locomotive();
        l1.id = 1L;
        l1.manufacturer = "Athearn";
        l1.scale = Scale.HO;
        results.add(l1);

        searchResult.put("results", results);

        when(service.search("Athearn", Scale.HO, MaintenanceStatus.OPERATIONAL)).thenReturn(searchResult);

        ResponseEntity<Map<String, Object>> response = controller.search("Athearn", Scale.HO, MaintenanceStatus.OPERATIONAL);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).search("Athearn", Scale.HO, MaintenanceStatus.OPERATIONAL);
    }

    @Test
    public void testGetByScale() {
        Map<String, Object> searchResult = new HashMap<>();
        List<Locomotive> results = new ArrayList<>();

        Locomotive l1 = new Locomotive();
        l1.id = 1L;
        l1.scale = Scale.HO;
        results.add(l1);

        Locomotive l2 = new Locomotive();
        l2.id = 2L;
        l2.scale = Scale.HO;
        results.add(l2);

        searchResult.put("results", results);

        when(service.search(null, Scale.HO, null)).thenReturn(searchResult);

        ResponseEntity<List<Locomotive>> response = controller.getByScale(Scale.HO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).search(null, Scale.HO, null);
    }
}
