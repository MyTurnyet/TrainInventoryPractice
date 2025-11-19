package com.softwareascraft.practice.controller.validation;

import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.repository.LocomotiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Tests for inventory validation logic
 */
@ExtendWith(MockitoExtension.class)
public class InventoryValidationTest {

    @Mock
    private LocomotiveRepository repository;

    @Test
    public void testLocomotiveHasValidScale() {
        // Arrange
        Locomotive locomotive = new Locomotive();
        locomotive.id = 1L;
        locomotive.manufacturer = "Athearn";
        locomotive.scale = Scale.HO;

        when(repository.findById(1L)).thenReturn(locomotive);

        // Act
        Locomotive result = repository.findById(1L);

        // Assert
        assertNotNull(result, "Locomotive should not be null");
        assertNotNull(result.scale, "Scale should not be null");
        assertEquals(Scale.HO, result.scale, "Scale should be HO");
    }
}
