package game.item;

import game.content.Images;
import game.entity.block.Blocks;

import java.awt.image.BufferedImage;

public class ItemWood extends ItemBlock{

	public ItemWood() {
		super(Blocks.LOG);
	}
	
	@Override
	public BufferedImage getTexture() {
		return Images.loadImage("/blocks/log.png");
	}

}
