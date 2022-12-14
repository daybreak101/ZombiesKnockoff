package entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import entities.areas.Areas;
import entities.blood.Blood;

import entities.creatures.Player;
import entities.creatures.Wolf;
import entities.creatures.Zombie;
import entities.powerups.PowerUps;
import entities.statics.Barrier;
import entities.statics.InteractableStaticEntity;
import entities.statics.traps.Trap;
import main.Handler;

public class EntityManager {

	int currentRound;
	public long zombieHealth;
	
	Random rand = new Random();
	
	private Wolf luna;
	private Entity map;
	private Handler handler;
	private Player player;
	//private EnemyManager zombies;
	private ArrayList<Zombie> zombies;
	private ArrayList<Entity> entities;
	private ArrayList<Barrier> barriers;
	private ArrayList<Blood> blood;
	private ArrayList<PowerUps> powerups;
	private ArrayList<Trap> traps;
	private ArrayList<InteractableStaticEntity> interactables;
	private ArrayList<Areas> areas;
	private Comparator<Entity> renderSorter = new Comparator<Entity>() {
		public int compare(Entity a, Entity b) {
			if (a.getY() + a.getHeight() < b.getY() + b.getHeight())
				return -1;
			if (a.getY() + a.getHeight() > b.getY() + b.getHeight())
				return 1;
			return 0;
		}
	};

	public EntityManager(Handler handler, Player player) {
		this.handler = handler;
		this.player = player;
		areas = new ArrayList<Areas>();
		barriers = new ArrayList<Barrier>();
		blood = new ArrayList<Blood>();
		entities = new ArrayList<Entity>();
		interactables = new ArrayList<InteractableStaticEntity>();
		powerups = new ArrayList<PowerUps>();
		traps = new ArrayList<Trap>();
		addEntity(player);
		handler.setPlayer(player);
		zombies = new ArrayList<Zombie>();
	}

	public void tick() {
		for(int i = 0; i < zombies.size(); i++) {
			Zombie e = zombies.get(i);
			e.tick();
			if(!e.isActive())
				zombies.remove(e);
		}
		
		if (luna != null) {
			luna.tick();
		}

		for (int i = 0; i < blood.size(); i++) {
			Blood e = blood.get(i);
			e.tick();
			if (e.getCounter() >= e.getTimer())
				blood.remove(e);
		}

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
			if (!e.isActive())
				entities.remove(e);
		}
		entities.sort(renderSorter);
		for (InteractableStaticEntity e : interactables) {
			e.tick();
		}
		for (int i = 0; i < powerups.size(); i++) {
			PowerUps e = powerups.get(i);
			e.tick();
			if (!e.isActive())
				powerups.remove(e);
		}
		for (Trap e : traps) {
			e.tick();
		}
		for (Areas e : areas) {
			e.tick();
		}
		for (Barrier e : barriers) {
			e.tick();
		}

		// System.out.println("Number of entities: " + numOfEntities());

	}

	public int numOfEntities() {
		return blood.size() + entities.size() + traps.size();
	}

	public void setMap(Entity e) {
		map = e;
	}

	public void render(Graphics g) {
		if (map != null) {
			map.render(g);
		}
		for (Areas e : areas) {
			e.render(g);
		}

		for (Trap e : traps) {
			e.render(g);
		}
		for (Blood e : blood) {
			e.render(g);
		}
		for (PowerUps e : powerups) {
			e.render(g);
		}
		for (Barrier e : barriers) {
			e.render(g);
		}
		for(Zombie e: zombies){
			e.render(g);
		}

		for (Entity e : entities) {
			e.render(g);
		}
		if (luna != null) {
			luna.render(g);
		}
	}

	public void activateLuna() {
		luna = new Wolf(handler, handler.getPlayer().getX(), handler.getPlayer().getY());
	}

	public void deactivateLuna() {
		luna = null;
	}

	public void addBlood(Blood e) {
		blood.add(e);
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void addZombie(Zombie e) {
		//entities.add(e);
		zombies.add(e);
		//zombies.addObject(e);
	}

	public void addInteractable(InteractableStaticEntity e) {
		entities.add(e);
		interactables.add(e);
	}

	public void addPowerUp(PowerUps e) {
		powerups.add(e);
	}

	public void addTrap(Trap e) {
		traps.add(e);
	}

	public void addArea(Areas e) {
		areas.add(e);
	}

	public void addBarrier(Barrier e) {
		barriers.add(e);
		interactables.add(e);
	}
	
	// getters and setters.
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public ArrayList<Areas> getAreas() {
		return areas;
	}

	public ArrayList<InteractableStaticEntity> getInteractables() {
		return interactables;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

	public ArrayList<Zombie> getZombies() {
		return zombies;
	}

	public void setZombies(ArrayList<Zombie> zombies) {
		this.zombies = zombies;
	}

	public ArrayList<PowerUps> getPowerups() {
		return powerups;
	}

	public void setPowerups(ArrayList<PowerUps> powerups) {
		this.powerups = powerups;
	}

	public ArrayList<Blood> getBlood() {
		return blood;
	}

	public ArrayList<Barrier> getBarriers() {
		return barriers;
	}

}
