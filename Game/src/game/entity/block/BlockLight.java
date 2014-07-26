package game.entity.block;

import java.awt.image.BufferedImage;

import game.World;
import game.item.Items;
import base.tilemap.TileMap;

public class BlockLight extends Block{

	public int lightRadius;
	
	public BlockLight(TileMap tm, World world, String uin) {
		super(tm, world, uin);
		
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

}
