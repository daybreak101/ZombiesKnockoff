package entities.powerups;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import entities.creatures.Zombie;
import main.Handler;

public class Nuke extends PowerUps{

	public Nuke(Handler handler, float x, float y) {
		super(handler, x, y);
		name = "Nuke";
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
		for(Zombie e: handler.getWorld().getEntityManager().getZombies().getObjects()) {
			e.die();
			handler.getWorld().getEntityManager().getEntities().remove(e);
		}
		handler.getWorld().getEntityManager().getZombies().getObjects().clear();
		handler.getWorld().getEntityManager().getPlayer().gainPoints(400);
	}

	@Override
	public void render(Graphics g) {
		if(!pickedUp) {
			g.setColor(Color.yellow);
			g.drawOval((int) (x - handler.getGameCamera().getxOffset()) , (int) (y - handler.getGameCamera().getyOffset()), width, height);
		}
	}
}
