package game.item.crafting;

import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.Items;

public class Crafting {

	public static final int Sticks = 0;
	public static final int CraftTable = 1;
	public static final int Pickaxe = 2;
	public static final int CampFire = 3;
	public static final int Sword = 4;
	public static final int Axe = 5;
	public static final int Oven = 6;
	public static final int Lantern = 7;

	public static ItemStack[] getRecipe(int i){
		ItemStack[] is = new ItemStack[10];

		switch(i){
		case Sticks:
			is[0] = new ItemStack(Items.log, 1);
			break;
		case CraftTable:
			is[0] = new ItemStack(Items.stick, 12);
			is[1] = new ItemStack(Items.log, 4);
			break;
		case Pickaxe:
			is[0] = new ItemStack(Items.stick, 2);
			is[1] = new ItemStack(Items.rock, 6);
			break;
		case CampFire:
			is[0] = new ItemStack(Items.log, 5);
			is[1] = new ItemStack(Items.rock, 12);
			is[2] = new ItemStack(Items.stick, 4);
			break;
		case Sword:
			is[0] = new ItemStack(Items.stick, 2);
			is[1] = new ItemStack(Items.log, 1);
			is[2] = new ItemStack(Items.rock, 8);
			break;
		case Axe:
			is[0] = new ItemStack (Items.stick , 2);
			is[1] = new ItemStack(Items.rock, 3);
			break;
		case Oven:
			is[0] = new ItemStack (Items.rock , 20);
			break;
		case Lantern:
			is[0] = new ItemStack(Items.ingot, 10);
			break;
			
		}
		return is;

	}

	public static ItemStack result(int i){

		ItemStack st = null;

		switch (i) {

		case Sticks:
			st = new ItemStack(Items.stick, 2);
			break;
		case CraftTable:
			st = new ItemStack(Items.craftTable, 1);
			break;
		case Pickaxe: 
			st = new ItemStack(Items.pickaxe, 1);
			break;
		case CampFire:
			st = new ItemStack(Items.campfire, 1);
			break;
		case Sword :
			st = new ItemStack(Items.sword, 1);
			break;
		case Axe :
			st = new ItemStack(Items.axe, 1);
			break;
		case Oven:
			st = new ItemStack(Items.oven, 1);
			break;
		case Lantern:
			st = new ItemStack(Items.lantern, 1);
			break;

		}
		return st;
	}

	public static void craft(Player player, int recipe){
		ItemStack[] input = getRecipe(recipe);
		ItemStack result = result(recipe);

		int index = 0;

		for(ItemStack st: input){
			if(st != null)
				index++;
		}

		boolean flag[] = new boolean[index];

		for(int it = 0; it < input.length; it++){
			for(int i = 0; i < player.getInventory().getItems().length; i++){
				if(player.getStackInSlot(i) != null){
					if(input[it] != null){
						if(player.getStackInSlot(i).getItem().equals(input[it].getItem())){
							if(player.getStackInSlot(i).stackSize >= input[it].stackSize){
								flag[it] = true;
							}
						}
					}
				}
			}
		}

		for(boolean b : flag)
			if(!b){
				System.out.println("Not all components are aquiered.");
				return;
			}

		player.setStackInNextAvailableSlot(result);
		for(int a = 0; a < input.length; a++){
			if(input[a] != null){
				int i = player.getInventory().getSlotForStack(input[a]);
				player.getItems()[i].stackSize -= input[a].stackSize;
				if(player.getItems()[i].stackSize == 0)
					player.getItems()[i] = null;
			}
		}
	}
}