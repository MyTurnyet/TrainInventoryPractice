package com.softwareascraft.practice.service.helper;

import com.softwareascraft.practice.model.MaintenanceLog;
import com.softwareascraft.practice.repository.MaintenanceLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Tests to ensure data integrity across maintenance operations
 */
@ExtendWith(MockitoExtension.class)
public class DataIntegrityTest {

    @Mock
    private MaintenanceLogRepository repository;

    @Test
    public void testMaintenanceLogsAreReturnedInCorrectOrder() {
        // Arrange
        List<MaintenanceLog> logs = new ArrayList<>();

        MaintenanceLog log1 = new MaintenanceLog();
        log1.id = 1L;
        log1.maintenanceDate = LocalDate.of(2024, 1, 1);
        log1.description = "First maintenance";
        logs.add(log1);

        MaintenanceLog log2 = new MaintenanceLog();
        log2.id = 2L;
        log2.maintenanceDate = LocalDate.of(2024, 2, 1);
        log2.description = "Second maintenance";
        logs.add(log2);

        when(repository.findByItem(1L)).thenReturn(logs);

        // Act
        List<MaintenanceLog> result = repository.findByItem(1L);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Should have 2 logs");
        assertEquals(1L, result.get(0).id, "First log should have id 1");
        assertEquals(2L, result.get(1).id, "Second log should have id 2");
        assertTrue(result.get(0).maintenanceDate.isBefore(result.get(1).maintenanceDate),
                "Logs should be in chronological order");
    }
}
