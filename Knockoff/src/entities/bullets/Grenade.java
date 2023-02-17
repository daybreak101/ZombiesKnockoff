package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import entities.statics.Wall;
import main.Handler;

public class Grenade extends Bullet {

	private int counter, timer, explosionTimer;
	private Ellipse2D explosionRadius;
	private Color color;
	private boolean isImpact;
	private float destX, destY;

	//normal grenades
	public Grenade(Handler handler, float x, float y, boolean isImpact, float destX, float destY) {
		super(handler, x, y, 10000);
		x = (int) x;
		y = (int) y;
		width = 10;
		height = 10;
		counter = 0;
		timer = 60;
		explosionTimer = 65;
		speed = 8;
		this.color = Color.orange;
		this.isImpact = isImpact;
		xMove = (float) ( Math.cos(angle));
		yMove = (float) ( Math.sin(angle));
		this.destX = destX;
		this.destY = destY;
	}

	//idk
	public Grenade(Handler handler, float x, float y, Color color) {
		super(handler, x, y, 10000);
		width = 10;
		height = 10;
		counter = 0;
		timer = 7;
		explosionTimer = 65;
		speed = 15;
		this.color = color;
		isImpact = false;
		xMove = (float) (speed * Math.cos(angle));
		yMove = (float) (speed * Math.sin(angle));
	}

	public void tick() {
		if (isImpact) {
			timer = 10;
		}
		counter++;
		if (counter >= timer) {
			if (isImpact) {
				findEntitiesInRadius();
				handler.getWorld().getEntityManager().getEntities().remove(this);
			} else if (counter >= explosionTimer) {
				findEntitiesInRadius();
				handler.getWorld().getEntityManager().getEntities().remove(this);
			}
		} else {
			if (isImpact) {
				if (checkForImpact()) {
					findEntitiesInRadius();
					handler.getWorld().getEntityManager().getEntities().remove(this);
				}
			}
			while(travelTicker < speed) {
				moveX();
				moveY();
				travelTicker++;
				
				if(((int) x == (int) destX || (int) y == (int) destY) && !slowed) {
					slowed = true;
					speed = speed/8;
				}
				
			}
			travelTicker = 0;
		}
	}
	
	boolean slowed = false;

	public boolean checkForImpact() {
		cb = new Rectangle((int) (x), (int) (y), width, height);

		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (e.getCollisionBounds(0, 0).intersects(cb)) {
				return true;
			}
		}

		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			if (!handler.getWorld().getEntityManager().getBarriers().contains(e)
					&& e.getCollisionBounds(0, 0).intersects(cb)) {
				return true;
			}
		}
		for (Wall e : handler.getWorld().getEntityManager().getWalls()) {
			if (e.getCollisionBounds(0, 0).intersects(cb)) {
				return true;
			}
		}
		return false;

	}

	public void findEntitiesInRadius() {
		float damageMultiplier = 1;
		if (handler.getPlayer().getInv().getPhd() == 3) {
			damageMultiplier += 1;
		}
		if (handler.getPlayer().getInv().getStronghold() > -1) {
			damageMultiplier += handler.getPlayer().getStrongholdDamageMultiplier();
		}

		explosionRadius = new Ellipse2D.Float(x - 100, y - 100, 200, 200);
		for (Zombie f : handler.getWorld().getEntityManager().getZombies()) {
			int damage = 1000;
			if (isImpact) {
				damage = (int) (gunFiredFrom.getDamage() * damageMultiplier);
			}
			if (explosionRadius.intersects(f.getHitBox(0, 0))) {
				f.takeDamage((int) (damage * damageMultiplier));
				float currentPercent = (float) ((float) f.getHealth() / (float) f.getMaxHealth());
				float thirtyPercent = (float) ((float) (f.getMaxHealth() * 3 / 10) / (float) f.getMaxHealth());
				if (currentPercent < thirtyPercent && f.getZombieType() == 0) {
					f.turnToCrawler();
				}
			}
		}

		if (explosionRadius.intersects(handler.getPlayer().getCollisionBounds(0, 0))) {
			handler.getPlayer().takeExplosionDamage(60);
		}
		handler.getWorld().getEntityManager().addBlood(new Explosion(handler, x - 100, y - 100, 200, 200, color));

	}

	public void findPlayerInRadius() {
		explosionRadius = new Ellipse2D.Float(x - 100, y - 100, 200, 200);
		handler.getWorld().getEntityManager().addBlood(new Explosion(handler, x - 100, y - 100, 200, 200, color));
		if (explosionRadius.intersects(handler.getPlayer().getCollisionBounds(0, 0))) {
			handler.getPlayer().takeExplosionDamage(20);
		}
	}

	public void moveX() {
		x += xMove;
		if (checkForImpact()) {
			speed = speed/4;
			x -= xMove;
			xMove = -xMove;
		}

	}

	public void moveY() {
		y += yMove;
		if (checkForImpact()) {
			speed = speed/4;
			y -= yMove;
			yMove = -yMove;
		} 
	}

	@Override
	public void render(Graphics g) {

		g.setColor(new Color(150, 200, 100));

		g.fillOval((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()),
				width, height);
	}

}
