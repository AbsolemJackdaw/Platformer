package game.item.crafting;

import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.Items;

public class Crafting {
	
	public static final int Sticks = 0;
	public static final int CraftTable = 1;
	public static final int Pickaxe = 2;
	public static final int CampFire = 3;
	
	public static ItemStack[] getRecipe(int i){
		ItemStack[] is = new ItemStack[10];

		if(i == Sticks){
			is[0] = new ItemStack(Items.log, 1);
		}else if(i == CraftTable){
			is[0] = new ItemStack(Items.stick, 8);
			is[1] = new ItemStack(Items.log, 1);
		}
		else if(i == Pickaxe){
			is[0] = new ItemStack(Items.stick, 2);
			is[1] = new ItemStack(Items.rock, 6);
		}
		else if(i == CampFire){
			is[0] = new ItemStack(Items.log, 5);
			is[1] = new ItemStack(Items.rock, 12);
		}

		return is;
	}

	public static ItemStack result(int i){

		ItemStack st = null;

		switch (i) {

		case Sticks:
			st = new ItemStack(Items.stick, 1);
			break;

		case CraftTable:
			st = new ItemStack(Items.craftTable, 1);
			break;
		case Pickaxe: 
			st = new ItemStack(Items.pickaxe, 1);
			break;
			
		case CampFire:
			st = new ItemStack(Items.campfire, 1);
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

	//	public static void craftCraftTable(Player player){
	//		ItemStack[] input = new ItemStack[]{new ItemStack(Items.log, 1), new ItemStack(Items.stick, 8)};
	//		ItemStack result = new ItemStack(Items.craftTable, 1);
	//		boolean flag[] = new boolean[]{false,false};
	//
	//		if(player.hasStack(input[0])){
	//			if(player.hasStack(input[1])){
	//				for(int it = 0; it < input.length; it++){
	//					for(int i = 0; i < player.getInventory().getItems().length; i++){
	//						if(player.getStackInSlot(i) != null){
	//							if(player.getStackInSlot(i).getItem().equals(input[it].getItem())){
	//								if(player.getStackInSlot(i).stackSize >= input[it].stackSize){
	//									flag[it] = true;
	//								}
	//							}
	//						}
	//					}
	//				}
	//			}
	//		}
	//
	//		if(flag[0] && flag[1]){
	//			player.setStackInNextAvailableSlot(result);
	//			for(int a = 0; a < input.length; a++){
	//				int i = player.getInventory().getSlotForStack(input[a]);
	//				player.getItems()[i].stackSize -= input[a].stackSize;
	//				if(player.getItems()[i].stackSize == 0)
	//					player.getItems()[i] = null;
	//			}
	//		}
	//	}
}