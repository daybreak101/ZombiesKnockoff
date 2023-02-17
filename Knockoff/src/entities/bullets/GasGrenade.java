package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import entities.statics.Wall;
import main.Handler;
import utils.Timer;

public class GasGrenade extends Bullet {

	private int counter, timer, explosionTimer;
	private Timer damageTicker = new Timer(30);
	private Timer gasTimer = new Timer(600);
	private Ellipse2D gasRadius;
	private boolean exploded;
	private int damage = 750;
	private float destX, destY;
	boolean slowed = false;

	public GasGrenade(Handler handler, float x, float y, float destX, float destY) {
		super(handler, x, y, 10000);
		x = (int) x;
		y = (int) y;
		width = 10;
		height = 10;
		counter = 0;
		timer = 60;
		explosionTimer = 65;
		speed = 8;
		xMove = (float) (Math.cos(angle));
		yMove = (float) (Math.sin(angle));
		this.destX = destX;
		this.destY = destY;
	}

	public void tick() {
		counter++;
		if (counter >= timer) {
			if (counter >= explosionTimer) {
				gasRadius = new Ellipse2D.Float(x - 100, y - 100, 200, 200);
				exploded = true;
			}
			if(exploded) {
				gasTimer.tick();
				damageTicker.tick();
				if(damageTicker.isReady()) {
					findEntitiesInRadius();
					damageTicker.resetTimer();
				}			
				if(gasTimer.isReady())
					handler.getWorld().getEntityManager().getEntities().remove(this);
			}
			
		} else {
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
	
	

	public boolean checkForImpact() {
		cb = new Rectangle((int) (x + bounds.x - 1), (int) (y + bounds.y - 1), bounds.width + 2, bounds.height + 2);

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

		for (Zombie f : handler.getWorld().getEntityManager().getZombies()) {
			if (gasRadius.intersects(f.getHitBox(0, 0))) {
				f.takeDamage((int) (damage * damageMultiplier));
				float currentPercent = (float) ((float) f.getHealth() / (float) f.getMaxHealth());
				float thirtyPercent = (float) ((float) (f.getMaxHealth() * 3 / 10) / (float) f.getMaxHealth());
				if (currentPercent < thirtyPercent && f.getZombieType() == 0) {
					f.turnToCrawler();
				}
			}
		}

		if (gasRadius.intersects(handler.getPlayer().getCollisionBounds(0, 0))) {
			handler.getPlayer().takeDamage(5);
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
		
		if(gasRadius != null) {
			g.setColor(new Color(150, 200, 100, 150));
			g.fillOval((int) (gasRadius.getX() - handler.getGameCamera().getxOffset()), (int) (gasRadius.getY() - handler.getGameCamera().getyOffset()),
					(int) gasRadius.getWidth(), (int) gasRadius.getHeight());
		}
	}

}
