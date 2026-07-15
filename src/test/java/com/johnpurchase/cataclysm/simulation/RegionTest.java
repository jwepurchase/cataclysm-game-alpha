package com.johnpurchase.cataclysm.simulation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegionTest {

    private Region region;

    @BeforeEach
    void setUp() {
        // id, x, y, population, ideology, corruption, taxRate, ownerId
        region = new Region(1, 0, 0, 1000, 0.5, 0.2, 0.1, 0);
    }

    @Test
    void baseRevenueShouldBePopulationMultipliedByTaxRate() {
        assertEquals(100.0, region.calculateBaseRevenue(), 0.001);
    }

    @Test
    void actualRevenueShouldReflectCorruption() {
        // 1000 * 0.1 * (1 - 0.2) = 80.0
        assertEquals(80.0, region.calculateActualRevenue(), 0.001);
    }

    @Test
    void corruptionLossShouldBeBaseMinusActual() {
        assertEquals(20.0, region.calculateCorruptionLoss(), 0.001);
    }

    @Test
    void corruptionShouldNotExceedOne() {
        region.setCorruption(1.5);
        assertEquals(1.0, region.getCorruption(), 0.001);
    }

    @Test
    void corruptionShouldNotGoBelowZero() {
        region.setCorruption(-0.5);
        assertEquals(0.0, region.getCorruption(), 0.001);
    }

    @Test
    void ideologyShouldNotExceedOne() {
        region.setIdeology(1.5);
        assertEquals(1.0, region.getIdeology(), 0.001);
    }

    @Test
    void ideologyShouldNotGoBelowZero() {
        region.setIdeology(-0.5);
        assertEquals(0.0, region.getIdeology(), 0.001);
    }
}
