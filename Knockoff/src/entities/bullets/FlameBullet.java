package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import main.Handler;

public class FlameBullet extends Bullet {

	ArrayList<Zombie> zombiesHit;
	
	public FlameBullet(Handler handler, float x, float y, int range) {
		super(handler, x, y, range);
		this.speed = 4;
		xMove = (float) (speed * Math.cos(angle));
		yMove = (float) (speed * Math.sin(angle));
		this.width = 50;
		this.height = 50;
		zombiesHit = new ArrayList<Zombie>();
	}

	public void postTick() {
		width++;
		height++;
	}

	public boolean checkForImpact() {
		cb = new Rectangle((int) (x + bounds.x - 1), (int) (y + bounds.y - 1), width + 1, height + 1);

		float damageMultiplier = 1;
		if(handler.getPlayer().getInv().isDoubletap()) {
			damageMultiplier += 1;
		}
		if(handler.getPlayer().getInv().isStronghold()) {
			damageMultiplier += handler.getPlayer().getStrongholdDamageMultiplier();
		}
		
		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (!zombiesHit.contains(e) && e.getHitBox(0, 0).intersects(cb)) {
				e.takeDamage((int) (gunFiredFrom.getDamage() * damageMultiplier));
				System.out.println("Damage: " + (int) (gunFiredFrom.getDamage() * damageMultiplier));
				System.out.println("Damage Multiplier is" + damageMultiplier);
				e.setBurn((int) (gunFiredFrom.getDamage() / 2 * damageMultiplier));
				zombiesHit.add(e);
				//handler.getWorld().getEntityManager().getEntities().remove(this);
			}
		}

		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			if (!handler.getWorld().getEntityManager().getBarriers().contains(e) && e.getCollisionBounds(0, 0).intersects(cb)) {
				handler.getWorld().getEntityManager().getEntities().remove(this);
				return true;
			}
		}
		return false;
	}

	int color = 0;
	int alpha = 200;
	Random rand = new Random();

	@Override
	public void render(Graphics g) {
		if (color < 10) {
			g.setColor(new Color(255,255,0,alpha));
			if(gunFiredFrom.isUpgraded())
				g.setColor(new Color(0,255,255,alpha));
		} else if (color < 20) {
			g.setColor(new Color(205, 50, 0,alpha));
			if(gunFiredFrom.isUpgraded())
				g.setColor(new Color(0, 100, 100,alpha));
		} else if (color < 30) {
			g.setColor(new Color(255,102,0,alpha));
			if(gunFiredFrom.isUpgraded())
				g.setColor(new Color(10, 0, 100,alpha));
		}

		
		
		
		g.fillOval((int) (x + rand.nextInt(-10,10) - handler.getGameCamera().getxOffset()), (int) (y + rand.nextInt(-10,10) - handler.getGameCamera().getyOffset()),
				width, height);
		
		color += 1;
		alpha -= 3;
		if(alpha < 0)
			alpha = 0;
		if (color >= 30) {
			color = 0;
		}
	}

}
