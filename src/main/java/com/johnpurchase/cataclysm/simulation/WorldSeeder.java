package com.johnpurchase.cataclysm.simulation;


import java.util.Random;

import static com.johnpurchase.cataclysm.simulation.SimulationConstants.*;

public class WorldSeeder {

    private final Random random;

    public WorldSeeder(long seed) {
        this.random = new Random(seed);
    }

    public World seed(int regionCount, int dynastyCount, int mapWidth, int mapHeight) {
        World world = new World();

        seedDynasties(world, dynastyCount);
        seedRegions(world, regionCount, mapWidth, mapHeight);
        calculateNeighbors(world);
        assignRegionsToDynasties(world);

        return world;
    }

    private void seedDynasties(World world, int dynastyCount) {
        String[] names = {"Atreus", "Borean", "Caldris", "Domar",
                "Elvar", "Fenrath", "Gorvain", "Haldis"};
        for (int i = 0; i < dynastyCount; i++) {
            double treasury = TREASURY_SEED_MIN +
                    random.nextDouble() * (TREASURY_SEED_MAX - TREASURY_SEED_MIN);
            double ideology = random.nextDouble();
            String name = i < names.length ? names[i] : "Dynasty_" + i;
            world.addDynasty(new Dynasty(i, name, treasury, ideology));
        }
    }

    private void seedRegions(World world, int regionCount, int mapWidth, int mapHeight) {
        for (int i = 0; i < regionCount; i++) {
            int x = random.nextInt(mapWidth);
            int y = random.nextInt(mapHeight);
            int population = POP_MIN + random.nextInt(POP_MAX - POP_MIN);
            double ideology = random.nextDouble();
            double corruption = random.nextDouble() * CORRUPTION_SEED_MAX;
            double taxRate = TAX_RATE_MIN +
                    random.nextDouble() * (TAX_RATE_MAX - TAX_RATE_MIN);
            world.addRegion(new Region(i, x, y, population,
                    ideology, corruption, taxRate, -1));
        }
    }

    private void calculateNeighbors(World world) {
        for (Region region : world.getRegions().values()) {
            world.getRegions().values().stream()
                    .filter(other -> other.getId() != region.getId())
                    .filter(other -> distance(region, other) < NEIGHBOR_DISTANCE_THRESHOLD)
                    .sorted((a, b) -> Double.compare(distance(region, a), distance(region, b)))
                    .limit(MAX_NEIGHBORS)
                    .forEach(neighbor -> region.addNeighbor(neighbor.getId()));
        }
    }

    private void assignRegionsToDynasties(World world) {
        int dynastyCount = world.getDynasties().size();
        for (Region region : world.getRegions().values()) {
            int dynastyId = random.nextInt(dynastyCount);
            region.setOwnerId(dynastyId);
            world.getDynasty(dynastyId).addRegion(region.getId());
        }
    }

    private double distance(Region a, Region b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
