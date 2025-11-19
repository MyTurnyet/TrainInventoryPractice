package com.softwareascraft.practice.service;

import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.model.MaintenanceLog;
import com.softwareascraft.practice.repository.LocomotiveRepository;
import com.softwareascraft.practice.repository.MaintenanceLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocomotiveServiceTest {

    @Mock
    private LocomotiveRepository repository;

    @Mock
    private MaintenanceLogRepository maintenanceLogRepository;

    @InjectMocks
    private LocomotiveService service;

    @BeforeEach
    public void setup() {
        // Mocks are initialized by @ExtendWith(MockitoExtension.class)
    }

    @Test
    public void testCreate() {
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

        when(repository.save(any(Locomotive.class))).thenReturn(saved);

        Locomotive result = service.create(l);

        assertNotNull(result);
        assertNotNull(result.id);
        verify(repository, times(1)).save(any(Locomotive.class));
    }

    @Test
    public void test2() {
        Locomotive l = new Locomotive();
        l.id = 1L;
        l.manufacturer = "Test";
        l.scale = Scale.HO;
        l.locomotiveType = LocomotiveType.STEAM;
        l.powerType = PowerType.DC;

        when(repository.findById(1L)).thenReturn(l);

        Locomotive found = service.getById(1L);

        assertNotNull(found);
        assertEquals("Test", found.manufacturer);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void testGetAll() {
        List<Locomotive> list = new ArrayList<>();

        Locomotive l1 = new Locomotive();
        l1.id = 1L;
        l1.manufacturer = "Test1";
        l1.scale = Scale.HO;
        l1.locomotiveType = LocomotiveType.DIESEL;
        l1.powerType = PowerType.DCC;
        list.add(l1);

        Locomotive l2 = new Locomotive();
        l2.id = 2L;
        l2.manufacturer = "Test2";
        l2.scale = Scale.N;
        l2.locomotiveType = LocomotiveType.STEAM;
        l2.powerType = PowerType.DC;
        list.add(l2);

        when(repository.findAll()).thenReturn(list);

        List<Locomotive> all = service.getAll();

        assertEquals(2, all.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testSearch() {
        List<Locomotive> allLocos = new ArrayList<>();

        Locomotive l1 = new Locomotive();
        l1.id = 1L;
        l1.manufacturer = "Athearn";
        l1.scale = Scale.HO;
        l1.locomotiveType = LocomotiveType.DIESEL;
        l1.powerType = PowerType.DCC;
        l1.maintenanceStatus = MaintenanceStatus.OPERATIONAL;
        allLocos.add(l1);

        Locomotive l2 = new Locomotive();
        l2.id = 2L;
        l2.manufacturer = "Walthers";
        l2.scale = Scale.HO;
        l2.locomotiveType = LocomotiveType.STEAM;
        l2.powerType = PowerType.DC;
        l2.maintenanceStatus = MaintenanceStatus.NEEDS_MAINTENANCE;
        allLocos.add(l2);

        when(repository.findAll()).thenReturn(allLocos);

        Map<String, Object> result = service.search("Athearn", null, null);
        List<Locomotive> list = (List<Locomotive>) result.get("results");

        assertEquals(1, list.size());
        assertEquals("Athearn", list.get(0).manufacturer);
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testUpdateStatus() {
        Locomotive l = new Locomotive();
        l.id = 1L;
        l.manufacturer = "Test";
        l.scale = Scale.O;
        l.locomotiveType = LocomotiveType.ELECTRIC;
        l.powerType = PowerType.BATTERY;
        l.maintenanceStatus = MaintenanceStatus.OPERATIONAL;

        when(repository.findById(1L)).thenReturn(l);
        when(repository.update(eq(1L), any(Locomotive.class))).thenReturn(l);

        service.updateStatus(1L, MaintenanceStatus.NEEDS_MAINTENANCE);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).update(eq(1L), any(Locomotive.class));
    }

    @Test
    public void testUpdate() {
        Locomotive updated = new Locomotive();
        updated.id = 1L;
        updated.manufacturer = "Updated";

        when(repository.update(eq(1L), any(Locomotive.class))).thenReturn(updated);

        Locomotive input = new Locomotive();
        input.manufacturer = "Updated";

        Locomotive result = service.update(1L, input);

        assertNotNull(result);
        assertEquals("Updated", result.manufacturer);
        verify(repository, times(1)).update(eq(1L), any(Locomotive.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    public void testSearchWithEmptyResults() {
        List<Locomotive> allLocos = new ArrayList<>();

        Locomotive l1 = new Locomotive();
        l1.id = 1L;
        l1.manufacturer = "Athearn";
        l1.scale = Scale.HO;
        allLocos.add(l1);

        when(repository.findAll()).thenReturn(allLocos);

        Map<String, Object> result = service.search("Walthers", null, null);
        List<Locomotive> list = (List<Locomotive>) result.get("results");

        assertEquals(0, list.size());
        assertEquals(0, result.get("total"));
    }

    @Test
    public void testSearchWithMultipleCriteria() {
        List<Locomotive> allLocos = new ArrayList<>();

        Locomotive l1 = new Locomotive();
        l1.id = 1L;
        l1.manufacturer = "Athearn";
        l1.scale = Scale.HO;
        l1.maintenanceStatus = MaintenanceStatus.OPERATIONAL;
        allLocos.add(l1);

        Locomotive l2 = new Locomotive();
        l2.id = 2L;
        l2.manufacturer = "Athearn";
        l2.scale = Scale.N;
        l2.maintenanceStatus = MaintenanceStatus.NEEDS_MAINTENANCE;
        allLocos.add(l2);

        when(repository.findAll()).thenReturn(allLocos);

        Map<String, Object> result = service.search("Athearn", Scale.HO, MaintenanceStatus.OPERATIONAL);
        List<Locomotive> list = (List<Locomotive>) result.get("results");

        assertEquals(1, list.size());
        assertEquals(Scale.HO, list.get(0).scale);
    }

    @Test
    public void testGetWithLogs() {
        Locomotive l = new Locomotive();
        l.id = 1L;
        l.manufacturer = "Test";
        l.scale = Scale.HO;

        List<MaintenanceLog> logs = new ArrayList<>();
        MaintenanceLog log = new MaintenanceLog();
        log.id = 1L;
        log.description = "Test log";
        logs.add(log);

        when(repository.findById(1L)).thenReturn(l);
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

        assertEquals(null, result);
        verify(repository, times(1)).findById(999L);
        verify(maintenanceLogRepository, never()).findByItem(any());
    }

    @Test
    public void testUpdateStatusWhenLocomotiveNotFound() {
        when(repository.findById(999L)).thenReturn(null);

        service.updateStatus(999L, MaintenanceStatus.IN_MAINTENANCE);

        verify(repository, times(1)).findById(999L);
        verify(repository, never()).update(any(), any());
    }

    @Test
    public void testUpdateReturnsNullWhenNotFound() {
        when(repository.update(eq(999L), any(Locomotive.class))).thenReturn(null);

        Locomotive input = new Locomotive();
        input.manufacturer = "Test";

        Locomotive result = service.update(999L, input);

        assertEquals(null, result);
        verify(repository, times(1)).update(eq(999L), any(Locomotive.class));
    }
}
