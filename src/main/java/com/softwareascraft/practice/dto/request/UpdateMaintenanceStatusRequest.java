package com.softwareascraft.practice.dto.request;

import com.softwareascraft.practice.enums.MaintenanceStatus;

public class UpdateMaintenanceStatusRequest {
    private MaintenanceStatus maintenanceStatus;

    public MaintenanceStatus getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(MaintenanceStatus maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }
}
