package com.softwareascraft.practice.controller;

import com.softwareascraft.practice.dto.request.CreateRollingStockRequest;
import com.softwareascraft.practice.dto.request.UpdateRollingStockRequest;
import com.softwareascraft.practice.dto.response.RollingStockResponse;
import com.softwareascraft.practice.service.RollingStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for RollingStock endpoints (ANTI-PATTERN for teaching purposes)
 * - Creates service with 'new' instead of @Autowired
 * - Tightly coupled to concrete service implementation
 * - Cannot be easily tested with mocked dependencies
 */
@RestController
@RequestMapping("/api/rolling-stock")
public class RollingStockController {

    // Direct instantiation - ANTI-PATTERN (should use @Autowired)
    private final RollingStockService rollingStockService;

    public RollingStockController() {
        // Creating dependency with 'new' instead of injection (ANTI-PATTERN)
        this.rollingStockService = new RollingStockService();
    }

    @PostMapping
    public ResponseEntity<RollingStockResponse> createRollingStock(@RequestBody CreateRollingStockRequest request) {
        RollingStockResponse response = rollingStockService.createRollingStock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RollingStockResponse> getRollingStockById(@PathVariable Long id) {
        RollingStockResponse response = rollingStockService.getRollingStockById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RollingStockResponse>> getAllRollingStock() {
        List<RollingStockResponse> responses = rollingStockService.getAllRollingStock();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RollingStockResponse> updateRollingStock(
            @PathVariable Long id,
            @RequestBody UpdateRollingStockRequest request) {
        RollingStockResponse response = rollingStockService.updateRollingStock(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRollingStock(@PathVariable Long id) {
        rollingStockService.deleteRollingStock(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/manufacturer/{manufacturer}")
    public ResponseEntity<List<RollingStockResponse>> getRollingStockByManufacturer(
            @PathVariable String manufacturer) {
        List<RollingStockResponse> responses = rollingStockService.getRollingStockByManufacturer(manufacturer);
        return ResponseEntity.ok(responses);
    }
}
