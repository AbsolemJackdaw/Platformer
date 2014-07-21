package game.entity;


import game.World;
import game.content.Images;
import game.content.save.DataTag;
import game.entity.living.player.Player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import base.main.GamePanel;
import base.tilemap.Tile;
import base.tilemap.TileMap;

public abstract class MapObject {

	// tile stuff
	protected TileMap tileMap;

	protected int tileSize;
	/**pos x on the map*/
	protected double xmap;
	/**pos y on the map*/
	protected double ymap;

	// position and vector
	/**pos x on screen*/
	protected double xScreen;
	/**pos y on screen*/
	protected double yScreen;
	/**direction x. used for updating position*/
	protected double dx;
	/**direction y. used for updating position*/
	protected double dy;

	// dimensions
	/**texture width*/
	protected int width;
	/**texture height*/
	protected int height;

	// collision box
	/**the object's size, x*/
	protected int entitySizeX;
	/**the object's size, y*/
	protected int entitySizeY;

	// collision
	/**y row on the map*/
	protected int currRow;
	/**x row on the map*/
	protected int currCol;

	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;

	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;

	protected boolean tileHurtsPlayer = false;

	// animation
	private Animation animation;
	protected int currentAction;
	protected int previousAction;

	public boolean facingRight;

	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;

	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;

	/**used for flying entities. true will skip falling logic*/
	protected boolean ignoreGravity = false;

	protected World world;
	
	private final String UIN;

	/**used to remove objects*/
	public boolean remove;
	// constructor
	public MapObject(TileMap tm, World world, String uin) {
		UIN = uin;
		
		tileMap = tm;
		tileSize = tm.getTileSize();
		this.world = world;
		currentAction = -1;

		if(hasAnimation()){
			animation = new Animation();
		}else{
			animation = new Animation();
			BufferedImage[] bi = new BufferedImage[]{getEntityTexture()};
			animation.setFrames(bi);
			animation.setDelay(Animation.NONE);
		}
	}

	public String getUin(){
		return UIN;
	}
	
	public Animation getAnimation(){
		return animation;
	}
	
	/**if this returns false, make sure you override getEntityTexture to set a non-animated texture*/
	public boolean hasAnimation(){
		return false;
	}
	
	public BufferedImage getEntityTexture(){
		return Images.instance.defaultAnim[0];
	}

	/**processes the surounding tiles so the entity can update it's falling/walking logic*/
	public void calculateCorners(double x, double y) {

		final int leftTile = (int) (x - (entitySizeX / 2)) / tileSize;
		final int rightTile = (int) ((x + (entitySizeX / 2)) - 1) / tileSize;
		final int topTile = (int) (y - (entitySizeY / 2)) / tileSize;
		final int bottomTile = (int) ((y + (entitySizeY / 2)) - 1) / tileSize;

		final int tl = tileMap.getType(topTile, leftTile);
		final int tr = tileMap.getType(topTile, rightTile);
		final int bl = tileMap.getType(bottomTile, leftTile);
		final int br = tileMap.getType(bottomTile, rightTile);

		topLeft = tl == Tile.SOLID;
		topRight = tr == Tile.SOLID;
		bottomLeft = bl == Tile.SOLID;
		bottomRight = br == Tile.SOLID;

	}

	public void checkTileMapCollision() {

		currCol = (int) xScreen / tileSize;
		currRow = (int) yScreen / tileSize;

		xdest = xScreen + dx;
		ydest = yScreen + dy;

		xtemp = xScreen;
		ytemp = yScreen;

		calculateCorners(xScreen, ydest);
		if (dy < 0)
			if (topLeft || topRight) {
				dy = 0;
				ytemp = (currRow * tileSize) + (entitySizeY / 2);

			} else
				ytemp += dy;
		if (dy > 0)
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = ((currRow + 1) * tileSize) - (entitySizeY / 2);
			} else
				ytemp += dy;

