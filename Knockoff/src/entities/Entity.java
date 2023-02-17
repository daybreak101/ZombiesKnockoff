package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import entities.statics.Barrier;
import entities.statics.InteractableStaticEntity;
import entities.statics.Wall;
import main.Handler;

public abstract class Entity {
	protected Handler handler;
	protected float x, y;
	protected int width, height;
	protected int health;
	protected boolean active = true;
	public static final int DEFAULT_HEALTH = 100;
	protected Rectangle bounds;
	
	public Entity(Handler handler, float x, float y, int width, int height) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		health = DEFAULT_HEALTH;
		bounds = new Rectangle(0, 0, width, height);
	}
	
	public boolean checkEntityCollisions(float xOffset, float yOffset) {
		for(Entity e: handler.getWorld().getEntityManager().getEntities()) {
			if(e.equals(this))
				continue;
			if(handler.getWorld().getEntityManager().getBarriers().contains(e)) {
				continue;
			}
			else if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset)))
				return true;
		}
		for(Wall e: handler.getWorld().getEntityManager().getWalls()) {
			if(e.equals(this))
				continue;
			if(e.getCollisionBounds(0,0).intersects(getCollisionBounds(xOffset, yOffset))) {
				return true;
			}
		}
		for(InteractableStaticEntity e: handler.getWorld().getEntityManager().getInteractables()) {
			if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset)))
				return true;
		}
		return false;
	}
	
	
	
	public Rectangle getCollisionBounds(float xOffset, float yOffset) {
		return new Rectangle((int) (x + bounds.x + xOffset), (int) (y + bounds.y + yOffset), (int) (bounds.width), (int) (bounds.height)); 
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
	
	public int getCenterX() {
		return (int) x + width/2;
	}
	
	public int getCenterY() {
		return (int) y + height/2;
	}
	
	public float getRenderX() {
		return x - handler.getGameCamera().getxOffset();
	}
	
	public float getRenderY() {
		return y - handler.getGameCamera().getyOffset();
	}

	public void tick() {}
	public void render(Graphics g) {}
	public void die() {}
	public void dontMove() {}
}
