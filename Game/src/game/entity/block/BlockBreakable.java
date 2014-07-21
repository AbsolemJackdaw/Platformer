package game.entity.block;

import game.World;
import game.content.save.DataTag;
import game.entity.MapObject;
import game.entity.living.player.Player;

import java.awt.Color;
import java.awt.Graphics2D;

import base.tilemap.TileMap;

public class BlockBreakable extends Block{

	private int health;
	private boolean jiggle;

	int tracker = 0;

	public BlockBreakable(TileMap tm, World world, String uin) {
		super(tm, world, uin);
		health = getHealth();
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
		health -= world.getPlayer().getAttackDamage();
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
