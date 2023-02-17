package entities.creatures.playerinfo;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;
import java.util.Random;

import entities.EntityManager;
import entities.creatures.Player;
import entities.creatures.Zombie;
import entities.powerups.DeathMachine;
import entities.powerups.DoublePoints;
import entities.powerups.HealthUp;
import entities.powerups.InfiniteAmmo;
import entities.powerups.InstaKill;
import entities.powerups.MaxAmmo;
import entities.powerups.Nuke;
import entities.powerups.PerkBag;
import entities.statics.Barrier;
import entities.statics.InteractableStaticEntity;
import entities.statics.Wall;
import main.Handler;
import utils.Node;
import utils.Timer;

public class Blessings {

	private String blessing;
	private Handler handler;
	private Player player;
	private Inventory inv;
	private EntityManager em;

	private int currentPoints;
	private int maxMeterPoints;
	private boolean running;
	private Timer blessingTimer;

	public Blessings(Handler handler) {
		this.handler = handler;
		blessing = "In Plain Sight";
		running = false;

		currentPoints = 3000;
		maxMeterPoints = 3000;
	}

	public void tick() {
		player = handler.getPlayer();
		inv = player.getInv();
		em = handler.getWorld().getEntityManager();
		
		if(running) {
			blessingTimer.tick();
			if(blessingTimer.isReady()) {
				running = false;
				blessingTimer = null;
				//check if out of blessing, if so make it ""
			}
		}
	}

	public void activateBlessing() {
		if (currentPoints >= maxMeterPoints && blessing != "" && running == false) {
			currentPoints = 0;
			maxMeterPoints += 500;
			doBlessing();
		}
	}

	public void doBlessing() {

		if (blessing == "Double Time") {
			em.addPowerUp(new DoublePoints(handler, player.getX(), player.getY()));
		} else if (blessing == "KABOOM") {
			em.addPowerUp(new Nuke(handler, player.getX(), player.getY()));
		} else if (blessing == "Full Supply") {
			em.addPowerUp(new MaxAmmo(handler, player.getX(), player.getY()));
		} else if (blessing == "Infinite Supply") {
			em.addPowerUp(new InfiniteAmmo(handler, player.getX(), player.getY()));
		} else if (blessing == "No Mercy") {
			em.addPowerUp(new InstaKill(handler, player.getX(), player.getY()));
		} else if (blessing == "EZ Points") {
			inv.gainPoints(500);
		} else if (blessing == "HP up") {
			em.addPowerUp(new HealthUp(handler, player.getX(), player.getY()));
		} else if (blessing == "Death Machine!") {
			em.addPowerUp(new DeathMachine(handler, player.getX(), player.getY()));
		} else if (blessing == "Crawl Space") {
			for (Zombie z : em.getZombies()) {
				z.turnToCrawler();
			}
		} else if (blessing == "Extra Change") {
			running = true;
			blessingTimer = new Timer(3 * 60 * 60);
		} else if(blessing == "So No Head?") {
			running = true;
			blessingTimer = new Timer(60 * 60);
		} else if(blessing == "Graded up") {
			if(player.getInv().getGun().isUpgraded()) {
				maxMeterPoints -= 500;
				currentPoints = maxMeterPoints;
			}
			else {
				player.getInv().getGun().upgradeWeapon();
			}
		} else if(blessing == "Extra Sodium") {
			em.addPowerUp(new PerkBag(handler, player.getX(), player.getY()));
		} else if(blessing == "Reign Drops") {
			em.addPowerUp(new DoublePoints(handler, player.getX(), player.getY()));
			em.addPowerUp(new Nuke(handler, player.getX(), player.getY()));
			em.addPowerUp(new MaxAmmo(handler, player.getX(), player.getY()));
			em.addPowerUp(new InfiniteAmmo(handler, player.getX(), player.getY()));
			em.addPowerUp(new InstaKill(handler, player.getX(), player.getY()));
			em.addPowerUp(new HealthUp(handler, player.getX(), player.getY()));
			em.addPowerUp(new DeathMachine(handler, player.getX(), player.getY()));
			em.addPowerUp(new PerkBag(handler, player.getX(), player.getY()));
		} else if(blessing == "Round Robbin") {
			handler.getRoundLogic().wipeRound();
		} else if(blessing == "I'm Feeling Lucky") {
			Random rand = new Random();
			int rng = rand.nextInt(8);
			switch(rng) {
			case 0:
				em.addPowerUp(new DoublePoints(handler, player.getX(), player.getY()));
				break;
			case 1:	
				em.addPowerUp(new Nuke(handler, player.getX(), player.getY()));
				break;
			case 2:	
				em.addPowerUp(new MaxAmmo(handler, player.getX(), player.getY()));
				break;
			case 3:	
				em.addPowerUp(new InfiniteAmmo(handler, player.getX(), player.getY()));
				break;
			case 4:
				em.addPowerUp(new InstaKill(handler, player.getX(), player.getY()));
				break;
			case 5:	
				em.addPowerUp(new HealthUp(handler, player.getX(), player.getY()));
				break;
			case 6:	
				em.addPowerUp(new DeathMachine(handler, player.getX(), player.getY()));
				break;
			case 7:	
				em.addPowerUp(new PerkBag(handler, player.getX(), player.getY()));
				break;
			default:
				em.addPowerUp(new DoublePoints(handler, player.getX(), player.getY()));
				break;
			}
	
		} else if(blessing == "Anywhere But Here") {
			Random rand = new Random();
			Node node = handler.getWorld().getNodes().get(rand.nextInt(handler.getWorld().getNodes().size()));
			Rectangle radius = new Rectangle(node.getX() -100, node.getY() - 100, 200, 200);
			while(!node.withinPlayable() && findBlocked(radius)) {
				node = handler.getWorld().getNodes().get(rand.nextInt(handler.getWorld().getNodes().size()));
				radius = new Rectangle(node.getX() - 100, node.getY() - 100, 200, 200);
			}
			for(Zombie z : em.getZombies()) {
				z.meander();
			}
			player.setX(node.getX() - player.getWidth()/2);
			player.setY(node.getY() - player.getHeight()/2);
		} else if(blessing == "In Plain Sight") {
			running = true;
			blessingTimer = new Timer(10 * 60);
		} else if(blessing == "Brain Freeze") {
			for(Zombie z : em.getZombies())
				z.getFreezeStatus().freeze();
		}

	}
	
	public boolean findBlocked(Rectangle radius) {
		
		for(InteractableStaticEntity e : em.getInteractables()) {
			if(e.getCollisionBounds(0, 0).intersects(radius))
				return true;
		}
		for(Wall e: em.getWalls()) {
			if(e.getCollisionBounds(0, 0).intersects(radius))
				return true;
		}
		for(Barrier e: em.getBarriers()) {
			if(e.getCollisionBounds(0, 0).intersects(radius))
				return true;
		}
		return false;
	}

	public float getBlessingMeter() {
		if(!running)
			return (float) currentPoints / (float) maxMeterPoints;
		else
			return getBlessingTimer().getDecrementalProgress();
	}

	public void addPoints(int dPoints) {
		if (running == false) {
			currentPoints += dPoints;

			if (currentPoints > maxMeterPoints) {
				currentPoints = maxMeterPoints;
			}
		}
	}

	public String getBlessing() {
		return blessing;
	}
	
	public void setBlessing(String blessing) {
		running = false;
		this.blessing = blessing;
	}

	public boolean isRunning() {
		return running;
	}
	
	public Timer getBlessingTimer() {
		return blessingTimer;
	}
}
