
package game.entity.living.player;

import game.Loading;
import game.World;
import game.content.Images;
import game.content.save.DataList;
import game.content.save.DataTag;
import game.entity.Animation;
import game.entity.MapObject;
import game.entity.block.Block;
import game.entity.inventory.IInventory;
import game.entity.living.EntityLiving;
import game.item.ItemBlock;
import game.item.ItemStack;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.main.keyhandler.KeyHandler;
import base.tilemap.TileMap;

public class Player extends EntityLiving implements IInventory{

	private boolean flinching;
	private long flinchTimer;

	private boolean attacking;

	private static final int BODY_IDLE = 0;
	private static final int HEAD_IDLE = 1;
	private static final int ARMS_IDLE = 2;
	private static final int LEGS_IDLE = 3;
	private static final int HEAD_JUMP = 4;
	private static final int ARM_JUMP = 5; //same as leg, but 1st frame
	private static final int LEG_JUMP = 5; //same as arm, but 2nd frame
	private static final int ARMS_RUN = 6;
	private static final int BODY_RUN = 7;
	private static final int LEGS_RUN = 8;
	private static final int ARMS_WEAPON = 9;
	private static final int ARMS_ATTACK = 10;
	private static final int LEGS_ATTACK = 11; //frame 0
	private static final int HEAD_ATTACK = 11; //frame 1
	private static final int BODY_ATTACK = 11; //frame 2
	
	private static final int ACTION_ATTACK = 0;
	private static final int ACTION_WALK = 1;
	private static final int ACTION_JUMPING = 2;
	private static final int ACTION_FALLING = 3;
	private static final int ACTION_IDLE = 4;
	
	private Animation head = new Animation();
	private Animation arms = new Animation();
	private Animation legs = new Animation();

	private ItemStack[] inventory = new ItemStack[10];
	private ItemStack[] armorItems = new ItemStack[4];
	//ARMOR INFO
	//0 helm
	//1 chest
	//2 legs 
	//3 weapon

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
		
		//Draw all parts here
		super.draw(g);
		super.draw(g, head);
		super.draw(g, arms);
		super.draw(g, legs);

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
		if ((currentAction == ACTION_ATTACK) && !(jumping || falling))
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

	private int[] keys = new int[]{KeyHandler.ONE, KeyHandler.TWO, KeyHandler.THREE,
			KeyHandler.FOUR, KeyHandler.FIVE, KeyHandler.SIX,KeyHandler.SEVEN, KeyHandler.EIGHT, KeyHandler.NINE};

