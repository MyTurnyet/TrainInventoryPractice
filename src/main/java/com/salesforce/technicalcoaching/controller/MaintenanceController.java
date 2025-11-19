package com.salesforce.technicalcoaching.controller;

import com.salesforce.technicalcoaching.enums.MaintenanceStatus;
import com.salesforce.technicalcoaching.model.MaintenanceLog;
import com.salesforce.technicalcoaching.service.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private MaintenanceService service = new MaintenanceService();

    @PostMapping
    public ResponseEntity<MaintenanceLog> create(@RequestBody MaintenanceLog log) {
        MaintenanceLog result = service.create(log);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceLog> getById(@PathVariable Long id) {
        MaintenanceLog result = service.getById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<MaintenanceLog>> getByItemId(@PathVariable Long itemId) {
        return ResponseEntity.ok(service.getByItemId(itemId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status/{itemId}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long itemId, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        MaintenanceStatus status = MaintenanceStatus.valueOf(statusStr);
        service.updateItemStatus(itemId, status);
        return ResponseEntity.ok().build();
    }
}
