package game;

import game.content.Images;
import game.content.SpawningLogic;
import game.content.save.DataList;
import game.content.save.DataTag;
import game.content.save.Save;
import game.entity.MapObject;
import game.entity.block.Blocks;
import game.entity.living.enemy.Entities;
import game.entity.living.player.Player;
import game.gui.Gui;
import game.gui.GuiHud;
import game.gui.GuiPause;
import game.gui.GuiPlayerInventory;
import game.item.ItemStack;
import game.item.Items;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import base.main.GamePanel;
import base.main.GameState;
import base.main.GameStateManager;
import base.main.keyhandler.KeyHandler;
import base.tilemap.Background;
import base.tilemap.TileMap;

public class World extends GameState{

	private Player player;

	public TileMap tileMap;

	public boolean showBB;

	protected ArrayList<Background> backGrounds;

	private String worldPath = "/maps/cave_rand_1.map";

	public ArrayList<MapObject> listWithMapObjects ;

	public boolean isDisplayingGui;
	public Gui guiDisplaying;

	public static int GameTime = 0;

	public World(GameStateManager gsm) {
		this.gsm = gsm;

		tileMap = new TileMap(32);

		loadWorld();

		player = new Player(tileMap, this);

		if(Save.getPlayerData() != null)
			player.readFromSave(Save.getPlayerData());

		backGrounds = new ArrayList<Background>();
		backGrounds.add(Images.instance.menuBackGround);

		listWithMapObjects = new ArrayList<MapObject>();

		displayGui(new GuiHud(this, player));

	}

	BufferedImage lighting = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
	static float nightAlhpa = 0;

	@Override
	public void draw(Graphics2D g){

		g.setColor(Color.white);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		if(backGrounds != null && !backGrounds.isEmpty())
			for(Background bg : backGrounds)
				bg.draw(g);

		tileMap.draw(g);

		for(MapObject obj : listWithMapObjects){
			obj.draw(g);
		}

		player.draw(g);


		// Creates the buffered image. has to be recreated every time for transparancy
		BufferedImage lighting = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);

		Graphics2D gbi = lighting.createGraphics();
		if(isNightTime()){
			if(nightAlhpa < 0.95f)
				nightAlhpa +=0.0003f;
		}
		else{
			if(nightAlhpa > 0f)
				nightAlhpa -= 0.0005f;
		}

