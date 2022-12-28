package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import entities.Entity;
import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import main.Handler;
import weapons.Gun;

public class Bullet extends Entity {

	protected float mouseX, mouseY;
	protected Rectangle cb;
	protected Gun gunFiredFrom;
	protected float speed;
	protected float xMove, yMove, angle;
	protected int range, rangeCounter;
	protected boolean fromTrap;
	ArrayList<Zombie> zombiesHit;

	//normal bullet
	public Bullet(Handler handler, float x, float y, int range) {
		super(handler, x, y, 5, 5);
		bounds = new Rectangle(0, 0, 0, 0);
		fromTrap = false;
		this.handler = handler;
		this.range = range;
		this.rangeCounter = 0;
		this.gunFiredFrom = handler.getPlayer().getInv().getGun();
		this.speed = 30;
		this.mouseX = x - handler.getGameCamera().getxOffset() - (int) handler.getMouseManager().getMouseX();
		this.mouseY = y - handler.getGameCamera().getyOffset() - (int) handler.getMouseManager().getMouseY();
		angle = (float) Math.atan2(-mouseY, -mouseX);
		xMove = (float) (speed * Math.cos(angle));
		yMove = (float) (speed * Math.sin(angle));
		zombiesHit = new ArrayList<Zombie>();
	}
	
	//turret bullet
	public Bullet(Handler handler, Gun gun, float x, float y, float targetX, float targetY, int range) {
		super(handler, x, y, 5, 5);
		bounds = new Rectangle(0, 0, 0, 0);
		fromTrap = true;
		this.handler = handler;
		this.range = range;
		this.rangeCounter = 0;
		this.gunFiredFrom = gun;
		this.speed = 30;
		this.mouseX = x - (int) targetX;
		this.mouseY = y - (int) targetY;
		angle = (float) Math.atan2(-mouseY, -mouseX);
		xMove = (float) (speed * Math.cos(angle));
		yMove = (float) (speed * Math.sin(angle));
		zombiesHit = new ArrayList<Zombie>();
	}
	
	//idk
	public Bullet(Handler handler, float x, float y, int range, float radianOffset) {
		super(handler, x, y, 5, 5);
		bounds = new Rectangle(0, 0, 0, 0);
		fromTrap = false;
		this.handler = handler;
		this.range = range;
		this.rangeCounter = 0;
		this.gunFiredFrom = handler.getPlayer().getInv().getGun();
		this.speed = 30;
		this.mouseX = x - handler.getGameCamera().getxOffset() - (int) handler.getMouseManager().getMouseX();
		this.mouseY = y - handler.getGameCamera().getyOffset() - (int) handler.getMouseManager().getMouseY();
		angle = (float) Math.atan2(-mouseY, -mouseX);
		xMove = (float) (speed * Math.cos(angle + radianOffset));
		yMove = (float) (speed * Math.sin(angle + radianOffset));
		zombiesHit = new ArrayList<Zombie>();
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	@Override
	public void tick() {
		// if bullet hits a rock, it should end there, since it cannot penetrate it
		moveX();
		moveY();
		checkForImpact();
		die();
		postTick();
	}
	
	public void postTick() {
		
	}

	public void moveX() {
		x += xMove;
	}

	public void moveY() {
		y += yMove;
	}

	@Override
	public void render(Graphics g) {
		if (gunFiredFrom.isUpgraded())
			g.setColor(new Color(255, 160, 240));
		else
			g.setColor(Color.yellow);
		g.fillRect((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()),
				width, height);
	}

	public boolean checkForImpact() {
		cb = new Rectangle((int) (x + bounds.x - 1), (int) (y + bounds.y - 1), bounds.width + 1, bounds.height + 1);

		float damageMultiplier = 1;
		if(handler.getPlayer().getInv().isDoubletap()) {
			damageMultiplier += 1;
		}
		if(handler.getPlayer().getInv().isStronghold()) {
			damageMultiplier += handler.getPlayer().getStrongholdDamageMultiplier();
		}
		
		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (e.getHitBox(0, 0).intersects(cb) && !zombiesHit.contains(e)) {
				if(fromTrap)
					e.damageByTrap(gunFiredFrom.getDamage());
				else {
					e.takeDamage((int) (gunFiredFrom.getDamage()/(zombiesHit.size()+1) * damageMultiplier));
					zombiesHit.add(e);
				}
				
				if(zombiesHit.size() >= 4) {
					handler.getWorld().getEntityManager().getEntities().remove(this);
					return true;
				}
				
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

	// bullet automatically dies if it goes off of screen
	@Override
	public void die() {
		rangeCounter++;
		if (handler.getPlayer().getInv().isDeadshot()) {
			if (rangeCounter >= range * 1.5) {
				handler.getWorld().getEntityManager().getEntities().remove(this);
			}
		} else if (rangeCounter >= range) {
			handler.getWorld().getEntityManager().getEntities().remove(this);
		}
	}

	public float getMouseX() {
		return mouseX;
	}

	public void setMouseX(float mouseX) {
		this.mouseX = mouseX;
	}

	public float getMouseY() {
		return mouseY;
	}

	public void setMouseY(float mouseY) {
		this.mouseY = mouseY;
	}

}
