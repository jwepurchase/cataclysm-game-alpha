package com.johnpurchase.cataclysm.simulation;

import java.util.ArrayList;
import java.util.List;

public class Dynasty {
    private final int id;
    private final String name;
    private double treasury;
    private double ideology;       // dynasty's preferred ideology
    private double legitimacy;     // core score 0.0 to 1.0
    private List<Integer> regionIds;

    public Dynasty(int id, String name, double treasury, double ideology) {
        this.id = id;
        this.name = name;
        this.treasury = treasury;
        this.ideology = ideology;
        this.legitimacy = 0.5;
        this.regionIds = new ArrayList<>();
    }

    public void addRevenue(double amount) {
        this.treasury += amount;
    }

    public void addRegion(int regionId) {
        if (!regionIds.contains(regionId)) {
            regionIds.add(regionId);
        }
    }

    public void removeRegion(int regionId) {
        regionIds.remove(Integer.valueOf(regionId));
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getTreasury() { return treasury; }
    public double getIdeology() { return ideology; }
    public double getLegitimacy() { return legitimacy; }
    public List<Integer> getRegionIds() { return regionIds; }

    // Setters
    public void setTreasury(double treasury) { this.treasury = treasury; }
    public void setIdeology(double ideology) { this.ideology = Math.max(0.0, Math.min(1.0, ideology)); }
    public void setLegitimacy(double legitimacy) { this.legitimacy = Math.max(0.0, Math.min(1.0, legitimacy)); }
}
