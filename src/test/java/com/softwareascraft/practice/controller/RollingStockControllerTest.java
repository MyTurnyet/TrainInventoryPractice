package com.softwareascraft.practice.controller;

import com.softwareascraft.practice.enums.AARType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.RollingStock;
import com.softwareascraft.practice.service.RollingStockService;
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
public class RollingStockControllerTest {

    @Mock
    private RollingStockService service;

    @InjectMocks
    private RollingStockController controller;

    @BeforeEach
    public void setup() {
        // Mocks are initialized by @ExtendWith(MockitoExtension.class)
    }

    @Test
    public void testCreate() {
        RollingStock rs = new RollingStock();
        rs.manufacturer = "Walthers";
        rs.scale = Scale.HO;
        rs.aarType = AARType.XM;
        rs.carType = "Box Car";

        RollingStock saved = new RollingStock();
        saved.id = 1L;
        saved.manufacturer = "Walthers";
        saved.scale = Scale.HO;
        saved.aarType = AARType.XM;
        saved.carType = "Box Car";

        when(service.create(any(RollingStock.class))).thenReturn(saved);

        ResponseEntity<RollingStock> response = controller.create(rs);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().id);
        verify(service, times(1)).create(any(RollingStock.class));
    }

    @Test
    public void test2() {
        Map<String, Object> rsData = new HashMap<>();
        rsData.put("id", 1L);
        rsData.put("manufacturer", "Test");
        rsData.put("scale", Scale.N);

        when(service.getWithLogs(1L)).thenReturn(rsData);

        ResponseEntity<Map<String, Object>> response = controller.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(service, times(1)).getWithLogs(1L);
    }

    @Test
    public void testGetByIdReturnsNotFoundWhenNull() {
        when(service.getWithLogs(999L)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = controller.getById(999L);

        assertEquals(404, response.getStatusCodeValue());
        verify(service, times(1)).getWithLogs(999L);
    }

    @Test
    public void testGetAll() {
        List<RollingStock> list = new ArrayList<>();

        RollingStock rs1 = new RollingStock();
        rs1.id = 1L;
        rs1.manufacturer = "Test1";
        list.add(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.id = 2L;
        rs2.manufacturer = "Test2";
        list.add(rs2);

        when(service.getAll()).thenReturn(list);

        ResponseEntity<List<RollingStock>> response = controller.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).getAll();
    }

    @Test
    public void testUpdate() {
        RollingStock updated = new RollingStock();
        updated.id = 1L;
        updated.manufacturer = "Updated";

        when(service.update(eq(1L), any(RollingStock.class))).thenReturn(updated);

        RollingStock input = new RollingStock();
        input.manufacturer = "Updated";

        ResponseEntity<RollingStock> response = controller.update(1L, input);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated", response.getBody().manufacturer);
        verify(service, times(1)).update(eq(1L), any(RollingStock.class));
    }

    @Test
    public void testUpdateReturnsNotFoundWhenNull() {
        when(service.update(eq(999L), any(RollingStock.class))).thenReturn(null);

        RollingStock input = new RollingStock();
        input.manufacturer = "Test";

        ResponseEntity<RollingStock> response = controller.update(999L, input);

        assertEquals(404, response.getStatusCodeValue());
        verify(service, times(1)).update(eq(999L), any(RollingStock.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(service).delete(1L);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).delete(1L);
    }

    @Test
    public void testSearch() {
        Map<String, Object> searchResult = new HashMap<>();
        List<RollingStock> results = new ArrayList<>();

        RollingStock rs1 = new RollingStock();
        rs1.id = 1L;
        rs1.manufacturer = "Athearn";
        results.add(rs1);

        searchResult.put("results", results);
        searchResult.put("total", 1);

        when(service.search("Athearn", null, null)).thenReturn(searchResult);

        ResponseEntity<Map<String, Object>> response = controller.search("Athearn", null, null);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        List<RollingStock> resultList = (List<RollingStock>) body.get("results");
        assertEquals(1, resultList.size());
        verify(service, times(1)).search("Athearn", null, null);
    }

    @Test
    public void testSearchWithMultipleParams() {
        Map<String, Object> searchResult = new HashMap<>();
        List<RollingStock> results = new ArrayList<>();

        RollingStock rs1 = new RollingStock();
        rs1.id = 1L;
        rs1.manufacturer = "Athearn";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.XM;
        results.add(rs1);

        searchResult.put("results", results);

        when(service.search("Athearn", Scale.HO, AARType.XM)).thenReturn(searchResult);

        ResponseEntity<Map<String, Object>> response = controller.search("Athearn", Scale.HO, AARType.XM);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).search("Athearn", Scale.HO, AARType.XM);
    }

    @Test
    public void testGetByAarType() {
        List<RollingStock> list = new ArrayList<>();

        RollingStock rs1 = new RollingStock();
        rs1.id = 1L;
        rs1.manufacturer = "Test1";
        rs1.aarType = AARType.XM;
        list.add(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.id = 2L;
        rs2.manufacturer = "Test2";
        rs2.aarType = AARType.XM;
        list.add(rs2);

        when(service.getByAarType(AARType.XM)).thenReturn(list);

        ResponseEntity<List<RollingStock>> response = controller.getByAarType(AARType.XM);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).getByAarType(AARType.XM);
    }
}
