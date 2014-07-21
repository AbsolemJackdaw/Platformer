package game.entity.block;

import game.Loading;
import game.World;
import game.content.Images;
import game.item.ItemStack;
import game.item.Items;

import java.awt.image.BufferedImage;
import java.util.Random;

import base.tilemap.TileMap;

public class BlockLog extends BlockBreakable{

	public BlockLog(TileMap tm, World world) {
		super(tm, world, Loading.LOG);
	}
	
	@Override
	public int getHealth() {
		return 3;
	}

	@Override
	public BufferedImage getEntityTexture() {
		return Images.instance.log;
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
