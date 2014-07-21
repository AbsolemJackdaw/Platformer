package game.item;

import game.content.Images;
import game.content.save.DataTag;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Item {

	private final String UIN;

	private final BufferedImage texture;

	public Item(String uin){
		UIN = uin;
		texture = getTexture();
	}

	public BufferedImage getTexture(){
		return Images.instance.defaultAnim[0];
	}

	Font font = new Font("Century Gothic", Font.PLAIN, 10);

	public void draw(Graphics2D g, int x, int y, ItemStack stack){

		g.drawImage(texture, x, y, 16, 16, null);

		g.setFont(font);
		if(stack.stackSize < 10)
		{
			g.setColor(Color.DARK_GRAY);
			g.drawString(stack.stackSize+"", x+11, y+17);
			g.setColor(Color.white);
			g.drawString(stack.stackSize+"", x+10, y+16);
		}
		else
		{
			g.setColor(Color.DARK_GRAY);
			g.drawString(stack.stackSize+"", x+4, y+17);
			g.setColor(Color.white);
			g.drawString(stack.stackSize+"", x+3, y+16);
		}
	}
	public void writeToSave(DataTag tag){
		tag.writeString("UIN", UIN);
	}

	public void readFromSave(DataTag tag){

	}

	public String getUIN(){
		return UIN;
	}

}
