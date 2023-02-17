package entities.creatures.zombieinfo;

import entities.creatures.Zombie;

public class SlownessStatus {
	
	private Zombie zombie;
	private float slowness = 1;
	
	public SlownessStatus(Zombie zombie) {
		this.zombie = zombie;
	}
	
	public float getSlowness() {
		return slowness;
	}
	
	public void setSlowness(float slowness) {
		this.slowness = slowness;
	}
	
	public void addToSlowness(float slowness) {
		this.slowness += slowness;
	}
}
