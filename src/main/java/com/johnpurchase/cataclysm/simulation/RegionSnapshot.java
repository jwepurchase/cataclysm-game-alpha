package com.johnpurchase.cataclysm.simulation;

public class RegionSnapshot {
    public final int id;
    public final int ownerId;
    public final int population;
    public final double corruption;
    public final double ideology;

    public RegionSnapshot(int id, int ownerId, int population,
                          double corruption, double ideology) {
        this.id = id;
        this.ownerId = ownerId;
        this.population = population;
        this.corruption = corruption;
        this.ideology = ideology;
    }
}
