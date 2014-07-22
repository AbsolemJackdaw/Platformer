package game.item;

import java.util.HashMap;

public class Items {

	private static HashMap<String, Item> registeredItems = new HashMap<String, Item>();

	public static Item getItemFromUIN(String uin){
		if(registeredItems.containsKey(uin))
			return registeredItems.get(uin);
		return null;
	}
	
	public static ItemStick stick = new ItemStick();
	public static ItemIron iron = new ItemIron();
	public static ItemStone rock= new ItemStone();

	public static ItemWood log = new ItemWood();
	public static ItemCraftingTable craftTable = new ItemCraftingTable();
	
	public static void loadItems(){
		registerItem(stick);
		registerItem(craftTable);
		registerItem(log);
		registerItem(iron);
		registerItem(rock);
	}

	public static void registerItem(Item item){
		registeredItems.put(item.getUIN(), item);
	}
}
