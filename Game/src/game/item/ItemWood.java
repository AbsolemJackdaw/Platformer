package game.item;

import game.Loading;
import game.content.Images;

import java.awt.image.BufferedImage;

public class ItemWood extends ItemBlock{

	public ItemWood() {
		super(Loading.LOG);
	}
	
	@Override
	public BufferedImage getTexture() {
		return Images.loadImage("/blocks/log.png");
	}

}
