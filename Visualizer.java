package com.siinus;

import java.util.Arrays;

public class Visualizer {
    public static final int NUM_BIOMES = 64;

    private static Program program = new Program();

        /*public static final double[][] hyperbiomeColors = {
                {21, 128, 209},  // #ocean 0

    {
        227, 217, 159
    },  // #warm 1

    {
        163, 207, 114
    },  // #medium
                {
        45, 102, 43
    },  // #cold 3

    {
        209, 240, 238
    },  // #ice 4

    {
        87, 207, 54
    },  // # 5

    {
        87, 207, 54
    },  // # 6

    {
        87, 207, 54
    },  // # 7

    {
        87, 207, 54
    },  // # 8

    {
        87, 207, 54
    },  // # 9

    {
        87, 207, 54
    },  // # 10

    {
        87, 207, 54
    },  // # 11

    {
        87, 207, 54
    },  // # 12

    {
        87, 207, 54
    },  // # 13

    {
        224, 171, 245
    },  // #mushroom_island 14

    {
        87, 207, 54
    },  // # 15

    {
        87, 207, 54
    },  // # 16

    {
        87, 207, 54
    },  // # 17

    {
        87, 207, 54
    },  // # 18

    {
        87, 207, 54
    },  // # 19

    {
        87, 207, 54
    },  // # 20

    {
        87, 207, 54
    },  // # 21

    {
        87, 207, 54
    },  // # 22

    {
        87, 207, 54
    },  // # 23

    {
        21, 109, 209
    }  // #deep_ocean 24
};

        public static final double[][] biomeColors = {
        {21,128,209},  // # ocean 0
        {129,194,101},  // # plains 1
        {227,217,159},  // # desert 2
        {184,194,178},  // # extreme_hills 3
        {55,125,52},  // # forest 4
        {45,102,43},  // # taiga 5
        {78,125,25},  // # swampland 6
        {23,173,232},  // # river 7
        {148,41,15},  // # hell 8
        {245,245,245},  // # sky 9
        {181,217,245},  // # frozen_ocean 10
        {181,217,245},  // # frozen_river 11
        {209,240,238},  // # ice_flats 12
        {235,245,244},  // # ice_mountains 13
        {224,171,245},  // # mushroom_island 14
        {213,170,230},  // # mushroom_island_shore 15
        {227,217,159},  // # beaches 16
        {227,217,159},  // # desert_hills 17
        {71,145,68},  // # forest_hills 18
        {45,102,43},  // # taiga_hills 19
        {169,199,151},  // # smaller_extreme_hills 20
        {56,186,0},  // # jungle 21
        {86,204,35},  // # jungle_hills 22
        {49,138,11},  // # jungle_edge 23
        {21,109,209},  // # deep_ocean 24
        {166,165,151},  // # stone_beach 25
        {224,220,193},  // # cold_beach 26
        {212,235,145},  // # birch_forest 27
        {223,240,173},  // # birch_forest_hills 28
        {59,130,5},  // # roofed_forest 29
        {121,140,122},  // # taiga_cold 30
        {145,161,145},  // # taiga_cold_hills 31
        {56,82,42},  // # redwood_taiga 32
        {74,99,61},  // # redwood_taiga_hills 33
        {167,199,157},  // # extreme_hills_with_trees 34
        {175,181,69},  // # savanna 35
        {171,170,114},  // # savanna_rock 36
        {115,66,44},  // # mesa 37
        {107,81,70},  // # mesa_rock 38
        {143,114,101}  // # mesa_clear_rock 39
        };

        //hyperbiomeColors /= 255
        //biomeColors /= 255
    static {
        for (int i=0; i<hyperbiomeColors.length; i++) {
            for (double c : hyperbiomeColors[i]) {
                c /= 255.0;
            }
        }
            for (int i=0; i<biomeColors.length; i++) {
                for (double c : biomeColors[i]) {
                    c /= 255.0;
                }
            }
    }
        hb_cm = ListedColormap(hyperbiomeColors, N=NUM_BIOMES)
        b_cm = ListedColormap(biomeColors, N=NUM_BIOMES)


        def show_hyperbiome_map(arr, title="", file_name=None, extent=None):
        arr = arr % (1 << 8)
        plt.figure(figsize=(10, 10))
        plt.imshow(np.transpose(arr), cmap=hb_cm, vmin=0, vmax=NUM_BIOMES-1, interpolation='nearest', extent=extent)
        plt.title(title)
        if file_name is not None:
        plt.savefig(file_name)
        plt.clf()
        else:
        plt.show()

        def show_biome_map(arr, title="", file_name=None, extent=None):
        plt.figure(figsize=(10, 10))
        plt.imshow(np.transpose(arr) % NUM_BIOMES, cmap=b_cm, vmin=0, vmax=NUM_BIOMES-1, interpolation='nearest', extent=extent)
        plt.title(title)
        if file_name is not None:
        plt.savefig(file_name)
        plt.clf()
        else:
        plt.show()

        def show_gen_procedure(layer: GenLayers.GenLayer, size: int, is_hyper_biomes=False, save_gif=False):
        count = 0
        if layer.parent is not None:
        if isinstance(layer, GenLayers.Zoom):
        count = show_gen_procedure(layer.parent, size // 2, is_hyper_biomes=is_hyper_biomes, save_gif=save_gif)
        elif isinstance(layer, GenLayers.Biome):
        count = show_gen_procedure(layer.parent, size, is_hyper_biomes=True, save_gif=save_gif)
        else:
        count = show_gen_procedure(layer.parent, size, is_hyper_biomes=is_hyper_biomes, save_gif=save_gif)
        x = y = -size // 2
        width = height = size + 1
        image = layer.get_ints(x, y, width, height)
        file_name = f"gif/{count}.png" if save_gif else None
        if is_hyper_biomes:
        show_hyperbiome_map(image, title=str(type(layer)), file_name=file_name, extent=[x, x + width, y + height, y])
        else:
        show_biome_map(image, title=str(type(layer)), file_name=file_name, extent=[x, x+width, y+height, y])
        return count + 1*/

