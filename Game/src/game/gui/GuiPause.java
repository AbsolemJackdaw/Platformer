package game.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import base.main.GamePanel;
import base.main.GameStateManager;
import base.main.keyhandler.KeyHandler;
import game.Loading;
import game.World;
import game.content.save.Save;
import game.entity.living.player.Player;

public class GuiPause extends Gui {

	private Font font = new Font("Century Gothic", Font.PLAIN, 32);
	private Font fontChoices = new Font("Arial", Font.PLAIN, 12);
	private Color color = new Color(250, 231, 217);
	private Color clr = new Color(0xcfd9e7);
	
	private int currentChoice = 0;
	private final String[] options = { "Resume", "Save", "Menu", "Quit" };
	
	public GuiPause(World world, Player p) {
		super(world, p);

	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		g.setFont(font);
		g.setColor(color);
		g.drawString("Pause", GamePanel.WIDTH/2 - 40, GamePanel.HEIGHT/2 - 50);

		// Draw menu square
		Rectangle rect = new Rectangle((GamePanel.WIDTH / 2) - 20, 127 + (currentChoice * 15), 55, 15);
		g.setColor(clr);
		g.draw(rect);

		// draw menu options
		g.setFont(fontChoices);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice)
				g.setColor(Color.BLACK);
			else
				g.setColor(Color.RED);
			g.drawString(options[i], (GamePanel.WIDTH / 2) - 15 + (i > 0 ? 10 : 0), 140 + (i * 15));
		}
	}

	private void select(){
		if (currentChoice == 0){
			world.displayGui(null);
		}
		if (currentChoice == 1)
			saveGame();
		if(currentChoice == 2)
		{
			saveGame();
			world.gsm.setState(world.gsm.MENUSTATE);
		}
		if (currentChoice == 3)
		{
			saveGame();
			System.exit(0);
		}
	}
	
	@Override
	public void handleGuiKeyInput() {
		if (KeyHandler.isPressed(KeyHandler.ENTER))
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
	
	private void saveGame()
	{
		Save.writePlayerData(player);
		Save.writeRandomParts();
		Save.writeWorld((World)world.gsm.getGameState(world.gsm.getCurrentState()), Loading.index);
	}
	
	@Override
	public boolean pausesGame() {
		return true;
	}
}
