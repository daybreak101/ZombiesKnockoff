package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import main.Handler;

public class Rocket extends Bullet{
	
	private Ellipse2D explosionRadius;

	public Rocket(Handler handler, float x, float y) {
		super(handler, x, y, 10000);
	}
	
	public void findEntitiesInRadius() {
		explosionRadius = new Ellipse2D.Float(x - 150, y - 150, 300, 300);
		handler.getWorld().getEntityManager().addBlood(new Explosion(handler, x - 150, y - 150, 300, 300));
		
		float damageMultiplier = 1;
		if(handler.getPlayer().getInv().isDoubletap()) {
			damageMultiplier += 1;
		}
		if(handler.getPlayer().getInv().isStronghold()) {
			damageMultiplier += handler.getPlayer().getStrongholdDamageMultiplier();
		}
		
		for(Zombie f: handler.getWorld().getEntityManager().getZombies()) {
			if(explosionRadius.intersects(f.getCollisionBounds(0, 0).getX(), f.getCollisionBounds(0, 0).getY(),
					f.getCollisionBounds(0, 0).getWidth(), f.getCollisionBounds(0, 0).getHeight())) {
				f.takeDamage((int) (gunFiredFrom.getDamage() * damageMultiplier));
				if(f.getHealth()/handler.getRoundLogic().getZombieHealth()  < (f.getHealth()* 3/10 )&& f.getZombieType() == 0) {
					f.turnToCrawler();
				}
			}
		}
		
		if(explosionRadius.intersects(handler.getWorld().getEntityManager().getPlayer().getCollisionBounds(0, 0))){
			if(!handler.getPlayer().getInv().isPhd())
				handler.getWorld().getEntityManager().getPlayer().takeDamage(gunFiredFrom.getDamage() / 50);
		}
	}
	
	public boolean checkForImpact() {
		cb = new Rectangle((int)(x + bounds.x - 1), (int)(y + bounds.y - 1), bounds.width + 1, bounds.height + 1);
		
		
		for(Zombie e: handler.getWorld().getEntityManager().getZombies()) {
			if(e.getHitBox(0, 0).intersects(cb)) {
				findEntitiesInRadius();			
				handler.getWorld().getEntityManager().getEntities().remove(this);
				return true;
			}
		}
		for(InteractableStaticEntity e: handler.getWorld().getEntityManager().getInteractables()){
			if(e.getCollisionBounds(0, 0).intersects(cb)) {
				findEntitiesInRadius();
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
