package game.item.tool;

import game.item.Item;

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

	public int getAttackDamage(){
		return attackDamage;
	}

	/**used to calculate damage against entities*/
	public int getEffectiveness(){
		return effectiveness;
	}

	public ItemTool setEffectiveness(int i){
		this.effectiveness = i;
		return this;
	}
	/**used for breaking blocks*/
	public int getEffectiveDamage() {
		return effectiveDamage;
	}

	public ItemTool setEffectiveDamage(int i) {
		this.effectiveDamage = i;
		return this;
	}

}
