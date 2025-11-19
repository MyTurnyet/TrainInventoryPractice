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

    public void updateItemStatus(Long itemId, MaintenanceStatus status) {
        Locomotive l = locoRepo.findById(itemId);
        if (l != null) {
            l.maintenanceStatus = status;
            locoRepo.update(itemId, l);
            return;
        }

        RollingStock rs = rsRepo.findById(itemId);
        if (rs != null) {
            rs.maintenanceStatus = status;
            rsRepo.update(itemId, rs);
        }
    }
}
