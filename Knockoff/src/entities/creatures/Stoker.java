package entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import entities.blood.Blood;
import entities.bullets.Grenade;
import entities.bullets.StokerFireball;
import graphics.Assets;
import main.Handler;
import utils.Timer;
import utils.Utils;

public class Stoker extends Zombie {

	private Timer shootTimer, chargeTimer;
	private int angerHealthThreshold;
	private boolean isAngry = false;

	// make sure zombie cannot burn

	public Stoker(Handler handler, float x, float y, float dspeed, int health) {
		super(handler, x, y, dspeed, health);
		zombieType = STOKER;
		speed = 2.0f + dspeed - 1f;
		this.health = health * 2;
		angerHealthThreshold = health / 5;
		shootTimer = new Timer(500);
		chargeTimer = new Timer(120);
	}

	@Override
	public void render(Graphics g) {

		g.drawImage(Assets.shadow, (int) (x - handler.getGameCamera().getxOffset()),
				(int) (y - handler.getGameCamera().getyOffset()), width, height, null);

		float moveToX = handler.getPlayer().getX() - handler.getGameCamera().getxOffset()
				+ handler.getPlayer().getWidth() / 2;
		float moveToY = handler.getPlayer().getY() - handler.getGameCamera().getyOffset()
				+ handler.getPlayer().getHeight() / 2;
		float angle = (float) Math
				.toDegrees(Math.atan2(-(x - handler.getGameCamera().getxOffset() - moveToX + width / 2),
						y - handler.getGameCamera().getyOffset() - moveToY + height / 2));
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		g2d.rotate(Math.toRadians(angle), x - handler.getGameCamera().getxOffset() + width / 2,
				y - handler.getGameCamera().getyOffset() + height / 2);
		g2d.drawImage(Assets.toxen, (int) (x - handler.getGameCamera().getxOffset()),
				(int) (y - handler.getGameCamera().getyOffset()), width, height, null);
		g2d.setTransform(old);

	}

	// implement where stoker only shoots when near player
	public void postTick() {
		if (!freezeStatus.isFrozen()) {
			if (isAngry == false) {
				fireballAttack();
			}
			if (health < angerHealthThreshold && isAngry == false) {
				getAngry();
			}
			if (isAngry) {
				pulsateFlame();
			}
		}
	}

	Timer pulsateTimer = new Timer(60);

	public void pulsateFlame() {
		pulsateTimer.tick();
		if (pulsateTimer.isReady()) {
			new Grenade(handler, x + width / 2, y + height / 2, Color.orange).findPlayerInRadius();
		}
	}

	public void getAngry() {
		isAngry = true;
		dontMove();
		speed = speed * 2;
	}

	boolean attacking = false;

	public void fireballAttack() {
		shootTimer.tick();
		if (shootTimer.isReady()
				&& Utils.getEuclideanDistance(x, y, handler.getPlayer().getX(), handler.getPlayer().getY()) <= 300) {
			attacking = true;
			chargeTimer.tick();
		}
		if (attacking) {
			justAttacked = true;
			chargeTimer.tick();
		}
		if (chargeTimer.isReady()) {
			handler.getWorld().getEntityManager()
					.addEntity(new StokerFireball(handler, x + width / 2, y + height / 2, 300));
			shootTimer.resetTimer();
			chargeTimer.resetTimer();
			justAttacked = false;
			attacking = false;
		}
	}

	@Override
	public void die() {
		if(handler.getPlayer().getInv().getVamp() >= 1) {
			handler.getPlayer().incrementTempHealth(5);
		}
		else if (handler.getPlayer().getInv().getVamp() >= 0) {
			handler.getPlayer().incrementTempHealth(1);
		}
		if(handler.getPlayer().getInv().getStronghold() == 3) {
			if(handler.getPlayer().getStrongholdRadius() != null) {
				if(handler.getPlayer().getStrongholdRadius().intersects(getHitBox(0,0))) {
					handler.getPlayer().gainArmor(5);
					handler.getPlayer().gainStrongholdDamageMultiplier(.05f);
				}
			}
		}
		handler.getPlayer().getStats().gainKill();
		// put "flame" effect on death as blood that stays there for 7 seconds
		handler.getWorld().getEntityManager().addBlood(new Blood(handler, x, y, ZombieType.STOKER));
	}
}
