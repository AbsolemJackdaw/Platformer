package game.item;

import game.content.Images;

import java.awt.image.BufferedImage;

public class ItemStick extends Item{

	public ItemStick() {
		super("stick");
	}

	@Override
	public BufferedImage getTexture() {
		return Images.loadImage("/items/stick.png");
	}
	
}
