package com.softwareascraft.practice.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.softwareascraft.practice.enums.AARType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.RollingStock;
import com.softwareascraft.practice.util.IdGenerator;
import com.softwareascraft.practice.util.JsonFileManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RollingStockRepository {

    private static final String FILE_NAME = "rolling-stock.json";
    private static final String ENTITY_TYPE = "rolling_stock";

    public RollingStockRepository() {
        List<RollingStock> existing = findAll();
        IdGenerator.initializeCounter(ENTITY_TYPE, existing);
    }

    public RollingStock save(RollingStock rollingStock) {
        List<RollingStock> rollingStocks = findAll();

        Long newId = IdGenerator.generateId(ENTITY_TYPE);
        rollingStock.setId(newId);

        rollingStocks.add(rollingStock);

        JsonFileManager.writeToFile(FILE_NAME, rollingStocks);

        return rollingStock;
    }

    public Optional<RollingStock> findById(Long id) {
        List<RollingStock> rollingStocks = findAll();
        return rollingStocks.stream()
                .filter(rs -> rs.getId().equals(id))
                .findFirst();
    }

    public List<RollingStock> findAll() {
        return JsonFileManager.readFromFile(FILE_NAME, new TypeReference<List<RollingStock>>() {});
    }

    public RollingStock update(Long id, RollingStock updatedRollingStock) {
        List<RollingStock> rollingStocks = findAll();

        boolean found = false;
        for (int i = 0; i < rollingStocks.size(); i++) {
            if (rollingStocks.get(i).getId().equals(id)) {
                updatedRollingStock.setId(id);
                rollingStocks.set(i, updatedRollingStock);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ResourceNotFoundException("RollingStock", id);
        }

        JsonFileManager.writeToFile(FILE_NAME, rollingStocks);

        return updatedRollingStock;
    }

    public void deleteById(Long id) {
        List<RollingStock> rollingStocks = findAll();

        boolean removed = rollingStocks.removeIf(rs -> rs.getId().equals(id));

        if (!removed) {
            throw new ResourceNotFoundException("RollingStock", id);
        }

        JsonFileManager.writeToFile(FILE_NAME, rollingStocks);
    }

    public List<RollingStock> findByManufacturer(String manufacturer) {
        return findAll().stream()
                .filter(rs -> rs.getManufacturer() != null &&
                            rs.getManufacturer().equalsIgnoreCase(manufacturer))
                .collect(Collectors.toList());
    }

    public List<RollingStock> findByScale(Scale scale) {
        return findAll().stream()
                .filter(rs -> rs.getScale() == scale)
                .collect(Collectors.toList());
    }

    public List<RollingStock> findByMaintenanceStatus(MaintenanceStatus status) {
        return findAll().stream()
                .filter(rs -> rs.getMaintenanceStatus() == status)
                .collect(Collectors.toList());
    }

    public List<RollingStock> findByRoadName(String roadName) {
        return findAll().stream()
                .filter(rs -> rs.getRoadName() != null &&
                            rs.getRoadName().equalsIgnoreCase(roadName))
                .collect(Collectors.toList());
    }

    public List<RollingStock> findByAarType(AARType aarType) {
        return findAll().stream()
                .filter(rs -> rs.getAarType() == aarType)
                .collect(Collectors.toList());
    }
}
