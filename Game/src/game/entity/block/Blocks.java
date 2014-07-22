package game.entity.block;

import game.World;
import game.entity.MapObject;
import base.tilemap.TileMap;

public class Blocks {

	public static final String LOG = "log";
	public static final String CRAFTINGTABLE = "craftingtable";
	public static final String IRON = "ironOre";
	public static final String ROCK = "rock";

	public static MapObject loadMapObjectFromString(String uin, TileMap tm, World w){
		switch (uin) {
		case LOG:
			return new BlockLog(tm, w);
		case CRAFTINGTABLE:
			return new BlockCraftingTable(tm, w);
		case IRON:
			return new BlockIron(tm, w);
		case ROCK:
			return new BlockRock(tm, w);
			
		default:
			break;
		}
		return null;
	}
	
}
