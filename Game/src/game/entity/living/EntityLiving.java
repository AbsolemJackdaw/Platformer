package game.entity.living;

import game.World;
import game.content.save.DataTag;
import game.entity.MapObject;
import base.tilemap.TileMap;

public class EntityLiving extends MapObject{

	protected int health;
	protected int maxHealth;
	
	public EntityLiving(TileMap tm, World world, String uin) {
		super(tm, world, uin);

		health = 1;
	}

	public EntityLiving setHealth(int health){
		this.health = health;
		maxHealth = health;
		return this;
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);

		data.writeInt("health", health);

	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);

		health = data.readInt("health");
	}

	public int getHealth(){
		return health;
	}
}
