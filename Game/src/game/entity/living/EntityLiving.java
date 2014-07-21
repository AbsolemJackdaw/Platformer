package game.entity.living;

import base.tilemap.TileMap;
import game.World;
import game.content.save.DataTag;
import game.entity.MapObject;

public class EntityLiving extends MapObject{

	protected int health;
	
	public EntityLiving(TileMap tm, World world, String uin) {
		super(tm, world, uin);
	
		health = 1;
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
