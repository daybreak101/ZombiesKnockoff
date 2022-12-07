package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import main.Handler;

public abstract class Entity {
	protected Handler handler;
	protected float x, y;
	protected int width, height;
	protected int health;
	protected boolean active = true;
	public static final int DEFAULT_HEALTH = 100;
	protected Rectangle bounds;
	//protected boolean isFrozen = false;
	
	public Entity(Handler handler, float x, float y, int width, int height) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		health = DEFAULT_HEALTH;
		bounds = new Rectangle(0, 0, width, height);
	}
	
	public void takeDamage(int amount) {
		
			
		if(handler.getRoundLogic().getPowerups().isInstakillActive()) {
			health = 0;
		}
		else {
			boolean crit = isCritical();
			if(crit && handler.getPlayer().getInv().isDeadshot()) {
				health -= (amount * 3);
				System.out.println("critical");
			}
			else if(crit) {
				health -= (amount * 2);
				System.out.println("critical");
			}
			else{
				health -= amount;
			}
			
		}
		
		if(health <= 0 && active == true) {
			handler.getPlayer().gainPoints(70);
			active = false;
			die();
		}
		else {
			handler.getPlayer().gainPoints(10);
		}
	}
	
	public boolean isCritical() {
		Random rng = new Random();
		int criticalChance = rng.nextInt(100);
		
		if(handler.getPlayer().getInv().isDeadshot()) {
			if(criticalChance < 20)
				return true;
		}	
		else {
			if(criticalChance < 10)
				return true;
		}
		
		return false;
	}
	
	public boolean checkEntityCollisions(float xOffset, float yOffset) {
		for(Entity e: handler.getWorld().getEntityManager().getEntities()) {
			if(e.equals(this))
				continue;
			if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset)))
				return true;
		}
		return false;
	}
	
	public Rectangle getCollisionBounds(float xOffset, float yOffset) {
		return new Rectangle((int) (x + bounds.x + xOffset), (int) (y + bounds.y + yOffset), bounds.width, bounds.height); 
	}
	
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void tick() {}
	public void render(Graphics g) {}
	public void die() {}
	public void dontMove() {}
}
