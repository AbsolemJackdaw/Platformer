package game.entity;

import game.World;
import game.entity.living.EntityLiving;
import game.entity.living.EntityPig;
import base.tilemap.TileMap;

public class Entity {

	public static final String PIG = "pig";

	public static EntityLiving createEntityFromUIN(String s, TileMap tm, World world){
		
		switch (s) {
		case PIG:
			return new EntityPig(tm, world, PIG);
		}
		return null;
	}
}
