package com.kawaiiwolf.kawaiicrops.waila;

import java.util.ArrayList;
import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.collect.Lists;
import com.kawaiiwolf.kawaiicrops.block.*;
import com.kawaiiwolf.kawaiicrops.lib.ConfigurationLoader;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

public class WailaTileHandler implements IWailaDataProvider 
{
	public static WailaTileHandler instance = new WailaTileHandler();
	
	public static void callbackRegister(IWailaRegistrar register) 
	{
		ArrayList<Class> blocks = Lists.newArrayList( new Class[] { 
			BlockKawaiiCuttingBoard.class,
			BlockKawaiiFryingPan.class,
			BlockKawaiiBigPot.class,
			BlockKawaiiChurn.class,
			BlockKawaiiMill.class,
			BlockKawaiiGrill.class,
			BlockKawaiiBarrel.class,
			BlockKawaiiCake.class,
			BlockKawaiiCrop.class,
			BlockKawaiiTreeBlocks.class
		} );
		
		for (Class block : blocks)
			register.registerBodyProvider(instance, block);
	}
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		if (accessor.getBlock() instanceof IWailaTooltip)
		{
			ItemStack display = ((IWailaTooltip)accessor.getBlock()).getDisplayStack(accessor.getWorld(), accessor.getPosition().blockX, accessor.getPosition().blockY, accessor.getPosition().blockZ, accessor.getMetadata(), accessor.getTileEntity());
			if (display != null)
				return display;
		}
		return accessor.getStack();
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		if (accessor.getBlock() instanceof IWailaTooltip)
			currenttip.add(SpecialChars.WHITE + NamespaceHelper.getBlockLocalizedName(accessor.getBlock()));
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		if (accessor.getBlock() instanceof IWailaTooltip)
		{
			if (accessor.getBlock() instanceof BlockKawaiiCrop)
				currenttip.clear();
			String items = ((IWailaTooltip)accessor.getBlock()).getBody(accessor.getWorld(), accessor.getPosition().blockX, accessor.getPosition().blockY, accessor.getPosition().blockZ, accessor.getMetadata(), accessor.getTileEntity());
			if (items != null)
			{
				if (items.contains("\n"))
					for (String item : items.split("\n"))
						currenttip.add(item);
				else
					currenttip.add(items);
			}
		}
		return currenttip;
	}
	
	 @Override
	 public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	 {
		 if (accessor.getBlock() instanceof IWailaTooltip)
			 currenttip.add(SpecialChars.BLUE + SpecialChars.ITALIC + ConfigurationLoader.WAILAName);
		 return currenttip;
	 }

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}
}
