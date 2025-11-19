package com.softwareascraft.practice.model;

import com.softwareascraft.practice.enums.LocomotiveType;
import com.softwareascraft.practice.enums.MaintenanceStatus;
import com.softwareascraft.practice.enums.PowerType;
import com.softwareascraft.practice.enums.Scale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Locomotive {
    public Long id;
    public String manufacturer;
    public String modelNumber;
    public Scale scale;
    public String roadName;
    public String color;
    public String era;
    public String description;
    public BigDecimal purchasePrice;
    public LocalDate purchaseDate;
    public BigDecimal currentValue;
    public String notes;
    public LocalDateTime createdDate;
    public LocalDateTime lastModifiedDate;
    public MaintenanceStatus maintenanceStatus;
    public LocomotiveType locomotiveType;
    public PowerType powerType;
    public String roadNumber;
}
