package com.kawaiiwolf.kawaiicrops.nei;


import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiTreeBlocks;
import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.lib.Constants;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIKawaiicropsConfig implements IConfigureNEI
{
	@Override
	public void loadConfig()
	{
		for (BlockKawaiiCrop block : ModBlocks.AllCrops)
			API.hideItem(new ItemStack((Block)block));
		
		for (BlockKawaiiTreeBlocks block : ModBlocks.AllTrees)
			API.hideItem(new ItemStack((Block)block));
		
		API.registerRecipeHandler(new NEIRecipeHandlerKawaiiCuttingBoard());
		API.registerUsageHandler(new NEIRecipeHandlerKawaiiCuttingBoard());
	}

	@Override
	public String getName() 
	{
		return Constants.MOD_NAME + " NEI Plugin";
	}

	@Override
	public String getVersion() 
	{
		return Constants.VERSION;
	}

}
