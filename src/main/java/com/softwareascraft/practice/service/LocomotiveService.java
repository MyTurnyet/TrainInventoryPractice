package com.softwareascraft.practice.service;

import com.softwareascraft.practice.dto.request.CreateLocomotiveRequest;
import com.softwareascraft.practice.dto.request.UpdateLocomotiveRequest;
import com.softwareascraft.practice.dto.response.LocomotiveResponse;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.repository.LocomotiveRepository;
import com.softwareascraft.practice.util.ModelMapper;

import java.util.List;

/**
 * Service for Locomotive business logic (ANTI-PATTERN for teaching purposes)
 * - Creates repository with 'new' instead of dependency injection
 * - Calls static ModelMapper methods
 * - No interface abstraction
 * - Tightly coupled to concrete repository implementation
 */
public class LocomotiveService {

    // Direct instantiation - ANTI-PATTERN
    private final LocomotiveRepository locomotiveRepository;

    public LocomotiveService() {
        // Creating dependency with 'new' instead of injection (ANTI-PATTERN)
        this.locomotiveRepository = new LocomotiveRepository();
    }

    public LocomotiveResponse createLocomotive(CreateLocomotiveRequest request) {
        // Call static mapper method directly (ANTI-PATTERN)
        Locomotive locomotive = ModelMapper.toLocomotive(request);

        Locomotive saved = locomotiveRepository.save(locomotive);

        // Call static mapper method directly (ANTI-PATTERN)
        return ModelMapper.toLocomotiveResponse(saved);
    }

    public LocomotiveResponse getLocomotiveById(Long id) {
        Locomotive locomotive = locomotiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Locomotive", id));

        // Call static mapper method directly (ANTI-PATTERN)
        return ModelMapper.toLocomotiveResponse(locomotive);
    }

    public List<LocomotiveResponse> getAllLocomotives() {
        List<Locomotive> locomotives = locomotiveRepository.findAll();

        // Call static mapper method directly (ANTI-PATTERN)
        return ModelMapper.toLocomotiveResponseList(locomotives);
    }

    public LocomotiveResponse updateLocomotive(Long id, UpdateLocomotiveRequest request) {
        Locomotive existingLocomotive = locomotiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Locomotive", id));

        // Call static mapper method directly (ANTI-PATTERN)
        ModelMapper.updateLocomotive(existingLocomotive, request);

        Locomotive updated = locomotiveRepository.update(id, existingLocomotive);

        // Call static mapper method directly (ANTI-PATTERN)
        return ModelMapper.toLocomotiveResponse(updated);
    }

    public void deleteLocomotive(Long id) {
        locomotiveRepository.deleteById(id);
    }

    public List<LocomotiveResponse> getLocomotivesByManufacturer(String manufacturer) {
        List<Locomotive> locomotives = locomotiveRepository.findByManufacturer(manufacturer);
        return ModelMapper.toLocomotiveResponseList(locomotives);
    }
}
