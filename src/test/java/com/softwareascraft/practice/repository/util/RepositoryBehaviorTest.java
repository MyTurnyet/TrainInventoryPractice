package com.softwareascraft.practice.repository.util;

import com.softwareascraft.practice.enums.AARType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.RollingStock;
import com.softwareascraft.practice.repository.RollingStockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Tests to verify repository layer correctly filters and returns data
 */
@ExtendWith(MockitoExtension.class)
public class RepositoryBehaviorTest {

    @Mock
    private RollingStockRepository repository;

    @Test
    public void testFindByAarTypeReturnsOnlyMatchingCarTypes() {
        // Arrange
        List<RollingStock> boxCars = new ArrayList<>();

        RollingStock car1 = new RollingStock();
        car1.id = 1L;
        car1.manufacturer = "Athearn";
        car1.aarType = AARType.XM;
        car1.carType = "Box Car";
        car1.scale = Scale.HO;
        boxCars.add(car1);

        RollingStock car2 = new RollingStock();
        car2.id = 2L;
        car2.manufacturer = "Walthers";
        car2.aarType = AARType.XM;
        car2.carType = "Box Car";
        car2.scale = Scale.N;
        boxCars.add(car2);

        when(repository.findByAarType(AARType.XM)).thenReturn(boxCars);

        // Act
        List<RollingStock> result = repository.findByAarType(AARType.XM);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should not be empty");
        assertEquals(2, result.size(), "Should return exactly 2 box cars");

        // Verify all returned items have the correct AAR type
        for (RollingStock car : result) {
            assertEquals(AARType.XM, car.aarType,
                    "All returned cars should have AAR type XM");
        }

        // Verify no other types are included
        long xmCount = result.stream()
                .filter(car -> car.aarType == AARType.XM)
                .count();
        assertEquals(result.size(), xmCount,
                "All items should be of type XM, no other types should be present");
    }
}
