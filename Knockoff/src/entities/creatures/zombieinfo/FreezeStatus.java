package entities.creatures.zombieinfo;

import java.awt.geom.Ellipse2D;

import entities.areas.Areas;
import entities.creatures.Player;
import entities.creatures.Zombie;
import entities.creatures.ZombieType;
import entities.statics.traps.IcyWater;
import main.Handler;
import utils.Timer;

public class FreezeStatus {
	private Zombie zombie;
	private Handler handler;
	protected boolean isFrozen = false;
	private boolean inWater = false;

	private Player player;
	
	public FreezeStatus(Handler handler, Zombie zombie) {
		this.handler = handler;
		this.zombie = zombie;
	}
	
	public FreezeStatus(Handler handler, Player player) {
		this.player = player;
		this.handler = handler;
	}
	
	public void freezeNearbyZombies() {
		Ellipse2D freezeRadius = new Ellipse2D.Float(zombie.getCenterX() - 100, zombie.getCenterY() - 100, 200, 200);
		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (freezeRadius.intersects(e.getHitBox(0, 0)) && e != zombie) {
				e.getFreezeStatus().freeze();
			}
		}
	}
	
	public boolean isFrozen() {
		return isFrozen;
	}
	
	public void setFrozen(boolean isFrozen) {
		this.isFrozen = isFrozen;
	}
	
	
	public void freeze() {
		if ((zombie.getZombieType() != ZombieType.ICE_ENHANCED_ZOMBIE || zombie.getZombieType() != ZombieType.WHITE_WALKER)) {
			if (isFrozen) {
				zombie.setActive(false);
				zombie.die();
			} else {
				isFrozen = true;
				zombie.removeSpeed();
				zombie.setHealth(1);
			}
		}

	}
	
	public void checkIfInIcyWater() {
		boolean found = false;
		for (Areas e : handler.getWorld().getEntityManager().getAreas()) {
			if (((IcyWater) e).checkIfEntityIsContained(zombie.getHitBox(0, 0))) {
				inWater = true;
				found = true;
			}
		}
		if (!found) {
			inWater = false;
		}
	}
	
	public boolean inWater() {
		return inWater;
	}
	
	private int iceCounter = 0, iceMax = 300;
	public void freezing() {
		if (inWater) {
			iceCounter++;
		} else {
			iceCounter--;
		}
		if (iceCounter >= iceMax) {
			isFrozen = true;
		}
	}
	
	private Timer breakCooldown = new Timer(60);
	private int breakCounter = 0;

	public void breakFreeFromIce() {
		if (breakCooldown.isReady()) {
			breakCooldown.resetTimer();
			breakCounter++;
		}
		if (breakCounter >= 3) {
			breakCounter = 0;
			isFrozen = false;
			iceCounter = 0;
		}
	}
	
	public Timer getBreakCooldown() {
		return breakCooldown;
	}
	
	public void breakPlayerIceWhenHit() {
		if (isFrozen) {
			isFrozen = false;
			iceCounter = 0;
		}
	}
}
