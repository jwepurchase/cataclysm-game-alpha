package com.johnpurchase.cataclysm.simulation;

public class SimulationConstants {
    public static final double CORRUPTION_GROWTH_RATE = 0.01;
    public static final double DIFFUSION_RATE = 0.05;
    public static final double TREASURY_BENCHMARK = 1000.0;
    public static final double NEIGHBOR_DISTANCE_THRESHOLD = 2.5;
    public static final int MAX_NEIGHBORS = 6;

    // Legitimacy weights — must sum to 1.0
    public static final double LEGITIMACY_WEIGHT_TREASURY = 0.3;
    public static final double LEGITIMACY_WEIGHT_CORRUPTION = 0.4;
    public static final double LEGITIMACY_WEIGHT_IDEOLOGY = 0.3;

    // Seeding ranges
    public static final int POP_MIN = 100;
    public static final int POP_MAX = 1000;
    public static final double CORRUPTION_SEED_MAX = 0.2;
    public static final double TAX_RATE_MIN = 0.1;
    public static final double TAX_RATE_MAX = 0.3;
    public static final double TREASURY_SEED_MIN = 500.0;
    public static final double TREASURY_SEED_MAX = 1500.0;
}
