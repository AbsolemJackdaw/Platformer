package game;

import game.content.Images;
import game.content.SpawningLogic;
import game.content.save.DataList;
import game.content.save.DataTag;
import game.content.save.Save;
import game.entity.Entity;
import game.entity.MapObject;
import game.entity.block.BlockLight;
import game.entity.block.Blocks;
import game.entity.living.player.Player;
import game.gui.Gui;
import game.gui.GuiHud;
import game.gui.GuiPause;
import game.gui.GuiPlayerInventory;
import game.item.Item;
import game.item.ItemLantern;
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
import base.main.keyhandler.XboxController;
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

	public int GameTime = 0;

	//	private static final BufferedImage lighting = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
	static float nightAlhpa = 0;

	ItemStack lantern = new ItemStack(Items.lantern,1);

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

	@Override
	public void draw(Graphics2D g){

		g.setColor(Color.white);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		if(backGrounds != null && !backGrounds.isEmpty())
			for(Background bg : backGrounds)
				bg.draw(g);

		tileMap.draw(g, player);


		for(MapObject obj : listWithMapObjects){
			//do not draw entities outside of the player's range
			if(!isOutOfBounds(obj)){
				obj.draw(g);
			}
		}

		player.draw(g);

		// Creates the buffered image. has to be recreated every time for transparancy
		BufferedImage lighting = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);

		Graphics2D gbi = lighting.createGraphics();

		if((guiDisplaying instanceof GuiHud))
			if(isNightTime()){
				if(nightAlhpa < 0.97f)
					nightAlhpa +=0.0003f;
			}
			else{
				if(nightAlhpa > 0f)
					nightAlhpa -= 0.0005f;
			}

		gbi.setColor(new Color(0f, 0f, 0.07f, nightAlhpa));
		gbi.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		Ellipse2D ellipse = null;

		//campfire cuts out circle here
		if(nightAlhpa > 0.1f){

			/**changed light to player instead of blocks*/

//			for(MapObject mo : listWithMapObjects){
//				if(mo instanceof BlockLight){
//
//					if(isOutOfBounds(mo))
//						continue;
//
//					BlockLight light = (BlockLight)mo;
//
//					//Draws the circle of light into the buffered image.
////					for(int i = 0; i < 5; i++){
////						float f =  0f + (float)i / 10f;
////						gbi.setColor(new Color(0.0f, 0.0f, 0.0f, f));    
////						gbi.setComposite(AlphaComposite.DstOut);
////						gbi.fill(new Ellipse2D.Double((light.posX() + i * 5) - (light.getRadius()/2 - 32/2), (light.posY() + i * 5) - (light.getRadius()/2 - 32/2), light.getRadius() - i * 10, light.getRadius() - i *10));
////					}
//					
//					int i = 2;
//					
//					int x =(light.posX() + i * 5) - (light.getRadius()/2 - 32/2);
//					int y = (light.posY() + i * 5) - (light.getRadius()/2 - 32/2);
//					int h = light.getRadius() - i * 10;
//					int w = light.getRadius() - i *10;
//					
//					float scale = 15f;
//					float f =  0f + (float)i / 10f;
//
//					for(float i1 = 0; i1 <5; i1++){
//						gbi.setColor(new Color(0f, 0f, 0f, f));    
//						gbi.setComposite(AlphaComposite.DstOut);
//						ellipse = new Ellipse2D.Double(x+(i1*(scale/2f)),y+(i1*(scale/2f)),h-(i1*scale),w-(i1*scale));
//						gbi.fill(ellipse);
//					}
//				}
//			}
			
			//cut torch light out dynamicly
			for(ItemStack stack : player.getItems())
				if(stack != null)
					if(stack.getItem().equals(Items.lantern)){

						ItemLantern lant = (ItemLantern) stack.getItem();
						if(!lant.isLit())
							break;
						
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

		}
		// Draws the buffered image.
		g.drawImage(lighting, 0,0, null);

		if(isDisplayingGui && guiDisplaying != null){
			guiDisplaying.draw(g);
		}

		if(isConsoleDisplayed){
			g.setColor(new Color(0f,0f,0f,0.5f));
			g.fillRect(10, 10, 250, 25);
			g.setColor(Color.white);
			g.drawString(consolePrint, 25,25);
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

		//		if(guiDisplaying instanceof GuiHud)
		//			System.out.println(GameTime);

		if(backGrounds != null && !backGrounds.isEmpty())
			for(Background bg : backGrounds)
				bg.update();

		handleInput();

		tileMap.setPosition((GamePanel.WIDTH / 2) - player.getScreenXpos(),(GamePanel.HEIGHT / 2) - player.getScreenYpos());

		if((guiDisplaying instanceof GuiHud)){
//			GameTime++;
//
//			if(GameTime >= 32000){
//				GameTime = 0;
//			}
			//TODO put night time back into the game

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

		if(isConsoleDisplayed){
			consoleInput();
			return;
		}

		if(KeyHandler.isPressed(KeyHandler.CTRL)){
			displayConsole();
			return;
		}


		//returns when gui's are closed are to prevent player from jumping
		//right after you press the jump/validation key to select a close option
		if(KeyHandler.isPressed(KeyHandler.ESCAPE) && guiDisplaying instanceof GuiHud && !(guiDisplaying instanceof GuiPause)){
			displayGui(new GuiPause(this, player));
		}else if (guiDisplaying instanceof GuiPause && KeyHandler.isPressed(KeyHandler.ESCAPE) || guiDisplaying instanceof GuiPause && KeyHandler.isPressed(KeyHandler.ESCAPE2) && XboxController.controller != null){
			displayGui(null);
			return;
		}

		if(KeyHandler.isPressed(KeyHandler.INVENTORY) && guiDisplaying instanceof GuiHud && !(guiDisplaying instanceof GuiPlayerInventory)){
			displayGui(new GuiPlayerInventory(this, player));
		}else if (guiDisplaying instanceof GuiPlayerInventory && KeyHandler.isPressed(KeyHandler.INVENTORY) ){
			displayGui(null);
			return;
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
				mo = Entity.createEntityFromUIN(uin, tileMap, this);
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

	private boolean isOutOfBounds(MapObject obj){
		int Px = player.getScreenXpos();
		int Py = player.getScreenYpos();

		int arroundX = 32*16;
		int arroundY = 32*10;

		int someX = Px-arroundX;
		int someXMax = Px+arroundX;

		int someY = Py-arroundY;
		int someYMax = Py+arroundY;

		if(obj.getScreenXpos() >= someX && obj.getScreenXpos() < someXMax)
			if(obj.getScreenYpos() >= someY && obj.getScreenYpos() < someYMax)
				return false;
		return true;
	}

	private boolean isConsoleDisplayed = false;
	private String consolePrint = "";

	private void displayConsole(){
		isConsoleDisplayed = true;
	}

	private void consoleInput(){
		if(KeyHandler.isPressed(KeyHandler.ESCAPE))
			isConsoleDisplayed = false;
		else
			consolePrint = KeyHandler.keyPressed(KeyHandler.ANYKEY, consolePrint);
		if(KeyHandler.isPressed(KeyHandler.ENTER))
			consoleCommands(consolePrint);
	}

	private void consoleCommands(String cmd){
		if(cmd.equals("night"))
			GameTime = 18000;
		else if(cmd.equals("day"))
			GameTime = 0;
		else if(cmd.startsWith("need")){
			String [] split = cmd.split("\\s+") ;
			if(split.length == 3){
				Item item = Items.getItemFromUIN(split[1]);
				if(item != null){
					ItemStack stack = new ItemStack(item, Integer.valueOf(split[2]));
					player.setStackInNextAvailableSlot(stack);
				}
			}
		}
		consolePrint = "";
		isConsoleDisplayed = false;
	}
}
