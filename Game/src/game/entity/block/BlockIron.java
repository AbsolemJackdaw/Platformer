package game.entity.block;

import game.World;
import game.content.Images;
import game.item.ItemStack;
import game.item.Items;
import game.item.tool.ItemTool;

import java.awt.image.BufferedImage;
import java.util.Random;

import base.tilemap.TileMap;

public class BlockIron extends BlockBreakable {

	public BlockIron(TileMap tm, World world) {
		super(tm, world, Blocks.IRON, ItemTool.PICKAXE);
		setHealth(50);
	}

	@Override
	public BufferedImage getEntityTexture() {
		return Images.loadImage("/blocks/rock_iron.png");
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
	public ItemStack getDrop() {
		return new ItemStack(Items.iron, new Random().nextInt(2)+1);
	}
}
