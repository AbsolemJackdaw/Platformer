package base.main.keyhandler;

import java.awt.event.KeyEvent;

public class KeyHandler {
	public static final int NUM_KEYS = 256;

	public static int keyCode;

	public static boolean anyKey;
	public static boolean prevAnyKey;
	
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];

	public static int UP = 0;
	public static int LEFT = 1;
	public static int RIGHT = 2;
	public static int SPACE = 3;
	public static int ENTER = 4;
	public static int DOWN = 5;
	public static int CTRL = 6;
	public static int INVENTORY = 7;
	public static int B = 8;
	public static int ESCAPE = 9;

	public static int ONE = 10;
	public static int TWO = 11;
	public static int THREE = 12;
	public static int FOUR = 13;
	public static int FIVE = 14;
	public static int SIX = 15;
	public static int SEVEN = 16;
	public static int EIGHT = 17;
	public static int NINE = 18;
	public static int PLACE = 19;

	public static int INTERACT = 20;

	public static int ESCAPE2 = 21;

	public static int ANYKEY = 22;

	public static boolean anyKeyPress() {
		for (int i = 0; i < NUM_KEYS; i++)
			if (keyState[i])
				return true;
		return false;
	}

	public static String keyPressed(int i, String string){

		if(keyState[ANYKEY] && !prevKeyState[ANYKEY]){
			String s = KeyEvent.getKeyText(keyCode);
			if(s.length() == 1)
				string +=s;
			if(keyCode == KeyEvent.VK_BACK_SPACE && string.length() > 0){
				String s2 = string.substring(0, string.length()-1);
				string = s2;
			}
			if(keyCode == KeyEvent.VK_SPACE)
				string+=" ";
			
			System.out.println("key typed "+s);
			System.out.println("string returned " + string);
		}
		return string.toLowerCase();
	}

	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i] ;
	}

	public static boolean isReleased(int i) {
		return !keyState[i] && prevKeyState[i];
	}


	public static boolean isValidationKeyPressed(){
		return (keyState[SPACE] && !prevKeyState[SPACE]) || (keyState[ENTER] && !prevKeyState[ENTER]);
	}

	public static void keySet(int i, boolean b){

		keyCode = i;

		//		if (i == KeyEvent.VK_UP)
		//			keyState[UP] = b;
		//		else if (i == KeyEvent.VK_LEFT)
		//			keyState[LEFT] = b;
		//		else if (i == KeyEvent.VK_RIGHT)
		//			keyState[RIGHT] = b;
		//		else if (i == KeyEvent.VK_DOWN)
		//			keyState[DOWN] = b;

		if (i == KeyEvent.VK_Z)
			keyState[UP] = b;
		else if (i == KeyEvent.VK_Q)
			keyState[LEFT] = b;
		else if (i == KeyEvent.VK_D)
			keyState[RIGHT] = b;
		else if (i == KeyEvent.VK_S)
			keyState[DOWN] = b;

		else if (i == KeyEvent.VK_SPACE)
			keyState[SPACE] = b;

		else if (i == KeyEvent.VK_ENTER)
			keyState[ENTER] = b;

		else if (i == KeyEvent.VK_ESCAPE)
			keyState[ESCAPE] = b;

		else if (i == KeyEvent.VK_T)
			keyState[CTRL] = b;

		else if (i == KeyEvent.VK_E)
			keyState[INVENTORY] = b;

		else if (i == KeyEvent.VK_B)
			keyState[B] = b;

		else if (i == KeyEvent.VK_1){
			keyState[ONE] = b;
		}
		else if (i == KeyEvent.VK_2){
			keyState[TWO] = b;
		}
		else if (i == KeyEvent.VK_3){
			keyState[THREE] = b;
		}
		else if (i == KeyEvent.VK_4){
			keyState[FOUR] = b;
		}
		else if (i == KeyEvent.VK_5){
			keyState[FIVE] = b;
		}
		else if (i == KeyEvent.VK_6){
			keyState[SIX] = b;
		}
		else if (i == KeyEvent.VK_7){
			keyState[SEVEN] = b;
		}
		else if (i == KeyEvent.VK_8){
			keyState[EIGHT] = b;
		}
		else if (i == KeyEvent.VK_9){
			keyState[NINE] = b;
		}
		else if(i == KeyEvent.VK_F){
			keyState[INTERACT] = b;
		}
		//xbox only.
		else if(i == KeyEvent.VK_M){
			keyState[PLACE] = b;
		}
		else if(i == KeyEvent.VK_N){
			keyState[ESCAPE2] = b;
		}
		
		keyState[ANYKEY] = b;
	}

	public static void update() {
		for (int i = 0; i < NUM_KEYS; i++)
			prevKeyState[i] = keyState[i];
	}
}
