package game.entity.block;

import game.World;
import game.content.Images;
import game.entity.MapObject;
import game.entity.living.player.Player;
import game.gui.GuiCrafting;

import java.awt.image.BufferedImage;

import base.tilemap.TileMap;

public class BlockCraftingTable extends Block {

	public BlockCraftingTable(TileMap tm, World world) {
		super(tm, world, "craftingtable");
		entitySizeY = 24;
		height = 32;
	}
	
	@Override
	public void interact(Player p, MapObject mo) {
		getWorld().displayGui(new GuiCrafting(getWorld(), p));
	}
	
	@Override
	public BufferedImage getEntityTexture() {
		return Images.loadImage("/blocks/workbench.png");
	}
	
	@Override
	public boolean hasAnimation() {
		return false;
	}
	
	@Override
	public boolean isStackable() {
		return false;
	}

}
