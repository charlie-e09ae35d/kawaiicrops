package com.kawaiiwolf.kawaiicrops.lib;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCake;
import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiFood;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiIngredient;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class ConfigurationLoader {

	// Parent folder for configuration files.
	private String configFolder = null;
	
	// Dump a list of Block/Item names to a config file.
	private static boolean DumpIDs = false;
	
	public ConfigurationLoader(String configFolder) {
		this.configFolder = configFolder;
	}
	
	public static final String HEADER_COMMENT = ""+
			"Note: If your block/item doesn't show up in this file, make sure you set it in general.cfg, save\n" +
			"and open minecraft at least once. You don't need to load into a world, just get to the splash \n" +
			"screen and the necesary configuration will be automatically generated for you.";

	
	public static final String GENERAL_CROP_COMMENT = "" +
			"Here you'll list the names of all the plants that you want the mod to generate. Make sure each crop\n"+
			"name is lower case and has no spaces or punctuation. You can separate these with commas or spaces.\n"+
			"Once this list is completed, a new entry will be created in the blocks.cfg file to hold all the\n"+
			"details for this plant. Plants must appear in this list to be included in the mod.\n"+
			"\n"+
			"Bad Name: Snow Peas\n"+
			"Good Name: snowpeas\n"+
			"\n"+
			"S:Crops=snowpea tomato broccoli";
	
	public static final String GENERAL_CAKE_COMMENT = "" + 
			"List the names of all cakes you the mod to generate. Make sure each cake name is lower case and has no\n" + 
			"spaces or punctuation. You can separate these with commas or spaces.\n" +
			"\n"+
			"Bad Name: Strawberry Shortcake\n"+
			"Good Name: strawberryshort\n"+
			"\n"+
			"S:Cakes=strawberryshort chocolate carrot";
	
	
	public static final String GENERAL_FOOD_COMMENT = "" + 
			"List the names of all foods you the mod to generate. Make sure each name is lower case and has no\n" + 
			"spaces or punctuation. You can separate these with commas or spaces.\n" +
			"\n"+
			"Bad Name: Raspberry Tea\n"+
			"Good Name: raspberrytea\n"+
			"\n"+
			"S:Cakes=raspberrytea banananutmuffin";	
	
	
	public static final String GENERAL_INGREDIENTS_COMMENT = "" + 
			"List the names of all non-food items you the mod to generate. Typically these will be used as byproducts\n" +
			"or half-steps in recipies, such as an unbaked cake or ground pepper. Make sure each name is lower case\n" +
			"and has no spaces or punctuation. You can separate these with commas or spaces.\n" +
			"\n"+
			"Bad Name: Unbaked Chocoalte Cake\n"+
			"Good Name: unbakedchocolatecake\n"+
			"\n"+
			"S:Cakes=unbakedchocolatecake groundpepper";
	
	
	public static final String REFERENCE_DROPTABLES_COMMENT = "" +
			"A drop table is defined with the following syntax, in BNF:\n"+
			"\n"+
			"<drop-table> ::= <items> | <items> \"|\" <drop-table>\n"+
			"     <items> ::= <item> | <item> \",\" <items>\n"+
			"      <item> ::= <item-name> | <item-name> \" \" <num-drops> | <item-name> \" \" <num-drops> \" \" <weight>\n"+
			" <item-name> ::= \"seed\" | \"crop\" | \"nothing\" | <minecraft-item-name>\n"+
			" <num-drops> ::= <integer-between-1-and-64>\n"+
			"    <weight> ::= <integer-between-1-and-64>\n"+
			"\n"+
			"\n"+
			"The keywords 'seed', 'crop', and 'nothing' are shorthand so you don't have to type out the fully\n"+
			"qualified names for the products of a crop (or empty blocks.) Number of drops and weight default to \n"+
			"one if not supplied. Here are some examples of how to use this:\n"+
			"\n"+
			"\n"+
			"Example 1:\n"+
			"   S:DropTable=seed\n"+
			"\n"+
			"Drops a single seed 100% of the time. This is great for breaking unripe plants.\n"+
			"\n"+
			"\n"+
			"Example 2:\n"+
			"   S:DropTable=seed 1, seed 2, seed 3 | crop 1 1, crop 2 2, crop 3 1 | nothing 1 99, minecraft:diamond 1 1\n"+
			"\n"+
			"This drops between 1 and 3 seeds, all with equal chance. It will also drop one crop 1 in 4 times, two\n"+
			"crops 2 in 4 times and three crops 1 in 4 times. Lastly, it has a 1 in 100 chance of dropping a diamond.\n"+
			"\n"+
			"\n"+
			"Example 3:\n"+
			"   S:DropTable=seed 2 | minecraft:carrot, minecraft:potato, minecraft:apple 1 2\n"+
			"\n"+
			"This drops two seeds and either a carrot, potato or apple with a 25%, 25% or 50% chance, respectively.\n"+
			"\n"+
			"\n"+
			"Example 4:\n"+
			"   S:DropTable=nothing\n"+
			"\n"+
			"No drops.\n";

	
	public static final String REFERENCE_POTION_COMMENT = "" +
			"A Potion Effect is deifned with the following syntax:\n"+
			"\n"+
			"  <Potion-Effects> ::= <Potion-Effect> | <Potion-Effect> \"|\" <Potion-Effects>\n"+
			"   <Potion-Effect> ::= <potion-id> \" \" <duration-seconds> \" \" <Amplifier> \" \" <Chance>\n"+
			"       <potion-id> ::= <number-between-1-and-23>\n"+
			"<duration-seconds> ::= <positive-number>\n"+
			"       <Amplifier> ::= <positive-number>\n"+
			"          <Chance> ::= <decimal-between-0.0-and-1.0>\n"+
			"\n"+
			"\n"+
			"Ex: \n"+
			"  \"1 4 1 1.0\" This applies Speed I for 4 seconds 100% of the time\n"+
			"  \"9 10 1 .05\" This applies Nausea for 10 seconds, 5% of the time\n"+
			"  \"1 4 1 1.0 | 9 10 1 .05\" This always applies speed I and 5% of the time, Nausea\n"+
			"\n"+
			"\n"+
			"Potion IDs:\n"+
			"\n"+
			" 1 Speed \n"+
			" 2 Slowness \n"+
			" 3 Haste \n"+
			" 4 Mining Fatigue \n"+
			" 5 Strength \n"+
			" 6 Instant Health \n"+
			" 7 Instant Damage \n"+
			" 8 Jump Boost \n"+
			" 9 Nausea \n"+
			"10 Regeneration \n"+
			"11 Resistance \n"+
			"12 Fire Resistance \n"+
			"13 Water Breathing \n"+
			"14 Invisibility \n"+
			"15 Blindness \n"+
			"16 Night vision \n"+
			"17 Hunger \n"+
			"18 Weakness \n"+
			"19 Poison \n"+
			"20 Wither \n"+
			"21 Health Boost \n"+
			"22 Absorption \n"+
			"23 Saturation";

	public static final String REFERENCE_ORE_COMMENT = "" +
			"Use this field to add items to ore dictionary references. These can be used as\n"+
			"shortcuts when making recipies. Ore recipies should be separated by a space and may\n" +
			"contain upper and lowercase letters and numbers.\n" +
			"\n"+
			"For example, to the snowpea crop we could set the \"Ore Dictionary Entries\" to:\n"+
			"  \"vegetables stirFryVegetables saladVegetables\"" +
			"and use any of those three names in recipies.\n"+
			"\n"+
			"To see a list of all Ore Dictionary names, turn on \"Dump All IDs\" and see dump.cfg";
	
	public static final String REFERENCE_RECIPES = "" +
			"These values control the number of recipies to be parsed. If it's not enough\n" +
			"simply increase these numbers and load up the game to automatically create\n" +
			"new slots below.";
	
	public static final String REFERENCE_RECIPES_2 = "" +
			"Format for 2x2 Shaped Crafting Recipies:\n"+
			"\n"+
			"+---+---+\n"+
			"| 1 | 2 |\n"+
			"+---+---+\n"+
			"| 3 | 4 |\n"+
			"+---+---+\n"+
			"\n"+
			"\n"+
			"\"<item/block name> <number crafted> <1> <2> <3> <4>\"\n"+
			"\n"+
			"Where 1,2,3 or 4 are the names of blocks, items or ore dictonary names.\n"+
			"You can use \"nothing\" if you want that spot to be blank. For a list of\n"+
			"all valid IDs, turn on \"Dump All IDs\" in general.cfg\n"+
			"\n"+
			"Example:\n"+
			"\n"+
			"\"minecraft:lever 1 stickWood nothing cobblestone nothing\"\n"+
			"Makes a vanilla lever using stick and cobble ore dictionary entries.\n"+
			"\n"+
			"\"minecraft:arrow 8 minecraft:feather minecraft:feather minecraft:feather minecraft:feather\"\n"+
			"Makes 8 arrows out of 4 feathers";

	public static final String REFERENCE_RECIPES_3 = "" +
			"Format for 2x2 Shaped Crafting Recipies:\n"+
			"\n"+
			"+---+---+---+\n"+
			"| 1 | 2 | 3 |\n"+
			"+---+---+---+\n"+
			"| 4 | 5 | 6 |\n"+
			"+---+---+---+\n"+
			"| 7 | 8 | 9 |\n"+
			"+---+---+---+\n"+
			"\n"+
			"\n"+
			"\"<item/block name> <number crafted> <1> <2> <3> <4> <5> <6> <7> <8> <9>\"\n"+
			"\n"+
			"Where 1, 2, ..., 9 are the names of blocks, items or ore dictonary names.\n"+
			"You can use \"nothing\" if you want that spot to be blank. For a list of\n"+
			"all valid IDs, turn on \"Dump All IDs\" in general.cfg\n"+
			"\n"+
			"Example:\n"+
			"\n"+
			"\"minecraft:furnace 1 cobblestone cobblestone cobblestone cobblestone \n"+
			"nothing cobblestone cobblestone cobblestone cobblestone \"\n"+
			"Makes a vanilla furnace cobblestone ore dictionary entries.";

	public static final String REFERENCE_RECIPES_U = "" +
			"Format for Shapeless Crafting Recipies:\n"+
			"\n"+
			"\"<item/block name> <number crafted> <1> <2> <3> ...\"\n"+
			"\n"+
			"Where 1 is the names of blocks or item you're throwing in the furnace.\n"+
			"For a list of all valid IDs, turn on \"Dump All IDs\" in general.cfg\n"+
			"\n"+
			"Example:\n"+
			"\n"+
			"\"minecraft:mushroom_stew 4 minecraft:bowl minecraft:bowl minecraft:bowl \n" +
			"minecraft:bowl minecraft:brown_mushroom minecraft:red_mushroom minecraft:carrots\"\n"+
			"Makes 4 bowls of soup with 4 bowls, one of each mushroom and a carrot\n"+
			"\n"+
			"\"minecraft:record_13 1 dustRedstone record\"\n"+
			"Create record 13 by adding redstone to any record.";
	
	public static final String REFERENCE_RECIPES_S = "" +
			"Format for Smelting Crafting Recipies:\n"+
			"\n"+
			"\"<item/block name> <number crafted> <1>\"\n"+
			"\n"+
			"Where 1 is the names of blocks or item you're throwing in the furnace.\n"+
			"For a list of all valid IDs, turn on \"Dump All IDs\" in general.cfg\n"+
			"\n"+
			"Example:\n"+
			"\n"+
			"\"minecraft:stick 1 minecraft:sapling \"\n"+
			"Smelting a sapling ";
	
	public void loadConfiguration_PreInit() 
	{
		Configuration cfg_general = new Configuration(new File(configFolder + Constants.CONFIG_GENERAL));
		cfg_general.load();
		
		cfg_general.setCategoryComment(Configuration.CATEGORY_GENERAL, "Global Settings for KawaiiCraft");
		cfg_general.setCategoryComment("Reference: Drop Table Help", REFERENCE_DROPTABLES_COMMENT);
		cfg_general.setCategoryComment("Reference: Potions Help", REFERENCE_POTION_COMMENT);
		cfg_general.setCategoryComment("Reference: Ore Dictionary Help", REFERENCE_ORE_COMMENT);
		DumpIDs = cfg_general.getBoolean("Dump All IDs", Configuration.CATEGORY_GENERAL, DumpIDs, "Creates a list of Block and Item Names in the configuration directory ?");
		
		// Crops
		
		cfg_general.setCategoryComment("KawaiiCrop Crops", GENERAL_CROP_COMMENT);
		String cropsRaw = cfg_general.getString("Crops", "KawaiiCrop Crops", "snowpea","Crop List");
		String[] cropsParsed = cropsRaw.toLowerCase().replaceAll("[^a-z, ]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		
		if(cropsParsed.length > 0)
		{
			Configuration cfg = new Configuration(new File(configFolder + Constants.CONFIG_CROPS));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT);
			for (String crop : cropsParsed)
				loadCrop(cfg, crop);
			cfg.save();
		}
		
		// Cakes
		
		cfg_general.setCategoryComment("KawaiiCrop Yummy Cakes", GENERAL_CAKE_COMMENT);
		String cakesRaw = cfg_general.getString("Cakes", "KawaiiCrop Yummy Cakes", "strawberryshort", "Cake List");
		String[] cakesParsed = cakesRaw.toLowerCase().replaceAll("[^a-z, ]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		
		if(cakesParsed.length > 0)
		{
			Configuration cfg = new Configuration(new File(configFolder + Constants.CONFIG_CAKES));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT);
			for (String cake : cakesParsed)
				loadCake(cfg, cake);
			cfg.save();
		}
		
		// Foods
		
		cfg_general.setCategoryComment("KawaiiCrop Foods", GENERAL_FOOD_COMMENT);
		String foodsRaw = cfg_general.getString("Foods", "KawaiiCrop Foods", "", "Food List");
		String[] foodsParsed = foodsRaw.toLowerCase().replaceAll("[^a-z, ]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		
		if(foodsParsed.length > 0)
		{
			Configuration cfg = new Configuration(new File(configFolder + Constants.CONFIG_FOODS));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT);
			for (String food : foodsParsed)
				loadFood(cfg, food);
			cfg.save();
		}
		
		// Ingredients
		
		cfg_general.setCategoryComment("KawaiiCrop Ingredients", GENERAL_INGREDIENTS_COMMENT);
		String ingredientsRaw = cfg_general.getString("Ingredients", "KawaiiCrop Ingredients", "", "Ingredients List");
		String[] ingredientsParsed = ingredientsRaw.toLowerCase().replaceAll("[^a-z, ]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		
		if(ingredientsParsed.length > 0)
		{
			Configuration cfg = new Configuration(new File(configFolder + Constants.CONFIG_INGREDIENTS));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT);
			for (String ingredient : ingredientsParsed)
				loadIngredient(cfg, ingredient);
			cfg.save();
		}
		
		
		cfg_general.save();
	}
	
	public void loadConfiguration_PostInit(FMLPostInitializationEvent event) {
		
		if (DumpIDs) dumpIDs();
		
		Configuration cfg = new Configuration(new File(configFolder + Constants.CONFIG_RECIPES));
		cfg.load();
		
		String category = "0 Main Settings";
		cfg.setCategoryComment(category, REFERENCE_RECIPES);
		
		int recipes2 = cfg.getInt("2 by 2", category, 10, 0, 10000, "Number of 2x2 Shaped crafting recipies ?");
		int recipes3 = cfg.getInt("3 by 3", category, 10, 0, 10000, "Number of 3x3 Shaped crafting recipies ?");
		int recipesU = cfg.getInt("Unshaped", category, 10, 0, 10000, "Number of Unshaped crafting recipies ?");
		int recipesS = cfg.getInt("Smelting", category, 10, 0, 10000, "Number of Smelting crafting recipies ?");
		
		category = "2 by 2 Shaped Crafting Recipies";
		cfg.setCategoryComment(category, REFERENCE_RECIPES_2);
		for (int i = 0; i < recipes2; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.register2x2recipie(recipe);
		}

		category = "3 by 3 Shaped Crafting Recipies";
		cfg.setCategoryComment(category, REFERENCE_RECIPES_3);
		for (int i = 0; i < recipes3; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.register3x3recipie(recipe);
		}
		
		category = "Shapeless Crafting Recipies";
		cfg.setCategoryComment(category, REFERENCE_RECIPES_U);
		for (int i = 0; i < recipesU; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.registerShapelessRecipie(recipe);
		}
		
		category = "Smelting Crafting Recipies";
		cfg.setCategoryComment(category, REFERENCE_RECIPES_S);
		for (int i = 0; i < recipesS; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.registerSmeltingRecipie(recipe);
		}

		cfg.save();
	}
	
	private void dumpIDs() {
		
		File f = new File(configFolder + Constants.CONFIG_DUMP);
		
		// Try to clear it out if it exists. Fresh File
		try { if (f.exists()) f.delete(); } catch (Exception e) { }
		
		String blockList = "", itemList = "", oreList = "";
		
		Iterator<Block> blocks = NamespaceHelper.getBlockIterator();
		while (blocks.hasNext())
			blockList += NamespaceHelper.getBlockName(blocks.next()) + "\n";
		
		Iterator<Item> items = NamespaceHelper.getItemIterator();
		while (items.hasNext())
			itemList += NamespaceHelper.getItemName(items.next()) + "\n";
		
		for (String name : OreDictionary.getOreNames())
			oreList += name + "\n";
		
		Configuration config = new Configuration(f);
		config.load();
		config.setCategoryComment("Blocks", blockList);
		config.setCategoryComment("Items", itemList);
		config.setCategoryComment("OreDictionary", oreList);
		config.save();
	}

	private BlockKawaiiCrop loadCrop(Configuration config, String name) {

		if (name == null || name.length() == 0) return null;

		String category = "KawaiiCrops: " + name;
		BlockKawaiiCrop b = new BlockKawaiiCrop(name);
		
		// Get crop variables from config file
		b.Enabled = config.getBoolean("0. Enabled", category, b.Enabled, "Is this a block in minecraft ? Defaults to false to allow you to configure before putting it in game.");
		
		String r = config.getString("1.General  Render Type", category, "Hash", "How will the crop render ? Valid values are [Hash, Cross, Block]").toLowerCase();
		if (r.equals("cross"))
			b.RenderType = BlockKawaiiCrop.EnumRenderType.CROSS;
		else if (r.equals("hash"))
			b.RenderType = BlockKawaiiCrop.EnumRenderType.HASH;
		else
			b.RenderType = BlockKawaiiCrop.EnumRenderType.BLOCK;
		
		System.out.println("Render type set to: " + b.RenderType + ". Input: [" + r + "]");
		System.out.println("Render type set to: " + b.RenderType + ". Input: [" + r + "]");
		System.out.println("Render type set to: " + b.RenderType + ". Input: [" + r + "]");
		System.out.println("Render type set to: " + b.RenderType + ". Input: [" + r + "]");
		System.out.println("Render type set to: " + b.RenderType + ". Input: [" + r + "]");
		
		b.CropStages = config.getInt("1.General  Crop Stages", category, b.CropStages, 2, 8, "Number of crop states ?  Valid values are between 2 and 8. (Ex: Carrots = 4, Wheat = 8)");
		b.MaxHeight = config.getInt("1.General  Max Height", category, b.MaxHeight, 1, 32, "How many blocks tall will this crop grow ?");
		b.CropGrowsOn = new HashSet<Block>(NamespaceHelper.getBlocksByName(config.getString("1.General  Soil Block", category, "minecraft:farmland", "What blocks does this grow on ? Seperate blocks with a space or comma. For a list of blocks, see [DumpNames] setting in General.cfg. (Note, 'minecraft:water' is an option.)"))); 
		
		b.MaxHeightRequiredToRipen = config.getBoolean("2.Harvest  Max Height Required to Ripen", category, b.MaxHeightRequiredToRipen, "Does the plant need to be at max height before lower blocks are ready to harvest ?");
		b.MultiHarvest = config.getBoolean("2. Multi Harvest", category, b.MultiHarvest, "Upon harvesting this crop, does it grow back to an earlier, unripe state ?");
		b.UnripeStage = config.getInt("2.Harvest  Unripe Stage", category, b.UnripeStage, 0, 7, "What stage is a crop finished growing before starting to ripen ?  For MultiHarvest, the stage the crop is set to after harvesting. For crops more than 1 block tall, the stage at which blocks will grow above.");
		b.UnripeHardness = config.getFloat("2.Harvest  Unripe Hardness", category, b.UnripeHardness, 0.0f, 1.0f, "Hardness of unripe crops (0 breaks instantly. Set higher to prevent accidental harvests) ?");
		
		b.GrowthMutliplier = config.getFloat("3.Growth  Growth Multiplier", category, b.GrowthMutliplier, 0.001f, 1000.0f, "How fast does your plant grow ? (1.0 is normal vanilla speeds, 3.0 is growth rate on wet farmland when growing on other block types.");
		b.BoneMealMin = config.getInt("3.Growth  Bonemeal Min", category, b.BoneMealMin, 0, 8, "Minimum stages of growth when using bonemeal.");
		b.BoneMealMax = config.getInt("3.Growth  Bonemeal Max", category, b.BoneMealMax, 0, 8, "Maximum stages of growth when using bonemeal.");
		b.LightLevelMin = config.getInt("3.Growth  Light Minimum", category, b.LightLevelMin, 0, 15, "Minimum level of light required for plant to grow naturally.");
		b.LightLevelMax = config.getInt("3.Growth  Light Maximum", category, b.LightLevelMax, 0, 15, "Maximum level of light required for plant to grow naturally.");
				
		if (b.BoneMealMin > b.BoneMealMax) b.BoneMealMin = b.BoneMealMax;
		if (b.BoneMealMax < b.BoneMealMin) b.BoneMealMax = b.BoneMealMin;
		
		b.DropTableRipeString = config.getString("4.Drops  Ripe Drop Table", category, b.DropTableRipeString, "What is the drop table for Ripe crops ? Please see General.cfg to see how to use these.");
		b.DropTableUnripeString = config.getString("4.Drops  Unripe Drop Table", category, b.DropTableUnripeString, "What is the drop table for Unripe crops ? Please see General.cfg to see how to use these.");
		
		String comment = "Resource Pack settings for " + name + "\n\nLangage Name: tile.kawaiicrops." + name + ".seed.name\n\n";
		for (int i = 0; i < (b.MaxHeightRequiredToRipen ? b.MaxHeight : 1); i++)
			for (int j = 0; j < b.CropStages; j++)
				comment += "Texture Name: textures/blocks/" + name + "_stage_" + (b.MaxHeightRequiredToRipen && b.MaxHeight > 1 ? j + "_" : "") + j + ".png\n";
		comment += "\nWarning: Texture names listed above will change as \"1.General  Crop Stages\", \"1.General  Max Height\" " +
				"\n and \"2.Harvest  Max Height Required to Ripen\" settings are changed."; 
		
		config.setCategoryComment(category, comment);
		
		String category_seeds = category + " Seeds";
		
		b.SeedsEnabled = config.getBoolean("0.  Enabled", category_seeds, b.SeedsEnabled, "Does this crop have seeds ?");
		b.SeedsEdible = config.getBoolean("1.General  Edible", category_seeds, b.SeedsEdible, "Are seeds also a food ?");
		b.SeedsHunger = config.getInt("1.General  Hunger", category_seeds, b.SeedsHunger, 0, 20, "If SeedsEdible, how many half shanks of food does this restore ?");
		b.SeedsSaturation = config.getFloat("1.General  Saturation", category_seeds, b.SeedsSaturation, 0, 20.0f, "If SeedsEdible, how saturating is this food ?");
		b.SeedsMysterySeedWeight = config.getInt("2.Other  Mystery Seed Weight", category_seeds, b.SeedsMysterySeedWeight, 0, 1000, "If mystery seeds enabled, what weight should this have on mystery seed results (0 = None)");
		b.SeedsToolTip = config.getString("2.Other  Tool Tip", category_seeds, b.SeedsToolTip, "Tooltip for the seed in game.");
		b.SeedsPotionEffects = new PotionEffectHelper(config.getString("2.Other  Potion Effect", category_seeds, "", "What potion effect do you want triggered on eating this seed ?  Please see General.cfg to see how to use these."));
		b.SeedOreDict = config.getString("2.Other  Ore Dictionary Entries", category_seeds, b.SeedOreDict, "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");

		config.setCategoryComment(category_seeds, 
				"Resource Pack settings for " + name + " seed\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".seed.name\n" +
				"Texture Name: textures/items/" + name + ".seed.png");
		
		String category_crops = category + " Crops";
		
		b.CropEnabled = config.getBoolean("0.  CropEnabled", category_crops, b.CropEnabled, "Does this plant drop crops other than seeds ?");
		b.CropEdible = config.getBoolean("1.General  Edible", category_crops, b.CropEdible, "Are Crop also a food ?");
		b.CropHunger = config.getInt("1.General  Hunger", category_crops, b.CropHunger, 0, 20, "If CropEdible, how many half shanks of food does this restore ?");
		b.CropSaturation = config.getFloat("1.General  Saturation", category_crops, b.CropSaturation, 0, 20.0f, "If CropEdible, how is the saturating is this food ?");
		b.CropToolTip = config.getString("2.Other  Tool Tip", category_crops, b.CropToolTip, "Tooltip for the crop in game.");
		b.CropPotionEffects = new PotionEffectHelper(config.getString("2.Other  Potion Effect", category_crops, "", "What potion effect do you want triggered on eating this crop ?  Please see General.cfg to see how to use these."));
		b.CropOreDict = config.getString("2.Other  Ore Dictionary Entries", category_crops, b.CropOreDict, "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");

		config.setCategoryComment(category_crops, 
				"Resource Pack settings for " + name + " crop\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".crop.name\n" +
				"Texture Name: textures/items/" + name + ".crop.png");
		
		b.register();
		
		return b; 
	}
	
	private BlockKawaiiCake loadCake(Configuration config, String name){
		
		if (name == null || name.length() == 0) return null;
		
		BlockKawaiiCake c = new BlockKawaiiCake(name);
		String category = "Kawaiicrops: " + name + " cake";
		
		c.Enabled = config.getBoolean("0.  Enabled", category, c.Enabled, "Is this a block in minecraft ? Defaults to false to allow you to configure before putting it in game.");
		c.Hunger = config.getInt("1.General  Hunger Restored", category, c.Hunger, 0, 20, "How many half shanks of food does eating a slice of cake restore ?");
		c.Saturation = config.getFloat("1.General  Saturation", category, c.Saturation, 0.0F, 20.0f, "How saturating is a slice of cake ?");
		c.ToolTipText = config.getString("2.Other  Tool Tip Text", category, c.ToolTipText, "Tooltip for the cake in game.");
		c.Potion = new PotionEffectHelper(config.getString("2.Other  Potion Effect", category, "", "What potion effect do you want triggered on eating this cake ?  Please see General.cfg to see how to use these."));
		c.OreDict = config.getString("2.Other  Ore Dictionary Entries", category, "", "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");
		
		config.setCategoryComment(category, 
				"Resource Pack settings for " + name + " Cake\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".cake.name\n" +
				"Langage Name: tile.kawaiicrops." + name + ".cake.name\n\n" +
				"Texture Name: textures/items/" + name + ".cake.png\n\n" +
				"Texture Name: textures/blocks/" + name + ".cake_top.png\n" +
				"Texture Name: textures/blocks/" + name + ".cake_bottom.png\n" +
				"Texture Name: textures/blocks/" + name + ".cake_side.png\n" +
				"Texture Name: textures/blocks/" + name + ".cake_inner.png");
		
		c.register();
		
		return c;
	}
	
	private ItemKawaiiFood loadFood(Configuration config, String name)
	{
		if (name == null || name.length() == 0) return null;
		
		String category = "Kawaiicrops: " + name;
		
		boolean enabled = config.getBoolean("0.  Enabled", category, false, "Is this an item in minecraft ? Defaults to false to allow you to configure before putting it in the game.");
		int hunger = config.getInt("1.General  Hunger", category, 1, 0, 20, "How many half shanks of food does this restore ?");
		float saturation = config.getFloat("1.General  Saturation", category, 0, 0, 20.0F, "How saturating is this food ?");
		boolean drinkable = config.getBoolean("1.General  Drink", category, false, "Do you drink this instead of eat ?");
		boolean anytime = config.getBoolean("1.General  Eat Anytime", category, false, "Can you eat this food even while full ?");
		String toolTipText = config.getString("2.Other  Tool Tip Text", category, "", "Tooltip for this food.");
		PotionEffectHelper potion = new PotionEffectHelper(config.getString("2.Other  Potion Effect", category, "", "What potion effect do you want triggered on eating this food ?  Please see General.cfg to see how to use these."));
		String oreDict = config.getString("2.Other  Ore Dictionary Entries", category, "", "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");
		
		config.setCategoryComment(category, 
				"Resource Pack settings for " + name + "\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".name\n" +
				"Texture Name: textures/items/" + name + ".png");
		
		ItemKawaiiFood food = new ItemKawaiiFood(name, toolTipText, hunger, saturation, potion);
		food.OreDict = oreDict;
		if (enabled)
			GameRegistry.registerItem(food, Constants.MOD_ID + "." + name);
		
		return food;
	}
	
	private ItemKawaiiIngredient loadIngredient(Configuration config, String name)
	{
		if (name == null || name.length() == 0) return null;
		
		String category = "Kawaiicrops: " + name;
		
		boolean enabled = config.getBoolean("0.  Enabled", category, false, "Is this an item in minecraft ? Defaults to false to allow you to configure before putting it in the game.");
		String toolTipText = config.getString("2.Other  Tool Tip Text", category, "", "Tooltip for this food.");
		String oreDict = config.getString("2.Other  Ore Dictionary Entries", category, "", "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");
		
		config.setCategoryComment(category, 
				"Resource Pack settings for " + name + "\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".name\n" +
				"Texture Name: textures/items/" + name + ".png");
		
		ItemKawaiiIngredient ingredient = new ItemKawaiiIngredient(name, toolTipText);
		ingredient.OreDict = oreDict;
		
		if (enabled)
			GameRegistry.registerItem(ingredient, Constants.MOD_ID + "." + name);		
		
		return ingredient;
	}
	
}
