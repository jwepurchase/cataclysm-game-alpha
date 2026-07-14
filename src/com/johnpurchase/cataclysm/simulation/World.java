package com.johnpurchase.cataclysm.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    private final Map<Integer, Region> regions;
    private final Map<Integer, Dynasty> dynasties;
    private int turn;
    private final List<WorldSnapshot> history;
    private final int mapWidth;
    private final int mapHeight;

    public World(int mapWidth, int mapHeight) {
        this.regions = new HashMap<>();
        this.dynasties = new HashMap<>();
        this.turn = 0;
        this.history = new ArrayList<>();
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void addRegion(Region region) {
        regions.put(region.getId(), region);
    }

    public void addDynasty(Dynasty dynasty) {
        dynasties.put(dynasty.getId(), dynasty);
    }

    // Add to World.java
    private final List<String> eventLog = new ArrayList<>();
    public void logEvent(String event) { eventLog.add(event); }
    public List<String> getEventLog() { return eventLog; }

    public Region getRegion(int id) { return regions.get(id); }
    public Dynasty getDynasty(int id) { return dynasties.get(id); }
    public Map<Integer, Region> getRegions() { return regions; }
    public Map<Integer, Dynasty> getDynasties() { return dynasties; }
    public int getTurn() { return turn; }
    public List<WorldSnapshot> getHistory() { return history; }
    public int getMapWidth() { return mapWidth; }
    public int getMapHeight() { return mapHeight; }

    public void incrementTurn() { turn++; }
    public void addSnapshot(WorldSnapshot snapshot) { history.add(snapshot); }
}
