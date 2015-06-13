package game;

import game.content.Images;
import game.content.save.Save;
import game.item.Items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import base.main.GamePanel;
import base.main.GameState;
import base.main.GameStateManager;
import base.main.keyhandler.KeyHandler;
import base.tilemap.Background;

public class GameStateMenu extends GameState{

	private Background bg;
	private Font font;
	private Font fontChoices;
	private Color color = new Color(250, 231, 217);
	private Color clr = new Color(0xcfd9e7);

	private int currentChoice = 0;
	private final String[] options = { "Start", "Help", "Quit" };


	public GameStateMenu(GameStateManager gsm) {
		super();
		this.gsm = gsm;

		bg = Images.instance.menuBackGround;
		font = new Font("Constantia", Font.PLAIN, 36);

		fontChoices = new Font("Arial", Font.PLAIN, 12);
		
		Items.loadItems();
	}

	@Override
	public void draw(Graphics2D g){
		bg.draw(g);

		g.setFont(font);
		g.setColor(color);
		g.drawString("The Brim of Life", GamePanel.WIDTH/2 - 110, GamePanel.HEIGHT/2 - 50);

		// Draw menu square
		Rectangle rect = new Rectangle((GamePanel.WIDTH / 2) - 20, 127 + (currentChoice * 15), 35, 15);
		g.setColor(clr);
		g.draw(rect);

		// draw menu options
		g.setFont(fontChoices);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice)
				g.setColor(Color.BLACK);
			else
				g.setColor(color);
			g.drawString(options[i], (GamePanel.WIDTH / 2) - 15, 140 + (i * 15));
		}
	}

	@Override
	public void update() {
		
		if (KeyHandler.isValidationKeyPressed())
			select();
		if (KeyHandler.isPressed(KeyHandler.UP)) {
			currentChoice--;
			if (currentChoice == -1)
				currentChoice = options.length - 1;
		}
		if (KeyHandler.isPressed(KeyHandler.DOWN)) {
			currentChoice++;
			if (currentChoice == options.length)
				currentChoice = 0;
		}
	}

	private void select() {
		if (currentChoice == 0){
			gsm.setState(GameStateManager.GAME);
			Save.readRandomParts();
			Loading.startAtLastSavedLevel(gsm);
			
		}
		if (currentChoice == 1)
			gsm.setState(GameStateManager.HELP);
		if (currentChoice == 2)
			System.exit(0);
	}

}
