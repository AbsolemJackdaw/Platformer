package base.main.keyhandler;

import java.awt.event.KeyEvent;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class XboxController {

	public static Controller controller;

	public static void init(){

		try {
			Controllers.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Controllers.poll();

		int index = 0;

		for(int i = 0; i < Controllers.getControllerCount(); i ++){
			System.out.println(Controllers.getController(i).getName() + " : index " + i);
			controller = Controllers.getController(i);
			if(controller.getName().toLowerCase().contains("xbox")){
				if(controller.getName().toLowerCase().contains("360")){
					if(controller.getName().toLowerCase().contains("windows")){
						index = i;
						break;
					}
				}
			}
		}

		try {
			controller = Controllers.getController(index);
		} catch (Exception e) {
			System.out.println("No Controller detected");
		}
	}

	public static int A = 0;
	public static int B = 1;
	public static int X = 2;
	public static int Y = 3;
	public static int LS = 4;
	public static int RS = 5;
	public static int BACK = 6;
	public static int START = 7;
	public static int LSTICK = 8;
	public static int RSTICK = 9;

	public static boolean LEFT;
	public static boolean RIGHT;
	public static boolean UP;
	public static boolean DOWN;

	public static void update(){

		controller.poll();

		LEFT = controller.getPovX() == -1f;
		RIGHT = controller.getPovX() == 1f;
		UP = controller.getPovY() == -1f;
		DOWN = controller.getPovY() == 1f;

//		KeyHandler.keySet(KeyEvent.VK_LEFT, LEFT);
//		KeyHandler.keySet(KeyEvent.VK_RIGHT, RIGHT);
//		KeyHandler.keySet(KeyEvent.VK_UP, UP);
//		KeyHandler.keySet(KeyEvent.VK_DOWN, DOWN);
		
		KeyHandler.keySet(KeyEvent.VK_Q, LEFT);
		KeyHandler.keySet(KeyEvent.VK_D, RIGHT);
		KeyHandler.keySet(KeyEvent.VK_Z, UP);
		KeyHandler.keySet(KeyEvent.VK_S, DOWN);

		if(controller.isButtonPressed(X)){
			KeyHandler.keySet(KeyEvent.VK_R, true);
		}else{
			KeyHandler.keySet(KeyEvent.VK_R, false);
		}

		if(controller.isButtonPressed(A)){
			KeyHandler.keySet(KeyEvent.VK_SPACE, true);
		}else{
			KeyHandler.keySet(KeyEvent.VK_SPACE, false);
		}

		if(controller.isButtonPressed(START)){
			KeyHandler.keySet(KeyEvent.VK_ESCAPE, true);
		}else{
			KeyHandler.keySet(KeyEvent.VK_ESCAPE, false);
		}

		if(controller.isButtonPressed(B)){
			KeyHandler.keySet(KeyEvent.VK_N, true);
		}else{
			KeyHandler.keySet(KeyEvent.VK_N, false);
		}

		if(controller.isButtonPressed(BACK)){
			KeyHandler.keySet(KeyEvent.VK_I, true);
		}else{
			KeyHandler.keySet(KeyEvent.VK_I, false);
		}

		if(controller.isButtonPressed(LS) || controller.isButtonPressed(RS)){
			KeyHandler.keySet(KeyEvent.VK_M, true);
		}else{
			KeyHandler.keySet(KeyEvent.VK_M, false);
		}

		if(controller.isButtonPressed(Y)){
			KeyHandler.keySet(KeyEvent.VK_E, true);
		}else{
			KeyHandler.keySet(KeyEvent.VK_E, false);
		}
	}
}
