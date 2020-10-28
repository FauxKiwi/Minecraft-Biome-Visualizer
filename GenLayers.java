package com.siinus;

import java.util.List;

public class GenLayers {

    public static long convolute(long x, long a) {
        return (x * (x * 6364136223846793005L + 1442695040888963407L)) + a;
    }

    public static class GenLayer {
        private long baseSeed;
        private long worldGenSeed;
        private long chunkSeed;

        protected GenLayer parent;

        public GenLayer(long baseSeed) {
            this.baseSeed = baseSeed;
            this.baseSeed = convolute(this.baseSeed, baseSeed);
            this.baseSeed = convolute(this.baseSeed, baseSeed);
            this.baseSeed = convolute(this.baseSeed, baseSeed);

            worldGenSeed = 0L;
            parent = null;
            chunkSeed = 0L;
        }

        public void initWorldGenSeed(long seed) {
            worldGenSeed = seed;

            if (parent != null)
                parent.initWorldGenSeed(seed);

            worldGenSeed = convolute(worldGenSeed, baseSeed);
            worldGenSeed = convolute(worldGenSeed, baseSeed);
            worldGenSeed = convolute(worldGenSeed, baseSeed);
        }

        public void initChunkSeed(long x, long z) {
            chunkSeed = worldGenSeed;

            chunkSeed = convolute(chunkSeed, x);
            chunkSeed = convolute(chunkSeed, z);
            chunkSeed = convolute(chunkSeed, x);
            chunkSeed = convolute(chunkSeed, z);
        }

        public int next_int(long r) {
            int i = (int) ((chunkSeed >> 24) % r);

            if (i < 0)
                i += r;

            chunkSeed = convolute(chunkSeed, worldGenSeed);

            return i;
        }

        public int selectRandom(int... choices) {
            return choices[next_int(choices.length)];
        }

        public int selectRandom(List<Integer> choices) {
            return choices.get(next_int(choices.size()));
        }

        public boolean biomesEqualOrMesaPlateau(int a, int b) {
            if (a == b)
                return true;
            if (a == Biomes.MESA_ROCK || a == Biomes.MESA_CLEAR_ROCK)
                return b == Biomes.MESA_ROCK || b == Biomes.MESA_CLEAR_ROCK;
            return Biomes.getBiomeClass(a) == Biomes.getBiomeClass(b);
        }

        public boolean isOceanic(int a) {
            return a == Biomes.OCEAN || a == Biomes.DEEP_OCEAN || a == Biomes.FROZEN_OCEAN;
        }

        public int[][] getInts(int x, int y, int width, int height) {
            return null;
        }
    }

    public static class Island extends GenLayer {

        public Island(long baseSeed) {
            super(baseSeed);
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    initChunkSeed(x + i, y + j);
                    new_arr[i][j] = next_int(10) == 0 ? 1 : 0;
                }
            }

            if (-width < x && x <= 0 && -height < y && y <= 0)
                new_arr[-x][-y] = 1;

