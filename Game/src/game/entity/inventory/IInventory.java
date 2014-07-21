package game.entity.inventory;

import game.item.ItemStack;

public interface IInventory {
	
	public ItemStack[] getItems();
	public boolean hasStack(ItemStack stack);
	public ItemStack getStackInSlot(int slot);
	public int getMaxSlots();
	public boolean setStackInNextAvailableSlot(ItemStack item);
	public void setStackInSlot(int slot, ItemStack stack);
	public void removeStack(int slot);
	public boolean hasStackInSlot(int slot);
	public IInventory getInventory();
	public int getSlotForStack(ItemStack stack);

}