    // # Set up all the layers

    public static GenLayers.Island island = new GenLayers.Island(1);
    public static GenLayers.FuzzyZoom fuzzy_zoom = new GenLayers.FuzzyZoom(2000, island);
    public static GenLayers.AddIsland add_island1 = new GenLayers.AddIsland(1, fuzzy_zoom);
    public static GenLayers.Zoom zoom1 = new GenLayers.Zoom(2001, add_island1);
    public static GenLayers.AddIsland add_island2 = new GenLayers.AddIsland(2, zoom1);

    static {
        add_island2 = new GenLayers.AddIsland(50, add_island2);
        add_island2 = new GenLayers.AddIsland(70, add_island2);
    }

    public static GenLayers.RemoveTooMuchOcean remove_too_much_ocean = new GenLayers.RemoveTooMuchOcean(2, add_island2);
    public static GenLayers.AddSnow add_snow = new GenLayers.AddSnow(2, remove_too_much_ocean);
    public static GenLayers.AddIsland add_island3 = new GenLayers.AddIsland(3, add_snow);
    public static GenLayers.EdgeCoolWarm edge_cool_warm = new GenLayers.EdgeCoolWarm(2, add_island3);
    public static GenLayers.EdgeHeatIce edge_heat_ice = new GenLayers.EdgeHeatIce(2, edge_cool_warm);
    public static GenLayers.EdgeSpecial edge_special = new GenLayers.EdgeSpecial(3, edge_heat_ice);
    public static GenLayers.Zoom zoom2 = new GenLayers.Zoom(2002, edge_special);

    static {
        zoom2 = new GenLayers.Zoom(2003, zoom2);
    }

    public static GenLayers.AddIsland add_island4 = new GenLayers.AddIsland(4, zoom2);
    public static GenLayers.AddMushroomIsland mushroom = new GenLayers.AddMushroomIsland(5, add_island4);
    public static GenLayers.DeepOcean deep_ocean = new GenLayers.DeepOcean(4, mushroom);
    // # Also, this is all taken from 1.12, so there are no fancy ocean biomes

    public static GenLayers.Biome biome = new GenLayers.Biome(200, deep_ocean);
    public static GenLayers.RiverInit river_init = new GenLayers.RiverInit(100, deep_ocean);
    public static GenLayers.Zoom river_zoom1 = new GenLayers.Zoom(1000, river_init);

    static {
        river_zoom1 = new GenLayers.Zoom(1001, river_zoom1);
    }

    public static GenLayers.Zoom zoom3 = new GenLayers.Zoom(1000, biome);

    static {
        zoom3 = new GenLayers.Zoom(1001, zoom3);
    }

    public static GenLayers.BiomeEdge biome_edge = new GenLayers.BiomeEdge(1000, zoom3);
    public static GenLayers.Hills hills = new GenLayers.Hills(1000, biome_edge, river_zoom1); // # hills also should add mutated biomes, but I was too lazy to add those :\
    public static GenLayers.Zoom river_zoom2 = new GenLayers.Zoom(1000, river_zoom1);

    static {
        river_zoom2 = new GenLayers.Zoom(1001, river_zoom2);
        river_zoom2 = new GenLayers.Zoom(1002, river_zoom2);
        river_zoom2 = new GenLayers.Zoom(1003, river_zoom2);
    }

