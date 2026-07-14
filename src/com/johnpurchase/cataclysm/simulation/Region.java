package com.johnpurchase.cataclysm.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.johnpurchase.cataclysm.simulation.SimulationConstants.DEFECTION_RATE;
import static com.johnpurchase.cataclysm.simulation.SimulationConstants.DEFECTION_THRESHOLD;

public class Region {
    private final int id;
    private final int x;
    private final int y;
    private int population;
    private double ideology;       // 0.0 to 1.0
    private double corruption;     // 0.0 to 1.0
    private double taxRate;        // 0.0 to 1.0
    private int ownerId;           // dynasty id, -1 if independent
    private List<Integer> neighborIds;
    private Random random = new Random(1);

    public Region(int id, int x, int y, int population,
                  double ideology, double corruption, double taxRate, int ownerId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.population = population;
        this.ideology = ideology;
        this.corruption = corruption;
        this.taxRate = taxRate;
        this.ownerId = ownerId;
        this.neighborIds = new ArrayList<>();
    }

    // Revenue calculations
    public double calculateBaseRevenue() {
        return population * taxRate;
    }

    public double calculateActualRevenue() {
        return calculateBaseRevenue() * (1.0 - corruption);
    }

    public double calculateCorruptionLoss() {
        return calculateBaseRevenue() - calculateActualRevenue();
    }


    // Getters
    public int getId() { return id; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getPopulation() { return population; }
    public double getIdeology() { return ideology; }
    public double getCorruption() { return corruption; }
    public double getTaxRate() { return taxRate; }
    public int getOwnerId() { return ownerId; }
    public List<Integer> getNeighborIds() { return neighborIds; }

    // Setters
    public void setPopulation(int population) { this.population = population; }
    public void setIdeology(double ideology) { this.ideology = Math.max(0.0, Math.min(1.0, ideology)); }
    public void setCorruption(double corruption) { this.corruption = Math.max(0.0, Math.min(1.0, corruption)); }
    public void setTaxRate(double taxRate) { this.taxRate = Math.max(0.0, Math.min(1.0, taxRate)); }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public void addNeighbor(int neighborId) { this.neighborIds.add(neighborId); }
}
