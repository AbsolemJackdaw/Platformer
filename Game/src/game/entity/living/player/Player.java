
package game.entity.living.player;

import game.Loading;
import game.World;
import game.content.Images;
import game.content.save.DataList;
import game.content.save.DataTag;
import game.entity.Animation;
import game.entity.MapObject;
import game.entity.inventory.IInventory;
import game.entity.living.EntityLiving;
import game.item.ItemBlock;
import game.item.ItemStack;
import game.item.Items;

import java.awt.Color;
import java.awt.Graphics2D;

import base.main.keyhandler.KeyHandler;
import base.tilemap.TileMap;

public class Player extends EntityLiving implements IInventory{

	private boolean flinching;
	private long flinchTimer;

	private boolean attacking;

	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int ATTACKING = 4;

	public static Animation animation = new Animation();

	private ItemStack[] inventory = new ItemStack[10];
	private ItemStack[] armorItems = new ItemStack[4];

	public ArmorInventory invArmor = new ArmorInventory();

	public Player(TileMap tm, World world) {
		super(tm, world, "player");

		width = 32;
		height = 32;

		entitySizeX = 20;
		entitySizeY = 30;

		moveSpeed = 0.5; // inital walking speed. you speed up as you walk
		maxSpeed = 3; // change to jump farther and walk faster
		stopSpeed = 0.4;
		fallSpeed = 0.15; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		facingRight = true;

		setPosition(3*32, 3*32);

		setStackInSlot(0, new ItemStack(Items.craftTable,1));
	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	@Override
	public void draw(Graphics2D g) {

		if (flinching) {
			final long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (((elapsed / 100) % 2) == 0)
				g.setColor(Color.RED);
			else
				g.setColor(Color.WHITE);
			//				return;
		}
		super.draw(g);

	}

	@Override
	public void getNextPosition() {

		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed)
				dx = -maxSpeed;
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed)
				dx = maxSpeed;
		} else if (dx > 0) {
			dx -= stopSpeed;
			if (dx < 0)
				dx = 0;
		} else if (dx < 0) {
			dx += stopSpeed;
			if (dx > 0)
				dx = 0;
		}

		// cannot move while attacking, except in air
		if ((currentAction == ATTACKING) && !(jumping || falling))
			dx = 0;

		// jumping
		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		// falling
		if (falling) {
			dy += fallSpeed;

			if (dy > 0)
				jumping = false;
			if ((dy < 0) && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed)
				dy = maxFallSpeed;

		}
	}

	public void handleInput(){
		setLeft(KeyHandler.keyState[KeyHandler.LEFT]);

		setRight(KeyHandler.keyState[KeyHandler.RIGHT]);

		setJumping(KeyHandler.keyState[KeyHandler.UP]);

		if (KeyHandler.isPressed(KeyHandler.SPACE))
			setAttacking();

		if(KeyHandler.isPressed(KeyHandler.ONE)){
			if(getStackInSlot(0) != null){
				if(getStackInSlot(0).getItem() != null){
					if(getStackInSlot(0).getItem() instanceof ItemBlock){
						ItemBlock ib = (ItemBlock)getStackInSlot(0).getItem();
						ib.placeBlock(tileMap, getWorld(), this);
						getStackInSlot(0).stackSize--;
						if(getStackInSlot(0).stackSize == 0)
							setStackInSlot(0, null);
					}
				}
			}
		}
	}

	@Override
	public void update() {

		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		//check for animation to loop, then stop attacking
		if (currentAction == ATTACKING)
			if (getAnimation().hasPlayedOnce())
				attacking = false;

		if (attacking && (currentAction != ATTACKING)) {
			//TODO attack enemies
			for(MapObject o : getWorld().listWithMapObjects){
				entitySizeX += 5;
				if(getRectangle().intersects(o.getRectangle())){
					if(o.getScreenXpos() > getScreenXpos() && facingRight)
						o.interact(this, o);
					else if( o.getScreenXpos() < getScreenXpos() && !facingRight)
						o.interact(this, o);
				}
				entitySizeX -= 5;
			}
		}

		//check to stop flinching, and ready to get hurt again
		if (flinching) {
			final long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 1200)
				flinching = false;
		}

		updatePlayerAnimation();

		// set direction
		if (currentAction != ATTACKING) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}
	}

	public void setAttacking() {
		attacking = true;
	}

	private void updatePlayerAnimation() {
		// set animation
		if (attacking) {
			if (currentAction != ATTACKING) {
				currentAction = ATTACKING;
				getAnimation().setFrames(Images.instance.playerSheet.get(ATTACKING));
				getAnimation().setDelay(75);
			}
		} else if (dy > 0) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				getAnimation().setFrames(Images.instance.playerSheet.get(FALLING));
				getAnimation().setDelay(100);
			}
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				getAnimation().setFrames(Images.instance.playerSheet.get(JUMPING));
				getAnimation().setDelay(Animation.NONE);
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				getAnimation().setFrames(Images.instance.playerSheet.get(WALKING));
				getAnimation().setDelay(75);
			}
		} else if (currentAction != IDLE) {
			currentAction = IDLE;
			getAnimation().setFrames(Images.instance.playerSheet.get(IDLE));
			getAnimation().setDelay(100);
		}

		getAnimation().update();
	}

	@Override
	public void checkTileMapCollision() {

		if(tileMap.getBlockID(currCol, currRow) == 6){
			Loading.gotoNextLevel(getWorld().gsm);
		}

		if(tileMap.getBlockID(currCol, currRow) == 7){
			Loading.gotoPreviousLevel(getWorld().gsm);
		}

		super.checkTileMapCollision();
	}

	public int getAttackDamage(){
		//TODO include weapon strength
		return 1;
	}

	/*======================INVENTORY====================*/

	@Override
	public ItemStack[] getItems() {
		return inventory;
	}

	@Override
	public boolean hasStack(ItemStack stack) {
		for(int i = 0; i < inventory.length; i++)
			if(getStackInSlot(i) != null)
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
		for(int i = 0; i < inventory.length; i++)
			if(getStackInSlot(i) == null){
				setStackInSlot(i, item);
				return true;
			}else{
				if (inventory[i].getItem().equals(item.getItem())){
					setStackInSlot(i, item);
					return true;
				}
			}
		return false;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if(inventory[slot] == null)
			inventory[slot] = stack;
		else if (stack == null && inventory[slot] != null)
			inventory[slot] = null;
//		else if (inventory[slot].getItem().equals(stack.getItem()))
//			inventory[slot].stackSize += stack.stackSize;
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
	public void writeToSave(DataTag data) {
		super.writeToSave(data);

		DataList list = new DataList();
		for(int slot = 0; slot < getMaxSlots(); slot++){
			ItemStack stack = getStackInSlot(slot);
			if(stack != null){
				DataTag tag = new DataTag();
				stack.writeToSave(tag);
				list.write(tag);
			}
		}
		data.writeList("items", list);

		DataList armor = new DataList();
		for(int slot = 0; slot < invArmor.getMaxSlots(); slot++){
			ItemStack stack = invArmor.getStackInSlot(slot);
			if(stack != null){
				DataTag tag = new DataTag();
				stack.writeToSave(tag);
				armor.write(tag);
			}
		}
		data.writeList("armorItems", armor);
	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);

		DataList list = data.readList("items");
		for(int i = 0; i < list.data().size(); i++){
			DataTag tag = list.readArray(i);
			ItemStack stack = ItemStack.createFromSave(tag);
			setStackInSlot(i, stack);
		}

		DataList armor = data.readList("armorItems");
		for(int i = 0; i < armor.data().size(); i++){
			DataTag tag = armor.readArray(i);
			ItemStack stack = ItemStack.createFromSave(tag);
			invArmor.setStackInSlot(i, stack);
		}
	}

	public class ArmorInventory implements IInventory{

		@Override
		public ItemStack[] getItems() {
			return armorItems;
		}

		@Override
		public boolean hasStack(ItemStack stack) {
			for(int i = 0; i < armorItems.length; i++)
				if(getStackInSlot(i) != null)
					return true;

			return false;
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return armorItems[slot];
		}

		@Override
		public int getMaxSlots() {
			return armorItems.length;
		}

		@Override
		public boolean setStackInNextAvailableSlot(ItemStack item) {
			for(int i = 0; i < armorItems.length; i++)
				if(getStackInSlot(i) == null){
					setStackInSlot(i, item);
					return true;
				}else{
					if (armorItems[i].getItem().equals(item.getItem())){
						setStackInSlot(i, item);
						return true;
					}
				}
			return false;
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			if(armorItems[slot] == null)
				armorItems[slot] = stack;
			else if (stack == null)
				armorItems[slot] = null;
//			else if (armorItems[slot].getItem().equals(stack.getItem()))
//				armorItems[slot].stackSize += stack.stackSize;
		}

		@Override
		public void removeStack(int slot) {
			armorItems[slot] = null;
		}

		@Override
		public boolean hasStackInSlot(int slot) {
			return (armorItems[slot] != null);
		}

		@Override
		public IInventory getInventory() {
			return this;
		}

	}
}
