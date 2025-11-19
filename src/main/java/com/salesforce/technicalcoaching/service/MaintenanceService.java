package com.salesforce.technicalcoaching.service;

import com.salesforce.technicalcoaching.enums.MaintenanceStatus;
import com.salesforce.technicalcoaching.model.Locomotive;
import com.salesforce.technicalcoaching.model.MaintenanceLog;
import com.salesforce.technicalcoaching.model.RollingStock;
import com.salesforce.technicalcoaching.repository.LocomotiveRepository;
import com.salesforce.technicalcoaching.repository.MaintenanceLogRepository;
import com.salesforce.technicalcoaching.repository.RollingStockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceService {

    // tight coupling
    private MaintenanceLogRepository repo = new MaintenanceLogRepository();
    private LocomotiveRepository locoRepo = new LocomotiveRepository();
    private RollingStockRepository rsRepo = new RollingStockRepository();

    public MaintenanceLog create(MaintenanceLog log) {
        return repo.save(log);
    }

    public MaintenanceLog getById(Long id) {
        return repo.findById(id);
    }

    public List<MaintenanceLog> getByItemId(Long itemId) {
        return repo.findByItem(itemId);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // long method with multiple responsibilities
    public void updateItemStatus(Long itemId, MaintenanceStatus status) {
        // check if it's a locomotive
        Locomotive l = locoRepo.findById(itemId);
        if (l != null) {
            l.maintenanceStatus = status;
            locoRepo.update(itemId, l);
            return;
        }

        // check if it's rolling stock
        RollingStock rs = rsRepo.findById(itemId);
        if (rs != null) {
            rs.maintenanceStatus = status;
            rsRepo.update(itemId, rs);
        }
    }
}
