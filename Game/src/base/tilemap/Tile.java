package base.tilemap;

import java.awt.image.BufferedImage;

public class Tile {

	/**The image of the tile*/
	private final BufferedImage image;
	
	/**Every Tile has a type. either Solid or Ghost*/
	private final int type;

	// tile types
	public static final int GHOST = 0;
	public static final int SOLID = 1;

	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getType() {
		return type;
	}

}
