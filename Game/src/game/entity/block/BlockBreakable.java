package game.entity.block;

import game.World;
import game.content.save.DataTag;
import game.entity.MapObject;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.tool.ItemTool;

import java.awt.Color;
import java.awt.Graphics2D;

import base.tilemap.TileMap;

public class BlockBreakable extends Block{

	private int health;
	private boolean jiggle;
	private int effectiveTool;

	int tracker = 0;

	public BlockBreakable(TileMap tm, World world, String uin, int effectiveTool) {
		super(tm, world, uin);
		health = getHealth();
		this.effectiveTool = effectiveTool;
	}

	public int getHealth(){
		return 1;
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
	public void interact(Player p, MapObject mo) {

		jiggle = true;

		int wepDmg = 0;
		ItemStack wep = world.getPlayer().invArmor.getWeapon();
		if(wep != null && effectiveTool == ((ItemTool)wep.getItem()).getEffectiveness())
			wepDmg = ((ItemTool)wep.getItem()).getEffectiveDamage();

		health -= world.getPlayer().getAttackDamage() + wepDmg;
		if(health <= 0)
			mine(p);

	}

	protected void mine(Player p){
		if(getDrop() != null){
			if(p.getInventory().setStackInNextAvailableSlot(getDrop()))
				remove = true;
			else {
				remove = false;
				health = 3;
			}
		}else{
			health = 3;
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
