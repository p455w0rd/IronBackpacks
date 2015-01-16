package main.ironbackpacks.util;

import net.minecraftforge.common.config.Configuration;

import java.io.File;


public class ConfigHandler {

    //================Enums (for easy reference by name rather than some number) ============================

    public static int[] basicBackpack;
    public enum enumBasicBackpack{ //TODO - there must be a better way to init these enums...
        upgradeSlots(basicBackpack[0]),
        sizeX(basicBackpack[1]),
        sizeY(basicBackpack[2]);
        private int value;
        private enumBasicBackpack(int value){
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public static int[] ironBackpack;
    public enum enumIronBackpack{
        upgradeSlots(ironBackpack[0]),
        sizeX(ironBackpack[1]),
        sizeY(ironBackpack[2]);
        private int value;
        private enumIronBackpack(int value){
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public static int[] goldBackpack;
    public enum enumGoldBackpack{
        upgradeSlots(goldBackpack[0]),
        sizeX(goldBackpack[1]),
        sizeY(goldBackpack[2]);
        private int value;
        private enumGoldBackpack(int value){
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public static int[] diamondBackpack;
    public enum enumDiamondBackpack{
        upgradeSlots(diamondBackpack[0]),
        sizeX(diamondBackpack[1]),
        sizeY(diamondBackpack[2]);
        private int value;
        private enumDiamondBackpack(int value){
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    private static final int valuesToLoad = 3; //upgradeSlots, sizeX, sizeY

    //==================== All the publicly accessible values====================================


    public static String[] basicBackpackRecipe;
    public static String[] ironBackpackRecipe;
    public static String[] goldBackpackRecipe;
    public static String[] diamondBackpackRecipe;

    public static String[] buttonUpgradeRecipe;
    public static String[] nestingUpgradeRecipe;
    public static String[] renamingUpgradeRecipe;
    public static String[] damageBarUpgradeRecipe;
    public static String[] filterUpgradeRecipe;
    public static String[] hopperUpgradeRecipe;
    public static String[] condenserUpgradeRecipe;

    public static String[] nestRecipe;
    public static String[] upgradeCoreRecipe;

    public static boolean renamingUpgradeRequired;

    //========================init====================================

    public static void init(File file){

        Configuration config = new Configuration(file);

        basicBackpack = new int[valuesToLoad];
        ironBackpack = new int[valuesToLoad];
        goldBackpack = new int[valuesToLoad];
        diamondBackpack = new int[valuesToLoad];

        ///======================================================Recipe defaults================================

        String[] basicRecipe = {"items.leather", "items.leather", "items.leather", "items.leather", "blocks.chest", "items.leather", "items.leather", "items.leather", "items.leather"};
        String[] ironRecipe = {"ingotIron", "ingotIron", "ingotIron", "ingotIron", "items.ironbackpacks:basicBackpack", "ingotIron", "ingotIron", "ingotIron", "ingotIron"};
        String[] goldRecipe = {"ingotGold", "ingotGold", "ingotGold", "ingotGold", "items.ironbackpacks:ironBackpack", "ingotGold", "ingotGold", "ingotGold", "ingotGold"};
        String[] diamondRecipe = {"gemDiamond", "gemDiamond", "gemDiamond", "gemDiamond", "items.ironbackpacks:goldBackpack", "gemDiamond", "gemDiamond", "gemDiamond", "gemDiamond"};

        String[] buttonRecipe = {"items.leather","blocks.stone_button","items.leather", "blocks.wooden_button", "items.ironbackpacks:upgradeCore", "blocks.wooden_button", "items.leather", "blocks.stone_button", "items.leather"};
        String[] nestingRecipe = {"items.feather","items.ironbackpacks:nest","items.feather", "items.ironbackpacks:nest", "items.ironbackpacks:upgradeCore", "items.ironbackpacks:nest", "items.feather", "items.ironbackpacks:nest", "items.feather"};
        String[] renamingRecipe = {"stickWood","items.paper","items.flint", "items.paper", "items.ironbackpacks:upgradeCore", "items.paper", "items.leather", "items.paper", "items.leather"};
        String[] damageBarRecipe = {"items.leather","items.paper","items.leather", "items.paper", "items.ironbackpacks:upgradeCore", "items.paper", "items.leather", "items.paper", "items.leather"};
        String[] filterRecipe = {"items.string","items.paper","items.string", "items.paper", "items.ironbackpacks:upgradeCore", "items.paper", "items.string", "items.paper", "items.string"};
        String[] hopperRecipe = {"none","blocks.hopper","none", "blocks.hopper", "items.ironbackpacks:upgradeCore", "blocks.hopper", "none", "blocks.hopper", "none"};
        String[] condenserRecipe = {"items.string","items.leather","items.string", "items.leather", "items.ironbackpacks:upgradeCore", "items.leather", "items.string", "items.leather", "items.string"};

        String[] upgradeCoreRecipeDefault = {"items.leather","items.string","items.leather", "items.string", "items.paper", "items.string", "items.leather", "items.string", "items.leather"};
        String[] nestRecipeDefault = {"stickWood","stickWood","stickWood", "stickWood", "items.egg", "stickWood", "stickWood", "stickWood", "stickWood"};

        //===================================================Categories======================================

        config.load();

        config.addCustomCategoryComment("5) Recipes", "Each recipe has 9 lines, each corresponding to the next slot in the crafting grid. " +
                "Syntax: use 'items.item_name' or 'blocks.block_name' or 'oreDictionaryName' or 'none' for a blank space. " +
                "\nExample:\n\titems.paper - vanilla item\n\tblocks.gold_block - vanilla block\n\titems.paper - vanilla item\n\t" +
                "plankWood - oreDictionary\n\tingotSilver - oreDictionary(assuming another mod is loaded that adds this)\n\tplankWood - oreDictionary" +
                "\n\titems.modname:itemName - format for mod items (Note: mods may register their items/blocks differently, one possibility is their use of a period instead of a colon. ex: items.modname.itemName)\n\tblockGold - oreDictionary\n\tblocks.modname:blockName - format for mod blocks");

        config.addCustomCategoryComment("0) Backpacks", "Here you can modify the traits of the backpacks.");
        config.addCustomCategoryComment("6) Miscellaneous", "A variety of miscellaneous configurable tweaks and changes to the mod.");

        //======================Upgrade Slots========================
//        ConfigCategory upgradeSlotsCategory = config.getCategory("Upgrade Slots");
//        upgradeSlotsCategory.setComment(" The number of upgrade slots for each type of backpack.");
        //TODO - the categories are sorted alphabetically instead of in this order, so it looks weird - new TreeMap of categories using reflection
        //TODO - config.setCategoryPropertyOrder [maybe, if I deem it necessary]


        //============================================Initializing everything, the numbers keep them in the order I want=======================================

        basicBackpack[0] = config.get("1) Basic Backpack", "1) Upgrade Slots", 0, "The number of upgrades on the backpack. Default 0.").getInt();
        basicBackpack[1] = config.get("1) Basic Backpack", "2) Number of Slots Per Row", 9, "The size of the backpack. Either 9 or 11. Default 9.").getInt();
        basicBackpack[2] = config.get("1) Basic Backpack", "3) Number of Rows", 2, "The size of the backpack. Between 1 and 7. Default 2.").getInt();
        basicBackpackRecipe = config.get("5) Recipes", "1) Basic Backpack Recipe", basicRecipe, "The recipe for the basic backpack. Default is blocks.chest surrounded by items.leather.").getStringList();

        ironBackpack[0] = config.get("2) Iron Backpack", "1) Upgrade Slots", 1, "The number of upgrades on the backpack. Default 1.").getInt();
        ironBackpack[1] = config.get("2) Iron Backpack", "2) Number of Slots Per Row", 9, "The size of the backpack. Either 9 or 11. Default 9.").getInt();
        ironBackpack[2] = config.get("2) Iron Backpack", "3) Number of Rows", 4, "The size of the backpack. Between 1 and 7. Default 4.").getInt();
        ironBackpackRecipe = config.get("5) Recipes", "2) Iron Backpack Recipe", ironRecipe, "The recipe to upgrade the backpack to an Iron Backpack. Default is surrounded by ingotIron.").getStringList();

        goldBackpack[0] = config.get("3) Gold Backpack", "1) Upgrade Slots", 2, "The number of upgrades on the backpack. Default 2.").getInt();
        goldBackpack[1] = config.get("3) Gold Backpack", "2) Number of Slots Per Row", 9, "The size of the backpack. Either 9 or 11. Default 9.").getInt();
        goldBackpack[2] = config.get("3) Gold Backpack", "3) Number of Rows", 6, "The size of the backpack. Between 1 and 7. Default 6.").getInt();
        goldBackpackRecipe = config.get("5) Recipes", "3) Gold Backpack Recipe", goldRecipe, "The recipe to upgrade the backpack to a Gold Backpack. Default is surrounded by ingotGold.").getStringList();

        diamondBackpack[0] = config.get("4) Diamond Backpack", "1) Upgrade Slots", 3, "The number of upgrades on the backpack. Default 3.").getInt();
        diamondBackpack[1] = config.get("4) Diamond Backpack", "2) Number of Slots Per Row", 11, "The size of the backpack. Either 9 or 11. Default 11.").getInt();
        diamondBackpack[2] = config.get("4) Diamond Backpack", "3) Number of Rows", 7, "The size of the backpack. Between 1 and 7. Default 7.").getInt();
        diamondBackpackRecipe = config.get("5) Recipes", "4) Diamond Backpack Recipe", diamondRecipe, "The recipe to upgrade the backpack to a Diamond Backpack. Default is surrounded by gemDiamond.").getStringList();

        buttonUpgradeRecipe = config.get("5) Recipes", "5) Upgrade Recipe", buttonRecipe, "The recipe for the button upgrade.").getStringList();
        nestingUpgradeRecipe = config.get("5) Recipes", "6) Nesting Upgrade Recipe", nestingRecipe, "The recipe for the nesting upgrade.").getStringList();
        renamingUpgradeRecipe = config.get("5) Recipes", "7) Renaming Upgrade Recipe", renamingRecipe, "The recipe for the renaming upgrade. Only used if the upgrade is required (another option in config)").getStringList();
        damageBarUpgradeRecipe = config.get("5) Recipes", "8) Damage Bar Upgrade Recipe", damageBarRecipe, "The recipe for the damage bar upgrade.").getStringList();
        filterUpgradeRecipe = config.get("5) Recipes", "9) Filter Upgrade Recipe", filterRecipe, "The recipe for the filter upgrade.").getStringList();
        hopperUpgradeRecipe = config.get("5) Recipes", "10) Hopper Upgrade Recipe", hopperRecipe, "The recipe for the hopper upgrade.").getStringList();
        condenserUpgradeRecipe = config.get("5) Recipes", "9) Condenser Upgrade Recipe", condenserRecipe, "The recipe for the condenser upgrade.").getStringList();

        nestRecipe = config.get("5) Recipes", "10) Nest Recipe", nestRecipeDefault, "The recipe for the crafting item - nest.").getStringList();
        upgradeCoreRecipe = config.get("5) Recipes", "11) Upgrade Core Recipe", upgradeCoreRecipeDefault, "The recipe for the crafting item - upgrade core.").getStringList();

        renamingUpgradeRequired = config.get("6) Miscellaneous", "1) Renaming Upgrade Required", false, "If the renaming upgrade is required to rename the backpack. Default is false (so you can rename backpacks natively).").getBoolean();

        config.save(); //Don't forget to save
    }
}