package com.softwareascraft.practice.service;

import com.softwareascraft.practice.dto.request.CreateLocomotiveRequest;
import com.softwareascraft.practice.dto.request.UpdateLocomotiveRequest;
import com.softwareascraft.practice.dto.response.LocomotiveResponse;
import com.softwareascraft.practice.exception.ResourceNotFoundException;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.repository.LocomotiveRepository;
import com.softwareascraft.practice.util.ModelMapper;

import java.util.List;

public class LocomotiveService {

    private final LocomotiveRepository locomotiveRepository;

    public LocomotiveService() {
        this.locomotiveRepository = new LocomotiveRepository();
    }

    public LocomotiveResponse createLocomotive(CreateLocomotiveRequest request) {
        Locomotive locomotive = ModelMapper.toLocomotive(request);

        Locomotive saved = locomotiveRepository.save(locomotive);

        return ModelMapper.toLocomotiveResponse(saved);
    }

    public LocomotiveResponse getLocomotiveById(Long id) {
        Locomotive locomotive = locomotiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Locomotive", id));

        return ModelMapper.toLocomotiveResponse(locomotive);
    }

    public List<LocomotiveResponse> getAllLocomotives() {
        List<Locomotive> locomotives = locomotiveRepository.findAll();

        return ModelMapper.toLocomotiveResponseList(locomotives);
    }

    public LocomotiveResponse updateLocomotive(Long id, UpdateLocomotiveRequest request) {
        Locomotive existingLocomotive = locomotiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Locomotive", id));

        ModelMapper.updateLocomotive(existingLocomotive, request);

        Locomotive updated = locomotiveRepository.update(id, existingLocomotive);

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
