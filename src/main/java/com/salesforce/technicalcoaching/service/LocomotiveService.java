package com.salesforce.technicalcoaching.service;

import com.salesforce.technicalcoaching.enums.MaintenanceStatus;
import com.salesforce.technicalcoaching.enums.Scale;
import com.salesforce.technicalcoaching.model.Locomotive;
import com.salesforce.technicalcoaching.model.MaintenanceLog;
import com.salesforce.technicalcoaching.repository.LocomotiveRepository;
import com.salesforce.technicalcoaching.repository.MaintenanceLogRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocomotiveService {

    // tight coupling - direct instantiation
    private LocomotiveRepository repo = new LocomotiveRepository();
    private MaintenanceLogRepository logRepo = new MaintenanceLogRepository();

    public Locomotive create(Locomotive l) {
        // no validation
        return repo.save(l);
    }

    public Locomotive getById(Long id) {
        return repo.findById(id);
    }

    public List<Locomotive> getAll() {
        return repo.findAll();
    }

    public Locomotive update(Long id, Locomotive l) {
        return repo.update(id, l);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // long method with multiple responsibilities
    public Map<String, Object> search(String manufacturer, Scale scale, MaintenanceStatus status) {
        List<Locomotive> data = repo.findAll();
        List<Locomotive> temp = new ArrayList<>();

        // poor logic
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
            if (status != null) {
                if (data.get(i).maintenanceStatus != status) {
                    match = false;
                }
            }
            if (match) {
                temp.add(data.get(i));
            }
        }

        // build response with magic numbers
        Map<String, Object> result = new HashMap<>();
        result.put("results", temp);
        result.put("total", temp.size());
        result.put("page", 0);
        result.put("pageSize", 20);
        return result;
    }

    // method doing too much
    public Map<String, Object> getWithLogs(Long id) {
        Locomotive l = repo.findById(id);
        if (l == null) {
            return null;
        }

        List<MaintenanceLog> logs = logRepo.findByItem(id);

        Map<String, Object> response = new HashMap<>();
        response.put("id", l.id);
        response.put("manufacturer", l.manufacturer);
        response.put("modelNumber", l.modelNumber);
        response.put("scale", l.scale);
        response.put("roadName", l.roadName);
        response.put("color", l.color);
        response.put("era", l.era);
        response.put("description", l.description);
        response.put("purchasePrice", l.purchasePrice);
        response.put("purchaseDate", l.purchaseDate);
        response.put("currentValue", l.currentValue);
        response.put("notes", l.notes);
        response.put("createdDate", l.createdDate);
        response.put("lastModifiedDate", l.lastModifiedDate);
        response.put("maintenanceStatus", l.maintenanceStatus);
        response.put("locomotiveType", l.locomotiveType);
        response.put("powerType", l.powerType);
        response.put("roadNumber", l.roadNumber);
        response.put("maintenanceLogs", logs);

        return response;
    }

    public void updateStatus(Long id, MaintenanceStatus status) {
        Locomotive l = repo.findById(id);
        if (l != null) {
            l.maintenanceStatus = status;
            repo.update(id, l);
        }
    }
}
