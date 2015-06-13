package game.item;

import game.World;
import game.entity.MapObject;
import game.entity.block.Blocks;
import game.entity.living.player.Player;
import base.tilemap.TileMap;

public class ItemBlock extends Item{

	public ItemBlock(String s) {
		super(s);
	}

	public void placeBlock(TileMap map, World world, Player p){
		MapObject mo = Blocks.loadMapObjectFromString(getUIN(), map, world);
		mo.setPosition(p.getScreenXpos(), p.getScreenYpos());
		world.listWithMapObjects.add(mo);
	}

	@Override
	public void useItem(Item item, TileMap map, World world, Player player, int key) {
		ItemBlock ib = (ItemBlock)item;
		ib.placeBlock(map, world, player);
		player.getInventory().getStackInSlot(key).stackSize--;
		if(player.getInventory().getStackInSlot(key).stackSize == 0)
			player.getInventory().setStackInSlot(key, null);
	}
}
