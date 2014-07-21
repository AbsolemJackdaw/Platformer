package game.item;

import game.Loading;
import game.World;
import game.entity.MapObject;
import game.entity.living.player.Player;
import base.tilemap.TileMap;

public class ItemBlock extends Item{

	public ItemBlock(String s, String texturePath) {
		super(s);

	}

	public ItemBlock(String s) {
		super(s);
	}
	
	public void placeBlock(TileMap map, World world, Player p){
		MapObject mo = Loading.loadMapObjectFromString(getUIN(), map, world);
		mo.setPosition(p.getScreenXpos(), p.getScreenYpos());
		world.listWithMapObjects.add(mo);
	}

}
