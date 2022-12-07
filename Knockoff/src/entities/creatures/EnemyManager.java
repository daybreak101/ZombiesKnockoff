package entities.creatures;

import java.util.ArrayList;
import java.util.Random;

import main.Handler;

public class EnemyManager {
	private Handler handler;
	private ArrayList<Zombie> zombies;
	
	int currentRound;
	public long zombieHealth;
	
	Random rand = new Random();
	
	public EnemyManager(Handler handler) {
		this.handler = handler;
		zombies = new ArrayList<Zombie>();
	}
	
	public void tick() {
		for(int i = 0; i < zombies.size(); i++) {
			Zombie e = zombies.get(i);
			if(!e.isActive())
				zombies.remove(e);
		}
		
	}
	
	public ArrayList<Zombie> getObjects() {
		return zombies;
	}

	public void setObjects(ArrayList<Zombie> zombies) {
		this.zombies = zombies;
	}

	public void addObject(Zombie o) {
		zombies.add(o);
	}
	
	public void removeObject(Zombie o) {
		zombies.remove(o);
	}
	
	
	
	
	
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
}
