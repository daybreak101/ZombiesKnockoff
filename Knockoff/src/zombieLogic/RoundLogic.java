package zombieLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.creatures.Zombie;
import entities.powerups.PowerUpManager;
import main.Handler;
import utils.Timer;

public class RoundLogic {
	private Handler handler;
	int currentRound;
	private int nextDogRound;
	public int zombieHealth;
	private int zpr;
	private int zombiesLeft = 0;
	private float dSpeed = 0;
	Random rand = new Random();

	private int nextSpawnX, nextSpawnY;

	private int roundCooldown = 600;
	private int counter = 0;

	private int spawnTicker, spawnRate;

	private boolean isDogRound = false;

	private PowerUpManager powerups;

	private List<Spawner> queue;
	private ArrayList<Spawner> spawners;
	boolean test;
	int map;

	public RoundLogic(Handler handler, ArrayList<Spawner> spawners, int map) {
		this.handler = handler;
		currentRound = 0;
		nextDogRound = 5;
		zombieHealth = 50;
		zpr = 6;
		powerups = new PowerUpManager(handler);
		this.spawners = spawners;
		queue = new ArrayList<Spawner>();
		test = false;
		this.map = map;
	}

	public void tick() {
		if (test) {

		} else if (currentRound == -1) {
			noMansLandCycle();
		} else {
			getReadySpawners();
			if (zombiesLeft == 0 && handler.getWorld().getEntityManager().getZombies().size() == 0) {
				counter++;
				if (counter >= roundCooldown) {
					counter = 0;
					handler.getProgression().gainXP(currentRound * 100);
					nextRound();
				}

			} else {
				spawner();
			}
		}
	}

	int interval = 10 * 60;
	int n = 0;
	Timer ringBell = new Timer(interval);

