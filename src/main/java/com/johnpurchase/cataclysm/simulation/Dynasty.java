package com.johnpurchase.cataclysm.simulation;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Dynasty {
    private final int id;
    private final String name;
    private double treasury;
    private double ideology;       // dynasty's preferred ideology
    private double legitimacy;     // core score 0.0 to 1.0
    private List<Integer> regionIds;
    private boolean inExile = false;

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

    // Setters
    public void setLegitimacy(double legitimacy) { this.legitimacy = Math.max(0.0, Math.min(1.0, legitimacy)); }
}
