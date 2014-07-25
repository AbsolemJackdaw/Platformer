package game.item;

import game.content.Images;
import game.entity.block.Blocks;
import game.item.tool.ItemTool;

import java.util.HashMap;

public class Items {

	private static HashMap<String, Item> registeredItems = new HashMap<String, Item>();

	public static Item getItemFromUIN(String uin){
		if(registeredItems.containsKey(uin))
			return registeredItems.get(uin);
		return null;
	}
	
	public static Item stick = new Item("stick").setTexture(Images.loadImage("/items/stick.png"));
	public static Item iron = new Item("iron").setTexture(Images.loadImage("/items/iron.png"));
	public static Item rock= new Item("stone").setTexture(Images.loadImage("/items/stone.png"));

	public static ItemBlock log = (ItemBlock) new ItemBlock(Blocks.LOG).setTexture(Images.loadImage("/blocks/log.png"));
	public static ItemCraftingTable craftTable = new ItemCraftingTable();
	
	public static ItemTool pickaxe = new ItemTool("pickaxe").setAttackDamage(2).setEffectiveness(ItemTool.PICKAXE).setEffectiveDamage(5);
	
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
