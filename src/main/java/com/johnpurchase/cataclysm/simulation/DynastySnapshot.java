package com.johnpurchase.cataclysm.simulation;

public class DynastySnapshot {
    public final int id;
    public final String name;
    public final double treasury;
    public final double legitimacy;
    public final int regionCount;
    public final double avgCorruption;

    public DynastySnapshot(int id, String name, double treasury,
                           double legitimacy, int regionCount, double avgCorruption) {
        this.id = id;
        this.name = name;
        this.treasury = treasury;
        this.legitimacy = legitimacy;
        this.regionCount = regionCount;
        this.avgCorruption = avgCorruption;
    }
}
