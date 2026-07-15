package com.johnpurchase.cataclysm.simulation;


import java.util.Random;

import static com.johnpurchase.cataclysm.simulation.SimulationConstants.*;

public class RandomWorldSeeder implements WorldSeeder {

    private final Random random;

    public RandomWorldSeeder(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public World seed(int regionCount, int dynastyCount, int mapWidth, int mapHeight) {
        World world = new World();

        seedDynasties(world, dynastyCount);
        seedRegions(world, regionCount);
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

    private void seedRegions(World world, int regionCount) {
        for (int i = 0; i < regionCount; i++) {
            int population = POP_MIN + random.nextInt(POP_MAX - POP_MIN);
            double ideology = random.nextDouble();
            double corruption = random.nextDouble() * CORRUPTION_SEED_MAX;
            double taxRate = TAX_RATE_MIN +
                    random.nextDouble() * (TAX_RATE_MAX - TAX_RATE_MIN);
            world.addRegion(new Region(i, population,
                    ideology, corruption, taxRate, -1));
        }
    }

    private void calculateNeighbors(World world) {
        for (Region region : world.getRegions().values()) {
            world.getRegions().values().stream()
                    .filter(other -> other.getId() != region.getId())
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

}
