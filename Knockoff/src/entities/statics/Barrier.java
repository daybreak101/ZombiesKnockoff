package entities.statics;

import java.awt.Graphics;

import graphics.Assets;
import main.Handler;

public class Barrier extends InteractableStaticEntity {

	private int health;
	private boolean isBroken, cantAfford;
	
	public Barrier(Handler handler, float x, float y, int width, int height) {
		super(handler, x, y, width, height);
		isBroken = false;
		cantAfford = false;
		triggerText = "Press F to repair barricade";
	}
	
	

	@Override
	public void render(Graphics g) {
		if(isBroken)
			g.drawImage(Assets.stone, (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null );
		else
			g.drawImage(Assets.mysteryBox, (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null );
		
	}

	
	public void fulfillInteraction() {
		if(cooldownTimer >= cooldown &&
				!(health < 100)) {
			cooldownTimer = 0;
			if(handler.getWorld().getEntityManager().getPlayer().purchase(60)) {
				health = 100;
				cantAfford = false;
			}
			else {
				cantAfford = true;
				cooldownTimer = 0;
			}
			
		}

	}

	@Override
	public void postTick() {
		
		if(cantAfford && cooldownTimer < cooldown) {
			triggerText = "              Not enough points!";
		}
		else if(health == 100 && cooldownTimer < cooldown) {
			triggerText = "                Already repaired!";
		}
		else if(cooldownTimer >= cooldown) {
			triggerText = "Press F to refill current weapon: 1000";
		}
		else {
			triggerText = "";
		}
		
		if(health <= 0) {
			isBroken = true;
		}
		else {
			isBroken = false;
		}
	}
	
	public boolean getIsBroken() {
		return isBroken;
	}
}
