package game.item;

import game.content.Images;

import java.awt.image.BufferedImage;

public class ItemIron extends Item {

	public ItemIron() {
		super("iron");
	}
	
	@Override
	public BufferedImage getTexture() {
		return Images.loadImage("/items/iron.png");
	}
}
