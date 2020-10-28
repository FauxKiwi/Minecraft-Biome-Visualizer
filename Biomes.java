package com.siinus;

import java.util.List;
import java.util.Map;

public class Biomes {
    public static final int OCEAN = 0;
    public static final int PLAINS = 1;
    public static final int DESERT = 2;
    public static final int EXTREME_HILLS = 3;
    public static final int FOREST = 4;
    public static final int TAIGA = 5;
    public static final int SWAMPLAND = 6;
    public static final int RIVER = 7;
    public static final int HELL = 8;
    public static final int SKY = 9;
    public static final int FROZEN_OCEAN = 10;
    public static final int FROZEN_RIVER = 11;
    public static final int ICE_PLAINS = 12;
    public static final int ICE_MOUNTAINS = 13;
    public static final int MUSHROOM_ISLAND = 14;
    public static final int MUSHROOM_ISLAND_SHORE = 15;
    public static final int BEACHES = 16;
    public static final int DESERT_HILLS = 17;
    public static final int FOREST_HILLS = 18;
    public static final int TAIGA_HILLS = 19;
    public static final int SMALLER_EXTREME_HILLS = 20;
    public static final int JUNGLE = 21;
    public static final int JUNGLE_HILLS = 22;
    public static final int JUNGLE_EDGE = 23;
    public static final int DEEP_OCEAN = 24;
    public static final int STONE_BEACH = 25;
    public static final int COLD_BEACH = 26;
    public static final int BIRCH_FOREST = 27;
    public static final int BIRCH_FOREST_HILLS = 28;
    public static final int ROOFED_FOREST = 29;
    public static final int TAIGA_COLD = 30;
    public static final int TAIGA_COLD_HILLS = 31;
    public static final int REDWOOD_TAIGA = 32;
    public static final int REDWOOD_TAIGA_HILLS = 33;
    public static final int EXTREME_HILLS_WITH_TREES = 34;
    public static final int SAVANNA = 35;
    public static final int SAVANNA_ROCK = 36;
    public static final int MESA = 37;
    public static final int MESA_ROCK = 38;
    public static final int MESA_CLEAR_ROCK = 39;

    public static final int TEMP_COLD = 3;
    public static final int TEMP_MEDIUM = 2;
    public static final int TEMP_WARM = 1;

    public static final List<Integer> WARM_BIOMES = List.of(DESERT, DESERT, DESERT, SAVANNA, SAVANNA, PLAINS);
    public static final List<Integer> MEDIUM_BIOMES = List.of(FOREST, ROOFED_FOREST, EXTREME_HILLS, PLAINS, BIRCH_FOREST, SWAMPLAND);
    public static final List<Integer> COLD_BIOMES = List.of(FOREST, EXTREME_HILLS, TAIGA, PLAINS);
    public static final List<Integer> ICE_BIOMES = List.of(ICE_PLAINS, ICE_PLAINS, ICE_PLAINS, TAIGA_COLD);

    public static final Map<Integer, Double> TEMP = Map.ofEntries(
            Map.entry(OCEAN, 0.5),
            Map.entry(PLAINS, 0.8),
            Map.entry(DESERT, 2.0),
            Map.entry(EXTREME_HILLS, 0.2),
            Map.entry(FOREST, 0.8),
            Map.entry(TAIGA, 0.2),
            Map.entry(SWAMPLAND, 0.8),
            Map.entry(RIVER, 0.5),
            Map.entry(HELL, 2.0),
            Map.entry(SKY, 0.5),
            Map.entry(FROZEN_OCEAN, 0.0),
            Map.entry(FROZEN_RIVER, 0.0),
            Map.entry(ICE_PLAINS, 0.0),
            Map.entry(ICE_MOUNTAINS, 0.0),
            Map.entry(MUSHROOM_ISLAND, 0.9),
            Map.entry(MUSHROOM_ISLAND_SHORE, 0.9),
            Map.entry(BEACHES, 0.8),
            Map.entry(DESERT_HILLS, 2.0),
            Map.entry(FOREST_HILLS, 0.7),
            Map.entry(TAIGA_HILLS, 0.25),
            Map.entry(SMALLER_EXTREME_HILLS, 0.2),
            Map.entry(JUNGLE, 0.95),
            Map.entry(JUNGLE_HILLS, 0.95),
            Map.entry(JUNGLE_EDGE, 0.95),
            Map.entry(DEEP_OCEAN, 0.5),
            Map.entry(STONE_BEACH, 0.2),
            Map.entry(COLD_BEACH, 0.05),
            Map.entry(BIRCH_FOREST, 0.6),
            Map.entry(BIRCH_FOREST_HILLS, 0.6),
            Map.entry(ROOFED_FOREST, 0.7),
            Map.entry(TAIGA_COLD, -0.5),
            Map.entry(TAIGA_COLD_HILLS, -0.5),
            Map.entry(REDWOOD_TAIGA, 0.3),
            Map.entry(REDWOOD_TAIGA_HILLS, 0.3),
            Map.entry(EXTREME_HILLS_WITH_TREES, 0.2),
            Map.entry(SAVANNA, 1.2),
            Map.entry(SAVANNA_ROCK, 1.0),
            Map.entry(MESA, 2.0),
            Map.entry(MESA_ROCK, 2.0),
            Map.entry(MESA_CLEAR_ROCK, 2.0)
    );

