package com.johnpurchase.cataclysm.simulation;

import java.util.List;

public class WorldSnapshot {
    public final int turn;
    public final List<DynastySnapshot> dynastySnapshots;
    public final List<RegionSnapshot> regionSnapshots;

    public WorldSnapshot(int turn,
                         List<DynastySnapshot> dynastySnapshots,
                         List<RegionSnapshot> regionSnapshots) {
        this.turn = turn;
        this.dynastySnapshots = dynastySnapshots;
        this.regionSnapshots = regionSnapshots;
    }
}
