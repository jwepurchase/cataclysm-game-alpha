package com.johnpurchase.cataclysm.simulation;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.johnpurchase.cataclysm.simulation.SimulationConstants.*;

public class TurnResolver {

    public void resolve(World world) {
        resolveRegions(world);
        resolveDynasties(world);
        world.incrementTurn();
        world.addSnapshot(takeSnapshot(world));
    }

    private void resolveRegions(World world) {
        Map<Integer, Region> regions = world.getRegions();

        for (Region region : regions.values()) {

            // Pay revenue to owner
            if (region.getOwnerId() != -1) {
                Dynasty owner = world.getDynasty(region.getOwnerId());
                if (owner != null) {
                    owner.addRevenue(region.calculateActualRevenue());
                }
            }

            // Tick corruption
            region.setCorruption(region.getCorruption() + CORRUPTION_GROWTH_RATE);

            // Ideology diffusion from neighbors
            if (!region.getNeighborIds().isEmpty()) {
                double neighborIdeologySum = region.getNeighborIds().stream()
                        .map(regions::get)
                        .filter(n -> n != null)
                        .mapToDouble(Region::getIdeology)
                        .sum();
                int neighborCount = region.getNeighborIds().size();
                double neighborAvg = neighborIdeologySum / neighborCount;
                double newIdeology = region.getIdeology() +
                        (neighborAvg - region.getIdeology()) * DIFFUSION_RATE;
                region.setIdeology(newIdeology);
            }
        }
    }

    private void resolveDynasties(World world) {
        for (Dynasty dynasty : world.getDynasties().values()) {
            List<Integer> regionIds = dynasty.getRegionIds();
            if (regionIds.isEmpty()) continue;

            // Average corruption across owned regions
            double avgCorruption = regionIds.stream()
                    .map(world::getRegion)
                    .filter(r -> r != null)
                    .mapToDouble(Region::getCorruption)
                    .average()
                    .orElse(0.0);

            // Ideology alignment — how close are owned regions to dynasty ideology
            double avgIdeologyDistance = regionIds.stream()
                    .map(world::getRegion)
                    .filter(r -> r != null)
                    .mapToDouble(r -> Math.abs(r.getIdeology() - dynasty.getIdeology()))
                    .average()
                    .orElse(0.5);
            double ideologyAlignment = 1.0 - avgIdeologyDistance;

            // Treasury health
            double treasuryHealth = Math.min(1.0, dynasty.getTreasury() / TREASURY_BENCHMARK);

            // Weighted legitimacy
            double legitimacy =
                    (treasuryHealth * LEGITIMACY_WEIGHT_TREASURY) +
                            ((1.0 - avgCorruption) * LEGITIMACY_WEIGHT_CORRUPTION) +
                            (ideologyAlignment * LEGITIMACY_WEIGHT_IDEOLOGY);

            dynasty.setLegitimacy(legitimacy);
        }
    }

    private WorldSnapshot takeSnapshot(World world) {
        List<DynastySnapshot> dynastySnapshots = new ArrayList<>();
        for (Dynasty d : world.getDynasties().values()) {
            double avgCorruption = d.getRegionIds().stream()
                    .map(world::getRegion)
                    .filter(r -> r != null)
                    .mapToDouble(Region::getCorruption)
                    .average()
                    .orElse(0.0);
            dynastySnapshots.add(new DynastySnapshot(
                    d.getId(), d.getName(), d.getTreasury(),
                    d.getLegitimacy(), d.getRegionIds().size(), avgCorruption));
        }

        List<RegionSnapshot> regionSnapshots = new ArrayList<>();
        for (Region r : world.getRegions().values()) {
            regionSnapshots.add(new RegionSnapshot(
                    r.getId(), r.getOwnerId(), r.getPopulation(),
                    r.getCorruption(), r.getIdeology()));
        }

        return new WorldSnapshot(world.getTurn(), dynastySnapshots, regionSnapshots);
    }
}
