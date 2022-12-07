package entities.statics.traps;

import java.awt.Graphics;
import java.awt.Rectangle;

import entities.Entity;
import main.Handler;

public class Trap extends Entity {
	protected boolean activated = false;
	private int cost;
	protected int cooldown, cooldownTimer;

	public Trap(Handler handler, float x, float y, int width, int height,
		float switchX, float switchY, int switchRotation, int cooldown, int cost) {
		super(handler, x, y, width, height);
		this.cost = cost;
		handler.getWorld().getEntityManager().addInteractable(new TrapSwitch(handler, switchX, switchY, switchRotation, this, cooldown));
	}

	public Rectangle getCollisionBounds(float xOffset, float yOffset) {
		return new Rectangle(0,0,0,0); 
	}
	
	public void fulfillInteraction() {
		activated = true;
		cooldownTimer = 0;
		
	}
	
	public void tick() {
		cooldownTimer++;
		postTick();
	}
	

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	public void postTick() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean getActivation() {
		return activated;
	}

	public int getCost() {
		return cost;
	}
	
}
