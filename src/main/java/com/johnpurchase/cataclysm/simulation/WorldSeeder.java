package com.johnpurchase.cataclysm.simulation;

public interface WorldSeeder {
    World seed(int regionCount, int dynastyCount, int mapWidth, int mapHeight);
}
