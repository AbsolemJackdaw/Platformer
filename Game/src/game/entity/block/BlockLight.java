package game.entity.block;

import java.awt.image.BufferedImage;

import game.World;
import game.item.ItemStack;
import game.item.Items;
import base.tilemap.TileMap;

public class BlockLight extends BlockBreakable{

	public int lightRadius;
	
	public BlockLight(TileMap tm, World world, String uin) {
		super(tm, world, uin);
		setHealth(2);
		
	}
	
	public Block setRadius(int rad){
		lightRadius = rad;
		return this;
	}
	
	public int getRadius(){
		return lightRadius;
	}

	@Override
	public BufferedImage getEntityTexture() {
		return Items.campfire.getTexture();
	}

	@Override
	public ItemStack getDrop() {
		return new ItemStack(Items.campfire, 1);
	}
}