		calculateCorners(xdest, yScreen);
		if (dx < 0)
			if (topLeft || bottomLeft) {
				dx = 0;
				xtemp = (currCol * tileSize) + (entitySizeX / 2);
			} else
				xtemp += dx;
		if (dx > 0)
			if (topRight || bottomRight) {
				dx = 0;
				xtemp = ((currCol + 1) * tileSize) - (entitySizeX / 2);
			} else
				xtemp += dx;

		if (!falling) {
			calculateCorners(xScreen, ydest + 1);
			if (!bottomLeft && !bottomRight)
				falling = true;
		}
	}

	/**Draws the object with the default animation*/
	public void draw(Graphics2D g) {
		drawSprite(g, animation);
	}

	/**Draws the object with given animation*/
	public void draw(Graphics2D g, Animation anim) {
		drawSprite(g, anim);
	}

	/**
	 * draw the entity's sprite
	 * */
	private void drawSprite(Graphics2D g, Animation am) {

		setMapPosition();

		// draw

		if (facingRight)
			g.drawImage(am.getImage(),
					(int) ((xScreen + xmap) - (width / 2)),
					(int) ((yScreen + ymap) - (height / 2)), null);
		else
			g.drawImage(am.getImage(),
					(int) (((xScreen + xmap) - (width / 2)) + width),
					(int) ((yScreen + ymap) - (height / 2)), -width, height, null);

		if (getWorld().showBB) {
			g.setColor(Color.WHITE);
			g.draw(getRectangle());
		}
	}

	public int getCollisionHeight() {
		return entitySizeY;
	}

	public int getCollisionWidth() {
		return entitySizeX;
	}

	public void getNextPosition() {

		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed)
				dx = -maxSpeed;
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed)
				dx = maxSpeed;
		}

		if (falling && !ignoreGravity)
			dy += fallSpeed;
	}

	public Rectangle getRectangle() {
		return new Rectangle(
				(int) ((xScreen + xmap) - ((entitySizeX) / 2)),
				(int) ((yScreen + ymap) - (entitySizeY / 2)),
				entitySizeX, entitySizeY);
	}

	public int getScreenXpos() {
		return (int) xScreen;
	}

	public int getScreenYpos() {
		return (int) yScreen;
	}

	public int getTextureHeight() {
		return height;
	}

	public int getTextureWidth() {
		return width;
	}

	/** can be null */
	public World getWorld() {
		return world;
	}

	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}

	public boolean notOnScreen() {
		return ((xScreen + xmap + width) < 0)
				|| (((xScreen + xmap) - width) > GamePanel.WIDTH)
				|| ((yScreen + ymap + height) < 0)
				|| (((yScreen + ymap) - height) > GamePanel.HEIGHT);
	}

	public void setDown(boolean b) {
		down = b;
	}

	public void setJumping(boolean b) {
		jumping = b;
	}

	public void setLeft(boolean b) {
		left = b;
	}

	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}

	public void setPosition(double x, double y) {
		this.xScreen = x;
		this.yScreen = y;
	}

	public void setRight(boolean b) {
		right = b;
	}

	public void setUp(boolean b) {
		up = b;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void writeToSave(DataTag data){
		data.writeDouble("xmap", xmap);
		data.writeDouble("ymap", ymap);

		data.writeDouble("xScreen", xScreen);
		data.writeDouble("yScreen", yScreen);

		data.writeDouble("dirX", dx);
		data.writeDouble("dirY", dy);
		
		data.writeString("UIN", UIN);
	}

	public void readFromSave(DataTag data){
		xmap = data.readDouble("xmap");
		ymap = data.readDouble("ymap");

		xScreen = data.readDouble("xScreen");
		yScreen = data.readDouble("yScreen");

		dx = data.readDouble("dirX");
		dy = data.readDouble("dirY");
		
	}

	public void update(){
		updateAnimation();
	}

	protected void updateAnimation(){
		animation.update();
	}
	
	public void interact(Player p, MapObject mo){
		
	}
	
}
