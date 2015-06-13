package game.entity;

import game.entity.living.EntityLiving;


public class EntityAI {

	int entityTimer = 0;

	public void walkAroundRandomly(EntityLiving el){

		//start timer if x position is the same for a while
		if(el.dx == 0){
			el.setJumping(true);
		}
		
		if(el.jumping){
			entityTimer++;
		}
		
		if(entityTimer > 250){
			if(el.left){
				el.left = false;
				el.right = true;
				el.facingRight = true;
			}
			else if (el.right){
				el.left = true;
				el.right = false;
				el.facingRight = false;
			}
			entityTimer = 0;
		}
//		System.out.println(entityTimer +" " + el.left + " " +el.right);

		// turn around when arrow blocks are hit
		if( el.tileMap.getBlockID(el.currCol,el.currRow) == 7){
			el.left = false; el.right = true;
		}

		if(el.tileMap.getBlockID(el.currCol, el.currRow) == 6){
			el.left = true; el.right = false;
		}

		if (el.right)
			el.facingRight = true;
		if (el.left)
			el.facingRight = false;
		//
		
		if(el.jumping && !el.falling){
			
		}

		// jumping
		if (el.jumping && !el.falling) {
			el.dy = el.jumpStart;
			el.falling = true;
		}

		// falling
		if (el.falling) {
			el.dy += el.fallSpeed;

			if (el.dy > 0)
				el.jumping = false;
			if ((el.dy < 0) && !el.jumping)
				el.dy += el.stopJumpSpeed;

			if (el.dy > el.maxFallSpeed)
				el.dy = el.maxFallSpeed;

		}
	}

	public void panic(EntityLiving el) {
		if(el.left){
			el.setRight(true);
		}else
			el.setLeft(true);
	}
}
