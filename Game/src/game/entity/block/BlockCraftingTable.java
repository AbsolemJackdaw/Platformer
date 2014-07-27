package game.entity.block;

import game.World;
import game.content.Images;
import game.entity.MapObject;
import game.entity.living.player.Player;
import game.gui.GuiCrafting;
import game.item.ItemStack;
import game.item.Items;
import game.item.tool.ItemTool;

import java.awt.image.BufferedImage;

import base.tilemap.TileMap;

public class BlockCraftingTable extends BlockBreakable {

	public BlockCraftingTable(TileMap tm, World world) {
		super(tm, world, "craftingtable", ItemTool.NOTHING);
		setHealth(5);
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
	
	@Override
	public void interact(Player p, MapObject o) {
		getWorld().displayGui(new GuiCrafting(getWorld(), p));
	}
	
	@Override
	public ItemStack getDrop() {
		return new ItemStack(Items.craftTable, 1);
	}
	
}
