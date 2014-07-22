package game.gui;

import game.World;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;
import game.gui.container.Container;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import base.main.keyhandler.KeyHandler;

public class GuiContainer extends Gui implements Container{

	protected IInventory blockInventory;
	protected IInventory playerInventory;

	protected static final int PLAYER = 0;
	protected static final int BLOCK = 1;

	protected int currentContainer = PLAYER;

	/**Location the "cursor" currently is at, where 0 = x && 1 = y*/
	protected int[] slotIndex = new int[2];

	/**the little yellow square that indicates what slot is selected*/
	public static final Rectangle slotSelected = new Rectangle(17, 17);

	/**returns an integer => 0 && < getMaxSlots(). this is used to know what slot we have selected*/
	public int slot_index;

	public GuiContainer(IInventory blockInventory, Player p) {
		super(p.getWorld(), p);
		this.blockInventory = blockInventory;
		playerInventory = p;

		slotSelected.x = getFirstSlotLocationX();
		slotSelected.y = getFirstSlotLocationY();
	}

	public GuiContainer(World world, Player p) {
		super(world, p);

		blockInventory = null;
		playerInventory = p;

		slotSelected.x = getFirstSlotLocationX();
		slotSelected.y = getFirstSlotLocationY();
	}

	@Override
	public void draw(Graphics2D g){

		g.setColor(Color.yellow);
		g.draw(slotSelected);

		slot_index = slotIndex[0]+ (slotIndex[1]*(rowsX()));
	}

	@Override
	public int getFirstSlotLocationX() {
		return 0;
	}

	@Override
	public int getFirstSlotLocationY() {
		return 0;
	}

	@Override
	public int getSlotSpacingX() {
		return 0;
	}

	@Override
	public int getSlotSpacingY() {
		return 0;
	}

	@Override
	public void handleGuiKeyInput() {
		super.handleGuiKeyInput();

		if(KeyHandler.isPressed(KeyHandler.LEFT)){
			if(slotIndex[0] > 0){
				slotSelected.x -=getSlotSpacingX();
				slotIndex[0]--;
			}
		}

		else if(KeyHandler.isPressed(KeyHandler.RIGHT)){
			if(slotIndex[0] < (rowsX()-1)){
				slotSelected.x +=getSlotSpacingX();
				slotIndex[0]++;
			}
		}


		else if(KeyHandler.isPressed(KeyHandler.UP)){
			if(slotIndex[1] > 0){
				slotSelected.y -=getSlotSpacingY();
				slotIndex[1]--;
			}else if((slotIndex[1] == 0) && !isContainerInventory()){
				currentContainer = BLOCK;
				slotSelected.y = getFirstSlotLocationY();
				slotSelected.x = getFirstSlotLocationX();
				slotIndex[1] = slotIndex[0] = 0;
			}
		}

		else if(KeyHandler.isPressed(KeyHandler.DOWN))
			if(slotIndex[1] < (rowsY()-1)){
				slotSelected.y +=getSlotSpacingY();
				slotIndex[1]++;
			}else if((slotIndex[1] == (rowsY()-1)) && isContainerInventory()){
				currentContainer = PLAYER;
				slotSelected.y = getFirstSlotLocationY();
				slotSelected.x = getFirstSlotLocationX();
				slotIndex[1] = slotIndex[0] = 0;

			}

		//put stack from one inventory to another
		if(blockInventory != null)
			if(KeyHandler.isPressed(KeyHandler.ENTER) || KeyHandler.isPressed(KeyHandler.SPACE))
				if(isContainerInventory() && blockInventory != null){
					System.out.println(slot_index);
					if(blockInventory.getStackInSlot(slot_index) != null)
						if(playerInventory.setStackInNextAvailableSlot(blockInventory.getStackInSlot(slot_index)))
							blockInventory.setStackInSlot(slot_index, null);
				}else{
					int slot = slotIndex[0]+ (slotIndex[1]*(rowsX()));
					System.out.println(slot);
					if(playerInventory.getStackInSlot(slot) != null)
						if(blockInventory.setStackInNextAvailableSlot(playerInventory.getStackInSlot(slot)))
							playerInventory.setStackInSlot(slot, null);
				}
	}

	protected boolean isContainerInventory(){
		return currentContainer == BLOCK;
	}

	@Override
	public int rowsX() {
		return 0;
	}

	@Override
	public int rowsY() {
		return 0;
	}
}