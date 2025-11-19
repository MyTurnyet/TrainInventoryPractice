package com.salesforce.technicalcoaching.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaintenanceLog {
    public Long id;
    public Long inventoryItemId;
    public LocalDate maintenanceDate;
    public String description;
    public String workPerformed;
    public String performedBy;
    public String notes;
    public LocalDateTime createdDate;
}
