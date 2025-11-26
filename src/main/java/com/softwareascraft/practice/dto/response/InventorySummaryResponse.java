package com.softwareascraft.practice.dto.response;

import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.Scale;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class InventorySummaryResponse {
    private Integer totalItems;
    private Integer totalLocomotives;
    private Integer totalRollingStock;
    private Map<Scale, Integer> itemsByScale;
    private Map<MaintenanceStatus, Integer> itemsByStatus;
    private BigDecimal totalInventoryValue;

    public InventorySummaryResponse() {
        this.itemsByScale = new HashMap<>();
        this.itemsByStatus = new HashMap<>();
        this.totalInventoryValue = BigDecimal.ZERO;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getTotalLocomotives() {
        return totalLocomotives;
    }

    public void setTotalLocomotives(Integer totalLocomotives) {
        this.totalLocomotives = totalLocomotives;
    }

    public Integer getTotalRollingStock() {
        return totalRollingStock;
    }

    public void setTotalRollingStock(Integer totalRollingStock) {
        this.totalRollingStock = totalRollingStock;
    }

    public Map<Scale, Integer> getItemsByScale() {
        return itemsByScale;
    }

    public void setItemsByScale(Map<Scale, Integer> itemsByScale) {
        this.itemsByScale = itemsByScale;
    }

    public Map<MaintenanceStatus, Integer> getItemsByStatus() {
        return itemsByStatus;
    }

    public void setItemsByStatus(Map<MaintenanceStatus, Integer> itemsByStatus) {
        this.itemsByStatus = itemsByStatus;
    }

    public BigDecimal getTotalInventoryValue() {
        return totalInventoryValue;
    }

    public void setTotalInventoryValue(BigDecimal totalInventoryValue) {
        this.totalInventoryValue = totalInventoryValue;
    }
}
