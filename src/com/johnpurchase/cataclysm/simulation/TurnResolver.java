package com.johnpurchase.cataclysm.simulation;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.johnpurchase.cataclysm.simulation.SimulationConstants.*;

public class TurnResolver {

    public void resolve(World world) {
        resolveRegions(world);
        resolveDynasties(world);
        checkExileRecovery(world);
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

            checkDefection(world, region);
            checkAbsorption(world, region);
        }
    }


    private void resolveDynasties(World world) {
        for (Dynasty dynasty : world.getDynasties().values()) {
            List<Integer> regionIds = dynasty.getRegionIds();

            if (regionIds.isEmpty()) {
                if (!dynasty.isInExile()) {
                    dynasty.setInExile(true);
                    dynasty.setTreasury(dynasty.getTreasury() * EXILE_TREASURY_PENALTY);
                    world.logEvent("Turn " + world.getTurn() + ": " + dynasty.getName()
                            + " has been driven into exile");
                }
                continue;
            }

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

            if (dynasty.getRegionIds().isEmpty() && !dynasty.isInExile()) {
                dynasty.setInExile(true);
                dynasty.setTreasury(dynasty.getTreasury() * EXILE_TREASURY_PENALTY);
                world.logEvent("Turn " + world.getTurn() + ": " + dynasty.getName()
                        + " has been driven into exile");
            }

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

    private void checkAbsorption(World world, Region region) {
        if (region.getOwnerId() != -1) return;

        // Find neighboring dynasties
        region.getNeighborIds().stream()
                .map(world::getRegion)
                .filter(r -> r != null && r.getOwnerId() != -1)
                .map(r -> world.getDynasty(r.getOwnerId()))
                .filter(d -> d != null)
                .findFirst()
                .ifPresent(neighbor -> {
                    if (RANDOM.nextDouble() < ABSORPTION_RATE) {
                        region.setOwnerId(neighbor.getId());
                        neighbor.addRegion(region.getId());

                        world.logEvent("Turn " + world.getTurn() + ": Region " + region.getId()
                                + " absorbed by " + neighbor.getName());
                    }
                });
    }

    // In resolveRegions(), after the corruption tick
    private void checkDefection(World world, Region region) {
        Dynasty owner = world.getDynasty(region.getOwnerId());

        if (region.getOwnerId() == -1) return;

        // Last region protection
        if (owner.getRegionIds().size() <= 1) return;

        if (region.getCorruption() > DEFECTION_THRESHOLD) {
            double defectionChance = (region.getCorruption() - DEFECTION_THRESHOLD)
                    / (1.0 - DEFECTION_THRESHOLD);
            if (RANDOM.nextDouble() < defectionChance * DEFECTION_RATE) {
                owner.removeRegion(region.getId());
                region.setOwnerId(-1);

                world.logEvent("Turn " + world.getTurn() + ": Region " + region.getId()
                        + " defected from " + owner.getName());
            }
        }

    }

    private void checkExileRecovery(World world) {
        for (Dynasty dynasty : world.getDynasties().values()) {
            if (!dynasty.isInExile()) continue;

            // Find any independent region to seed the comeback
            world.getRegions().values().stream()
                    .filter(r -> r.getOwnerId() == -1)
                    .findFirst()
                    .ifPresent(region -> {
                        region.setOwnerId(dynasty.getId());
                        dynasty.addRegion(region.getId());
                        dynasty.setInExile(false);
                        world.logEvent("Turn " + world.getTurn() + ": " + dynasty.getName()
                                + " emerges from exile in Region " + region.getId());
                    });
        }
    }

}
