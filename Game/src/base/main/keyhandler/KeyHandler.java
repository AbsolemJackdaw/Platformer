package base.main.keyhandler;

import java.awt.event.KeyEvent;

public class KeyHandler {
	public static final int NUM_KEYS = 16;

	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];

	public static int UP = 0;
	public static int LEFT = 1;
	public static int RIGHT = 2;
	public static int SPACE = 3;
	public static int ENTER = 4;
	public static int DOWN = 5;
	public static int G = 6;
	public static int U = 7;
	public static int B = 8;
	public static int ESCAPE = 9;
	
	public static int ONE = 10;

	public static boolean anyKeyPress() {
		for (int i = 0; i < NUM_KEYS; i++)
			if (keyState[i])
				return true;
		return false;
	}

	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}

	public static boolean isValidationKeyPressed(){
		return (keyState[SPACE] && !prevKeyState[SPACE]) || (keyState[ENTER] && !prevKeyState[ENTER]);
	}

	public static void keySet(int i, boolean b) {
		if (i == KeyEvent.VK_UP)
			keyState[UP] = b;
		else if (i == KeyEvent.VK_LEFT)
			keyState[LEFT] = b;
		else if (i == KeyEvent.VK_RIGHT)
			keyState[RIGHT] = b;
		else if (i == KeyEvent.VK_SPACE)
			keyState[SPACE] = b;
		else if (i == KeyEvent.VK_ENTER)
			keyState[ENTER] = b;
		else if (i == KeyEvent.VK_DOWN)
			keyState[DOWN] = b;
		else if (i == KeyEvent.VK_ESCAPE)
			keyState[ESCAPE] = b;

		else if (i == KeyEvent.VK_G)
			keyState[G] = b;

		else if (i == KeyEvent.VK_U)
			keyState[U] = b;

		else if (i == KeyEvent.VK_B)
			keyState[B] = b;
		
		else if (i == KeyEvent.VK_1){
			keyState[ONE] = b;
		}

	}

	public static void update() {
		for (int i = 0; i < NUM_KEYS; i++)
			prevKeyState[i] = keyState[i];
	}
}
