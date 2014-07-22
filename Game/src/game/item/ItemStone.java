package game.item;

import game.content.Images;

import java.awt.image.BufferedImage;

public class ItemStone extends Item{

	public ItemStone() {
		super("stone");
	}
	
	@Override
	public BufferedImage getTexture(){
		return Images.loadImage("/items/stone.png");
	}

}
