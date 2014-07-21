package game.content;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import base.tilemap.Background;

public class Images {

	public static Images instance;

	public Images() {
		instance = this;
	}

	public static void init(){
		instance = new Images();
	}
	// for every animation, add the number of frames here
	// sample : 2,5,8,4 (2 for idle 0, 5 for walking 1, etc.
	public static final int[] numFrames = {20, 10, 1, 1, 3 };

	public BufferedImage menu = loadImage("/background/menu.png");
	public Background menuBackGround = new Background("/background/menu.png", 2, false, 5);
	public ArrayList<BufferedImage[]> playerSheet = loadMultiAnimation(numFrames, 32, 32, "/player/player.png");
	
	public BufferedImage[] defaultAnim = loadMultiImage("/images/default.png", 32, 0, 5);

	public BufferedImage log = loadImage("/blocks/log.png");
	
	public static BufferedImage loadImage(String s){
		BufferedImage image = null;

		try {
			image = ImageIO.read(Images.class.getClass().getResourceAsStream(s));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to load " + s + ". Shutting down system.");
			System.exit(0);
		}

		return image;
	}

	public static ArrayList<BufferedImage[]> loadMultiAnimation(int[] frames, int width, int height, String path){
		try {
			ArrayList<BufferedImage[]> sprites;

			final BufferedImage spritesheet = ImageIO.read(Images.class.getClass().getResourceAsStream(path));

			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < frames.length; i++) {

				final BufferedImage[] bi = new BufferedImage[frames[i]];

				for (int j = 0; j < frames[i]; j++)
					bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
				sprites.add(bi);
			}
			return sprites;

		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load " + path + ". Shutting down system.");
			System.exit(0);
		}
		return null;
	}
	
	public static BufferedImage[] loadMultiImage(String s, int x, int y, int subImages) {
		BufferedImage[] ret;
		try {
			final BufferedImage spritesheet = ImageIO.read(Images.class.getResourceAsStream(s));

			ret = new BufferedImage[subImages];

			for (int i = 0; i < subImages; i++)
				ret[i] = spritesheet.getSubimage(i * x, y, x, x);

			System.out.println(s);

			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics." + " " + s + " might be an invalid directory");
			System.exit(0);
		}
		return null;
	}
	
	public static BufferedImage getSimpleImage(String path){
		BufferedImage img = null;
		try {
			img = ImageIO.read(Images.class.getClass().getResourceAsStream(path));
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics." + " " + path + " might be an invalid directory");
			System.exit(0);
		}
		
		return img;
	}
}
