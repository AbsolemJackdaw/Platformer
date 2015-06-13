package game.item;

import game.World;
import game.content.save.DataTag;
import game.entity.living.player.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.tilemap.TileMap;

public class Item {

	private final String UIN;

	private BufferedImage texture;

	private boolean cookable;
	private boolean fuel;
	private int fuelTimer;

	public Item(String uin){
		UIN = uin;
	}

	public Item setTexture(BufferedImage img){
		texture = img;
		return this;
	}

	public BufferedImage getTexture(){
		return texture;
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

	public boolean isCookable(){
		return cookable;
	}

	public Item setCookable(){
		cookable = true;
		return this;
	}

	public boolean isFuel(){
		return fuel;
	}

	public Item setFuelTimer(int fuelTimer) {
		this.fuelTimer = fuelTimer;
		return this;
	}

	public int getFuelTimer() {
		if(isFuel())
			return fuelTimer;
		else
			return -1;
	}

	public Item setFuel(){
		fuel = true;
		return this;
	}
	
	/**called when the player presses the numbers on the keyboard to use an item*/
	public void useItem(Item item, TileMap map, World world, Player player, int key){
		System.out.println("item is used");
	}
	
	/**if the item can handle any logic set to true: it will call the update method*/
	public boolean isUpdateAble(){
		return false;
	}
	
	public void update(){
		
	}
}
