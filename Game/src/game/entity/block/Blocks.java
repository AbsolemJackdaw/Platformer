package game.entity.block;

import game.World;
import game.entity.MapObject;
import base.tilemap.TileMap;

public class Blocks {

	public static final String LOG = "log";
	public static final String CRAFTINGTABLE = "craftingtable";
	public static final String IRON = "ironOre";
	public static final String ROCK = "rock";
	public static final String CAMPFIRE = "campfire";
	public static final String OVEN = "oven";
	
	public static MapObject loadMapObjectFromString(String uin, TileMap tm, World w){
		switch (uin) {
		case LOG:
			return new BlockLog(tm, w).setType(Block.WOOD);
		case CRAFTINGTABLE:
			return new BlockCraftingTable(tm, w).setType(Block.WOOD);
		case IRON:
			return new BlockIron(tm, w).setType(Block.ROCK);
		case ROCK:
			return new BlockRock(tm, w).setType(Block.ROCK);
		case CAMPFIRE:
			return new BlockLight(tm, w, CAMPFIRE).setRadius(200);
		case OVEN:
			return new BlockOven(tm, w).setType(Block.ROCK);
			
		default:
			break;
		}
		return null;
	}
	
}
