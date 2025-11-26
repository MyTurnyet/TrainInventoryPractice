package com.softwareascraft.practice.util;

import com.softwareascraft.practice.dto.request.*;
import com.softwareascraft.practice.dto.response.LocomotiveResponse;
import com.softwareascraft.practice.dto.response.MaintenanceLogResponse;
import com.softwareascraft.practice.dto.response.RollingStockResponse;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.model.MaintenanceLog;
import com.softwareascraft.practice.model.RollingStock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Static utility class for mapping between entities and DTOs (ANTI-PATTERN for teaching purposes)
 * - All static methods
 * - Cannot be mocked in tests
 * - No configuration or customization possible
 * - Duplicated mapping logic
 */
public class ModelMapper {

    // Private constructor to prevent instantiation
    private ModelMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Locomotive mappings

    public static Locomotive toLocomotive(CreateLocomotiveRequest request) {
        Locomotive locomotive = new Locomotive();
        locomotive.setManufacturer(request.getManufacturer());
        locomotive.setModelNumber(request.getModelNumber());
        locomotive.setScale(request.getScale());
        locomotive.setRoadName(request.getRoadName());
        locomotive.setColor(request.getColor());
        locomotive.setDescription(request.getDescription());
        locomotive.setPurchasePrice(request.getPurchasePrice());
        locomotive.setPurchaseDate(request.getPurchaseDate());
        locomotive.setCurrentValue(request.getCurrentValue());
        locomotive.setNotes(request.getNotes());
        locomotive.setMaintenanceStatus(request.getMaintenanceStatus());
        locomotive.setLocomotiveType(request.getLocomotiveType());
        locomotive.setPowerType(request.getPowerType());
        locomotive.setRoadNumber(request.getRoadNumber());
        return locomotive;
    }

    public static void updateLocomotive(Locomotive locomotive, UpdateLocomotiveRequest request) {
        locomotive.setManufacturer(request.getManufacturer());
        locomotive.setModelNumber(request.getModelNumber());
        locomotive.setScale(request.getScale());
        locomotive.setRoadName(request.getRoadName());
        locomotive.setColor(request.getColor());
        locomotive.setDescription(request.getDescription());
        locomotive.setPurchasePrice(request.getPurchasePrice());
        locomotive.setPurchaseDate(request.getPurchaseDate());
        locomotive.setCurrentValue(request.getCurrentValue());
        locomotive.setNotes(request.getNotes());
        locomotive.setMaintenanceStatus(request.getMaintenanceStatus());
        locomotive.setLocomotiveType(request.getLocomotiveType());
        locomotive.setPowerType(request.getPowerType());
        locomotive.setRoadNumber(request.getRoadNumber());
        locomotive.setLastModifiedDate(LocalDateTime.now());
    }

    public static LocomotiveResponse toLocomotiveResponse(Locomotive locomotive) {
        LocomotiveResponse response = new LocomotiveResponse();
        response.setId(locomotive.getId());
        response.setManufacturer(locomotive.getManufacturer());
        response.setModelNumber(locomotive.getModelNumber());
        response.setScale(locomotive.getScale());
        response.setRoadName(locomotive.getRoadName());
        response.setColor(locomotive.getColor());
        response.setDescription(locomotive.getDescription());
        response.setPurchasePrice(locomotive.getPurchasePrice());
        response.setPurchaseDate(locomotive.getPurchaseDate());
        response.setCurrentValue(locomotive.getCurrentValue());
        response.setNotes(locomotive.getNotes());
        response.setCreatedDate(locomotive.getCreatedDate());
        response.setLastModifiedDate(locomotive.getLastModifiedDate());
        response.setMaintenanceStatus(locomotive.getMaintenanceStatus());
        response.setLocomotiveType(locomotive.getLocomotiveType());
        response.setPowerType(locomotive.getPowerType());
        response.setRoadNumber(locomotive.getRoadNumber());
        response.setMaintenanceLogs(new ArrayList<>());
        return response;
    }

    public static List<LocomotiveResponse> toLocomotiveResponseList(List<Locomotive> locomotives) {
        return locomotives.stream()
                .map(ModelMapper::toLocomotiveResponse)
                .collect(Collectors.toList());
    }

    // RollingStock mappings

