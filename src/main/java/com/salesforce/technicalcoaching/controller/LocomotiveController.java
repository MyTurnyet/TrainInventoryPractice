package com.salesforce.technicalcoaching.controller;

import com.salesforce.technicalcoaching.enums.MaintenanceStatus;
import com.salesforce.technicalcoaching.enums.Scale;
import com.salesforce.technicalcoaching.model.Locomotive;
import com.salesforce.technicalcoaching.service.LocomotiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locomotives")
public class LocomotiveController {

    // tight coupling - direct instantiation
    private LocomotiveService service = new LocomotiveService();

    @PostMapping
    public ResponseEntity<Locomotive> create(@RequestBody Locomotive l) {
        Locomotive result = service.create(l);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Map<String, Object> result = service.getWithLogs(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<Locomotive>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Locomotive> update(@PathVariable Long id, @RequestBody Locomotive l) {
        Locomotive result = service.update(id, l);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) Scale scale,
            @RequestParam(required = false) MaintenanceStatus status) {
        return ResponseEntity.ok(service.search(manufacturer, scale, status));
    }

    @GetMapping("/scale/{scale}")
    public ResponseEntity<List<Locomotive>> getByScale(@PathVariable Scale scale) {
        // duplicate call pattern
        String m = null;
        Map<String, Object> result = service.search(m, scale, null);
        return ResponseEntity.ok((List<Locomotive>) result.get("results"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        MaintenanceStatus status = MaintenanceStatus.valueOf(statusStr);
        service.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
