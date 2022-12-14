package entities.statics;

import main.Handler;

import java.awt.Rectangle;

public abstract class InteractableStaticEntity extends StaticEntity{

	protected Rectangle trigger;
	protected int cooldown, cooldownTimer;
	protected String triggerText;
	
	
	public InteractableStaticEntity(Handler handler, float x, float y, int width, int height) {
		super(handler, x, y, width, height);
		trigger = new Rectangle(0,0,0,0);
		cooldown = 200;
		triggerText = "";
	}
	
	public void tick() {
		cooldownTimer++;
		//trigger = new Rectangle((int) (x + bounds.x  - handler.getGameCamera().getxOffset() - 5), (int) (y + bounds.y  - handler.getGameCamera().getyOffset() - 5), bounds.width + 10, bounds.height + 10);
		trigger = new Rectangle((int) (x + bounds.x - 100), (int) (y + bounds.y - 100), bounds.width + 100, bounds.height + 100);
		postTick();
	}
	
	public Rectangle getTriggerRange() {
		return trigger;
	}
	
	public String getTriggerText() {
		return triggerText;
	}
	
	public void fulfillInteraction() {}
	public void postTick() {}

}
