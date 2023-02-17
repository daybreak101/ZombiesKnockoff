package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import entities.statics.Wall;
import main.Handler;

public class Rocket extends Bullet{
	
	private Ellipse2D explosionRadius;

	public Rocket(Handler handler, float x, float y) {
		super(handler, x, y, 1000);
	}
	
	@Override
	public void tick() {
		// if bullet hits a rock, it should end there, since it cannot penetrate it
		while (travelTicker < speed) {
			moveX();
			moveY();
			if(checkForImpact()) {
				findEntitiesInRadius();
				break;
			}
			travelTicker++;
		}
		travelTicker = 0;

		die();
		postTick();
	}
	
	public void findEntitiesInRadius() {
		explosionRadius = new Ellipse2D.Float(x - 150, y - 150, 300, 300);
		handler.getWorld().getEntityManager().addBlood(new Explosion(handler, x - 150, y - 150, 300, 300));
		
		float damageMultiplier = 1;
		if(handler.getPlayer().getInv().getPhd() == 3) {
			damageMultiplier += 1;
		}
		if(handler.getPlayer().getInv().getStronghold() > -1) {
			damageMultiplier += handler.getPlayer().getStrongholdDamageMultiplier();
		}
		
		for(Zombie f: handler.getWorld().getEntityManager().getZombies()) {
			if (explosionRadius.intersects(f.getHitBox(0, 0))) {
				f.takeDamage((int) (gunFiredFrom.getDamage() * damageMultiplier));
				float currentPercent = (float) ( (float)f.getHealth() /  (float)f.getMaxHealth());
				float thirtyPercent =   (float) ( (float) (f.getMaxHealth() * 3 / 10) /(float)f.getMaxHealth() );
				if (currentPercent < thirtyPercent && f.getZombieType() == 0) {
					f.turnToCrawler();
				}
			}
		}
		
		if(explosionRadius.intersects(handler.getWorld().getEntityManager().getPlayer().getCollisionBounds(0, 0))){
				handler.getWorld().getEntityManager().getPlayer().takeExplosionDamage(gunFiredFrom.getDamage() / 50);
		}
	}
	
	public boolean checkForImpact() {
		cb = new Rectangle((int)(x + bounds.x - 1), (int)(y + bounds.y - 1), bounds.width + 1, bounds.height + 1);
		
		
		for(Zombie e: handler.getWorld().getEntityManager().getZombies()) {
			if(e.getHitBox(0, 0).intersects(cb)) {
				handler.getWorld().getEntityManager().getEntities().remove(this);
				return true;
			}
		}
		for(InteractableStaticEntity e: handler.getWorld().getEntityManager().getInteractables()){
			if(!handler.getWorld().getEntityManager().getBarriers().contains(e) && e.getCollisionBounds(0, 0).intersects(cb)) {
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


	@Override
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height);
	}
}
