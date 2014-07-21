package game.gui;

import game.World;
import game.content.Images;
import game.entity.living.player.Player;
import game.item.ItemStack;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.main.GamePanel;

public class GuiPlayerInventory extends GuiContainer {

	private BufferedImage img;
	
	//TODO add armor only slot switching compatibility
	
	public GuiPlayerInventory(World world, Player p) {
		super(world, p);
		
		img = Images.loadImage("/player/playerGui.png");
		
		blockInventory = p.invArmor.getInventory();
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img.getSubimage(35, 3, 150, 75), GamePanel.WIDTH/2 - 150/2, GamePanel.HEIGHT/2 - 75/2 ,null);
		
		g.drawImage(player.getAnimation().getImage(),  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);
		
		super.draw(g);

		for(int slot = 0; slot < player.getMaxSlots(); slot++){
			ItemStack stack = player.getStackInSlot(slot);
			if(stack != null){
				int i = slot < 5 ? slot : slot - 5;
				int j = slot < 5 ? 0 : 18;
				stack.getItem().draw(g, GamePanel.WIDTH/2 - 150/2 + 49 + (18*i), (GamePanel.HEIGHT/2 - 4)+ j, stack);
			}
		}
		
		for(int slot = 0; slot < blockInventory.getMaxSlots(); slot++){
			ItemStack stack = blockInventory.getStackInSlot(slot);
			if(stack != null){
				stack.getItem().draw(g, GamePanel.WIDTH/2 - 150/2 + 51 + (18*slot)-2, (GamePanel.HEIGHT/2 - 28), stack);
			}
		}
		
	}
	
	@Override
	public boolean pausesGame() {
		return true;
	}
	
	@Override
	public int getFirstSlotLocationX() {
		return GamePanel.WIDTH/2 - 150/2 + 48;
	}
	
	@Override
	public int getFirstSlotLocationY() {
		return isContainerInventory() ? GamePanel.HEIGHT/2 - 75/2+ 8 :GamePanel.HEIGHT/2 - 75/2 + 32;
	}
	
	@Override
	public int getSlotSpacingX() {
		return 18;
	}
	
	@Override
	public int getSlotSpacingY() {
		return 18;
	}
	
	@Override
	public int rowsX() {
		return isContainerInventory() ? 4 : 5;
	}
	
	@Override
	public int rowsY() {
		return isContainerInventory() ? 1 : 2;
	}
}