    public static final Map<Integer, Integer> MUTATIONS = Map.ofEntries(
            Map.entry(PLAINS, PLAINS),
            Map.entry(DESERT, DESERT),
            Map.entry(EXTREME_HILLS, EXTREME_HILLS),
            Map.entry(FOREST, FOREST),
            Map.entry(TAIGA, TAIGA),
            Map.entry(SWAMPLAND, SWAMPLAND),
            Map.entry(ICE_PLAINS, ICE_PLAINS),
            Map.entry(JUNGLE, JUNGLE),
            Map.entry(JUNGLE_EDGE, JUNGLE_EDGE),
            Map.entry(BIRCH_FOREST, BIRCH_FOREST),
            Map.entry(BIRCH_FOREST_HILLS, BIRCH_FOREST_HILLS),
            Map.entry(ROOFED_FOREST, ROOFED_FOREST),
            Map.entry(TAIGA_COLD, TAIGA_COLD),
            Map.entry(REDWOOD_TAIGA, REDWOOD_TAIGA),
            Map.entry(REDWOOD_TAIGA_HILLS, REDWOOD_TAIGA_HILLS),
            Map.entry(EXTREME_HILLS_WITH_TREES, EXTREME_HILLS_WITH_TREES),
            Map.entry(SAVANNA, SAVANNA),
            Map.entry(SAVANNA_ROCK, SAVANNA_ROCK),
            Map.entry(MESA, MESA),
            Map.entry(MESA_ROCK, MESA_ROCK),
            Map.entry(MESA_CLEAR_ROCK, MESA_CLEAR_ROCK));

    public static final Map<Integer, Integer> CLASSES = Map.ofEntries(
            Map.entry(OCEAN,
                    OCEAN),
            Map.entry(PLAINS, PLAINS),
            Map.entry(DESERT, DESERT),
            Map.entry(EXTREME_HILLS, EXTREME_HILLS),
            Map.entry(FOREST, FOREST),
            Map.entry(TAIGA, TAIGA),
            Map.entry(SWAMPLAND, SWAMPLAND),
            Map.entry(RIVER, RIVER),
            Map.entry(HELL, HELL),
            Map.entry(SKY, SKY),
            Map.entry(FROZEN_OCEAN, OCEAN),
            Map.entry(FROZEN_RIVER, RIVER),
            Map.entry(ICE_PLAINS, ICE_PLAINS),
            Map.entry(ICE_MOUNTAINS, ICE_PLAINS),
            Map.entry(MUSHROOM_ISLAND, MUSHROOM_ISLAND),
            Map.entry(MUSHROOM_ISLAND_SHORE, MUSHROOM_ISLAND),
            Map.entry(BEACHES, BEACHES),
            Map.entry(DESERT_HILLS, DESERT),
            Map.entry(FOREST_HILLS, FOREST),
            Map.entry(TAIGA_HILLS, TAIGA),
            Map.entry(SMALLER_EXTREME_HILLS, EXTREME_HILLS),
            Map.entry(JUNGLE, JUNGLE),
            Map.entry(JUNGLE_HILLS, JUNGLE),
            Map.entry(JUNGLE_EDGE, JUNGLE),
            Map.entry(DEEP_OCEAN, OCEAN),
            Map.entry(STONE_BEACH, STONE_BEACH),
            Map.entry(COLD_BEACH, BEACHES),
            Map.entry(BIRCH_FOREST, FOREST),
            Map.entry(BIRCH_FOREST_HILLS, FOREST),
            Map.entry(ROOFED_FOREST, FOREST),
            Map.entry(TAIGA_COLD, TAIGA),
            Map.entry(TAIGA_COLD_HILLS, TAIGA),
            Map.entry(REDWOOD_TAIGA, TAIGA),
            Map.entry(REDWOOD_TAIGA_HILLS, TAIGA),
            Map.entry(EXTREME_HILLS_WITH_TREES, EXTREME_HILLS),
            Map.entry(SAVANNA, SAVANNA),
            Map.entry(SAVANNA_ROCK, SAVANNA),
            Map.entry(MESA, MESA),
            Map.entry(MESA_ROCK, MESA),
            Map.entry(MESA_CLEAR_ROCK, MESA)
    );


    public static Integer get_temp_category(int biome) {
        if (!TEMP.containsKey(biome))
            return TEMP_MEDIUM;
        if (TEMP.get(biome) < 0.2)
            return TEMP_COLD;
        else if (TEMP.get(biome) < 1.0)
            return TEMP_MEDIUM;
        return TEMP_WARM;
    }

    private static final List<Integer> SNOWY_BIOMES = List.of(FROZEN_OCEAN, FROZEN_RIVER, ICE_PLAINS, ICE_MOUNTAINS, COLD_BEACH, TAIGA_COLD, TAIGA_COLD_HILLS);

    public static boolean isSnowy(int biome) {
        return SNOWY_BIOMES.contains(biome);
    }

    public static Integer getBiomeClass(int biome) {
        return CLASSES.getOrDefault(biome, null);
    }

//Don't support mutations yet

    public static boolean isMutation(int biome) {
        return false;
    }

    public static Integer getMutationForBiome(int biome) {
        return MUTATIONS.getOrDefault(biome, null);
    }


}
