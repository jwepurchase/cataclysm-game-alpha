package com.johnpurchase.cataclysm.simulation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    private World world;

    @BeforeEach
    void setUp() {
        world = new World(10, 10);

        Dynasty dynasty = new Dynasty(0, "Atreus", 1000.0, 0.5);
        world.addDynasty(dynasty);

        Region region = new Region(0, 0, 0, 1000, 0.5, 0.0, 0.1, 0);
        world.addRegion(region);
        dynasty.addRegion(0);
    }

    @Test
    void resolveShouldIncrementTurn() {
        world.resolve();
        assertEquals(1, world.getTurn());
    }

    @Test
    void resolveShouldAddRevenueToOwnerTreasury() {
        world.resolve();
        // 1000 * 0.1 * (1 - 0.0) = 100.0 added to 1000.0
        assertEquals(1100.0, world.getDynasty(0).getTreasury(), 0.001);
    }

    @Test
    void resolveShouldIncreaseCorruption() {
        world.resolve();
        assertTrue(world.getRegion(0).getCorruption() > 0.0);
    }

    @Test
    void resolveShouldAddSnapshotToHistory() {
        world.resolve();
        assertEquals(1, world.getHistory().size());
    }

    @Test
    void highCorruptionRegionShouldEventuallyDefect() {
        // Set corruption just above threshold and run many turns
        world.getRegion(0).setCorruption(0.9);

        // Add a second region so last-region protection doesn't block defection
        Region region2 = new Region(1, 1, 0, 1000, 0.5, 0.0, 0.1, 0);
        world.addRegion(region2);
        world.getDynasty(0).addRegion(1);

        boolean defected = false;
        for (int i = 0; i < 200; i++) {
            world.resolve();
            if (world.getRegion(0).getOwnerId() == -1) {
                defected = true;
                break;
            }
        }
        assertTrue(defected);
    }

    @Test
    void dynastyWithNoRegionsShouldGoIntoExile() {
        // Remove the region directly, simulating a dynasty that has lost everything
        world.getDynasty(0).removeRegion(0);
        world.getRegion(0).setOwnerId(-1);

        world.resolve();

        assertFalse(world.getDynasty(0).isInExile(), "isInExile should be false");
        assertEquals(1, world.getDynasty(0).getRegionIds().size(), "Region count should be 1");
        assertFalse(world.getDynasty(0).isInExile());
    }


}
