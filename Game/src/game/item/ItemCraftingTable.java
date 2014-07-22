package game.item;

import game.content.Images;
import game.entity.block.Blocks;

import java.awt.image.BufferedImage;

public class ItemCraftingTable extends ItemBlock{
 
	public ItemCraftingTable() {
		super(Blocks.CRAFTINGTABLE);
	}
	
	@Override
	public BufferedImage getTexture() {
		return Images.loadImage("/blocks/workbench.png");
	}

}
