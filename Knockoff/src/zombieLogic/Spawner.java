package zombieLogic;

import java.awt.Rectangle;

import entities.Entity;
import entities.creatures.Licker;
import entities.creatures.Stoker;
import entities.creatures.Toxen;
import entities.creatures.Zombie;
import main.Handler;

public class Spawner {
	
	private Handler handler;
	private float x, y;
	private boolean isActive;
	private boolean isReady;
	private int counter, cooldown;
	private boolean collided;

	public Spawner(Handler handler, float x, float y) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		isReady = false;
		isActive = true;
		counter = 0;
		cooldown = 30;
		collided = false;
	}
	
	public void tick() {
		counter++;
		if(counter >= cooldown) {
			for(Entity e: handler.getWorld().getEntityManager().getEntities()) {
				collided = false;
				if(e.getCollisionBounds(-5f, -5f).intersects(new Rectangle((int) x- 50, (int) y - 50, 100, 100))) {
					collided = true;
					System.out.println("Spawn blocked!");
				}	
			}
		}
		if(collided) {
			
			isReady = false;
		}
		
		else if(!collided){
			isReady = true;
			counter = 0;
		}
	}
	
	public void spawnZombie(float dspeed, int health) {
		handler.getWorld().getEntityManager().addZombie(new Zombie(handler, x, y, dspeed, health));
	}
	
	public void spawnLicker(float dspeed, int health) {
		handler.getWorld().getEntityManager().addZombie(new Licker(handler, x, y, dspeed, health));
	}
	
	public void spawnToxen(float dspeed, int health) {
		handler.getWorld().getEntityManager().addZombie(new Toxen(handler, x, y, dspeed, health));
	}
	
	public void spawnStoker(float dspeed, int health) {
		handler.getWorld().getEntityManager().addZombie(new Stoker(handler, x, y, dspeed, health));
	}
	
	public void updateCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	
	public void setActive() {
		this.isActive = true;
	}
	
	public void setInactive() {
		this.isActive = false;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}
	
	
}
