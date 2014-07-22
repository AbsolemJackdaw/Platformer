package game.entity.block;

import game.World;
import game.content.Images;
import game.entity.MapObject;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.Items;

import java.awt.image.BufferedImage;
import java.util.Random;

import base.tilemap.TileMap;

public class BlockIron extends BlockBreakable {

	public BlockIron(TileMap tm, World world) {
		super(tm, world, Blocks.IRON);
		
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
	public int getHealth() {
		return 10;
	}
	
	@Override
	public void interact(Player p, MapObject mo) {
		super.interact(p, mo);
		System.out.println("Should have a pickaxe to mine !");
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
