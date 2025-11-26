package com.softwareascraft.practice.repository;

import com.softwareascraft.practice.model.MaintenanceLog;

import java.util.List;

public interface LogRepository {
    List<MaintenanceLog> findAll();

    MaintenanceLog findById(Long id);

    MaintenanceLog save(MaintenanceLog log);

    void deleteById(Long id);

    List<MaintenanceLog> findByItem(Long itemId);
}
