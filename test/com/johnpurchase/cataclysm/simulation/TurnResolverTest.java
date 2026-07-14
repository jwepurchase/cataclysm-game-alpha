package com.johnpurchase.cataclysm.simulation;

import com.johnpurchase.cataclysm.simulation.Dynasty;
import com.johnpurchase.cataclysm.simulation.Region;
import com.johnpurchase.cataclysm.simulation.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurnResolverTest {

    private World world;
    private TurnResolver resolver;

    @BeforeEach
    void setUp() {
        world = new World(10, 10);
        resolver = new TurnResolver();

        Dynasty dynasty = new Dynasty(0, "Atreus", 1000.0, 0.5);
        world.addDynasty(dynasty);

        Region region = new Region(0, 0, 0, 1000, 0.5, 0.0, 0.1, 0);
        world.addRegion(region);
        dynasty.addRegion(0);
    }

    @Test
    void resolveShouldIncrementTurn() {
        resolver.resolve(world);
        assertEquals(1, world.getTurn());
    }

    @Test
    void resolveShouldAddRevenueToOwnerTreasury() {
        resolver.resolve(world);
        // 1000 * 0.1 * (1 - 0.0) = 100.0 added to 1000.0
        assertEquals(1100.0, world.getDynasty(0).getTreasury(), 0.001);
    }

    @Test
    void resolveShouldIncreaseCorruption() {
        resolver.resolve(world);
        assertTrue(world.getRegion(0).getCorruption() > 0.0);
    }

    @Test
    void resolveShouldAddSnapshotToHistory() {
        resolver.resolve(world);
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
            resolver.resolve(world);
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

        resolver.resolve(world);

        assertFalse(world.getDynasty(0).isInExile(), "isInExile should be false");
        assertEquals(1, world.getDynasty(0).getRegionIds().size(), "Region count should be 1");
        assertFalse(world.getDynasty(0).isInExile());
    }


}