	public void noMansLandCycle() {
		getReadySpawners();
		spawner();
		ringBell.tick();
		if (ringBell.isReady()) {

			n++;
			System.out.println("RING! #" + (n));
			ringBell = new Timer(interval * n);

			// faster spawn rate
			spawnRate = spawnRate - 5;
			if (spawnRate < 5) {
				spawnRate = 5;
			}

			int oldHealth = zombieHealth;
			zombieHealth = zombieHealth * 2;

			System.out.println("NEW HEALTH: " + zombieHealth);

			// increase zombieSpeed
			dSpeed = dSpeed + 0.2f;
			if (dSpeed > 2.4f) {
				dSpeed = 2.4f;
			}

			// change variables for existing zombies
			for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
				if (e.getHealth() < oldHealth) {
					e.setHealth(e.getHealth() + 50);
				} else {
					e.setHealth(zombieHealth);
				}
				((Zombie) e).setSpeed(dSpeed);
			}

		}
	}

	public void getReadySpawners() {
		for (Spawner spawner : spawners) {
			if (spawner.isReady() && spawner.isActive()) {
				queue.add(spawner);
			} else if (!spawner.isReady()) {
				queue.remove(spawner);
			}
		}
	}

	public void spawner() {
		spawnTicker++;
		if (handler.getWorld().getEntityManager().getZombies().size() < 24 && zombiesLeft != 0
				&& spawnTicker >= spawnRate) {

			Random rand = new Random();
			int randomIndex = rand.nextInt(queue.size());
			Spawner spawner = queue.get(randomIndex);
			queue.remove(randomIndex);

			zombiesLeft--;
			// if(currentRound == 1) {
			// spawnTicker = 0;
			// spawner.spawnStoker(dSpeed, zombieHealth);
			// }
			// else

			if (map == 0) {
				spawnTicker = 0;
				spawner.spawnZombie(dSpeed, zombieHealth);
				//spawner.spawnToxen(dSpeed, zombieHealth);
			} else {
				if (isDogRound) {
					spawnTicker = 0;
					spawner.spawnLicker(dSpeed, zombieHealth);
				} else if (currentRound < 6) {
					spawnTicker = 0;
					spawner.spawnZombie(dSpeed, zombieHealth);
				} else if (currentRound > 7 && currentRound % 3 == 0) {
					spawnTicker = 0;
					int rng = rand.nextInt(100);
					if (rng < 10)
						spawner.spawnToxen(dSpeed, zombieHealth);
					else if (rng < 15 && rng >= 10)
						spawner.spawnLicker(dSpeed, zombieHealth);
					else
						spawner.spawnZombie(dSpeed, zombieHealth);
				} else {
					spawnTicker = 0;
					int rng = rand.nextInt(100);
					if (rng < 5)
						spawner.spawnLicker(dSpeed, zombieHealth);
					else
						spawner.spawnZombie(dSpeed, zombieHealth);
				}
			}
		}
	}

	public void nextRound() {
		if (handler.getWorld().getEntityManager().getPlayer().getHealth() > 0) {
			currentRound++;
			handler.getWorld().getEntityManager().getPlayer().setHealth();
		}

		if (currentRound == -1) {
			zpr = 10000;
		}
		// else if(currentRound == 1){
		// zpr = 1;
		// }
		else if (map != 0 && currentRound == nextDogRound) {
			zpr = 6;
			isDogRound = true;
		} else {
			isDogRound = false;
			calculateZPR();
		}
		calculateSpawnRate();
		calculateNextDogRound();
		calculateHealth();
		calculatedSpeed();
		zombiesLeft = zpr;
		powerups.resetManager();
		handler.getWorld().getEntityManager().getPlayer().getInv().roundReplenishGrenades();
		handler.getGlobalStats().writeToFile();
	}

	public void calculateSpawnRate() {
		if (currentRound < 3)
			spawnRate = 90;
		else if (currentRound < 6)
			spawnRate = 80;
		else if (currentRound < 8)
			spawnRate = 75;
		else if (currentRound < 12)
			spawnRate = 65;
		else if (currentRound < 16)
			spawnRate = 55;
		else if (currentRound < 24)
			spawnRate = 45;
		else if (currentRound < 31)
			spawnRate = 40;
		else if (currentRound < 37)
			spawnRate = 35;
		else if (currentRound < 45)
			spawnRate = 30;
		else if (currentRound >= 45) {
			spawnRate = 20;
		}
	}

	public void calculateZPR() {
		int zpr = 3;
		for (int i = 1; i <= currentRound; i++) {
			if (i > 10 && i < 75) {
				zpr = (int) (zpr + (float) (zpr * 0.05));
			} else if (i >= 75) {
				zpr = (int) (zpr + (float) (zpr * 0.025));
			} else {
				zpr = zpr + 3;
			}
		}
		this.zpr = zpr;
	}

	public void calculatedSpeed() {
		if (currentRound < 3)
			dSpeed = 0;
		else if (currentRound < 8)
			dSpeed = 0.2f;
		else if (currentRound < 12)
			dSpeed = 0.5f;
		else if (currentRound < 16)
			dSpeed = 0.7f;
		else if (currentRound < 24)
			dSpeed = 0.8f;
		else if (currentRound < 31)
			dSpeed = 0.9f;
		else if (currentRound < 37)
			dSpeed = 1.0f;
		else if (currentRound < 45)
			dSpeed = 1.1f;
		else if (currentRound >= 45) {
			dSpeed = 1.2f;
		}
	}

	public void calculateHealth() {
		int health = 50;
		for (int i = 1; i <= currentRound; i++) {
			// health cap
			// if (i == 30) {
			// zombieHealth = health;
			// return;
			// }
			if (i >= 10)
				health = health + (int) (health * 0.10);
			else
				health = health + 100;
		}
		zombieHealth = health;
	}

	public void calculateNextDogRound() {
		if (currentRound == nextDogRound) {
			double chance = Math.random();
			int add = 0;
			if (chance < .5)
				add = 5;
			else
				add = 6;
			nextDogRound = nextDogRound + add;
		}
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public int getZombiesLeft() {
		return handler.getWorld().getEntityManager().getZombies().size() + zombiesLeft;
	}

	public int getZpr() {
		return zpr;
	}

	public PowerUpManager getPowerups() {
		return powerups;
	}

	public boolean isDogRound() {
		return isDogRound;
	}

	public int getZombieHealth() {
		return zombieHealth;
	}

	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}
	
	public void wipeRound() {
		zombiesLeft = 0;
	}

}