		gbi.setColor(new Color(0f, 0f, 0.1f, nightAlhpa));
		gbi.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);


		System.out.println(GameTime);
		
		Ellipse2D ellipse = null;
		
		//campfire cuts out circle here
		if(nightAlhpa > 0.1f){

			/**changed light to player instead of blocks*/

			//			for(MapObject mo : listWithMapObjects){
			//				if(mo instanceof BlockLight){
			//					BlockLight light = (BlockLight)mo;

			// Draws the circle of light into the buffered image.
			//					for(int i = 0; i < 5; i++){
			//						int i =5;
			//						float f =  0f + (float)i / 10f;
			//						gbi.setColor(new Color(0.0f, 0.0f, 0.0f, f));    
			//						gbi.setComposite(AlphaComposite.DstOut);
			//						gbi.fill(new Ellipse2D.Double((light.posX() + i * 5) - (light.getRadius()/2 - 32/2), (light.posY() + i * 5) - (light.getRadius()/2 - 32/2), light.getRadius() - i * 10, light.getRadius() - i *10));
			//					}

			int i =5;
			
			float f =  0f + (float)i / 10f;
			
			int x =(player.posX() + i * 5) - (player.getRadius()/2 - 32/2);
			int y = (player.posY() + i * 5) - (player.getRadius()/2 - 32/2);
			int h = player.getRadius() - i * 10;
			int w = player.getRadius() - i *10;
			
			float scale = 15f;
			for(float i1 = 0; i1 <5; i1++){
				gbi.setColor(new Color(0f, 0f, 0f, f));    
				gbi.setComposite(AlphaComposite.DstOut);
				ellipse = new Ellipse2D.Double(x+(i1*(scale/2f)),y+(i1*(scale/2f)),h-(i1*scale),w-(i1*scale));
				gbi.fill(ellipse);
			}
		}

		// Draws the buffered image.
		g.drawImage(lighting, 0,0, null);

		if(isDisplayingGui && guiDisplaying != null){
			guiDisplaying.draw(g);
		}
	}

	public boolean isNightTime(){
		return GameTime > 18000;
	}

	public int getGameTime(){
		return GameTime;
	}

	@Override
	public void update(){

		if(backGrounds != null && !backGrounds.isEmpty())
			for(Background bg : backGrounds)
				bg.update();

		handleInput();

		tileMap.setPosition((GamePanel.WIDTH / 2) - player.getScreenXpos(),(GamePanel.HEIGHT / 2) - player.getScreenYpos());

		if(!(isDisplayingGui && guiDisplaying != null && guiDisplaying.pausesGame())){

//			TODO put time back into game, plus lighting logic
//			GameTime++;

//			if(GameTime >= 32000){
//				GameTime = 0;
//			}

			if(isNightTime())
				SpawningLogic.spawnNightCreatures(this);

			player.update();

			for(MapObject obj : listWithMapObjects){
				obj.update();
				if(obj.remove){
					listWithMapObjects.remove(obj);
					break;
				}
			}
		}
	}

	private void loadWorld() {
		tileMap.loadTiles(getTileTexture());
		tileMap.loadMap(worldPath);
		tileMap.setPosition(0, 0);
	}

	public void reloadMap(String s){
		tileMap.loadMap(s);
		worldPath = s;
	}

	public String getTileTexture(){
		return "/maps/platformer_tiles.png";
	}

	public void handleInput() {

		if(KeyHandler.isPressed(KeyHandler.ESCAPE) && guiDisplaying instanceof GuiHud && !(guiDisplaying instanceof GuiPause)){
			displayGui(new GuiPause(this, player));
		}

		if(KeyHandler.isPressed(KeyHandler.INVENTORY) && guiDisplaying instanceof GuiHud && !(guiDisplaying instanceof GuiPlayerInventory)){
			displayGui(new GuiPlayerInventory(this, player));
		}

		if (player.getHealth() == 0)
			return;

		if(guiDisplaying != null && guiDisplaying.pausesGame()){
			guiDisplaying.handleGuiKeyInput();
			return;
		}

		player.handleInput();

		if (KeyHandler.isPressed(KeyHandler.B)){
			showBB = showBB ? false :true;
			player.setStackInNextAvailableSlot(new ItemStack(Items.campfire, 5));
		}

	}

	public Player getPlayer(){
		return player;
	}

	public void writeToSave(DataTag tag){

		tag.writeString("map", worldPath);

		tag.writeInt("gametime", GameTime);
		tag.writeFloat("nightshade", new Float(nightAlhpa));

		DataList list = new DataList();
		for(MapObject mo : listWithMapObjects){
			DataTag dt = new DataTag();
			mo.writeToSave(dt);
			list.write(dt);
		}
		tag.writeList("content", list);
	}

	public void readFromSave(DataTag tag){

		reloadMap(tag.readString("map"));
		GameTime = tag.readInt("gametime");
		nightAlhpa = tag.readFloat("nightshade");

		DataList list = tag.readList("content");
		for(int i = 0; i < list.data().size(); i ++){
			DataTag dt = list.readArray(i);
			String uin = dt.readString("UIN");

			MapObject mo = Blocks.loadMapObjectFromString(uin, tileMap, this);

			if(mo == null)
				mo = Entities.loadEntityFromString(tileMap, this, uin);
			if(mo != null){
				mo.readFromSave(dt);
				listWithMapObjects.add(mo);
			}else{
				System.out.println("The Entity for " + uin + " was not recognized. Skipped loading this entity");
			}
		}
	}

	public void displayGui(Gui gui){
		if(gui != null){
			isDisplayingGui = true;
			guiDisplaying = gui;
		}
		else{
			isDisplayingGui = false;
			guiDisplaying = null;
			displayGui(new GuiHud(this, player));
		}
	}
}
