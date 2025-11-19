package com.softwareascraft.practice.controller;

import com.softwareascraft.practice.enums.AARType;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.RollingStock;
import com.softwareascraft.practice.service.RollingStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rolling-stock")
public class RollingStockController {

    private RollingStockService service = new RollingStockService();

    @PostMapping
    public ResponseEntity<RollingStock> create(@RequestBody RollingStock rs) {
        RollingStock result = service.create(rs);
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
    public ResponseEntity<List<RollingStock>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RollingStock> update(@PathVariable Long id, @RequestBody RollingStock rs) {
        RollingStock result = service.update(id, rs);
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
            @RequestParam(required = false) AARType aarType) {
        return ResponseEntity.ok(service.search(manufacturer, scale, aarType));
    }

    @GetMapping("/aar-type/{aarType}")
    public ResponseEntity<List<RollingStock>> getByAarType(@PathVariable AARType aarType) {
        return ResponseEntity.ok(service.getByAarType(aarType));
    }
}
