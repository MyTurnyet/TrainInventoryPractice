package com.softwareascraft.practice.enums;

public enum AARType {
    XM("Box Car"),
    XMO("Box Car, Overheight"),
    FC("Flat Car"),
    TA("Tank Car"),
    HM("Hopper Car"),
    RP("Refrigerator Car - Passenger Service"),
    GN("Gondola"),
    XL("Box Car - Loader"),
    GB("Gondola - Ballast"),
    FM("Flat Car - Military"),
    RB("Refrigerator Car - Bunkerless"),
    SA("Stock Car - Animal"),
    CA("Caboose"),
    PA("Passenger Car"),
    BA("Baggage Car");

    private final String description;

    AARType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
