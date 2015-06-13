package base.main;

import game.GameStateMenu;
import game.World;
import game.content.Images;

import java.awt.Color;
import java.awt.Graphics2D;

import base.main.keyhandler.XboxController;
import base.main.music.Music;


public class GameStateManager {

	private final GameState[] gameStates;
	private int currentState;

	public static final int NUMGAMESTATES = 100;

	public static final int DEATH = 0;
	public static final int MENUSTATE = 1;
	public static final int HELP = 2;
	
	public static final int GAME = 3;

	public GameStateManager() {

		Music.init();
		Images.init();
		
		XboxController.init();
		
		gameStates = new GameState[NUMGAMESTATES];

		currentState = MENUSTATE;
		loadState(currentState);
		
	}

	public void draw(Graphics2D g) {
		if (gameStates[currentState] != null)
			gameStates[currentState].draw(g);
		else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	}

	public int getCurrentState() {
		return currentState;
	}

	private void loadState(int state){
		switch (state){
		case MENUSTATE:
			gameStates[state] = new GameStateMenu(this);
			break;
		case GAME:
			gameStates[state] = new World(this);
			break;
		}
	}

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void update() {
		if(XboxController.controller != null)
			XboxController.update();
		
		if (gameStates[currentState] != null)
			gameStates[currentState].update();
		
	}

	public GameState getGameState(int state){
		return gameStates[state];
	}

}