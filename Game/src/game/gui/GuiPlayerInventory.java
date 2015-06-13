package game.gui;

import game.World;
import game.content.Images;
import game.entity.living.player.Player;
import game.item.ItemBlock;
import game.item.ItemStack;
import game.item.Items;
import game.item.crafting.Crafting;
import game.item.tool.ItemTool;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.main.GamePanel;
import base.main.keyhandler.KeyHandler;
import base.main.keyhandler.XboxController;

public class GuiPlayerInventory extends GuiContainer {

	private BufferedImage img;

	//TODO add armor only slot switching compatibility

	public GuiPlayerInventory(World world, Player p) {
		super(world, p);

		img = Images.loadImage("/gui/playerGui.png");

		secondairyInventory = p.invArmor.getInventory();

		buttonList.add(new Button(centerX - 50, centerY + 13, Items.craftTable.getTexture()));
		buttonList.add(new Button(centerX - 18 - 50, centerY + 13, Items.stick.getTexture()));
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img.getSubimage(35, 3, 150, 75), GamePanel.WIDTH/2 - 150/2, GamePanel.HEIGHT/2 - 75/2 ,null);

		g.drawImage(Images.instance.playerSheet.get(0)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);
		g.drawImage(Images.instance.playerSheet.get(1)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);
		g.drawImage(Images.instance.playerSheet.get(2)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);
		g.drawImage(Images.instance.playerSheet.get(3)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);

		super.draw(g);

		for(int slot = 0; slot < player.getMaxSlots(); slot++){
			ItemStack stack = player.getStackInSlot(slot);
			if(stack != null){
				int i = slot < 5 ? slot : slot - 5;
				int j = slot < 5 ? 0 : 18;
				stack.getItem().draw(g, GamePanel.WIDTH/2 - 150/2 + 49 + (18*i), (GamePanel.HEIGHT/2 - 4)+ j, stack);
			}
		}

		for(int slot = 0; slot < secondairyInventory.getMaxSlots(); slot++){
			ItemStack stack = secondairyInventory.getStackInSlot(slot);
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
					stack.getItem().draw(g, centerX + 75 + ((i%3)*getSlotSpacingX()), centerY - 27 + ((i/3)*getSlotSpacingY()), stack);
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
		return currentContainer == 2 ? centerY + 12 : isNotPlayerInventory() ? GamePanel.HEIGHT/2 - 75/2+ 8 :GamePanel.HEIGHT/2 - 75/2 + 32;
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
		return isNotPlayerInventory() ? 4 : 5;
	}

	@Override
	public int rowsY() {
		return isNotPlayerInventory() ? 1 : 2;
	}

	@Override
	public void handleGuiKeyInput() {

		if(currentContainer != 2){
			super.handleGuiKeyInput();

			if(KeyHandler.isPressed(KeyHandler.PLACE) && XboxController.controller != null){

				if(player.getStackInSlot(slot_index) != null){

					if(player.getStackInSlot(slot_index).getItem() != null){
						//place down blocks
						if(player.getStackInSlot(slot_index).getItem() instanceof ItemBlock){
							ItemBlock ib = (ItemBlock)player.getStackInSlot(slot_index).getItem();
							ib.placeBlock(player.getWorld().tileMap, player.getWorld(), player);
							player.getStackInSlot(slot_index).stackSize--;
							if(player.getStackInSlot(slot_index).stackSize == 0)
								player.setStackInSlot(slot_index, null);
						}
					}
				}
			}
		}

		if(slot_index == 5 && currentContainer != 2){
			if(!isNotPlayerInventory() ){
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

	@Override
	protected void containerItemSwappingLogic() {
		if(secondairyInventory != null)
			if(KeyHandler.isValidationKeyPressed())
				if(isNotPlayerInventory() && secondairyInventory != null){ //armor inventory
					System.out.println(slot_index);
					//switch armor to player inventory
					if(secondairyInventory.getStackInSlot(slot_index) != null)
						if(playerInventory.setStackInNextAvailableSlot(secondairyInventory.getStackInSlot(slot_index)))
							secondairyInventory.setStackInSlot(slot_index, null);
				}else{//inentory to armor logic
					int slot = slotIndex[0]+ (slotIndex[1]*(rowsX()));
					System.out.println(slot);
					if(playerInventory.getStackInSlot(slot) != null)
						if(playerInventory.getStackInSlot(slot).getItem() instanceof ItemTool){
							if(secondairyInventory.getStackInSlot(3) == null){//3 is weapon slot
								secondairyInventory.setStackInSlot(3, playerInventory.getStackInSlot(slot));
								playerInventory.setStackInSlot(slot, null);
							}else{
								ItemStack a = playerInventory.getStackInSlot(slot);
								ItemStack b = secondairyInventory.getStackInSlot(3);
								
								playerInventory.removeStack(slot);
								secondairyInventory.removeStack(3);
								
								if(playerInventory.setStackInNextAvailableSlot(b))//set tool to next available slot to occupy any spacing
									secondairyInventory.setStackInSlot(3, a);
								else{//switch out items
									playerInventory.setStackInSlot(slot, b);
									secondairyInventory.setStackInSlot(3, a);
								}
								
							}
						}
					//TODO instanceof ItemArmor
					//					if(playerInventory.getStackInSlot(slot).getItem() instanceof ItemTool){
					//						if(blockInventory.getStackInSlot(3) == null){
					//							blockInventory.setStackInSlot(3, playerInventory.getStackInSlot(slot));
					//							playerInventory.setStackInSlot(slot, null);
					//						}
					//					}
				}
	}

	private void buttonClicked(int id){
		System.out.println("click click : " + id);

		if(id == 0){
			Crafting.craft(player, Crafting.Sticks);
		}
		if(id == 1){
			Crafting.craft(player, Crafting.CraftTable);
		}
	}
}
