package entities.powerups;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entities.statics.StaticEntity;
import main.Handler;

public abstract class PowerUps extends StaticEntity {
	
	protected Handler handler;
	protected int cooldown, cooldownTimer;
	protected boolean pickedUp = false;
	protected int activeCounter;
	protected String name;
	protected BufferedImage icon;
	protected BufferedImage floatingAsset;
	protected Rectangle trigger;

	public PowerUps(Handler handler, float x, float y) {
		super(handler, x, y, 60, 60);
		this.handler = handler;
		trigger = new Rectangle(0,0,0,0);
		bounds = new Rectangle(0,0,0,0);
		cooldown = 1800;
		activeCounter = 0;
		
	}
	

	public void tick() {
		cooldownTimer++;
		trigger = new Rectangle((int) (x), (int) y,  width, height);
		
		if(cooldownTimer >= cooldown || activeCounter >= cooldown) {
			unbuff();
			handler.getWorld().getEntityManager().getPowerups().remove(this);
			handler.getWorld().getEntityManager().getEntities().remove(this);
		}

		else if(pickedUp) {
			cooldownTimer = 0;
			activeCounter++;
			fulfillInteraction();
		}	 
		else if(!pickedUp && cooldownTimer < cooldown) {
			checkPickedUp();
		}
	
	}
	
	public void checkPickedUp() {
		if(trigger.intersects(handler.getWorld().getEntityManager().getPlayer().getCollisionBounds(0f, 0f))) {
			
			for(PowerUps e : handler.getWorld().getEntityManager().getPowerups()) {
				if(e.getName() == this.name && e.isPickedUp() && e != this) {
					handler.getWorld().getEntityManager().getPowerups().remove(e);
					break;
				}
			}
			pickedUp = true;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void unbuff() {}
	
	public boolean isPickedUp() {
		return pickedUp;
	}
	
	public BufferedImage getIcon() {
		return icon;
	}
	
	public void fulfillInteraction() {}
	public void postTick() {}
}
