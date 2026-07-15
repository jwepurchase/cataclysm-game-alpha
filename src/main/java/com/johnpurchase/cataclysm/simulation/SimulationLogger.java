package com.johnpurchase.cataclysm.simulation;

import java.util.List;

public class SimulationLogger {

    public void printSummary(List<WorldSnapshot> history, int interval) {
        System.out.printf("%-6s %-12s %-10s %-12s %-8s %-12s%n",
                "Turn", "Dynasty", "Treasury", "Legitimacy", "Regions", "Avg Corrupt");
        System.out.println("-".repeat(65));

        for (WorldSnapshot snapshot : history) {
            if (snapshot.turn % interval != 0) continue;
            for (DynastySnapshot d : snapshot.dynastySnapshots) {
                System.out.printf("%-6d %-12s %-10.1f %-12.3f %-8d %-12.3f%n",
                        snapshot.turn,
                        d.name,
                        d.treasury,
                        d.legitimacy,
                        d.regionCount,
                        d.avgCorruption);
            }
            System.out.println();
        }

    }

    public void printEventLog(List<String> eventLog) {
        System.out.println("\n--- Event Log ---");
        eventLog.forEach(System.out::println);
    }


}
