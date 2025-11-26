package com.softwareascraft.practice.controller;

import com.softwareascraft.practice.dto.request.CreateMaintenanceLogRequest;
import com.softwareascraft.practice.dto.request.UpdateMaintenanceStatusRequest;
import com.softwareascraft.practice.dto.response.MaintenanceLogResponse;
import com.softwareascraft.practice.service.MaintenanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController() {
        this.maintenanceService = new MaintenanceService();
    }

    @PostMapping
    public ResponseEntity<MaintenanceLogResponse> createMaintenanceLog(
            @RequestBody CreateMaintenanceLogRequest request) {
        MaintenanceLogResponse response = maintenanceService.createMaintenanceLog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceLogResponse> getMaintenanceLogById(@PathVariable Long id) {
        MaintenanceLogResponse response = maintenanceService.getMaintenanceLogById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<MaintenanceLogResponse>> getMaintenanceLogsByItemId(@PathVariable Long itemId) {
        List<MaintenanceLogResponse> responses = maintenanceService.getMaintenanceLogsByItemId(itemId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/status/{itemId}")
    public ResponseEntity<Void> updateMaintenanceStatus(
            @PathVariable Long itemId,
            @RequestBody UpdateMaintenanceStatusRequest request) {
        maintenanceService.updateMaintenanceStatus(itemId, request.getMaintenanceStatus());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceLog(@PathVariable Long id) {
        maintenanceService.deleteMaintenanceLog(id);
        return ResponseEntity.noContent().build();
    }
}
