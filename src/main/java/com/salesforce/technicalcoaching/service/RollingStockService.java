package com.salesforce.technicalcoaching.service;

import com.salesforce.technicalcoaching.enums.AARType;
import com.salesforce.technicalcoaching.enums.Scale;
import com.salesforce.technicalcoaching.model.MaintenanceLog;
import com.salesforce.technicalcoaching.model.RollingStock;
import com.salesforce.technicalcoaching.repository.MaintenanceLogRepository;
import com.salesforce.technicalcoaching.repository.RollingStockRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RollingStockService {

    // tight coupling
    private RollingStockRepository repo = new RollingStockRepository();
    private MaintenanceLogRepository logRepo = new MaintenanceLogRepository();

    public RollingStock create(RollingStock rs) {
        return repo.save(rs);
    }

    public RollingStock getById(Long id) {
        return repo.findById(id);
    }

    public List<RollingStock> getAll() {
        return repo.findAll();
    }

    public RollingStock update(Long id, RollingStock rs) {
        return repo.update(id, rs);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // duplicate logic from LocomotiveService
    public Map<String, Object> search(String manufacturer, Scale scale, AARType aarType) {
        List<RollingStock> data = repo.findAll();
        List<RollingStock> temp = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            boolean match = true;
            if (manufacturer != null && !manufacturer.isEmpty()) {
                if (!data.get(i).manufacturer.equals(manufacturer)) {
                    match = false;
                }
            }
            if (scale != null) {
                if (data.get(i).scale != scale) {
                    match = false;
                }
            }
            if (aarType != null) {
                if (data.get(i).aarType != aarType) {
                    match = false;
                }
            }
            if (match) {
                temp.add(data.get(i));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("results", temp);
        result.put("total", temp.size());
        result.put("page", 0);
        result.put("pageSize", 20);
        return result;
    }

    // duplicated logic from LocomotiveService
    public Map<String, Object> getWithLogs(Long id) {
        RollingStock rs = repo.findById(id);
        if (rs == null) {
            return null;
        }

        List<MaintenanceLog> logs = logRepo.findByItem(id);

        Map<String, Object> response = new HashMap<>();
        response.put("id", rs.id);
        response.put("manufacturer", rs.manufacturer);
        response.put("modelNumber", rs.modelNumber);
        response.put("scale", rs.scale);
        response.put("roadName", rs.roadName);
        response.put("color", rs.color);
        response.put("era", rs.era);
        response.put("description", rs.description);
        response.put("purchasePrice", rs.purchasePrice);
        response.put("purchaseDate", rs.purchaseDate);
        response.put("currentValue", rs.currentValue);
        response.put("notes", rs.notes);
        response.put("createdDate", rs.createdDate);
        response.put("lastModifiedDate", rs.lastModifiedDate);
        response.put("maintenanceStatus", rs.maintenanceStatus);
        response.put("aarType", rs.aarType);
        response.put("carType", rs.carType);
        response.put("roadNumber", rs.roadNumber);
        response.put("capacity", rs.capacity);
        response.put("maintenanceLogs", logs);

        return response;
    }

    public List<RollingStock> getByAarType(AARType type) {
        return repo.findByAarType(type);
    }
}
