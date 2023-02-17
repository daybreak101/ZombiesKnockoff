package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import entities.statics.Wall;
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
				if(!e.getFreezeStatus().isFrozen()) {
					e.getFreezeStatus().freeze();
				}
				return true;
			}
		}
		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			if (!handler.getWorld().getEntityManager().getBarriers().contains(e) && e.getCollisionBounds(0, 0).intersects(cb)) {
				handler.getWorld().getEntityManager().getEntities().remove(this);
				return true;
			}
		}
		for (Wall e : handler.getWorld().getEntityManager().getWalls()) {
			if (e.getCollisionBounds(0, 0).intersects(cb)) {
				handler.getWorld().getEntityManager().getEntities().remove(this);
				return true;
			}
		}
		return false;
	}

}
