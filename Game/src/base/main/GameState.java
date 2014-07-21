package base.main;

import java.awt.Graphics2D;

public abstract class GameState {

	public GameStateManager gsm;

	public abstract void draw(Graphics2D g);

	public abstract void update();

}
