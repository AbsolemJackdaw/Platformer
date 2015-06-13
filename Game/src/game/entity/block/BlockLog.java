package game.entity.block;

import game.World;
import game.content.Images;
import game.item.ItemStack;
import game.item.Items;
import game.item.tool.ItemTool;

import java.awt.image.BufferedImage;

import base.tilemap.TileMap;

public class BlockLog extends BlockBreakable{

	BufferedImage img = Images.loadImage("/blocks/log.png");
	
	public BlockLog(TileMap tm, World world) {
		super(tm, world, Blocks.LOG, ItemTool.AXE);
		setHealth(5);
	}
	
	@Override
	public BufferedImage getEntityTexture() {
		return img;
	}

	@Override
	public boolean hasAnimation() {
		return false;
	}
	
	@Override
	public ItemStack getDrop() {
		return new ItemStack(Items.log, 1);
	}

}
