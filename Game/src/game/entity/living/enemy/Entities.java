//package game.entity.living.enemy;
//
//import game.World;
//import game.content.Images;
//import game.entity.MapObject;
//import base.tilemap.TileMap;
//
//public class Entities {
//
//	public static final String ZOMBIE = "zombie";
//	
//	public static MapObject loadEntityFromString(TileMap tm, World world, String s){
//		
//		switch (s) {
//		
//		case ZOMBIE:
//			return new EntityEnemy(tm, world, ZOMBIE).setTexture(Images.loadMultiImage("/entity/zombie.png", 32, 0, 8));
//			
//		default:
//			return null;
//		}
//	}
//}
