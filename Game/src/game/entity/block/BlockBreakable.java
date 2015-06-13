package game.entity.block;

import game.World;
import game.content.save.DataTag;
import game.entity.MapObject;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.tool.ItemTool;

import java.awt.Color;
import java.awt.Graphics2D;

import base.main.music.Music;
import base.tilemap.TileMap;

public class BlockBreakable extends Block{

	private int health;
	private boolean jiggle;
	private int effectiveTool;

	int tracker = 0;

	public BlockBreakable(TileMap tm, World world, String uin) {
		super(tm, world, uin);
	}

	public BlockBreakable(TileMap tm, World world, String uin, int toolEffectiveness) {
		this(tm, world, uin);
		setEffectiveTool(toolEffectiveness);
	}

	public BlockBreakable setEffectiveTool(int tool){
		effectiveTool = tool;
		return this;
	}

	public int getEffectiveTool(){
		return effectiveTool;
	}

	private int defaultHealth;
	
	public BlockBreakable setHealth(int i){
		health = i;
		defaultHealth = i;
		return this;
	}

	public void resetHealth(){
		health = defaultHealth;
	}
	
	public int getHealth(){
		return health;
	}

	@Override
	public void draw(Graphics2D g) {

		setMapPosition();

		if(jiggle){
			tracker ++;
			if(tracker < 2)
				xmap +=4;
			else if(tracker < 4)
				xmap-=4;
			else {
				jiggle = false;
				tracker = 0;
			}
		}
		if (facingRight)
			g.drawImage(getAnimation().getImage(),
					(int) ((xScreen + xmap) - (width / 2)),
					(int) ((yScreen + ymap) - (height / 2)), null);
		else
			g.drawImage(getAnimation().getImage(),
					(int) (((xScreen + xmap) - (width / 2)) + width),
					(int) ((yScreen + ymap) - (height / 2)), -width, height, null);

		if (getWorld().showBB) {
			g.setColor(Color.WHITE);
			g.draw(getRectangle());
		}
	}

	@Override
	public void onEntityHit(Player p, MapObject mo) {

		jiggle = true;

		int wepDmg = 0;
		ItemStack wep = world.getPlayer().invArmor.getWeapon();
		if(wep != null && effectiveTool == ((ItemTool)wep.getItem()).getEffectiveness())
			wepDmg = ((ItemTool)wep.getItem()).getEffectiveDamage();

		switch (getType()) {
		case ROCK:
			Music.play("hit_rock_" + (rand.nextInt(4)+1));
			break;
		case WOOD:
			Music.play("hit_wood_" + (rand.nextInt(5)+1));
			break;
		default:
			break;
		}
		health -= world.getPlayer().getAttackDamage() + wepDmg;
		//TODO add check so blocks might not be broken without specific tool
		//boolean check requiresTool ?

//		System.out.println(health);
		if(health <= 0)
			mine(p);

	}

	protected void mine(Player p){
		if(getDrop() != null){
			if(p.getInventory().setStackInNextAvailableSlot(getDrop()))
				remove = true;
			else {
				remove = false;
				health = defaultHealth;
			}
		}else{
			health = defaultHealth;
		}
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);
		data.writeInt("punchHealth", health);

	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);
		health = data.readInt("punchHealth");
	}
}
