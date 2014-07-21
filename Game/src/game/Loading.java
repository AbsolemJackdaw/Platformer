package game;

import game.content.save.DataTag;
import game.content.save.Save;
import game.entity.MapObject;
import game.entity.block.BlockLog;

import java.util.Random;

import base.main.GameStateManager;
import base.tilemap.TileMap;

public class Loading {

	//both ints are saved in World
	/**always increases. tracks the number of unlocked maps*/
	public static int maps = 1;

	/**increases and decreases. tracks the player's current map*/
	public static int index = 0;

	public static String newMap(){

		int i = new Random().nextInt(2);

		switch (i) {
		case 0:
			return "/maps/cave_rand.map";

		case 1 :
			return "/maps/cave_rand_2.map";

		}

		return "/maps/cave_rand.map";
	}

	public static void gotoNextLevel(GameStateManager gsm){

		//save world we are currently in
		World cw = (World)gsm.getGameState(gsm.getCurrentState());
		Save.writeWorld(cw, index);
		Save.writePlayerData(cw.getPlayer());

		//set a new world
		gsm.setState(GameStateManager.GAME);

		//increase index to indicate the new world's index
		index++;
		World nw = (World)gsm.getGameState(gsm.getCurrentState());

		//if its a new map
		if(index == maps){
			String s = newMap();
			nw.reloadMap(s);
			maps++;
			generateRandomTree(nw);
		}else{
			nw.readFromSave(Save.getWorldData(index));
		}
		
		Save.writeRandomParts();
		
		for(int i = 0; i < nw.tileMap.getXRows(); i++)
			for(int j = 0; j < nw.tileMap.getYRows(); j++){
				if(nw.tileMap.getBlockID(i, j) == 7)
					nw.getPlayer().setPosition(i*32 + 32+16, j*32);
			}

	}

	public static void gotoPreviousLevel(GameStateManager gsm){
		
		World cw = (World)gsm.getGameState(gsm.getCurrentState());
		
		if(index == 0){
			cw.getPlayer().setVector(4, 0);
			return;
		}
		
		//save world we are currently in
		Save.writeWorld(cw, index);
		Save.writePlayerData(cw.getPlayer());

		//set a new world
		gsm.setState(GameStateManager.GAME);

		//increase index to indicate the new world's index
		index--;
		World nw = (World)gsm.getGameState(gsm.getCurrentState());

		nw.readFromSave(Save.getWorldData(index));

		for(int i = 0; i < nw.tileMap.getXRows(); i++)
			for(int j = 0; j < nw.tileMap.getYRows(); j++){
				if(nw.tileMap.getBlockID(i, j) == 6){
					nw.getPlayer().setPosition(i*32 - 16, j*32);
					nw.getPlayer().facingRight = false;
				}
			}
		Save.writeRandomParts();
	}
	
	public static void startAtLastSavedLevel(GameStateManager gsm){
		World nw = (World)gsm.getGameState(gsm.getCurrentState());
		try {
			nw.readFromSave(Save.getWorldData(index));
		} catch (Exception e) {
			System.out.println("Savefiles not found. Starting new world.");
		}
	}

	private static void generateRandomTree(World world){
		BlockLog b = null;

		TileMap tm = world.tileMap;

		for(int numTrees = 0; numTrees < 4; numTrees++){

			int x = new Random().nextInt(tm.getXRows());
			int y = new Random().nextInt(tm.getYRows());

			for(int numLogs = 0; numLogs < 3; numLogs++){

				if(y - numLogs > 0)
					if(world.tileMap.isAir(x, (y-numLogs))){
						b = new BlockLog(tm, world);
						b.setPosition(x*32+16, (y-numLogs)*32+16);
						world.listWithMapObjects.add(b);
						System.out.println("added block at " + x + " " + (y-numLogs));
					}
			}
		}

	}
	
	
	public static void writeRandomParts(DataTag tag){
		tag.writeInt("worldIndex", Loading.index);
		tag.writeInt("mapNumber", Loading.maps);
	}
	
	public static void readRandomParts(DataTag tag){
		index = tag.readInt("worldIndex");
		maps = tag.readInt("mapNumber");
	}

	
	public static final String LOG = "log";

	public static MapObject loadMapObjectFromString(String uin, TileMap tm, World w){
		switch (uin) {
		case LOG:
			return new BlockLog(tm, w);

		default:
			break;
		}
		return null;
	}
}
