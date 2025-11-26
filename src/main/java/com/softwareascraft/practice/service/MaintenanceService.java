package com.softwareascraft.practice.service;

import com.softwareascraft.practice.dto.request.CreateMaintenanceLogRequest;
import com.softwareascraft.practice.dto.response.MaintenanceLogResponse;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.model.MaintenanceLog;
import com.softwareascraft.practice.model.RollingStock;
import com.softwareascraft.practice.repository.LocomotiveRepository;
import com.softwareascraft.practice.repository.MaintenanceLogRepository;
import com.softwareascraft.practice.repository.RollingStockRepository;
import com.softwareascraft.practice.util.ModelMapper;

import java.util.List;

public class MaintenanceService {

    private final MaintenanceLogRepository maintenanceLogRepository = new MaintenanceLogRepository();
    private final LocomotiveRepository locomotiveRepository = new LocomotiveRepository();
    private final RollingStockRepository rollingStockRepository = new RollingStockRepository();

    public MaintenanceLogResponse createMaintenanceLog(CreateMaintenanceLogRequest request) {
        Long itemId = request.getInventoryItemId();
        boolean itemExists = locomotiveRepository.findById(itemId).isPresent() ||
                           rollingStockRepository.findById(itemId).isPresent();

        if (!itemExists) {
            throw new ResourceNotFoundException("Inventory item with id " + itemId + " not found");
        }

        MaintenanceLog log = ModelMapper.toMaintenanceLog(request);

        MaintenanceLog saved = maintenanceLogRepository.save(log);

        return ModelMapper.toMaintenanceLogResponse(saved);
    }

    public MaintenanceLogResponse getMaintenanceLogById(Long id) {
        MaintenanceLog log = maintenanceLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaintenanceLog", id));

        return ModelMapper.toMaintenanceLogResponse(log);
    }

    public List<MaintenanceLogResponse> getMaintenanceLogsByItemId(Long itemId) {
        List<MaintenanceLog> logs = maintenanceLogRepository.findByInventoryItemId(itemId);

        return ModelMapper.toMaintenanceLogResponseList(logs);
    }

    public void updateMaintenanceStatus(Long itemId, MaintenanceStatus status) {
        try {
            Locomotive locomotive = locomotiveRepository.findById(itemId)
                    .orElse(null);
            if (locomotive != null) {
                locomotive.setMaintenanceStatus(status);
                locomotiveRepository.update(itemId, locomotive);
                return;
            }
        } catch (Exception e) {
        }

        RollingStock rollingStock = rollingStockRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item with id " + itemId + " not found"));

        rollingStock.setMaintenanceStatus(status);
        rollingStockRepository.update(itemId, rollingStock);
    }

    public void deleteMaintenanceLog(Long id) {
        maintenanceLogRepository.deleteById(id);
    }
}
