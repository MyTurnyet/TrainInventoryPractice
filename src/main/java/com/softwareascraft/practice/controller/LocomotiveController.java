package com.softwareascraft.practice.controller;

import com.softwareascraft.practice.dto.request.CreateLocomotiveRequest;
import com.softwareascraft.practice.dto.request.UpdateLocomotiveRequest;
import com.softwareascraft.practice.dto.response.LocomotiveResponse;
import com.softwareascraft.practice.service.LocomotiveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Locomotive endpoints (ANTI-PATTERN for teaching purposes)
 * - Creates service with 'new' instead of @Autowired
 * - Tightly coupled to concrete service implementation
 * - Cannot be easily tested with mocked dependencies
 */
@RestController
@RequestMapping("/api/locomotives")
public class LocomotiveController {

    // Direct instantiation - ANTI-PATTERN (should use @Autowired)
    private final LocomotiveService locomotiveService;

    public LocomotiveController() {
        // Creating dependency with 'new' instead of injection (ANTI-PATTERN)
        this.locomotiveService = new LocomotiveService();
    }

    @PostMapping
    public ResponseEntity<LocomotiveResponse> createLocomotive(@RequestBody CreateLocomotiveRequest request) {
        LocomotiveResponse response = locomotiveService.createLocomotive(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocomotiveResponse> getLocomotiveById(@PathVariable Long id) {
        LocomotiveResponse response = locomotiveService.getLocomotiveById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LocomotiveResponse>> getAllLocomotives() {
        List<LocomotiveResponse> responses = locomotiveService.getAllLocomotives();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocomotiveResponse> updateLocomotive(
            @PathVariable Long id,
            @RequestBody UpdateLocomotiveRequest request) {
        LocomotiveResponse response = locomotiveService.updateLocomotive(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocomotive(@PathVariable Long id) {
        locomotiveService.deleteLocomotive(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/manufacturer/{manufacturer}")
    public ResponseEntity<List<LocomotiveResponse>> getLocomotivesByManufacturer(
            @PathVariable String manufacturer) {
        List<LocomotiveResponse> responses = locomotiveService.getLocomotivesByManufacturer(manufacturer);
        return ResponseEntity.ok(responses);
    }
}
