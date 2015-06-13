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
	public static Item iron = new Item("iron").setTexture(Images.loadImage("/items/iron.png")).setCookable();
	public static Item rock = new Item("stone").setTexture(Images.loadImage("/items/stone.png"));
	public static Item ingot = new Item("ingot").setTexture(Images.loadImage("/items/ingot.png"));
	public static Item meat_pig_raw = new Item("meat_pig_raw").setTexture(Images.loadImage("/items/meat_pig.png")).setCookable();
	public static Item meat_pig = new Item("meat_pig_cooked").setTexture(Images.loadImage("/items/meat_pig_cooked.png"));
	public static Item grease = new Item("grease").setTexture(Images.loadImage("/items/grease.png"));
	
	public static ItemBlock log = (ItemBlock) new ItemBlock(Blocks.LOG).setTexture(Images.loadImage("/blocks/log.png")).setFuel().setFuelTimer(800);
	public static ItemBlock craftTable = (ItemBlock) new ItemBlock(Blocks.CRAFTINGTABLE).setTexture(Images.loadImage("/blocks/workbench.png"));
	public static ItemBlock campfire = (ItemBlock) new ItemBlock(Blocks.CAMPFIRE).setTexture(Images.loadImage("/blocks/campfire.png"));
	public static ItemBlock oven = (ItemBlock) new ItemBlock(Blocks.OVEN).setTexture(Images.loadImage("/blocks/oven.png"));
	
	public static ItemTool pickaxe = (ItemTool) new ItemTool("pickaxe").setAttackDamage(2).setEffectiveness(ItemTool.PICKAXE).setEffectiveDamage(5).setTexture(Images.loadImage("/items/pickaxe.png"));
	public static ItemTool sword   = (ItemTool) new ItemTool("sword").setAttackDamage(5).setEffectiveness(ItemTool.SWORD).setEffectiveDamage(2).setTexture(Images.loadImage("/items/sword.png"));
	public static ItemTool axe   = (ItemTool) new ItemTool("axe").setAttackDamage(2).setEffectiveness(ItemTool.AXE).setEffectiveDamage(5).setTexture(Images.loadImage("/items/axe.png"));

	public static ItemLantern lantern = (ItemLantern) new ItemLantern("lantern").setTexture(Images.loadImage("/items/lantern.png"));		

	public static void loadItems(){
		registerItem(stick);
		registerItem(craftTable);
		registerItem(log);
		registerItem(iron);
		registerItem(rock);
		registerItem(pickaxe);
		registerItem(campfire);
		registerItem(sword);
		registerItem(axe);
		registerItem(oven);
		registerItem(ingot);
		registerItem(meat_pig);
		registerItem(meat_pig_raw);
		registerItem(lantern);
		registerItem(grease);

	}

	public static void registerItem(Item item){
		registeredItems.put(item.getUIN(), item);
	}
}

