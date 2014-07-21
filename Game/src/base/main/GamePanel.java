package base.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import base.main.keyhandler.KeyHandler;


@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions
	public static final int WIDTH = 512;
	public static final int HEIGHT = 300;
	public static float SCALE = 2f;

	public static int SCALEDX;
	public static int SCALEDY;

	// game thread
	private Thread thread;
	private boolean running;
	private final int FPS = 60;
	private final long targetTime = 1000 / FPS;

	// image
	private BufferedImage image;
	private Graphics2D g;

	// game state manager
	private GameStateManager gsm;

	public GamePanel() {
		super();
		float x = WIDTH*SCALE;
		float y = HEIGHT * SCALE;
		SCALEDX = (int)x;
		SCALEDY = (int)y;

		setPreferredSize(new Dimension(SCALEDX, SCALEDY));
		setFocusable(true);
		requestFocus();
		
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	private void draw() {
		gsm.draw(g);
	}

	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, SCALEDX, SCALEDY, null);
		g2.dispose();
	}

	private void init() {

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		running = true;

		gsm = new GameStateManager();

	}

	@Override
	public void keyPressed(KeyEvent key) {
		KeyHandler.keySet(key.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent key) {
		KeyHandler.keySet(key.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent key) {
	}

	@Override
	public void run() {

		init();

		long start;
		long elapsed;
		long wait;

		// game loop
		while (running) {

			start = System.nanoTime();

			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;

			wait = targetTime - (elapsed / 1000000);
			if (wait < 0)
				wait = 5;

			try {
				Thread.sleep(wait);
			} catch (final Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void update() {
		gsm.update();
		KeyHandler.update();
	}
}
