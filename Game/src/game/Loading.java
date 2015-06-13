package game;

import game.content.save.DataTag;
import game.content.save.Save;
import game.entity.Entity;
import game.entity.block.BlockBreakable;
import game.entity.block.BlockLog;
import game.entity.block.Blocks;
import game.entity.living.EntityLiving;

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

		//int i = new Random().nextInt(5)+1;

		//skip map x_1, so that map is used only in the very beginning
		// 7+ 2 is max 9 maps, where map min is 2
		int i = new Random().nextInt(7)+2; 
		String s = "/maps/cave_rand_" + i + ".map";
		System.out.println(s);
		return s;
	}

	public static void gotoNextLevel(GameStateManager gsm){

		//save world we are currently in
		World cw = (World)gsm.getGameState(gsm.getCurrentState());
		Save.writeWorld(cw, index);
		Save.writePlayerData(cw.getPlayer());

		//get gametime to transfer to the new world and continue counting
		int time = cw.GameTime;

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
			generateRandomOre(nw, Blocks.IRON, 3);
			generateRandomOre(nw, Blocks.ROCK, 10);
			populateEntities(nw, Entity.PIG, 10);
			//set gametime to continue counting
			nw.GameTime = time;

		}else{
			nw.readFromSave(Save.getWorldData(index));
			//set gametime to continue counting
			nw.GameTime = time;
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

		//get gametime to transfer to the new world and continue counting
		int time = cw.GameTime;

		//set a new world
		gsm.setState(GameStateManager.GAME);

		//increase index to indicate the new world's index
		index--;
		World nw = (World)gsm.getGameState(gsm.getCurrentState());

		nw.readFromSave(Save.getWorldData(index));

		//set gametime to continue counting
		nw.GameTime = time;
		
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

	private static void generateRandomOre(World world, String block, int loops){
		TileMap tm = world.tileMap;

		for(int i = 0; i < loops; i++){
			BlockBreakable b = (BlockBreakable) Blocks.loadMapObjectFromString(block, tm, world);

			int x = new Random().nextInt(tm.getXRows());
			int y = new Random().nextInt(tm.getYRows());

			if(y+1 < tm.getYRows())
				if(world.tileMap.getBlockID(x, y) == 0){
					if(world.tileMap.getBlockID(x, y+1) > 0){
						b.setPosition(x*32 + 16, y*32 + 16);
						world.listWithMapObjects.add(b);
					}
				}
		}
	}

	private static void populateEntities(World world, String uin, int loops){

		TileMap tm = world.tileMap;

		for(int i = 0; i < loops; i++){
			EntityLiving el = (EntityLiving) Entity.createEntityFromUIN(uin, tm, world);

			int x = new Random().nextInt(tm.getXRows());
			int y = new Random().nextInt(tm.getYRows());

			if(y+1 < tm.getYRows())
				if(world.tileMap.getBlockID(x, y) == 0){
					if(world.tileMap.getBlockID(x, y+1) > 0){
						el.setPosition(x*32 + 16, y*32 + 16);
						world.listWithMapObjects.add(el);
						System.out.println("added pig at " + x + " " + (y));

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

}
