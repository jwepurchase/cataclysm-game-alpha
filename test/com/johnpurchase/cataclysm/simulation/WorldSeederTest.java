package com.johnpurchase.cataclysm.simulation;

import com.johnpurchase.cataclysm.simulation.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldSeederTest {

    @Test
    void seederShouldCreateCorrectNumberOfRegions() {
        WorldSeeder seeder = new WorldSeeder(42L);
        World world = seeder.seed(20, 4, 10, 10);
        assertEquals(20, world.getRegions().size());
    }

    @Test
    void seederShouldCreateCorrectNumberOfDynasties() {
        WorldSeeder seeder = new WorldSeeder(42L);
        World world = seeder.seed(20, 4, 10, 10);
        assertEquals(4, world.getDynasties().size());
    }

    @Test
    void seederShouldAssignAllRegionsToDynasties() {
        WorldSeeder seeder = new WorldSeeder(42L);
        World world = seeder.seed(20, 4, 10, 10);
        long unownedCount = world.getRegions().values().stream()
                .filter(r -> r.getOwnerId() == -1)
                .count();
        assertEquals(0, unownedCount);
    }

    @Test
    void seederWithSameSeedShouldProduceSameWorld() {
        WorldSeeder seeder1 = new WorldSeeder(42L);
        WorldSeeder seeder2 = new WorldSeeder(42L);
        World world1 = seeder1.seed(20, 4, 10, 10);
        World world2 = seeder2.seed(20, 4, 10, 10);
        assertEquals(world1.getRegions().size(), world2.getRegions().size());
        assertEquals(
                world1.getRegion(0).getCorruption(),
                world2.getRegion(0).getCorruption(),
                0.001
        );
    }

    @Test
    void allRegionsShouldHaveAtLeastOneNeighbor() {
        WorldSeeder seeder = new WorldSeeder(42L);
        World world = seeder.seed(20, 4, 10, 10);
        world.getRegions().values().forEach(r ->
                assertFalse(r.getNeighborIds().isEmpty(),
                        "Region " + r.getId() + " has no neighbors"));
    }
}
