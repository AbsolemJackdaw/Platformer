package base.tilemap;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import base.main.GamePanel;


public class TileMap {

	// position
	private double x;
	private double y;

	// bounds
	private int xColMin;
	private int yColMin;
	private int xColMax;
	private int yColMax;

	private double tween;

	// map
	/** map has Y stored first. then X */
	private int[][] map;
	/**the image size of the tiles*/
	private final int tileSize;

	/**the number of y rows that are in the map*/
	private int mapYRows;
	/**the number of x rows that are in the map*/
	private int mapXRows;

	/**the total map width : mapXRows* tileSize*/
	private int mapWidth;
	/**the total map width : mapYRows* tileSize*/
	private int mapHeight;

	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;

	// drawing
	private int rowOffset;
	private int colOffset;
	private final int numRowsToDraw;
	private final int numColsToDraw;

	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = (GamePanel.HEIGHT / tileSize) + 2;
		numColsToDraw = (GamePanel.WIDTH / tileSize) + 2;
		tween = 0.05;
	}

	/**x, y being the coords of the block in rows and collumns*/
	public int getBlockID(int x, int y){
		return map[y][x];
	}

	public void draw(Graphics2D g) {

		for (int row = rowOffset; row < (rowOffset + numRowsToDraw); row++) {

			if (row >= mapYRows)
				break;

			for (int col = colOffset; col < (colOffset + numColsToDraw); col++) {

				if (col >= mapXRows)
					break;

				if (map[row][col] == 0)
					continue;

				final int rc = map[row][col];
				final int r = rc / numTilesAcross;
				final int c = rc % numTilesAcross;
//				System.out.println(r + " " + c + " " + numTilesAcross + " " + rc/numTilesAcross);
				g.drawImage(tiles[r][c].getImage(), (int) x + (col * tileSize),
						(int) y + (row * tileSize), null);
			}
		}
	}

	private void fixBounds() {
		if (x < xColMin)
			x = xColMin;
		if (y < yColMin)
			y = yColMin;
		if (x > xColMax)
			x = xColMax;
		if (y > yColMax)
			y = yColMax;
	}

	public int getHeight() {
		return mapHeight;
	}

	/** collums = x */
	public int getXRows() {
		return mapXRows;
	}

	/** Rows = y */
	public int getYRows() {
		return mapYRows;
	}

	public int getTileSize() {
		return tileSize;
	}

	/**x and y are the rows of the map. returns either 0 or 1, for solid or ghost blocks*/
	public int getType(int y, int x) {
		final int rc = map[y][x];
		final int r = rc / numTilesAcross;
		final int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}

	public int getWidth() {
		return mapWidth;
	}

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public boolean isAir(int x, int y) {
	
		if(x >= mapXRows || y >= mapYRows || x < 0 || y < 0){
			System.out.println("coords " + x +" " + y + " did not exist");
			return false;
		}

		if(map[y][x] == 0)
			return true;	
		
		return false;
	}

	public boolean isDangerTile(int x, int y) {

		return false;
	}

	public void loadMap(String s) {

		System.out.println("CLASSPATH = " + s);

		try {

			final InputStream in = getClass().getResourceAsStream(s);
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));

			mapXRows = Integer.parseInt(br.readLine());
			mapYRows = Integer.parseInt(br.readLine());
			map = new int[mapYRows][mapXRows];
			mapWidth = mapXRows * tileSize;
			mapHeight = mapYRows * tileSize;

			xColMin = GamePanel.WIDTH - mapWidth;
			xColMax = 0;
			yColMin = GamePanel.HEIGHT - mapHeight;
			yColMax = 0;

			// reading the actual numbers from the map
			final String delims = "\\s+";
			for (int row = 0; row < mapYRows; row++) {
				final String line = br.readLine();
				final String[] tokens = line.split(delims);
				for (int col = 0; col < mapXRows; col++)
					map[row][col] = Integer.parseInt(tokens[col]);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void loadTiles(String s) {

		try {

			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];

			BufferedImage subimage;
			for (int col = 0; col < numTilesAcross; col++) {

				subimage = tileset.getSubimage(col * tileSize, 0, tileSize,
						tileSize);
				tiles[0][col] = new Tile(subimage, Tile.GHOST);

				subimage = tileset.getSubimage(col * tileSize, tileSize,
						tileSize, tileSize);
				tiles[1][col] = new Tile(subimage, Tile.SOLID);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void setPosition(double x, double y) {

		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;

		fixBounds();

		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;

	}

	public void setTween(double d) {
		tween = d;
	}
}