    public static RollingStock toRollingStock(CreateRollingStockRequest request) {
        RollingStock rollingStock = new RollingStock();
        rollingStock.setManufacturer(request.getManufacturer());
        rollingStock.setModelNumber(request.getModelNumber());
        rollingStock.setScale(request.getScale());
        rollingStock.setRoadName(request.getRoadName());
        rollingStock.setColor(request.getColor());
        rollingStock.setDescription(request.getDescription());
        rollingStock.setPurchasePrice(request.getPurchasePrice());
        rollingStock.setPurchaseDate(request.getPurchaseDate());
        rollingStock.setCurrentValue(request.getCurrentValue());
        rollingStock.setNotes(request.getNotes());
        rollingStock.setMaintenanceStatus(request.getMaintenanceStatus());
        rollingStock.setAarType(request.getAarType());
        rollingStock.setCarType(request.getCarType());
        rollingStock.setRoadNumber(request.getRoadNumber());
        rollingStock.setCapacity(request.getCapacity());
        return rollingStock;
    }

    public static void updateRollingStock(RollingStock rollingStock, UpdateRollingStockRequest request) {
        rollingStock.setManufacturer(request.getManufacturer());
        rollingStock.setModelNumber(request.getModelNumber());
        rollingStock.setScale(request.getScale());
        rollingStock.setRoadName(request.getRoadName());
        rollingStock.setColor(request.getColor());
        rollingStock.setDescription(request.getDescription());
        rollingStock.setPurchasePrice(request.getPurchasePrice());
        rollingStock.setPurchaseDate(request.getPurchaseDate());
        rollingStock.setCurrentValue(request.getCurrentValue());
        rollingStock.setNotes(request.getNotes());
        rollingStock.setMaintenanceStatus(request.getMaintenanceStatus());
        rollingStock.setAarType(request.getAarType());
        rollingStock.setCarType(request.getCarType());
        rollingStock.setRoadNumber(request.getRoadNumber());
        rollingStock.setCapacity(request.getCapacity());
        rollingStock.setLastModifiedDate(LocalDateTime.now());
    }

    public static RollingStockResponse toRollingStockResponse(RollingStock rollingStock) {
        RollingStockResponse response = new RollingStockResponse();
        response.setId(rollingStock.getId());
        response.setManufacturer(rollingStock.getManufacturer());
        response.setModelNumber(rollingStock.getModelNumber());
        response.setScale(rollingStock.getScale());
        response.setRoadName(rollingStock.getRoadName());
        response.setColor(rollingStock.getColor());
        response.setDescription(rollingStock.getDescription());
        response.setPurchasePrice(rollingStock.getPurchasePrice());
        response.setPurchaseDate(rollingStock.getPurchaseDate());
        response.setCurrentValue(rollingStock.getCurrentValue());
        response.setNotes(rollingStock.getNotes());
        response.setCreatedDate(rollingStock.getCreatedDate());
        response.setLastModifiedDate(rollingStock.getLastModifiedDate());
        response.setMaintenanceStatus(rollingStock.getMaintenanceStatus());
        response.setAarType(rollingStock.getAarType());
        response.setCarType(rollingStock.getCarType());
        response.setRoadNumber(rollingStock.getRoadNumber());
        response.setCapacity(rollingStock.getCapacity());
        response.setMaintenanceLogs(new ArrayList<>());
        return response;
    }

    public static List<RollingStockResponse> toRollingStockResponseList(List<RollingStock> rollingStocks) {
        return rollingStocks.stream()
                .map(ModelMapper::toRollingStockResponse)
                .collect(Collectors.toList());
    }

    // MaintenanceLog mappings

    public static MaintenanceLog toMaintenanceLog(CreateMaintenanceLogRequest request) {
        MaintenanceLog log = new MaintenanceLog();
        log.setInventoryItemId(request.getInventoryItemId());
        log.setMaintenanceDate(request.getMaintenanceDate());
        log.setDescription(request.getDescription());
        log.setWorkPerformed(request.getWorkPerformed());
        log.setPerformedBy(request.getPerformedBy());
        log.setNotes(request.getNotes());
        return log;
    }

    public static MaintenanceLogResponse toMaintenanceLogResponse(MaintenanceLog log) {
        MaintenanceLogResponse response = new MaintenanceLogResponse();
        response.setId(log.getId());
        response.setInventoryItemId(log.getInventoryItemId());
        response.setMaintenanceDate(log.getMaintenanceDate());
        response.setDescription(log.getDescription());
        response.setWorkPerformed(log.getWorkPerformed());
        response.setPerformedBy(log.getPerformedBy());
        response.setNotes(log.getNotes());
        response.setCreatedDate(log.getCreatedDate());
        return response;
    }

    public static List<MaintenanceLogResponse> toMaintenanceLogResponseList(List<MaintenanceLog> logs) {
        return logs.stream()
                .map(ModelMapper::toMaintenanceLogResponse)
                .collect(Collectors.toList());
    }
}
