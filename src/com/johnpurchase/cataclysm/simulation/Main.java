package com.johnpurchase.cataclysm.simulation;

public class Main {

    public static void main(String[] args) {
        int regionCount = 20;
        int dynastyCount = 4;
        int mapWidth = 10;
        int mapHeight = 10;
        int turns = 100;
        int logInterval = 10;
        long seed = 42L;

        WorldSeeder seeder = new WorldSeeder(seed);
        World world = seeder.seed(regionCount, dynastyCount, mapWidth, mapHeight);

        for (int i = 0; i < turns; i++) {
            world.resolve();
        }

        SimulationLogger logger = new SimulationLogger();
        logger.printSummary(world.getHistory(), logInterval);
        logger.printEventLog(world.getEventLog());

    }
}
