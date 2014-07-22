package game;

import game.content.Images;
import game.content.save.DataList;
import game.content.save.DataTag;
import game.content.save.Save;
import game.entity.MapObject;
import game.entity.block.Blocks;
import game.entity.living.player.Player;
import game.gui.Gui;
import game.gui.GuiHud;
import game.gui.GuiPause;
import game.gui.GuiPlayerInventory;

import java.awt.Color;
import java.awt.Graphics2D;
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

	private String worldPath = "/maps/cave_rand.map";

	public ArrayList<MapObject> listWithMapObjects ;

	public boolean isDisplayingGui;
	public Gui guiDisplaying;

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

		tileMap.draw(g);

		for(MapObject obj : listWithMapObjects){
			obj.draw(g);
		}

		player.draw(g);

		if(isDisplayingGui && guiDisplaying != null){
			guiDisplaying.draw(g);
		}

	}

	@Override
	public void update(){

		if(backGrounds != null && !backGrounds.isEmpty())
			for(Background bg : backGrounds)
				bg.update();

		handleInput();

		tileMap.setPosition((GamePanel.WIDTH / 2) - player.getScreenXpos(),(GamePanel.HEIGHT / 2) - player.getScreenYpos());

		if(!(isDisplayingGui && guiDisplaying != null && guiDisplaying.pausesGame())){
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

		if(KeyHandler.isPressed(KeyHandler.U) && guiDisplaying instanceof GuiHud && !(guiDisplaying instanceof GuiPlayerInventory)){
			displayGui(new GuiPlayerInventory(this, player));
		}

		if (player.getHealth() == 0)
			return;

		if(guiDisplaying != null && guiDisplaying.pausesGame()){
			guiDisplaying.handleGuiKeyInput();
			return;
		}

		player.handleInput();
		
		if (KeyHandler.isPressed(KeyHandler.B))
			showBB = showBB ? false :true;

	}

	public Player getPlayer(){
		return player;
	}

	public void writeToSave(DataTag tag){

		tag.writeString("map", worldPath);

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

		DataList list = tag.readList("content");
		for(int i = 0; i < list.data().size(); i ++){
			DataTag dt = list.readArray(i);
			String uin = dt.readString("UIN");
			MapObject mo = Blocks.loadMapObjectFromString(uin, tileMap, this);
			mo.readFromSave(dt);
			listWithMapObjects.add(mo);
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
