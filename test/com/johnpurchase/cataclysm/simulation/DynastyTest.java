package com.johnpurchase.cataclysm.simulation;

import com.johnpurchase.cataclysm.simulation.Dynasty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynastyTest {

    private Dynasty dynasty;

    @BeforeEach
    void setUp() {
        dynasty = new Dynasty(0, "Atreus", 1000.0, 0.5);
    }

    @Test
    void addRevenueShouldIncreaseTreasury() {
        dynasty.addRevenue(100.0);
        assertEquals(1100.0, dynasty.getTreasury(), 0.001);
    }

    @Test
    void addRegionShouldIncreaseRegionCount() {
        dynasty.addRegion(1);
        assertEquals(1, dynasty.getRegionIds().size());
    }

    @Test
    void addRegionShouldNotAddDuplicates() {
        dynasty.addRegion(1);
        dynasty.addRegion(1);
        assertEquals(1, dynasty.getRegionIds().size());
    }

    @Test
    void removeRegionShouldDecreaseRegionCount() {
        dynasty.addRegion(1);
        dynasty.removeRegion(1);
        assertEquals(0, dynasty.getRegionIds().size());
    }

    @Test
    void legitimacyShouldNotExceedOne() {
        dynasty.setLegitimacy(1.5);
        assertEquals(1.0, dynasty.getLegitimacy(), 0.001);
    }

    @Test
    void legitimacyShouldNotGoBelowZero() {
        dynasty.setLegitimacy(-0.5);
        assertEquals(0.0, dynasty.getLegitimacy(), 0.001);
    }

    @Test
    void newDynastyShouldNotBeInExile() {
        assertFalse(dynasty.isInExile());
    }
}