    // # rare_biome - There should be a layer here that turns some plains into sunflower plains, but I was also too lazy add it.
    // # Also, what's up with having a layer to make sunflower plains? That's just mutated plains. Should that already be handled by hills? Mojang your game makes no sense.
    public static GenLayers.Zoom zoom4 = new GenLayers.Zoom(1000, hills);
    public static GenLayers.AddIsland add_island5 = new GenLayers.AddIsland(3, zoom4);
    public static GenLayers.Zoom zoom5 = new GenLayers.Zoom(1001, add_island5);
    public static GenLayers.Shore shore = new GenLayers.Shore(1000, zoom5);
    public static GenLayers.Zoom zoom6 = new GenLayers.Zoom(1002, shore);

    static {
        zoom6 = new GenLayers.Zoom(1003, zoom6);
    }

    public static GenLayers.Smooth smooth = new GenLayers.Smooth(1000, zoom6);

    public static GenLayers.River river = new GenLayers.River(1, river_zoom2);
    public static GenLayers.Smooth river_smooth = new GenLayers.Smooth(1000, river);

    public static GenLayers.RiverMix river_mix = new GenLayers.RiverMix(100, smooth, river_smooth);
    // # There should be a layer here called voronoi_zoom, which does a fancy x4 zoom, but I'm lazy


    private static int[][] image;
    public static int[][] drawImage;

    public static int genProcedure(GenLayers.GenLayer layer, int size) {
        int count = 0;
        if (layer.parent != null) {
            count = genProcedure(layer.parent, layer instanceof GenLayers.Zoom ? size / 2 : size);
        }
        int x, y;
        x = y = -size / 2;
        int width, height;
        width = height = size + 1;
        image = layer.getInts(x, y, width, height);
        //updateDrawImage();
        return count + 1;
    }

    public static void updateDrawImage() {
        int[][] tI = new int[image[0].length][image.length];
        for (int i=0; i<image.length; i++) {
            for (int j=0; j<image[i].length; j++) {
                tI[j][i] = image[i][j];
            }
        }
        drawImage = tI;
    }

    public static void main(String[] args) {
        // # EDIT THIS TO CHANGE THE WORLD SEED
        long SEED = /*-2143500864L*/-7379792618385405355L;
        if (args.length > 0) {
            SEED = Long.parseLong(args[0]);
        }

        river_mix.initWorldGenSeed(SEED);

        // # Use show_gen_procedure() to see the full generation process.
        // # Enter the final layer and final size you want.
        // # Set save_gif=True to save a sequence of pngs instead of showing all images.
        //show_gen_procedure(river_mix, 2048, save_gif = False)
        genProcedure(river_mix, 2048);

        // # Show a single layer by using show_biome_map() or show_hyperbiome_map()
        // # Pass in the result of GenLayer.get_ints(x, y, width, height)
        // # Example:
        // # show_biome_map(river_mix.get_ints(-256, -256, 512, 512))

        updateDrawImage();
        program.init();
        program.getWindow().getFrame().setTitle("Minecraft seed map: "+SEED);
    }

    public static int colorOfBiome(int biome) {
        return switch (biome) {
            case 0, 7 -> 0xff00007f;
            case 1 -> 0xffbfff7f;
            case 2 -> 0xffffbf3f;
            case 3 -> 0xff7f7f7f;
            case 4 -> 0xff00bf3f;
            case 5 -> 0xff00bf7f;
            case 6 -> 0xff00ffff;
            case 10, 11 -> 0xffbfbfff;
            case 12 -> 0xffffffff;
            case 13 -> 0xffbfbfbf;
            case 14 -> 0xffff00ff;
            case 15 -> 0xff7f00ff;
            case 16 -> 0xffffff7f;
            case 17 -> 0xffff7f00;
            case 18 -> 0xff007f1f;
            case 19 -> 0xff007f5f;
            case 20 -> 0xff8f7f6f;
            case 21 -> 0xff3fbf00;
            case 22 -> 0xff1f7f00;
            case 23 -> 0xff5fdf00;
            case 24 -> 0xff00003f;
            case 25 -> 0xff6f6f8f;
            case 26 -> 0xffffffbf;
            case 27 -> 0xff3f9f3f;
            case 28 -> 0xff1f5f1f;
            case 29 -> 0xff1f9f2f;
            case 30 -> 0xff005f5f;
            case 31 -> 0xff002f2f;
            case 32 -> 0xff8f6f4f;
            case 33 -> 0xff6f4f2f;
            case 34 -> 0xff6f8f6f;
            case 35 -> 0xffbfbf5f;
            case 36 -> 0xff9f9f3f;
            case 37 -> 0xffff0000;
            case 38 -> 0xffff7f7f;
            case 39 -> 0xffff9f7f;
            default -> 0xff000000;
        };
    }
}