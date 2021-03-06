package com.kawaiiwolf.kawaiicrops.lib;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DropTable {

	private ArrayList<ArrayList<DropTableItem>> list = new ArrayList<ArrayList<DropTableItem>>();
	
	public DropTable(String table, ItemStack seed, ItemStack crop) {
		
		/* Look for pattern:
		 * 		<item-name>
		 * 		<item-name> <quantity>
		 * 	or	<item-name> <quantity> <weight>	 */
		Pattern pattern = Pattern.compile("([0-9A-Za-z:._]+)[ ]+([0-9]+)[ ]+([0-9]+)|([0-9A-Za-z:._]+)[ ]+([0-9]+)|([0-9A-Za-z:._]+)");
		
		// Split string by pipe symbol
		for (String dropSet : table.replaceAll("[^0-9A-Za-z:._ |,]", "").split("[|]")) {
			ArrayList<DropTableItem> set = new ArrayList<DropTableItem>();
			
			// Split further by Comma
			for (String itemStr : dropSet.split(",")) {
				DropTableItem item = new DropTableItem();
				//Matcher match = pattern.matcher(itemStr.toLowerCase());
				Matcher match = pattern.matcher(itemStr);
				
				// match on 'name [drops] [weight]'
				if (match.find()) {
					
					String name = (match.group(4) != null ? match.group(4) : (match.group(1) != null ? match.group(1) : match.group(0))); name = name.toString();
					String drops = (match.group(2) == null ? "" : match.group(2)) + (match.group(5) == null ? "" : match.group(5));
					String weight = (match.group(3) == null ? "" : match.group(3));
					
					if (name.equalsIgnoreCase("seed") || name.equalsIgnoreCase("sapling"))
						item.item = seed.copy();
					else if (name.equalsIgnoreCase("crop") || name.equalsIgnoreCase("fruit"))
						item.item = crop.copy();
					else if (name.equalsIgnoreCase("nothing"))
						item.item = null;
					else
						item.item = NamespaceHelper.getItemByName(name);
		
					if (drops.length() > 0 && item.item != null) {
						int i = Integer.parseInt(drops);
						if (i < 1) i = 1;
						if (i > 64) i = 64;
						item.item.stackSize = i;
					}
					
					if (weight.length() > 0) {
						int i = Integer.parseInt(weight);
						if (i < 1) i = 1;
						if (i > 64) i = 64;
						item.weight = i;
					}
					
					set.add(item);
				}
			}
			if (set.size() > 0) this.list.add(set);
		}
	}
	
	public ArrayList<ItemStack> generateLoot(Random rand) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		
		for (ArrayList<DropTableItem> table : list) {
			int count = 0, i;
			
			for (DropTableItem item : table)
				count += item.weight;
			
			count = (int)(count * rand.nextFloat());
			
			for (i = 0; count >= table.get(i).weight; i++)
				count -= table.get(i).weight;
			
			if (table.get(i).item != null)
				ret.add(table.get(i).item.copy());
		}
		
		return ret;
	}
	
	private ArrayList<ItemStack> display = null;
	public ArrayList<ItemStack> getDisplay()
	{
		if (display == null)
		{
			display = new ArrayList<ItemStack>();
			
			for (ArrayList<DropTableItem> set : list)
				if (set != null)
					for (DropTableItem item : set)
						if (item != null && item.item != null)
							display.add(item.item.copy());
		}
		return display;
	}
	
	public void DEBUG_OUT() 
	{
		System.out.println("Outputting Drop Table");
		for (int i = 0; i < list.size(); i++){
			System.out.println("  Drop Group " + (1 + i));
			for (int j = 0; j < list.get(i).size(); j++)
				System.out.println("    Item [" + (j + 1) + "] " + (list.get(i).get(j).item == null ? "Nothing" : list.get(i).get(j).item.getUnlocalizedName()) + " " + list.get(i).get(j).item.stackSize + " " + list.get(i).get(j).weight);
		}
	}
	
	private class DropTableItem 
	{
		public ItemStack item = null;
		public int weight = 1; 
	}
}
