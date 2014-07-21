package game.gui;

import game.content.Images;
import game.item.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Button {

	private static final BufferedImage tileTexture = Images.loadImage("/images/tile.png");

	private int posX;
	private int posY;

	BufferedImage icon;

	public Button(int x, int y) {
		posX = x;
		posY = y;
		icon = Items.stick.getTexture();
	}

	public Button(int x, int y, BufferedImage img) {
		posX = x;
		posY = y;
		icon = img;
	}

	public BufferedImage getIcon(){
		return icon;
	}

	public void draw(Graphics2D g) {

		g.drawImage(tileTexture, posX, posY, 16, 16, null);

		if(icon != null)
			g.drawImage(icon.getSubimage(0, 0, 32, 32), posX, posY, 16, 16, null);

	}
}

