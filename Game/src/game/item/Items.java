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
	
<<<<<<< HEAD
	//uin must be the same as the placed block
	public static ItemBlock craftTable = new ItemBlock("craftingtable");
=======
	//uin must be the same as the placed block.
	public static ItemBlock craftTable = new ItemBlock("craftingtable", "/blocks/workbench.png");
>>>>>>> refs/heads/master

	public static void loadItems(){
		registerItem(stick);
		registerItem(craftTable);
	}

	public static void registerItem(Item item){
		registeredItems.put(item.getUIN(), item);
	}
}
