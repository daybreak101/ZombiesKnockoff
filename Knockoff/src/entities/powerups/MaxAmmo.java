package entities.powerups;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import main.Handler;

public class MaxAmmo extends PowerUps{

	public MaxAmmo(Handler handler, float x, float y) {
		super(handler, x, y);
		name = "Max Ammo";
		icon = null;
		floatingAsset = null;
	}
	
	public void tick() {
		cooldownTimer++;
		trigger = new Rectangle((int) (x), (int) (y), width, height);
		
		if(cooldownTimer >= cooldown) {
			handler.getWorld().getEntityManager().getPowerups().remove(this);
			handler.getWorld().getEntityManager().getEntities().remove(this);
		}
		else if(pickedUp) {
			fulfillInteraction();
			handler.getWorld().getEntityManager().getPowerups().remove(this);
			handler.getWorld().getEntityManager().getEntities().remove(this);
		}
		else if(cooldownTimer >= cooldown) {
			handler.getWorld().getEntityManager().getPowerups().remove(this);
			handler.getWorld().getEntityManager().getEntities().remove(this);
		}
		else if(!pickedUp) {
			checkPickedUp();
		}
	}
	
	@Override
	public void fulfillInteraction() {
		handler.getWorld().getEntityManager().getPlayer().maxAmmo();
	}

	@Override
	public void render(Graphics g) {
		if(!pickedUp) {	
			g.setColor(Color.green);
			g.drawOval((int) (x - handler.getGameCamera().getxOffset()) , (int) (y - handler.getGameCamera().getyOffset()), width, height);
		}
	}

}
