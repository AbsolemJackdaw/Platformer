package game.gui;

import game.World;
import game.entity.living.player.Player;

import java.awt.Graphics2D;
import java.util.ArrayList;

import base.main.GamePanel;
import base.main.keyhandler.KeyHandler;
import base.main.keyhandler.XboxController;

public class Gui {

	protected final int centerX = GamePanel.WIDTH/2;
	protected final int centerY = GamePanel.HEIGHT/2;

	protected final Player player;
	protected final World world;

	protected ArrayList<Button> buttonList = new ArrayList<Button>();

	public Gui(World world, Player p) {
		player = p;
		this.world = world;
	}

	public void draw(Graphics2D g){

	}

	public void handleGuiKeyInput() {
		if(KeyHandler.isPressed(KeyHandler.ESCAPE)){
			world.displayGui(null);
		}
		if(KeyHandler.isPressed(KeyHandler.ESCAPE2) && XboxController.controller != null){
			world.displayGui(null);
		}
	}
	
	public boolean pausesGame(){
		return true;
	}

}
