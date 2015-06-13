package game.item.crafting;

import game.item.ItemStack;
import game.item.Items;

public class OvenRecipes {

	public static final int Iron = 0;
	
	
	public static ItemStack getRecipe(ItemStack input){
		
		if(input.getItem().getUIN().equals(Items.iron.getUIN())){
			return new ItemStack(Items.ingot,1);
		}
		else if(input.getItem().getUIN().equals(Items.meat_pig_raw.getUIN()))
		{
			return new ItemStack(Items.meat_pig, 1);
		}
		
		return null;
	}
	
}
