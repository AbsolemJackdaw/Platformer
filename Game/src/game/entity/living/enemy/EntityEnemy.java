package game.entity.living.enemy;

import game.World;
import game.entity.MapObject;
import game.entity.living.EntityLiving;
import game.entity.living.player.Player;
import game.item.tool.ItemTool;

import java.awt.image.BufferedImage;
import java.util.Random;

import base.tilemap.TileMap;

public class EntityEnemy extends EntityLiving {

	BufferedImage[] entityTexture;

	public EntityEnemy(TileMap tm, World world, String uin) {
		super(tm, world, uin);

		moveSpeed = 0.2; // inital walking speed. you speed up as you walk
		maxSpeed = 0.5; // change to jump farther and walk faster
		stopSpeed = 0.4;
		fallSpeed = 0.085; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		width = 32;
		height = 32;
		entitySizeX = 20;
		entitySizeY = 32;
		right = true;
	}

	public EntityEnemy setTexture(BufferedImage[] texture){
		entityTexture = texture;
		return this;
	}

	@Override
	public void onEntityHit(Player p, MapObject mo) {

		int bonus = 0;
		if(p.invArmor.getStackInSlot(3) != null){
			ItemTool weapon = (ItemTool) p.invArmor.getStackInSlot(3).getItem();
			bonus = weapon.getAttackDamage();
		}

		health -= p.getAttackDamage() + bonus;

	}

	private int moveDirectionCountDown = 600; //600 ticks : 10 seconds

	@Override
	public void update() {
		super.update();
		
		if(!getWorld().isNightTime()){
			if(new Random().nextInt(60) == 0){
				health--;
			}
		}
		
		if(health <= 0){
			this.remove = true;
		}

		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		moveDirectionCountDown --;

		if(moveDirectionCountDown == 0){
			moveDirectionCountDown = new Random().nextInt(600)+300;
			if(new Random().nextInt(2) == 0){
				left = true; right = false;
			}else{
				left = false; right = true;
			}
		}

		if(dx == 0){
			jumping = true;
			
		}

		// jumping
		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		// falling
		if (falling) {
			dy += fallSpeed;

			if (dy > 0)
				jumping = false;
			if ((dy < 0) && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed)
				dy = maxFallSpeed;

		}

		if( tileMap.getBlockID(currCol,currRow) == 7){
			left = false; right = true;
		}

		if(tileMap.getBlockID(currCol, currRow) == 6){
			left = true; right = false;
		}

		if (right)
			facingRight = true;
		if (left)
			facingRight = false;
	}

	@Override
	protected void updateAnimation() {

		if (left || right) {
			if (currentAction != 0) {
				currentAction = 0;
				getAnimation().setFrames(entityTexture);
				getAnimation().setDelay(75);
			}
		}
		getAnimation().update();
	}
}
