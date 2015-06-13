package game.gui;

import game.content.Images;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;
import game.item.ItemLantern;
import game.item.ItemStack;
import game.item.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.main.GamePanel;
import base.main.keyhandler.KeyHandler;

public class GuiLantern extends GuiContainer {

	ItemLantern lantern;
	BufferedImage img = Images.loadImage("/gui/lantern.png");

	public GuiLantern(IInventory inv, Player p) {
		super(inv, p);

	}

	public GuiLantern setLantern(ItemLantern lantern){
		this.lantern = lantern;
		return this;
	}

	@Override
	public void draw(Graphics2D g) {

		g.drawImage(img.getSubimage(0, 0, 100, 108), GamePanel.WIDTH/2 - 150/2, GamePanel.HEIGHT/2 - 75/2 ,null);

		int time = lantern.burnTime/20;
		int minutes = time/60;
		int seconds = time%60;
		String sec = seconds < 10 ? "0"+seconds : ""+seconds;

		g.drawString(minutes+":"+sec, centerX +1, centerY-25);
		
		if(lantern != null)
			if(lantern.isLit())
				g.drawImage(img.getSubimage(100, 0, 48, 62), GamePanel.WIDTH/2 - 128/2, GamePanel.HEIGHT/2 - 68/2 ,null);

		for(int slot = 0; slot < player.getInventory().getItems().length; slot++){
			ItemStack i = player.getStackInSlot(slot);
			if(i != null){

				int x = slot < 5 ? (centerX - 73) + (slot*18) : (centerX - 73) + ((slot-5)*18);
				int y = slot < 5 ? centerY + 35 : centerY + 35 + 18;

				i.getItem().draw(g, x, y, i);
			}
		}

		ItemStack i = secondairyInventory.getStackInSlot(0);
		if(i != null){
			int x = centerX - 48;
			int y = centerY - 5;
			i.getItem().draw(g, x, y, i);
		}
		super.draw(g);
		lantern.update();
	}

	@Override
	public int getFirstSlotLocationX() {
		return isNotPlayerInventory() ? centerX-49  :centerX - 2 - (72);
	}

	@Override
	public int getFirstSlotLocationY() {
		return isNotPlayerInventory() ? centerY - 6: centerY + 34;
	}

	@Override
	public boolean pausesGame() {
		return true;
	}

	@Override
	public int getSlotSpacingX() {
		return isNotPlayerInventory() ? 50 : 18;
	}

	@Override
	public int getSlotSpacingY() {
		return isNotPlayerInventory() ? 0 : 18;
	}

	@Override
	public int rowsX() {
		return isNotPlayerInventory() ? 2 : 5;
	}

	@Override
	public int rowsY() {
		return isNotPlayerInventory() ? 1 : 2;
	}


	@Override
	public void handleGuiKeyInput() {
		super.handleGuiKeyInput();
	}

	@Override
	protected void containerItemSwappingLogic() {

		if(KeyHandler.isValidationKeyPressed()){
			if(isNotPlayerInventory()){
				System.out.println(slot_index);
				if(slot_index == 0){
					if(secondairyInventory.getStackInSlot(0) != null){
						ItemStack stack = secondairyInventory.getStackInSlot(0);
						stack.stackSize--;
						ItemStack newStack = new ItemStack(stack.getItem(),1);
						playerInventory.setStackInNextAvailableSlot(newStack);
						if(stack.stackSize<=0)
							secondairyInventory.setStackInSlot(0, null);
					}
				}else{
					if(lantern != null){
						if(lantern.getStackInSlot(0) != null){
							lantern.getStackInSlot(0).stackSize--;
							lantern.burnTime += lantern.defaultBurnTime;
							if(lantern.getStackInSlot(0).stackSize <= 0)
								lantern.setStackInSlot(0, null);
							if(!lantern.isLit())
								lantern.setLit(true);
						}else{
							System.out.println(lantern.isLit());
							if(lantern.isLit()){
								lantern.setLit(false);
								return;
							}
							if(!lantern.isLit() && lantern.burnTime > 0){
								lantern.setLit(true);
								return;
							}
						}
					}
				}
			}else{
				int slot = slotIndex[0]+ (slotIndex[1]*(rowsX()));
				if(playerInventory.getStackInSlot(slot) != null){
					ItemStack stack = playerInventory.getStackInSlot(slot);
					if(stack.getItem().equals(Items.grease)){
						ItemStack newStack = new ItemStack(stack.getItem(), 1);
						if(secondairyInventory.setStackInNextAvailableSlot(newStack)){
							stack.stackSize--;
							if(stack.stackSize <= 0){
								playerInventory.setStackInSlot(slot, null);
							}
						}
					}
				}
			}
		}
	}

}
