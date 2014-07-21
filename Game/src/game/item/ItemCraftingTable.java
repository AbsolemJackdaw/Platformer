package game.item;

import game.content.Images;

import java.awt.image.BufferedImage;

public class ItemCraftingTable extends ItemBlock{

	public ItemCraftingTable(String s) {
		super(s);
	}
	
	@Override
	public BufferedImage getTexture() {
		return Images.loadImage("/blocks/workbench.png");
	}

}
