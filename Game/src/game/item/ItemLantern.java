package game.item;

import game.World;
import game.content.save.DataTag;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;
import game.gui.GuiLantern;
import base.tilemap.TileMap;

public class ItemLantern extends Item implements IInventory {

	public int burnTime = 0;
	public final int defaultBurnTime = 1200;
	private boolean isLit = false;

	public ItemLantern(String uin) {
		super(uin);
	}

	@Override
	public void useItem(Item item, TileMap map, World world, Player player,	int key) {
		//TODO open gui

		GuiLantern gui = new GuiLantern(this, player).setLantern(this);
		world.displayGui(gui);

	}

	//***********************INVENTORY****************************//

	ItemStack[] inventory = new ItemStack[1];

	@Override
	public ItemStack[] getItems() {
		return inventory;
	}

	@Override
	public boolean hasStack(ItemStack stack) {
		return stack.getItem().equals(inventory[0].getItem());
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[0];
	}

	@Override
	public int getMaxSlots() {
		return 2;
	}

	@Override
	public boolean setStackInNextAvailableSlot(ItemStack item) {
		if(item.getItem().equals(Items.grease)){
			setStackInSlot(0, item);
			return true;
		}
		return false;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if(stack != null)
			if(!stack.getItem().equals(Items.grease))
				return;

		if(inventory[0] == null)
			inventory[0] = stack;

		else if (stack == null && inventory[0] != null)
			inventory[0] = null;

		else if(inventory[0].getItem().equals(stack.getItem()))
			inventory[slot].stackSize += stack.stackSize;
		else
			System.out.println("something tried to replace the existing item" + inventory[slot].getItem().getUIN() +
					" by "+ stack.getItem().getUIN() );

	}

	@Override
	public void removeStack(int slot) {
		inventory[0] = null;
	}

	@Override
	public boolean hasStackInSlot(int slot) {
		return inventory[0] == null ? false : true;
	}

	@Override
	public IInventory getInventory() {
		return this;
	}

	@Override
	public int getSlotForStack(ItemStack stack) {
		return 0;
	}

	@Override
	public void writeToSave(DataTag tag) {
		super.writeToSave(tag);
		tag.writeInt("burntime", burnTime);
		tag.writeInt("isLit", isLit() ? 1 : 0);
	}

	@Override
	public void readFromSave(DataTag tag) {
		super.readFromSave(tag);
		burnTime = tag.readInt("burntime");
		setLit(tag.readInt("isLit") == 0 ? false : true);
	}

	public boolean isLit() {
		return isLit;
	}

	public void setLit(boolean isLit) {
		this.isLit = isLit;
	}

	@Override
	public void update(){
		
		if(burnTime > 0)
			if(isLit())
				burnTime--;
			else
				;
		else
			if(isLit())
				setLit(false);

	}

	@Override
	public boolean isUpdateAble() {
		return true;
	}
}
