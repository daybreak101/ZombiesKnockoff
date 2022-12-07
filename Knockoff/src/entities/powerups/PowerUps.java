package entities.powerups;

import java.awt.Rectangle;

import entities.statics.InteractableStaticEntity;
import main.Handler;

public abstract class PowerUps extends InteractableStaticEntity {
	
	protected boolean pickedUp = false;
	protected int activeCounter;

	public PowerUps(Handler handler, float x, float y) {
		super(handler, x, y, 60, 60);
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
			pickedUp = true;
		}
	}
	
	public void unbuff() {}
}