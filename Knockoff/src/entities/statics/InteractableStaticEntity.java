package entities.statics;

import main.Handler;

import java.awt.Rectangle;
import java.awt.geom.Line2D;

public abstract class InteractableStaticEntity extends StaticEntity{

	protected Rectangle trigger;
	protected int cooldown, cooldownTimer;
	protected String triggerText;
	
	
	public InteractableStaticEntity(Handler handler, float x, float y, int width, int height) {
		super(handler, x, y, width, height);
		trigger = new Rectangle(0,0,0,0);
		cooldown = 200;
		triggerText = "";
		cooldownTimer = cooldown;
	}
	
	public void tick() {
		cooldownTimer++;
		//trigger = new Rectangle((int) (x + bounds.x  - handler.getGameCamera().getxOffset() - 5), (int) (y + bounds.y  - handler.getGameCamera().getyOffset() - 5), bounds.width + 10, bounds.height + 10);
		boolean found = false;
		Line2D.Float line = new Line2D.Float(handler.getPlayer().getX() + handler.getPlayer().getWidth()/2, handler.getPlayer().getY() + handler.getPlayer().getHeight()/2, this.x + this.width/2, this.y + this.height/2);
		for(InteractableStaticEntity e: handler.getWorld().getEntityManager().getInteractables()) {
			if(e == this) {
				//System.out.println(e.getTriggerText());
			}
			else if(line.intersects(e.getCollisionBounds(0, 0))) {	
				found = true;
				break;
			}
		}
		for (Wall e : handler.getWorld().getEntityManager().getWalls()) {
			if (line.intersects(e.getCollisionBounds(0, 0))) {
				handler.getWorld().getEntityManager().getEntities().remove(this);
				found = true;
				break;
			}
		}
		if(!found)
			trigger = new Rectangle((int) (x + bounds.x - 100), (int) (y + bounds.y - 100), bounds.width + 100, bounds.height + 100);
		else
			trigger = new Rectangle(0,0,0,0);
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