            return new_arr;
        }
    }

    public static class Zoom extends GenLayer {
        public Zoom(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        public int modeOrRandom(int a, int b, int c, int d) {
            if (b ==
                    c &&
                    c == d)
                return b;
            else if (a ==
                    b &&
                    a == c)
                return a;
            else if (a ==
                    b &&
                    a == d)
                return a;
            else if (a ==
                    c &&
                    a == d)
                return a;
            else if (a ==
                    b &&
                    c != d)
                return a;
            else if (a ==
                    c &&
                    b != d)
                return a;
            else if (a ==
                    d &&
                    b != c)
                return a;
            else if (b ==
                    c &&
                    a != d)
                return b;
            else if (b ==
                    d &&
                    a != c)
                return b;
            return c ==
                    d &&
                    a != b ? c : selectRandom(a, b, c, d);
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int px = x >> 1;
            int py = y >> 1;
            int pWidth = (width >> 1) + 2;
            int pHeight = (height >> 1) + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);
            int iWidth = (pWidth - 1) << 1;
            int iHeight = (pHeight - 1) << 1;

            int[][] i_arr = new int[iWidth][iHeight];
            for (int j = 0; j < pHeight - 1; j++) {
                for (int i = 0; i < pWidth - 1; i++) {
                    initChunkSeed((px + i) * 2, (py + j) * 2);
                    i_arr[2 * i][2 * j] = p_arr[i][j];
                    i_arr[2 * i][2 * j + 1] = selectRandom(p_arr[i][j], p_arr[i][j + 1]);
                    i_arr[2 * i + 1][2 * j] = selectRandom(p_arr[i][j], p_arr[i + 1][j]);
                    i_arr[2 * i + 1][2 * j + 1] = modeOrRandom(p_arr[i][j], p_arr[i + 1][j], p_arr[i][j + 1],
                            p_arr[i + 1][j + 1]);
                }
            }
            int startX = x & 1;
            int startY = y & 1;
            /*int[][] new_arr = new int[i_arr.length - startX - width][i_arr.length - startY - height];
            for (int i = 0; i < new_arr.length; i++) {
                System.arraycopy(i_arr[i + startX + width], startY + height, new_arr[i], 0, i_arr[i + startX + width].length - startY - height);
            }*/
            //new_arr = i_arr[startX:(startX +width),startY:(startY +height)]
            int[][] new_arr = new int[i_arr.length-startY][i_arr.length-startX];
            for (int i = 0; i < new_arr.length; i++) {
                System.arraycopy(i_arr[i + startY], startY, new_arr[i], 0, i_arr[i + startX].length - startY);
            }

            return new_arr;
        }
    }

    public static class FuzzyZoom extends Zoom {
        public FuzzyZoom(long baseSeed, GenLayer parent) {
            super(baseSeed, parent);
        }

        @Override
        public int modeOrRandom(int a, int b, int c, int d) {
            return selectRandom(a, b, c, d);
        }
    }

    public static class AddIsland extends GenLayer {
        public AddIsland(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    initChunkSeed(x + i, y + j);
                    int a = p_arr[i][j];
                    int b = p_arr[i + 2][j];
                    int c = p_arr[i][j + 2];
                    int d = p_arr[i + 2][j + 2];
                    int k = p_arr[i + 1][j + 1];

                    if (k != 0

                            || (a == 0 && b == 0 && c == 0 && d == 0)) {
                        if (k > 0

                                && (a == 0 || b == 0 || c == 0 || d == 0)) {
                            if (next_int(5) == 0) {
                                if (k == 4)
                                    new_arr[i][j] = 4;
                                else
                                    new_arr[i][j] = 0;
                            } else
                                new_arr[i][j] = k;
                        } else
                            new_arr[i][j] = k;
                    } else {
                        int l2 = 1;
                        int n = 1;
                        if (a != 0) {
                            if (next_int(l2) == 0)
                                n = a;
                            l2++;
                        }
                        if (b != 0) {
                            if (next_int(l2) == 0)
                                n = b;
                            l2++;
                        }
                        if (c != 0) {
                            if (next_int(l2) == 0)
                                n = c;
                            l2++;
                        }
                        if (d != 0) {
                            if (next_int(l2) == 0)
                                n = d;
                            l2++;
                        }

                        if (next_int(3) == 0)
                            new_arr[i][j] = n;
                        else if (n == 4)
                            new_arr[i][j] = 4;
                        else
                            new_arr[i][j] = 0;
                    }
                }
            }

            return new_arr;
        }
    }

    public static class RemoveTooMuchOcean extends GenLayer {
        public RemoveTooMuchOcean(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    initChunkSeed(x + i, y + j);
                    int a = p_arr[i + 1][j];
                    int b = p_arr[i + 2][j + 1];
                    int c = p_arr[i][j + 1];
                    int d = p_arr[i + 1][j + 2];
                    int k = p_arr[i + 1][j + 1];

                    new_arr[i][j] = k;
                    if (k == 0
                            && a == 0
                            && b == 0
                            && c == 0
                            && d == 0
                            && next_int(2) == 0)
                        new_arr[i][j] = 1;
                }
            }

            return new_arr;
        }
    }

    public static class AddSnow extends GenLayer {
        public AddSnow(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int[][] p_arr = parent.getInts(x, y, width, height);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    initChunkSeed(x + i, y + j);
                    int k = p_arr[i][j];
                    if (k == 0)
                        new_arr[i][j] = 0;
                    else {
                        int r = next_int(6);
                        if (r == 0)
                            r = 4;
                        else if (r <= 1)
                            r = 3;
                        else
                            r = 1;
                        new_arr[i][j] = r;
                    }
                }
            }

            return new_arr;
        }
    }

    public static class EdgeCoolWarm extends GenLayer {
        public EdgeCoolWarm(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    int k = p_arr[i + 1][j + 1];

                    if (k == 1) {
                        int a = p_arr[i + 1][j];
                        int b = p_arr[i + 2][j + 1];
                        int c = p_arr[i][j + 1];
                        int d = p_arr[i + 1][j + 2];
                        boolean flag1 = a == 3 || b == 3 || c == 3 || d == 3;
                        boolean flag2 = a == 4 || b == 4 || c == 4 || d == 4;

                        if (flag1 || flag2)
                            k = 2;
                    }
                    new_arr[i][j] = k;
                }
            }

            return new_arr;
        }
    }

    public static class EdgeHeatIce extends GenLayer {
        public EdgeHeatIce(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    int k = p_arr[i + 1][j + 1];

                    if (k == 4) {
                        int a = p_arr[i + 1][j];
                        int b = p_arr[i + 2][j + 1];
                        int c = p_arr[i][j + 1];
                        int d = p_arr[i + 1][j + 2];
                        boolean flag1 = a == 2 || b == 2 || c == 2 || d == 2;
                        boolean flag2 = a == 1 || b == 1 || c == 1 || d == 1;

                        if (flag1 || flag2)
                            k = 3;
                    }
                    new_arr[i][j] = k;
                }
            }

            return new_arr;
        }
    }

    public static class EdgeSpecial extends GenLayer {
        public EdgeSpecial(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int[][] p_arr = parent.getInts(x, y, width, height);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    initChunkSeed(x + i, y + j);
                    int k = p_arr[i][j];
                    if (k != 0 && next_int(13) == 0)
                        k |= (((1 + next_int(15)) << 8) & 3840);
                    new_arr[i][j] = k;
                }
            }

            return new_arr;
        }
    }

    public static class AddMushroomIsland extends GenLayer {
        public AddMushroomIsland(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    initChunkSeed(x + i, y + j);
                    int a = p_arr[i][j];
                    int b = p_arr[i + 2][j];
                    int c = p_arr[i][j + 2];
                    int d = p_arr[i + 2][j + 2];
                    int k = p_arr[i + 1][j + 1];

                    if (k == 0 && a == 0 && b == 0 && c == 0 && d == 0 && next_int(100) == 0)
                        new_arr[i][j] = Biomes.MUSHROOM_ISLAND;
                    else
                        new_arr[i][j] = k;
                }
            }

            return new_arr;
        }
    }

    public static class DeepOcean extends GenLayer {
        public DeepOcean(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    initChunkSeed(x + i, y + j);
                    int a = p_arr[i][j];
                    int b = p_arr[i + 2][j];
                    int c = p_arr[i][j + 2];
                    int d = p_arr[i + 2][j + 2];
                    int k = p_arr[i + 1][j + 1];

                    if (k == 0 && a == 0 && b == 0 && c == 0 && d == 0)
                        new_arr[i][j] = Biomes.DEEP_OCEAN;
                    else
                        new_arr[i][j] = k;
                }
            }

            return new_arr;
        }
    }

    public static class Biome extends GenLayer {
        public Biome(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    initChunkSeed(x + i, y + j);
                    int k = p_arr[i][j];
                    int r = (k & 3840) >> 8;
                    k &= -3841;

                    if (k == 0 || k == Biomes.DEEP_OCEAN || k == Biomes.FROZEN_OCEAN || k == Biomes.MUSHROOM_ISLAND)
                        new_arr[i][j] = k;
                    else if (k == 1) {
                        if (r > 0) {
                            if (next_int(3) == 0)
                                new_arr[i][j] = Biomes.MESA_CLEAR_ROCK;
                            else
                                new_arr[i][j] = Biomes.MESA_ROCK;
                        } else
                            new_arr[i][j] = selectRandom(Biomes.WARM_BIOMES);
                    } else if (k == 2) {
                        if (r > 0)
                            new_arr[i][j] = Biomes.JUNGLE;
                        else
                            new_arr[i][j] = selectRandom(Biomes.MEDIUM_BIOMES);
                    } else if (k == 3) {
                        if (r > 0)
                            new_arr[i][j] = Biomes.REDWOOD_TAIGA;
                        else
                            new_arr[i][j] = selectRandom(Biomes.COLD_BIOMES);
                    } else if (k == 4)
                        new_arr[i][j] = selectRandom(Biomes.ICE_BIOMES);
                    else
                        new_arr[i][j] = Biomes.MUSHROOM_ISLAND;
                }
            }

            return new_arr;
        }
    }

    public static class BiomeEdge extends GenLayer {
        public BiomeEdge(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        public boolean canBeNeighbors(int a, int b) {
            if (biomesEqualOrMesaPlateau(a, b))
                return true;
            int temp_a = Biomes.get_temp_category(a);
            int temp_b = Biomes.get_temp_category(b);
            if (temp_a == temp_b || temp_a == Biomes.TEMP_MEDIUM || temp_b == Biomes.TEMP_MEDIUM)
                return true;
            return false;
        }

        public boolean replaceBiomeEdge(int[][] p_arr, int[][] new_arr, int i, int j, int k, int to_replace, int replace_with) {
            if (k != to_replace)
                return false;
            else {
                int a = p_arr[i + 1][j];
                int b = p_arr[i + 2][j + 1];
                int c = p_arr[i][j + 1];
                int d = p_arr[i + 1][j + 2];

                if (biomesEqualOrMesaPlateau(a, k) && biomesEqualOrMesaPlateau(b, k) && biomesEqualOrMesaPlateau(c, k) && biomesEqualOrMesaPlateau(d, k))
                    new_arr[i][j] = to_replace;
                else
                    new_arr[i][j] = replace_with;
                return true;
            }
        }

        public boolean replaceBiomeEdgeIfNecessary(int[][] p_arr, int[][] new_arr, int i, int j, int k, int to_replace, int replace_with) {
            if (!biomesEqualOrMesaPlateau(k, to_replace))
                return false;
            else {
                int a = p_arr[i + 1][j];
                int b = p_arr[i + 2][j + 1];
                int c = p_arr[i][j + 1];
                int d = p_arr[i + 1][j + 2];

                if (canBeNeighbors(a, k) && canBeNeighbors(b, k) && canBeNeighbors(c, k) && canBeNeighbors(d, k))
                    new_arr[i][j] = to_replace;
                else
                    new_arr[i][j] = replace_with;
                return true;
            }
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    initChunkSeed(x + i, y + j);
                    int k = p_arr[i][j];

                    if (!replaceBiomeEdgeIfNecessary(p_arr, new_arr, i, j, k, Biomes.EXTREME_HILLS, Biomes.SMALLER_EXTREME_HILLS)
                            && !replaceBiomeEdge(p_arr, new_arr, i, j, k, Biomes.MESA_ROCK, Biomes.MESA)
                            && !replaceBiomeEdge(p_arr, new_arr, i, j, k, Biomes.MESA_CLEAR_ROCK, Biomes.MESA)
                            && !replaceBiomeEdge(p_arr, new_arr, i, j, k, Biomes.REDWOOD_TAIGA, Biomes.TAIGA)) {

                        int a = p_arr[i + 1][j];
                        int b = p_arr[i + 2][j + 1];
                        int c = p_arr[i][j + 1];
                        int d = p_arr[i + 1][j + 2];

                        if (k == Biomes.DESERT) {
                            if (a != Biomes.ICE_PLAINS && b != Biomes.ICE_PLAINS && c != Biomes.ICE_PLAINS && d != Biomes.ICE_PLAINS)
                                new_arr[i][j] = k;
                            else
                                new_arr[i][j] = Biomes.EXTREME_HILLS_WITH_TREES;
                        } else if (k == Biomes.SWAMPLAND) {
                            if (a != Biomes.DESERT && b != Biomes.DESERT && c != Biomes.DESERT && d != Biomes.DESERT
                                    && a != Biomes.TAIGA_COLD && b != Biomes.TAIGA_COLD && c != Biomes.TAIGA_COLD && d != Biomes.TAIGA_COLD) {
                                if (a != Biomes.JUNGLE && b != Biomes.JUNGLE && c != Biomes.JUNGLE && d != Biomes.JUNGLE)
                                    new_arr[i][j] = k;
                                else
                                    new_arr[i][j] = Biomes.JUNGLE_EDGE;
                            } else
                                new_arr[i][j] = Biomes.PLAINS;
                        } else
                            new_arr[i][j] = k;
                    }
                }
            }

            return new_arr;
        }
    }

    public static class RiverInit extends GenLayer {
        public RiverInit(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int[][] p_arr = parent.getInts(x, y, width, height);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    initChunkSeed(x + i, y + j);
                    if (p_arr[i][j] > 0)
                        new_arr[i][j] = next_int(299999) + 2;
                    else
                        new_arr[i][j] = 0;
                }
            }

            return new_arr;
        }
    }

    public static class River extends GenLayer {
        public River(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        public int river_filter(int k) {
            if (k >= 2)
                return 2 + (k & 1);
            else
                return k;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    int a = river_filter(p_arr[i + 1][j]);
                    int b = river_filter(p_arr[i + 2][j + 1]);
                    int c = river_filter(p_arr[i][j + 1]);
                    int d = river_filter(p_arr[i + 1][j + 2]);
                    int k = river_filter(p_arr[i + 1][j + 1]);

                    if (k == a && k == b && k == c && k == d)
                        new_arr[i][j] = -1;
                    else
                        new_arr[i][j] = Biomes.RIVER;
                }
            }

            return new_arr;
        }
    }

    public static class Hills extends GenLayer {
        private GenLayer riverLayer;

        public Hills(long baseSeed, GenLayer parent, GenLayer riverLayer) {
            super(baseSeed);
            this.parent = parent;
            this.riverLayer = riverLayer;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);
            int[][] r_arr = riverLayer.getInts(x, y, width, height);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    initChunkSeed(x + i, y + j);
                    int k = p_arr[i][j];
                    int l = r_arr[i][j];
                    boolean mutateHills = l >= 2 && (l - 2) % 29 == 0;


                    if (k != 0 && l >= 2 && (l - 2) % 29 == 1 && !Biomes.isMutation(k)) {
                        //# Mutate biome
                        Integer mutated = Biomes.getMutationForBiome(k);
                        new_arr[i][j] = mutated != null ? mutated : k;
                    } else if (next_int(3) != 0 && !mutateHills)
                        new_arr[i][j] = k;
                    else {
                        int n = k;
                        if (k == Biomes.DESERT)
                            n = Biomes.DESERT_HILLS;
                        else if (k == Biomes.FOREST)
                            n = Biomes.FOREST_HILLS;
                        else if (k == Biomes.BIRCH_FOREST)
                            n = Biomes.BIRCH_FOREST_HILLS;
                        else if (k == Biomes.ROOFED_FOREST)
                            n = Biomes.PLAINS;
                        else if (k == Biomes.TAIGA)
                            n = Biomes.TAIGA_HILLS;
                        else if (k == Biomes.REDWOOD_TAIGA)
                            n = Biomes.REDWOOD_TAIGA_HILLS;
                        else if (k == Biomes.TAIGA_COLD)
                            n = Biomes.TAIGA_COLD_HILLS;
                        else if (k == Biomes.PLAINS) {
                            if (next_int(3) == 0)
                                n = Biomes.FOREST_HILLS;
                            else
                                n = Biomes.FOREST;
                        } else if (k == Biomes.ICE_PLAINS)
                            n = Biomes.ICE_MOUNTAINS;
                        else if (k == Biomes.JUNGLE)
                            n = Biomes.JUNGLE_HILLS;
                        else if (k == Biomes.OCEAN)
                            n = Biomes.DEEP_OCEAN;
                        else if (k == Biomes.EXTREME_HILLS)
                            n = Biomes.EXTREME_HILLS_WITH_TREES;
                        else if (k == Biomes.SAVANNA)
                            n = Biomes.SAVANNA_ROCK;
                        else if (biomesEqualOrMesaPlateau(k, Biomes.MESA_ROCK))
                            n = Biomes.MESA;
                        else if (k == Biomes.DEEP_OCEAN && next_int(3) == 0) {
                            int c = next_int(2);
                            if (c == 0)
                                n = Biomes.PLAINS;
                            else
                                n = Biomes.FOREST;
                        }

                        if (mutateHills && n != k) {
                            Integer mutated = Biomes.getMutationForBiome(n);
                            n = mutated != null ? mutated : k;
                        }

                        if (n == k)
                            new_arr[i][j] = k;
                        else {
                            int a = p_arr[i + 1][j];
                            int b = p_arr[i + 2][j + 1];
                            int c = p_arr[i][j + 1];
                            int d = p_arr[i + 1][j + 2];

                            int numSameNeighbors = 0;
                            if (biomesEqualOrMesaPlateau(a, k))
                                numSameNeighbors++;
                            if (biomesEqualOrMesaPlateau(b, k))
                                numSameNeighbors++;
                            if (biomesEqualOrMesaPlateau(c, k))
                                numSameNeighbors++;
                            if (biomesEqualOrMesaPlateau(d, k))
                                numSameNeighbors++;

                            if (numSameNeighbors >= 3)
                                new_arr[i][j] = n;
                            else
                                new_arr[i][j] = k;
                        }
                    }
                }
            }

            return new_arr;
        }
    }

    public static class Smooth extends GenLayer {
        public Smooth(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    int a = p_arr[i + 1][j];
                    int b = p_arr[i + 2][j + 1];
                    int c = p_arr[i][j + 1];
                    int d = p_arr[i + 1][j + 2];
                    int k = p_arr[i + 1][j + 1];

                    if (a == b && c == d) {
                        initChunkSeed(x + i, y + j);
                        if (next_int(2) == 0)
                            k = a;
                        else
                            k = c;
                    } else {
                        if (a == b)
                            k = a;
                        if (c == d)
                            k = c;
                    }
                    new_arr[i][j] = k;
                }
            }

            return new_arr;
        }
    }

    public static class RiverMix extends GenLayer {
        private GenLayer riverLayer;

        public RiverMix(long baseSeed, GenLayer parent, GenLayer riverLayer) {
            super(baseSeed);
            this.parent = parent;
            this.riverLayer = riverLayer;
        }

        public void initWorldGenSeed(long seed) {
            super.initWorldGenSeed(seed);
            if (riverLayer != null)
                riverLayer.initWorldGenSeed(seed);
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int[][] p_arr = parent.getInts(x, y, width, height);
            int[][] r_arr = riverLayer.getInts(x, y, width, height);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    if (p_arr[i][j] != Biomes.OCEAN && p_arr[i][j] != Biomes.DEEP_OCEAN) {
                        if (r_arr[i][j] == Biomes.RIVER) {
                            if (p_arr[i][j] == Biomes.ICE_PLAINS)
                                new_arr[i][j] = Biomes.FROZEN_RIVER;
                            else if (p_arr[i][j] != Biomes.MUSHROOM_ISLAND && p_arr[i][j] != Biomes.MUSHROOM_ISLAND_SHORE)
                                new_arr[i][j] = r_arr[i][j] & 255;
                            else
                                new_arr[i][j] = Biomes.MUSHROOM_ISLAND_SHORE;
                        } else
                            new_arr[i][j] = p_arr[i][j];
                    } else
                        new_arr[i][j] = p_arr[i][j];
                }
            }

            return new_arr;
        }
    }

    public static class Shore extends GenLayer {
        public Shore(long baseSeed, GenLayer parent) {
            super(baseSeed);
            this.parent = parent;
        }

        public boolean isMesa(int k) {
            return k == Biomes.MESA || k == Biomes.MESA_ROCK || k == Biomes.MESA_CLEAR_ROCK;
        }

        public boolean isJungleCompatible(int k) {
            return k == Biomes.JUNGLE_EDGE || k == Biomes.JUNGLE || k == Biomes.JUNGLE_HILLS || k == Biomes.FOREST || k == Biomes.TAIGA || isOceanic(k);
        }

        @Override
        public int[][] getInts(int x, int y, int width, int height) {
            int[][] new_arr = new int[width][height];
            int px = x - 1;
            int py = y - 1;
            int pWidth = width + 2;
            int pHeight = height + 2;
            int[][] p_arr = parent.getInts(px, py, pWidth, pHeight);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    int a = p_arr[i + 1][j];
                    int b = p_arr[i + 2][j + 1];
                    int c = p_arr[i][j + 1];
                    int d = p_arr[i + 1][j + 2];
                    int k = p_arr[i + 1][j + 1];

                    if (k == Biomes.MUSHROOM_ISLAND) {
                        if (a != Biomes.OCEAN && b != Biomes.OCEAN && c != Biomes.OCEAN && d != Biomes.OCEAN)
                            new_arr[i][j] = k;
                        else
                            new_arr[i][j] = Biomes.MUSHROOM_ISLAND_SHORE;
                    } else if (k == Biomes.JUNGLE || k == Biomes.JUNGLE_HILLS || k == Biomes.JUNGLE_EDGE) {
                        if (isJungleCompatible(a) && isJungleCompatible(b) && isJungleCompatible(c) && isJungleCompatible(d)) {
                            if (isOceanic(a) || isOceanic(b) || isOceanic(c) || isOceanic(d))
                                new_arr[i][j] = Biomes.BEACHES;
                            else
                                new_arr[i][j] = k;
                        } else
                            new_arr[i][j] = Biomes.JUNGLE_EDGE;
                    } else if (k == Biomes.EXTREME_HILLS || k == Biomes.EXTREME_HILLS_WITH_TREES || k == Biomes.SMALLER_EXTREME_HILLS) {
                        if (isOceanic(a) || isOceanic(b) || isOceanic(c) || isOceanic(d))
                            new_arr[i][j] = Biomes.STONE_BEACH;
                        else
                            new_arr[i][j] = k;
                    } else if (Biomes.isSnowy(k)) {
                        if (!isOceanic(k) && (isOceanic(a) || isOceanic(b) || isOceanic(c) || isOceanic(d)))
                            new_arr[i][j] = Biomes.COLD_BEACH;
                        else
                            new_arr[i][j] = k;
                    } else if (k == Biomes.MESA || k == Biomes.MESA_ROCK) {
                        if ((!isOceanic(a) && !isOceanic(b) && !isOceanic(c) && !isOceanic(d))
                                && (!isMesa(a) || !isMesa(b) || !isMesa(c) || !isMesa(d)))
                            new_arr[i][j] = Biomes.DESERT;
                        else
                            new_arr[i][j] = k;
                    } else if (k != Biomes.OCEAN && k != Biomes.DEEP_OCEAN && k != Biomes.RIVER && k != Biomes.SWAMPLAND) {
                        if (isOceanic(a) || isOceanic(b) || isOceanic(c) || isOceanic(d))
                            new_arr[i][j] = Biomes.BEACHES;
                        else
                            new_arr[i][j] = k;
                    } else
                        new_arr[i][j] = k;
                }
            }

            return new_arr;
        }
    }
}