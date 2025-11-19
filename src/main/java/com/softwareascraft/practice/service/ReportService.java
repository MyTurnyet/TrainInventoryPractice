package com.softwareascraft.practice.service;

import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.Scale;
import com.softwareascraft.practice.model.Locomotive;
import com.softwareascraft.practice.model.RollingStock;
import com.softwareascraft.practice.repository.LocomotiveRepository;
import com.softwareascraft.practice.repository.RollingStockRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private LocomotiveRepository locoRepo = new LocomotiveRepository();
    private RollingStockRepository rsRepo = new RollingStockRepository();

    public Map<String, Object> getSummary() {
        List<Locomotive> list1 = locoRepo.findAll();
        List<RollingStock> list2 = rsRepo.findAll();

        int total = list1.size() + list2.size();
        int locoCount = list1.size();
        int rsCount = list2.size();

        Map<Scale, Integer> scaleMap = new HashMap<>();
        for (int i = 0; i < list1.size(); i++) {
            Scale s = list1.get(i).scale;
            if (scaleMap.containsKey(s)) {
                scaleMap.put(s, scaleMap.get(s) + 1);
            } else {
                scaleMap.put(s, 1);
            }
        }
        for (int i = 0; i < list2.size(); i++) {
            Scale s = list2.get(i).scale;
            if (scaleMap.containsKey(s)) {
                scaleMap.put(s, scaleMap.get(s) + 1);
            } else {
                scaleMap.put(s, 1);
            }
        }

        Map<MaintenanceStatus, Integer> statusMap = new HashMap<>();
        for (int i = 0; i < list1.size(); i++) {
            MaintenanceStatus st = list1.get(i).maintenanceStatus;
            if (statusMap.containsKey(st)) {
                statusMap.put(st, statusMap.get(st) + 1);
            } else {
                statusMap.put(st, 1);
            }
        }
        for (int i = 0; i < list2.size(); i++) {
            MaintenanceStatus st = list2.get(i).maintenanceStatus;
            if (statusMap.containsKey(st)) {
                statusMap.put(st, statusMap.get(st) + 1);
            } else {
                statusMap.put(st, 1);
            }
        }

        BigDecimal totalValue = new BigDecimal("0");
        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).currentValue != null) {
                totalValue = totalValue.add(list1.get(i).currentValue);
            }
        }
        for (int i = 0; i < list2.size(); i++) {
            if (list2.get(i).currentValue != null) {
                totalValue = totalValue.add(list2.get(i).currentValue);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalItems", total);
        result.put("totalLocomotives", locoCount);
        result.put("totalRollingStock", rsCount);
        result.put("itemsByScale", scaleMap);
        result.put("itemsByStatus", statusMap);
        result.put("totalInventoryValue", totalValue);

        return result;
    }
}
