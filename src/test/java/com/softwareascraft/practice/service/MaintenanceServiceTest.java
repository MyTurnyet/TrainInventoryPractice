package com.softwareascraft.practice.service;

import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.model.MaintenanceLog;
import com.softwareascraft.practice.model.RollingStock;
import com.softwareascraft.practice.repository.LocomotiveRepository;
import com.softwareascraft.practice.repository.MaintenanceLogRepository;
import com.softwareascraft.practice.repository.RollingStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MaintenanceServiceTest {

    @Mock
    private MaintenanceLogRepository repository;

    @Mock
    private LocomotiveRepository locomotiveRepository;

    @Mock
    private RollingStockRepository rollingStockRepository;

    @InjectMocks
    private MaintenanceService service;

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

        when(repository.save(any(MaintenanceLog.class))).thenReturn(saved);

        MaintenanceLog result = service.create(log);

        assertNotNull(result);
        assertNotNull(result.id);
        verify(repository, times(1)).save(any(MaintenanceLog.class));
    }

    @Test
    public void testGetById() {
        MaintenanceLog log = new MaintenanceLog();
        log.id = 1L;
        log.inventoryItemId = 1L;
        log.maintenanceDate = LocalDate.now();
        log.description = "Test";

        when(repository.findById(1L)).thenReturn(log);

        MaintenanceLog result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Test", result.description);
        verify(repository, times(1)).findById(1L);
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

        when(repository.findByItem(1L)).thenReturn(logs);

        List<MaintenanceLog> result = service.getByItemId(1L);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByItem(1L);
    }

    @Test
    public void testDelete() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateItemStatusForLocomotive() {
        Locomotive locomotive = new Locomotive();
        locomotive.id = 1L;
        locomotive.manufacturer = "Test";
        locomotive.maintenanceStatus = MaintenanceStatus.OPERATIONAL;

        when(locomotiveRepository.findById(1L)).thenReturn(locomotive);
        when(locomotiveRepository.update(eq(1L), any(Locomotive.class))).thenReturn(locomotive);

        service.updateItemStatus(1L, MaintenanceStatus.IN_MAINTENANCE);

        verify(locomotiveRepository, times(1)).findById(1L);
        verify(locomotiveRepository, times(1)).update(eq(1L), any(Locomotive.class));
        verify(rollingStockRepository, never()).findById(any());
    }

    @Test
    public void testUpdateItemStatusForRollingStock() {
        RollingStock rollingStock = new RollingStock();
        rollingStock.id = 2L;
        rollingStock.manufacturer = "Test";
        rollingStock.maintenanceStatus = MaintenanceStatus.OPERATIONAL;

        when(locomotiveRepository.findById(2L)).thenReturn(null);
        when(rollingStockRepository.findById(2L)).thenReturn(rollingStock);
        when(rollingStockRepository.update(eq(2L), any(RollingStock.class))).thenReturn(rollingStock);

        service.updateItemStatus(2L, MaintenanceStatus.NEEDS_MAINTENANCE);

        verify(locomotiveRepository, times(1)).findById(2L);
        verify(rollingStockRepository, times(1)).findById(2L);
        verify(rollingStockRepository, times(1)).update(eq(2L), any(RollingStock.class));
    }

    @Test
    public void testUpdateItemStatusForNonExistentItem() {
        when(locomotiveRepository.findById(999L)).thenReturn(null);
        when(rollingStockRepository.findById(999L)).thenReturn(null);

        service.updateItemStatus(999L, MaintenanceStatus.OUT_OF_SERVICE);

        verify(locomotiveRepository, times(1)).findById(999L);
        verify(rollingStockRepository, times(1)).findById(999L);
        verify(locomotiveRepository, never()).update(any(), any());
        verify(rollingStockRepository, never()).update(any(), any());
    }

    @Test
    public void testGetByItemIdReturnsEmptyList() {
        when(repository.findByItem(999L)).thenReturn(new ArrayList<>());

        List<MaintenanceLog> result = service.getByItemId(999L);

        assertEquals(0, result.size());
        verify(repository, times(1)).findByItem(999L);
    }

    @Test
    public void testGetByIdReturnsNull() {
        when(repository.findById(999L)).thenReturn(null);

        MaintenanceLog result = service.getById(999L);

        assertEquals(null, result);
        verify(repository, times(1)).findById(999L);
    }
}
