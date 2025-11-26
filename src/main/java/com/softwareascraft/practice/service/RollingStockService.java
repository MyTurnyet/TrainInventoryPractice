package com.softwareascraft.practice.service;

import com.softwareascraft.practice.dto.request.CreateRollingStockRequest;
import com.softwareascraft.practice.dto.request.UpdateRollingStockRequest;
import com.softwareascraft.practice.dto.response.RollingStockResponse;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.RollingStock;
import com.softwareascraft.practice.repository.RollingStockRepository;
import com.softwareascraft.practice.util.ModelMapper;

import java.util.List;

public class RollingStockService {

    private final RollingStockRepository rollingStockRepository;

    public RollingStockService() {
        this.rollingStockRepository = new RollingStockRepository();
    }

    public RollingStockResponse createRollingStock(CreateRollingStockRequest request) {
        RollingStock rollingStock = ModelMapper.toRollingStock(request);

        RollingStock saved = rollingStockRepository.save(rollingStock);

        return ModelMapper.toRollingStockResponse(saved);
    }

    public RollingStockResponse getRollingStockById(Long id) {
        RollingStock rollingStock = rollingStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RollingStock", id));

        return ModelMapper.toRollingStockResponse(rollingStock);
    }

    public List<RollingStockResponse> getAllRollingStock() {
        List<RollingStock> rollingStocks = rollingStockRepository.findAll();

        return ModelMapper.toRollingStockResponseList(rollingStocks);
    }

    public RollingStockResponse updateRollingStock(Long id, UpdateRollingStockRequest request) {
        RollingStock existingRollingStock = rollingStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RollingStock", id));

        ModelMapper.updateRollingStock(existingRollingStock, request);

        RollingStock updated = rollingStockRepository.update(id, existingRollingStock);

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
