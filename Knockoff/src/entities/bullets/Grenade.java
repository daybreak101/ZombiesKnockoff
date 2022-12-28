package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import main.Handler;

public class Grenade extends Bullet {

	private int counter, timer, explosionTimer;
	private Ellipse2D explosionRadius;
	//private Arc2D.Float[] explosionRadius;
	private Color color;
	private boolean isImpact;

	public Grenade(Handler handler, float x, float y) {
		super(handler, x, y, 10000);
		width = 10;
		height = 10;
		counter = 0;
		timer = 7;
		explosionTimer = 65;
		speed = 0.2f;
		this.color = Color.orange;
		isImpact = false;
	}

	public Grenade(Handler handler, float x, float y, Color color) {
		super(handler, x, y, 10000);
		width = 10;
		height = 10;
		counter = 0;
		timer = 7;
		explosionTimer = 65;
		speed = 0.2f;
		this.color = color;
		isImpact = false;
	}

	public Grenade(Handler handler, float x, float y, boolean isImpact) {
		super(handler, x, y, 10000);
		width = 10;
		height = 10;
		counter = 0;
		timer = 7;
		explosionTimer = 65;
		speed = 0.2f;
		this.color = Color.orange;
		this.isImpact = isImpact;
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
			moveX();
			moveY();
		}
	}

	public boolean checkForImpact() {
		cb = new Rectangle((int) (x + bounds.x - 1), (int) (y + bounds.y - 1), bounds.width + 2, bounds.height + 2);

		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (e.getCollisionBounds(0, 0).intersects(cb)) {
				return true;
			}
		}

		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			if (!handler.getWorld().getEntityManager().getBarriers().contains(e) && e.getCollisionBounds(0, 0).intersects(cb)) {
				return true;
			}
		}
		return false;

	}

//	public void initializeArcs() {
//		explosionRadius = new Arc2D.Float[12];
//		for (int i = 0; i < 12; i++) {
//			explosionRadius[i] = new Arc2D.Float(x + width/2 - 100, y + height/2 - 100, 200,200, 30 * i,
//					30, Arc2D.PIE);
//		}
//	}

	public void findEntitiesInRadius() {
		float damageMultiplier = 1;
		if (handler.getPlayer().getInv().isDoubletap()) {
			damageMultiplier += 1;
		}
		if (handler.getPlayer().getInv().isStronghold()) {
			damageMultiplier += handler.getPlayer().getStrongholdDamageMultiplier();
		}

		explosionRadius = new Ellipse2D.Float(x - 100, y - 100, 200, 200);
		//initializeArcs();
//		boolean isActive = true;
//		for (int i = 0; i < 12; i++) {
//			for(InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
//				if(!handler.getWorld().getEntityManager().getBarriers().contains(e) &&
//						explosionRadius.intersects(e.getCollisionBounds(0, 0))) {
//					isActive = false;
//					break;
//				}
//			}
//			
//
//			if (isActive) {
				for (Zombie f : handler.getWorld().getEntityManager().getZombies()) {
					int damage = 1000;
					if (isImpact) {
						damage = (int) (gunFiredFrom.getDamage() * damageMultiplier);
					}
					if (explosionRadius.intersects(f.getHitBox(0, 0))) {
						f.takeDamage((int) (damage * damageMultiplier));
						if (f.getHealth() / handler.getRoundLogic().getZombieHealth() < (f.getHealth() * 3 / 10)
								&& f.getZombieType() == 0) {
							f.turnToCrawler();
						}
					}
				}

				if (explosionRadius.intersects(handler.getPlayer().getCollisionBounds(0, 0))) {
					if (!handler.getPlayer().getInv().isPhd())
						handler.getPlayer().takeDamage(60);
				}
				handler.getWorld().getEntityManager().addBlood(new Explosion(handler, x - 100, y - 100, 200, 200, color));

		//	}
		//}
	}

	public void findPlayerInRadius() {
		Ellipse2D.Float dmgRadius = new Ellipse2D.Float(x - 100, y - 100, 200, 200);
		handler.getWorld().getEntityManager().addBlood(new Explosion(handler, x - 100, y - 100, 200, 200, color));
		if (dmgRadius.intersects(handler.getPlayer().getCollisionBounds(0, 0))) {
			handler.getPlayer().takeDamage(20);
		}
	}

	public void moveX() {
		if (!checkForImpact()) {
			x += xMove;
		} else {
			xMove = -xMove;
		}

	}

	public void moveY() {
		if (!checkForImpact()) {
			y += yMove;
		} else {
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
