package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import main.Handler;

public class IcePellet extends Bullet{

	public IcePellet(Handler handler, float x, float y, int range, float radianOffset) {
		super(handler, x, y, range, radianOffset);

	}
	

	@Override
	public void render(Graphics g) {
		if (gunFiredFrom.isUpgraded())
			g.setColor(Color.blue);
		else
			g.setColor(Color.cyan);
		g.fillRect((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()),
				width, height);
	}
	
	@Override
	public boolean checkForImpact() {
		cb = new Rectangle((int) (x + bounds.x - 1), (int) (y + bounds.y - 1), bounds.width + 1, bounds.height + 1);

		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (e.getHitBox(0, 0).intersects(cb)) {
					//e.takeDamage((int) (gunFiredFrom.getDamage()));
				if(!e.isFrozen()) {
					e.freeze();
				}
				
					System.out.println("Damage: " + (int) (gunFiredFrom.getDamage()));
				//handler.getWorld().getEntityManager().getEntities().remove(this);
				return true;
			}
		}
		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			if (e.getCollisionBounds(0, 0).intersects(cb)) {
				handler.getWorld().getEntityManager().getEntities().remove(this);
				return true;
			}
		}
		return false;
	}

}
