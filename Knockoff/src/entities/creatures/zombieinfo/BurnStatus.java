package entities.creatures.zombieinfo;

import entities.creatures.Creature;
import utils.Timer;

public class BurnStatus {
	private Creature entity;
	private boolean isBurning = false;
	private int burnDamage = 0;
	
	private Timer burnDuration = new Timer(300);
	private Timer eachBurn = new Timer(20);

	public BurnStatus(Creature entity) {
		this.entity = entity;
	}
	
	public void burn() {
		if (isBurning) {
			burnDuration.tick();
			eachBurn.tick();
			if(eachBurn.isReady()) {
				entity.takeDamage(burnDamage);
				eachBurn.resetTimer();
			}
			if(burnDuration.isReady()) {
				isBurning = false;
				burnDuration.resetTimer();
			}
		}
	}
	
	public boolean isBurning() {
		return isBurning;
	}
	
	public void setBurn(int damage) {
		isBurning = true;
		burnDuration.resetTimer();
		burnDamage = damage;
	}
}
