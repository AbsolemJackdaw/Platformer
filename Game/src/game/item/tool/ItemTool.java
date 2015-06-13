package game.item.tool;

import base.tilemap.TileMap;
import game.World;
import game.entity.living.player.Player;
import game.item.Item;
import game.item.ItemStack;

public class ItemTool extends Item {

	private int attackDamage;
	private int effectiveness;
	private int effectiveDamage;

	public static final int NOTHING = 0;
	public static final int PICKAXE = 1;
	public static final int AXE = 2;
	public static final int SWORD = 3;

	public ItemTool(String uin){
		super(uin);
	}

	public ItemTool setAttackDamage(int i){
		attackDamage = i;
		return this;
	}
	/**Used to calculate damage done to entities*/
	public int getAttackDamage(){
		return attackDamage;
	}

	/**Returns the effective type of this tool (pickaxe, axe, sword or none)*/
	public int getEffectiveness(){
		return effectiveness;
	}

	public ItemTool setEffectiveness(int i){
		this.effectiveness = i;
		return this;
	}
	
	/**Used to calculate damage done to blocks*/
	public int getEffectiveDamage() {
		return effectiveDamage;
	}

	public ItemTool setEffectiveDamage(int i) {
		this.effectiveDamage = i;
		return this;
	}

	@Override
	public void useItem(Item item, TileMap map, World world, Player player,int key) {

		ItemStack invCopy = player.getInventory().getStackInSlot(key);
		ItemStack equippedWeapon = player.invArmor.getWeapon();

		ItemStack newInvItem = new ItemStack(equippedWeapon.getItem(), equippedWeapon.stackSize);
		ItemStack newWeapon = new ItemStack(invCopy.getItem(), invCopy.stackSize);

		player.invArmor.setWeapon(newWeapon);
		player.getInventory().setStackInSlot(key, null);
		player.getInventory().setStackInSlot(key, newInvItem);
	
	}
}
