package com.softwareascraft.practice.service;

import com.softwareascraft.practice.enums.AARType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.MaintenanceLog;
import com.softwareascraft.practice.model.RollingStock;
import com.softwareascraft.practice.repository.MaintenanceLogRepository;
import com.softwareascraft.practice.repository.RollingStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RollingStockServiceTest {

    @Mock
    private RollingStockRepository repository;

    @Mock
    private MaintenanceLogRepository maintenanceLogRepository;

    @InjectMocks
    private RollingStockService service;

    @BeforeEach
    public void setup() {
        // Mocks are initialized by @ExtendWith(MockitoExtension.class)
    }

    @Test
    public void test1() {
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

        when(repository.save(any(RollingStock.class))).thenReturn(saved);

        RollingStock result = service.create(rs);

        assertNotNull(result);
        assertNotNull(result.id);
        verify(repository, times(1)).save(any(RollingStock.class));
    }

    @Test
    public void testGetById() {
        RollingStock rs = new RollingStock();
        rs.id = 1L;
        rs.manufacturer = "Test";
        rs.scale = Scale.N;
        rs.aarType = AARType.FC;
        rs.carType = "Flat Car";

        when(repository.findById(1L)).thenReturn(rs);

        RollingStock found = service.getById(1L);

        assertNotNull(found);
        assertEquals("Test", found.manufacturer);
        verify(repository, times(1)).findById(1L);
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

        when(repository.findAll()).thenReturn(list);

        List<RollingStock> result = service.getAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testUpdate() {
        RollingStock updated = new RollingStock();
        updated.id = 1L;
        updated.manufacturer = "Updated";

        when(repository.update(eq(1L), any(RollingStock.class))).thenReturn(updated);

        RollingStock result = service.update(1L, updated);

        assertNotNull(result);
        assertEquals("Updated", result.manufacturer);
        verify(repository, times(1)).update(eq(1L), any(RollingStock.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    public void testSearch() {
        List<RollingStock> allRollingStock = new ArrayList<>();

        RollingStock rs1 = new RollingStock();
        rs1.id = 1L;
        rs1.manufacturer = "Athearn";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.XM;
        allRollingStock.add(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.id = 2L;
        rs2.manufacturer = "Walthers";
        rs2.scale = Scale.N;
        rs2.aarType = AARType.FC;
        allRollingStock.add(rs2);

        when(repository.findAll()).thenReturn(allRollingStock);

        Map<String, Object> result = service.search(null, Scale.HO, null);
        List<RollingStock> list = (List<RollingStock>) result.get("results");

        assertEquals(1, list.size());
        assertEquals("Athearn", list.get(0).manufacturer);
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testSearchByManufacturer() {
        List<RollingStock> allRollingStock = new ArrayList<>();

        RollingStock rs1 = new RollingStock();
        rs1.id = 1L;
        rs1.manufacturer = "Athearn";
        rs1.scale = Scale.HO;
        allRollingStock.add(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.id = 2L;
        rs2.manufacturer = "Walthers";
        rs2.scale = Scale.HO;
        allRollingStock.add(rs2);

        when(repository.findAll()).thenReturn(allRollingStock);

        Map<String, Object> result = service.search("Athearn", null, null);
        List<RollingStock> list = (List<RollingStock>) result.get("results");

        assertEquals(1, list.size());
        assertEquals("Athearn", list.get(0).manufacturer);
    }

    @Test
    public void testSearchWithMultipleCriteria() {
        List<RollingStock> allRollingStock = new ArrayList<>();

        RollingStock rs1 = new RollingStock();
        rs1.id = 1L;
        rs1.manufacturer = "Athearn";
        rs1.scale = Scale.HO;
        rs1.aarType = AARType.XM;
        allRollingStock.add(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.id = 2L;
        rs2.manufacturer = "Athearn";
        rs2.scale = Scale.N;
        rs2.aarType = AARType.FC;
        allRollingStock.add(rs2);

        when(repository.findAll()).thenReturn(allRollingStock);

        Map<String, Object> result = service.search("Athearn", Scale.HO, AARType.XM);
        List<RollingStock> list = (List<RollingStock>) result.get("results");

        assertEquals(1, list.size());
        assertEquals(Scale.HO, list.get(0).scale);
    }

    @Test
    public void testGetByAarType() {
        List<RollingStock> xmList = new ArrayList<>();

        RollingStock rs1 = new RollingStock();
        rs1.id = 1L;
        rs1.manufacturer = "Test1";
        rs1.aarType = AARType.XM;
        xmList.add(rs1);

        RollingStock rs2 = new RollingStock();
        rs2.id = 2L;
        rs2.manufacturer = "Test2";
        rs2.aarType = AARType.XM;
        xmList.add(rs2);

        when(repository.findByAarType(AARType.XM)).thenReturn(xmList);

        List<RollingStock> result = service.getByAarType(AARType.XM);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByAarType(AARType.XM);
    }

    @Test
    public void testGetWithLogs() {
        RollingStock rs = new RollingStock();
        rs.id = 1L;
        rs.manufacturer = "Test";
        rs.scale = Scale.HO;
        rs.maintenanceStatus = MaintenanceStatus.OPERATIONAL;

        List<MaintenanceLog> logs = new ArrayList<>();
        MaintenanceLog log = new MaintenanceLog();
        log.id = 1L;
        log.inventoryItemId = 1L;
        log.description = "Test log";
        logs.add(log);

        when(repository.findById(1L)).thenReturn(rs);
        when(maintenanceLogRepository.findByItem(1L)).thenReturn(logs);

        Map<String, Object> result = service.getWithLogs(1L);

        assertNotNull(result);
        assertEquals(1L, result.get("id"));
        assertEquals("Test", result.get("manufacturer"));
        List<MaintenanceLog> resultLogs = (List<MaintenanceLog>) result.get("maintenanceLogs");
        assertEquals(1, resultLogs.size());
        verify(repository, times(1)).findById(1L);
        verify(maintenanceLogRepository, times(1)).findByItem(1L);
    }

    @Test
    public void testGetWithLogsReturnsNullWhenNotFound() {
        when(repository.findById(999L)).thenReturn(null);

        Map<String, Object> result = service.getWithLogs(999L);

        assertNull(result);
        verify(repository, times(1)).findById(999L);
        verify(maintenanceLogRepository, never()).findByItem(any());
    }

    @Test
    public void testGetByIdReturnsNull() {
        when(repository.findById(999L)).thenReturn(null);

        RollingStock result = service.getById(999L);

        assertNull(result);
        verify(repository, times(1)).findById(999L);
    }

    @Test
    public void testUpdateReturnsNullWhenNotFound() {
        when(repository.update(eq(999L), any(RollingStock.class))).thenReturn(null);

        RollingStock rs = new RollingStock();
        rs.manufacturer = "Test";

        RollingStock result = service.update(999L, rs);

        assertNull(result);
        verify(repository, times(1)).update(eq(999L), any(RollingStock.class));
    }
}