	public void handleInput(){
		setLeft(KeyHandler.keyState[KeyHandler.LEFT]);

		setRight(KeyHandler.keyState[KeyHandler.RIGHT]);

		setJumping(KeyHandler.keyState[KeyHandler.SPACE]);

		if (KeyHandler.isPressed(KeyHandler.CTRL))
			setAttacking();

		if(KeyHandler.isPressed(KeyHandler.INTERACT)){
			for(MapObject o : getWorld().listWithMapObjects){
				if(o instanceof Block){
					if(o.intersects(this)){
						Block b = (Block)o;
						b.interact(this, o);
					}
				}
			}
		}

		for(int key : keys)
			if(KeyHandler.isPressed(key)){
				if(getStackInSlot(key-10) != null){
					if(getStackInSlot(key-10).getItem() != null){
						if(getStackInSlot(key-10).getItem() instanceof ItemBlock){
							ItemBlock ib = (ItemBlock)getStackInSlot(key-10).getItem();
							ib.placeBlock(tileMap, getWorld(), this);
							getStackInSlot(key-10).stackSize--;
							if(getStackInSlot(key-10).stackSize == 0)
								setStackInSlot(key-10, null);
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
		if (currentAction == ACTION_ATTACK)
			if (arms.hasPlayedOnce()) //arms needs to be looped, as only arms are animated
				attacking = false;

		if (attacking && (currentAction != ACTION_ATTACK)) {
			//TODO attack enemies
			for(MapObject o : getWorld().listWithMapObjects){
				entitySizeX += 5;
				if(getRectangle().intersects(o.getRectangle())){
					if(o.getScreenXpos() > getScreenXpos() && facingRight)
						o.onEntityHit(this, o);
					else if( o.getScreenXpos() < getScreenXpos() && !facingRight)
						o.onEntityHit(this, o);
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
		if (currentAction != ACTION_ATTACK) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}
	}

	public void setAttacking() {
		attacking = true;
	}

	/**return an array of images that conseales the body parts*/
	private BufferedImage[] getBodyPart(int i){
		return Images.instance.playerSheet.get(i);
	}
	
	private void updatePlayerAnimation() {
		// set animation
		if (attacking) {
			if (currentAction != ACTION_ATTACK) {
				currentAction = ACTION_ATTACK;
				
				getAnimation().setFrames(getBodyPart(BODY_ATTACK));
				getAnimation().setDelay(Animation.NONE);
				getAnimation().setFrame(2);
				head.setFrames(getBodyPart(HEAD_ATTACK));
				head.setFrame(1);
				head.setDelay(Animation.NONE);
				arms.setFrames(getBodyPart(ARMS_ATTACK));
				arms.setDelay(50);
				legs.setFrames(getBodyPart(LEGS_ATTACK));
				legs.setFrame(0);
				legs.setDelay(Animation.NONE);
				
			}
		} else if (dy > 0) {
			if (currentAction != ACTION_FALLING) {
				currentAction = ACTION_FALLING;
				
				getAnimation().setFrames(getBodyPart(BODY_RUN));
				getAnimation().setDelay(100);
				
				head.setFrames(getBodyPart(HEAD_JUMP));
				head.setFrame(1);
				head.setDelay(Animation.NONE);
				arms.setFrames(getBodyPart(ARM_JUMP));
				arms.setFrame(0);
				arms.setDelay(Animation.NONE);
				legs.setFrames(getBodyPart(LEG_JUMP));
				legs.setFrame(1);
				legs.setDelay(Animation.NONE);
			}
		} else if (dy < 0) {
			if (currentAction != ACTION_JUMPING) {
				currentAction = ACTION_JUMPING;
				
				getAnimation().setFrames(getBodyPart(BODY_RUN));
				head.setFrames(getBodyPart(HEAD_JUMP));
				head.setFrame(0);
				head.setDelay(Animation.NONE);
				arms.setFrames(getBodyPart(ARM_JUMP));
				arms.setFrame(0);
				arms.setDelay(Animation.NONE);
				legs.setFrames(getBodyPart(LEG_JUMP));
				legs.setFrame(1);
				legs.setDelay(Animation.NONE);
			}
		} else if (left || right) {
			if (currentAction != ACTION_WALK) {
				currentAction = ACTION_WALK;
				
				getAnimation().setFrames(getBodyPart(BODY_RUN));
				head.setFrames(getBodyPart(HEAD_IDLE));
				arms.setFrames(getBodyPart(ARMS_RUN));
				arms.setDelay(75);
				legs.setFrames(getBodyPart(LEGS_RUN));
				legs.setDelay(75);
				
			}
		} else if (currentAction != ACTION_IDLE) {
			currentAction = ACTION_IDLE;
						
			//body
			getAnimation().setFrames(getBodyPart(BODY_IDLE));
			head.setFrames(getBodyPart(HEAD_IDLE));
			//change arm animation from idle in sheet
			arms.setFrames(getBodyPart(ARMS_IDLE));
			arms.setDelay(500);
			legs.setFrames(getBodyPart(LEGS_IDLE));
		}

		getAnimation().update();
		head.update();
		arms.update();
		legs.update();
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

		for(int i = 0; i < inventory.length; i++)
			if(getStackInSlot(i) != null){
				if (inventory[i].getItem().equals(item.getItem())){
					setStackInSlot(i, item);
					return true;
				}
			}

		for(int i = 0; i < inventory.length; i++)
			if(getStackInSlot(i) == null){
				setStackInSlot(i, item);
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
				tag.writeInt("slot", slot);
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
				tag.writeInt("slot", slot);
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
			int slot = tag.readInt("slot");
			ItemStack stack = ItemStack.createFromSave(tag);
			setStackInSlot(slot, stack);
		}

		DataList armor = data.readList("armorItems");
		for(int i = 0; i < armor.data().size(); i++){
			DataTag tag = armor.readArray(i);
			int slot = tag.readInt("slot");
			ItemStack stack = ItemStack.createFromSave(tag);
			invArmor.setStackInSlot(slot, stack);
		}
	}

	public class ArmorInventory implements IInventory{

		public ItemStack getWeapon(){
			return armorItems[3];
		}

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
			else if(armorItems[slot].getItem().equals(stack.getItem())){
				armorItems[slot].stackSize += stack.stackSize;
			}
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

		@Override
		public int getSlotForStack(ItemStack stack) {
			return 0;
		}

	}

	/**returns slot index for that item*/
	@Override
	public int getSlotForStack(ItemStack drop) {
		for(int slot = 0; slot < getMaxSlots(); slot++){
			if(getStackInSlot(slot) != null){
				if(getStackInSlot(slot).getItem() == drop.getItem())
					return slot;
			}
		}
		return -1;
	}
	
	
	/**used in torchlight for lighting at night*/
	public int getRadius(){
		//if player has torch, get light strenght from torch
		return 200;
	}
}
