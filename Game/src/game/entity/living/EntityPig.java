package game.entity.living;

import java.awt.Graphics2D;

import game.World;
import game.content.Images;
import game.entity.EntityAI;
import game.entity.MapObject;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.Items;
import game.item.tool.ItemTool;
import base.main.music.Music;
import base.tilemap.TileMap;

public class EntityPig extends EntityLiving{

	private EntityAI ai = new EntityAI();
	private boolean flicker;
	private int flickerTimer = 100;

	public EntityPig(TileMap tm, World world, String uin) {
		super(tm, world, uin);

		setHealth(8);

		getAnimation().setFrames(Images.loadMultiImage("/entity/piggy.png", 32, 0, 4));
		getAnimation().setDelay(150);

		entitySizeX = 32;
		entitySizeY = 32;

		width = 32;
		height = 32;


		moveSpeed = 0.1; // inital walking speed. you speed up as you walk
		maxSpeed = 1; // change to jump farther and walk faster
		stopSpeed = 0.1;
		fallSpeed = 0.15; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		boolean b = rand.nextBoolean();
		facingRight = b;
		right = b;
		left = !b;

	}

	@Override
	public void draw(Graphics2D g) {
		if(flicker)
			flickerTimer++;
		if(flickerTimer > 50){
			flickerTimer = 0;
			flicker = false;
		}
		if(flickerTimer % 5 == 0)
		{
			super.draw(g);
		}

	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	@Override
	public void update() {
		super.update();

		getNextPosition(); // needed for falling
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

//		System.out.println(dx);

		ai.walkAroundRandomly(this);
	}

	@Override
	public void onEntityHit(Player p, MapObject mo) {

		flicker = true;

		int dmg = p.getAttackDamage();

		int wepDmg = 0;
		ItemStack wep = world.getPlayer().invArmor.getWeapon();
		if(wep != null && 3 == ((ItemTool)wep.getItem()).getEffectiveness())
			wepDmg = ((ItemTool)wep.getItem()).getEffectiveDamage();

		health -= wepDmg + dmg;

		Music.play("hitpig_" + (rand.nextInt(5)+1));
		ai.panic(this);
		if(health < 0)
			kill(p);
	}

	private ItemStack[] drops = new ItemStack[3];

	public ItemStack[] getDrops() {
		drops[0] = new ItemStack(Items.meat_pig_raw, rand.nextInt(3)+1);
		drops[1] = new ItemStack(Items.meat_pig_raw, rand.nextInt(3)+1);
		drops[2] = new ItemStack(Items.meat_pig_raw, rand.nextInt(3)+1);
		return drops;
		
	}

	public void kill(Player p)
	{
		if(p.setStackInNextAvailableSlot(getDrops()[rand.nextInt(drops.length)])){
			this.remove = true;
		}else{
			health = maxHealth;
		}
	}
}
