package game.item;

import game.content.save.DataTag;

public class ItemStack
{
	public int stackSize;
	private Item item;
	
	public ItemStack(Item i, int size) {
		item = i;
		stackSize = size;
	}
	
	public Item getItem(){
		return item;
	}
	
	public static ItemStack createFromSave(DataTag data){
		String s = data.readString("uniqueItemName");
		Item item = Items.getItemFromUIN(s);
		int size = data.readInt("stacksize");
		
		item.readFromSave(data);
		ItemStack is = new ItemStack(item, size );
		
		return is;
	}

	public void writeToSave(DataTag data){
		//item name aka unique identifier
		data.writeString("uniqueItemName", item.getUIN());
		data.writeInt("stacksize", stackSize);
		item.writeToSave(data);
	}
}