package game.gui;

import game.World;
import game.content.Images;
import game.entity.living.player.Player;
import game.gui.GuiCrafting.Crafting;
import game.item.ItemStack;
import game.item.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.main.GamePanel;
import base.main.keyhandler.KeyHandler;

public class GuiPlayerInventory extends GuiContainer {

	private BufferedImage img;

	//TODO add armor only slot switching compatibility

	public GuiPlayerInventory(World world, Player p) {
		super(world, p);

		img = Images.loadImage("/player/playerGui.png");

		blockInventory = p.invArmor.getInventory();

		buttonList.add(new Button(centerX - 50, centerY + 13, Items.craftTable.getTexture()));
		buttonList.add(new Button(centerX - 18 - 50, centerY + 13, Items.stick.getTexture()));
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

		for(Button b : buttonList){
			b.draw(g);
		}
		
		if(currentContainer == 2){
			int i = 0;
			for(ItemStack stack : Crafting.getRecipe(slot_index)){
				if(stack != null){
					stack.getItem().draw(g, centerX + 51 + ((i%3)*getSlotSpacingX()), centerY - 45 + ((i/3)*getSlotSpacingY()), stack);
					i++;
				}
			}
		}

	}

	@Override
	public boolean pausesGame() {
		return true;
	}

	@Override
	public int getFirstSlotLocationX() {
		return currentContainer == 2 ? centerX - 51: GamePanel.WIDTH/2 - 150/2 + 48;
	}

	@Override
	public int getFirstSlotLocationY() {
		return currentContainer == 2 ? centerY + 12 : isContainerInventory() ? GamePanel.HEIGHT/2 - 75/2+ 8 :GamePanel.HEIGHT/2 - 75/2 + 32;
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

	@Override
	public void handleGuiKeyInput() {

		if(currentContainer != 2)
			super.handleGuiKeyInput();
		
		if(slot_index == 5 && currentContainer != 2){
			if(!isContainerInventory() ){
				if(KeyHandler.isPressed(KeyHandler.LEFT)){
					currentContainer = 2;
					slotSelected.y = getFirstSlotLocationY();
					slotSelected.x = getFirstSlotLocationX();
					slotIndex[1] = 0;
					slotIndex[0] = 1;
				}
			}
		}
		
		else if(currentContainer == 2){
			if(KeyHandler.isPressed(KeyHandler.ESCAPE)){
				world.displayGui(null);
			}
			
			else if(KeyHandler.isPressed(KeyHandler.LEFT)){
				if(slotIndex[0] == 1){
					slotSelected.x -=getSlotSpacingX();
					slotIndex[0]--;
				}
			}
			
			else if(KeyHandler.isPressed(KeyHandler.RIGHT)){
				if(slotIndex[0] == 0){
					slotSelected.x +=getSlotSpacingX();
					slotIndex[0]++;
				}
				
				else if(slotIndex[0] == 1){
					currentContainer = PLAYER;
					slotSelected.y = getFirstSlotLocationY();
					slotSelected.x = getFirstSlotLocationX();
					slotIndex[1] = slotIndex[0] = 0;
				}
			}
			
			else if(KeyHandler.isValidationKeyPressed())
				buttonClicked(slot_index);

		}

	}
	
	private void buttonClicked(int id){
		System.out.println("click click : " + id);

		if(id == 0){
			Crafting.craftSticks(player);
		}
		if(id == 1){
			Crafting.craftCraftTable(player);
		}
	}
}
