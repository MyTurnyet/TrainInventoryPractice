package com.softwareascraft.practice.service;

import com.softwareascraft.practice.dto.request.CreateRollingStockRequest;
import com.softwareascraft.practice.dto.request.UpdateRollingStockRequest;
import com.softwareascraft.practice.dto.response.RollingStockResponse;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.RollingStock;
import com.softwareascraft.practice.repository.RollingStockRepository;
import com.softwareascraft.practice.util.ModelMapper;

import java.util.List;

/**
 * Service for RollingStock business logic (ANTI-PATTERN for teaching purposes)
 * - Creates repository with 'new' instead of dependency injection
 * - Calls static ModelMapper methods
 * - No interface abstraction
 * - Tightly coupled to concrete repository implementation
 */
public class RollingStockService {

    // Direct instantiation - ANTI-PATTERN
    private final RollingStockRepository rollingStockRepository;

    public RollingStockService() {
        // Creating dependency with 'new' instead of injection (ANTI-PATTERN)
        this.rollingStockRepository = new RollingStockRepository();
    }

    public RollingStockResponse createRollingStock(CreateRollingStockRequest request) {
        // Call static mapper method directly (ANTI-PATTERN)
        RollingStock rollingStock = ModelMapper.toRollingStock(request);

        RollingStock saved = rollingStockRepository.save(rollingStock);

        // Call static mapper method directly (ANTI-PATTERN)
        return ModelMapper.toRollingStockResponse(saved);
    }

    public RollingStockResponse getRollingStockById(Long id) {
        RollingStock rollingStock = rollingStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RollingStock", id));

        // Call static mapper method directly (ANTI-PATTERN)
        return ModelMapper.toRollingStockResponse(rollingStock);
    }

    public List<RollingStockResponse> getAllRollingStock() {
        List<RollingStock> rollingStocks = rollingStockRepository.findAll();

        // Call static mapper method directly (ANTI-PATTERN)
        return ModelMapper.toRollingStockResponseList(rollingStocks);
    }

    public RollingStockResponse updateRollingStock(Long id, UpdateRollingStockRequest request) {
        RollingStock existingRollingStock = rollingStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RollingStock", id));

        // Call static mapper method directly (ANTI-PATTERN)
        ModelMapper.updateRollingStock(existingRollingStock, request);

        RollingStock updated = rollingStockRepository.update(id, existingRollingStock);

        // Call static mapper method directly (ANTI-PATTERN)
        return ModelMapper.toRollingStockResponse(updated);
    }

    public void deleteRollingStock(Long id) {
        rollingStockRepository.deleteById(id);
    }

    public List<RollingStockResponse> getRollingStockByManufacturer(String manufacturer) {
        List<RollingStock> rollingStocks = rollingStockRepository.findByManufacturer(manufacturer);
        return ModelMapper.toRollingStockResponseList(rollingStocks);
    }
}
