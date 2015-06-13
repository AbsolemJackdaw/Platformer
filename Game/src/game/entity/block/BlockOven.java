package game.entity.block;

import game.World;
import game.content.Images;
import game.content.save.DataList;
import game.content.save.DataTag;
import game.entity.Animation;
import game.entity.MapObject;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;
import game.gui.GuiOven;
import game.item.ItemStack;
import game.item.Items;
import game.item.crafting.OvenRecipes;
import game.item.tool.ItemTool;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.tilemap.TileMap;

public class BlockOven extends BlockBreakable implements IInventory{


	private ItemStack[] inventory = new ItemStack[3];

	public int timer;
	public int currentFuelTimer;

	Animation fire = new Animation();
	
	BufferedImage img = Images.loadImage("/blocks/oven.png");

	public BlockOven(TileMap tm, World world) {
		super(tm, world, "oven",ItemTool.NOTHING);

		fire.setFrames(Images.loadMultiImage("/blocks/ovenFire.png", 32, 0, 4));
		fire.setDelay(50);
	}

	@Override
	public BufferedImage getEntityTexture() {
		return img;
	}

	@Override
	public boolean hasAnimation() {
		return false;
	}

	@Override
	public boolean isStackable() {
		return false;
	}

	@Override
	public void update() {
		super.update();

//		System.out.println(timer + " " + (((float)timer/(float)currentFuelTimer)*32f));
		
		if(timer > 0){
			timer -- ;

			if(timer == 0){
				if(getStackInSlot(1) != null){
					ItemStack result = OvenRecipes.getRecipe(getStackInSlot(1));
					getStackInSlot(1).stackSize--;
					if(getStackInSlot(1).stackSize <= 0){
						setStackInSlot(1, null);
					}
					setStackInSlot(2, result);
				}
			}
		}

		if(getStackInSlot(0) != null && getStackInSlot(1) != null){
			if(timer == 0 && getStackInSlot(0).stackSize > 0){
				if(getStackInSlot(0) != null)
					currentFuelTimer = timer = getStackInSlot(0).getItem().getFuelTimer();

				getStackInSlot(0).stackSize--;
				if(getStackInSlot(0).stackSize <= 0){
					setStackInSlot(0, null);
				}
			}
		}

		if(timer > 0)
			fire.update();
		
//		System.out.println(getHealth());
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		if(timer > 0)
			g.drawImage(fire.getImage(),
					(int) ((xScreen + xmap) - (width / 2)),
					(int) ((yScreen + ymap) - (height / 2)), null);

	}

	@Override
	public void interact(Player p, MapObject o) {
		GuiOven gui = new GuiOven(this, p);
		gui.setOven((BlockOven)o);
		getWorld().displayGui(gui);
	}

	@Override
	public ItemStack getDrop(){
		return new ItemStack(Items.oven, 1);
	}

	@Override
	protected void mine(Player p) {
		//oven can not be destroyed with items in it
		if(inventory[0] == null && inventory[1] == null && inventory[2] == null){
			super.mine(p);
		}else{
			System.out.println("Oven can not be recovered while it has items stocked !");
			resetHealth();
		}

	}

	@Override
	public ItemStack[] getItems() {
		return inventory;
	}

	@Override
	public boolean hasStack(ItemStack stack) {
		for(int i = 0; i < inventory.length; i++)
			if(getStackInSlot(i) != null && getStackInSlot(i).getItem().equals(stack.getItem()))
				return true;

		return false;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public int getMaxSlots() {
		return inventory.length;
	}


	@Override
	public boolean setStackInNextAvailableSlot(ItemStack item) {
		if(item.getItem().isFuel()){
			setStackInSlot(0, item);
			return true;
		}

		if(item.getItem().isCookable()){
			setStackInSlot(1, item);
			return true;
		}
		return false;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if(inventory[slot] == null)
			inventory[slot] = stack;
		else if (stack == null && inventory[slot] != null)
			inventory[slot] = null;
		else if(inventory[slot].getItem().equals(stack.getItem()))
			inventory[slot].stackSize += stack.stackSize;
		else
			System.out.println("something tried to replace the existing item" + inventory[slot].getItem().getUIN() +
					" by "+ stack.getItem().getUIN() );
	}

	@Override
	public void removeStack(int slot) {
		inventory[slot] = null;
	}

	@Override
	public boolean hasStackInSlot(int slot) {
		return (inventory[slot] != null);
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
	public void writeToSave(DataTag data) {
		super.writeToSave(data);

		DataList list = new DataList();
		for(int slot = 0; slot < getMaxSlots(); slot++){
			ItemStack stack = getStackInSlot(slot);
			if(stack != null){
				DataTag tag = new DataTag();
				stack.writeToSave(tag);
				tag.writeInt("slot", slot);
				list.write(tag);
			}
		}
		data.writeList("items", list);

		data.writeInt("timer", timer);
		data.writeInt("fueltimer", currentFuelTimer);
	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);

		DataList list = data.readList("items");
		for(int i = 0; i < list.data().size(); i++){
			DataTag tag = list.readArray(i);
			int slot = tag.readInt("slot");
			ItemStack stack = ItemStack.createFromSave(tag);
			setStackInSlot(slot, stack);
		}
		timer = data.readInt("timer");
		currentFuelTimer = data.readInt("fueltimer");
	}
}
