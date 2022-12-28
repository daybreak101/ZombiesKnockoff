package entities.bullets;

import java.awt.Rectangle;

import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import main.Handler;

public class SniperBullet extends Bullet{
	
	int range = 1000;
	int rangeCounter = 0;

	public SniperBullet(Handler handler, float x, float y, int range) {
		super(handler, x, y, range);
	}
	
	
	public boolean checkForImpact() {
		cb = new Rectangle((int)(x + bounds.x - 1), (int)(y + bounds.y - 1), bounds.width + 1, bounds.height + 1);
		
		float damageMultiplier = 1;
		if(handler.getPlayer().getInv().isDoubletap()) {
			damageMultiplier += 1;
		}
		if(handler.getPlayer().getInv().isStronghold()) {
			damageMultiplier += handler.getPlayer().getStrongholdDamageMultiplier();
		}
		
		for(Zombie e: handler.getWorld().getEntityManager().getZombies()) {
			if(e.getHitBox(0, 0).intersects(cb)) {
				if(e.getSniperBullet() == null) {
					e.takeDamage((int) (gunFiredFrom.getDamage() * damageMultiplier)); 
					e.setSniperBullet(this);
				}
				else if(e.getSniperBullet() != this) {
					e.takeDamage((int) (gunFiredFrom.getDamage() * damageMultiplier)); 
					e.setSniperBullet(this);
				}
			}
		}		
		for(InteractableStaticEntity e: handler.getWorld().getEntityManager().getInteractables()){
			if(e.getCollisionBounds(0, 0).intersects(cb)) {
				handler.getWorld().getEntityManager().getEntities().remove(this);
				return true;
			}
		}		
		return false;
	}	
}
