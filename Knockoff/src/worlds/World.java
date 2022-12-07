package worlds;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.EntityManager;
import entities.creatures.Player;
import entities.statics.AmmoRefill;
import entities.statics.InteractableStaticEntity;
import entities.statics.MysteryBox;
import entities.statics.PackAPunch;
import entities.statics.RandomPerk;
import entities.statics.Wall;
import entities.statics.traps.ConveyorBeltTrap;
import entities.statics.traps.ElectricTrap;
import entities.statics.traps.IcyWater;
import entities.statics.traps.MineFieldTrap;
import entities.statics.traps.Turret;
import main.Handler;
import maps.FactoryMap;
import perks.Luna;
import utils.Graph;
import utils.Utils;
import zombieLogic.RoundLogic;
import zombieLogic.Spawner;

public class World {
	private Handler handler;
	private int width, height;
	private int spawnX, spawnY;
	private int[][] tiles;

	// Entities
	private EntityManager entityManager;
	private RoundLogic rounds;
	private ArrayList<Spawner> spawners;

	private Graph graph;
	private int[][] nextStep; // [src][dest]
	// int i = 0;
	int ticker = 0, tickerLimit = 600;

	public World(Handler handler, String path, String entityPath) {
		this.handler = handler;

		entityManager = new EntityManager(handler, new Player(handler, 100, 100));

		spawners = new ArrayList<Spawner>();

		

		rounds = new RoundLogic(handler, spawners);
		handler.setRoundLogic(rounds);
		//rounds.setCurrentRound(-2);

		loadWorld(path);
		handler.setWorld(this);
		createStaticEntities(entityPath);
		nextStep = new int[width * height][width * height];
		for (int i = 0; i < width * height; i++) {
			buildGraph(i);

		}

		entityManager.getPlayer().setX(spawnX);
		entityManager.getPlayer().setY(spawnY);
	}

	public void createStaticEntities(String entityPath) {
		String file = Utils.loadFileAsString(entityPath);
		String[] tokens = file.split("\\s+");
		int i = 0;
		int token = 0;
		int x, y;
		while (i < tokens.length) {
			token = Utils.parseInt(tokens[i++]);
			x = Utils.parseInt(tokens[i++]);
			y = Utils.parseInt(tokens[i++]);
			switch (token) {
			case 0:
				entityManager.addInteractable(new MysteryBox(handler, x, y));
				break;
			case 1:
				entityManager.addInteractable(new AmmoRefill(handler, x, y));
				break;
			case 2:
				entityManager.addInteractable(new RandomPerk(handler, x, y));
				break;
			case 3:
				entityManager.addInteractable(new PackAPunch(handler, x, y));
				break;
			case 4: 
				spawners.add(new Spawner(handler, x, y));
				break;
			case 5:
				int wallLength = Utils.parseInt(tokens[i++]);
				int whatWall = Utils.parseInt(tokens[i++]);
				entityManager.addInteractable(new Wall(handler, x, y, wallLength, whatWall));
				break;
			case 6:
				entityManager.setMap(new FactoryMap(handler, 99, 99, 3400, 1700));
				break;
			case 7:
				int sx = Utils.parseInt(tokens[i++]);
				int sy = Utils.parseInt(tokens[i++]);
				entityManager.addTrap(new ElectricTrap(handler, x, y, sx, sy, 0));
				break;
			case 8:
				sx = Utils.parseInt(tokens[i++]);
				sy = Utils.parseInt(tokens[i++]);
				entityManager.addTrap(new MineFieldTrap(handler, x, y, sx, sy, 0));
				break;
			case 9:
				sx = Utils.parseInt(tokens[i++]);
				sy = Utils.parseInt(tokens[i++]);
				entityManager.addTrap(new Turret(handler, x, y, sx, sy, 0));
				break;
			case 10:
				sx = Utils.parseInt(tokens[i++]);
				sy = Utils.parseInt(tokens[i++]);
				entityManager.addTrap(new ConveyorBeltTrap(handler, x, y, sx, sy, 0));
				break;
			case 11:
				entityManager.addArea(new IcyWater(handler, x, y));
				break;
			default:
				break;

			}
		}
	}

	public Graph getGraph() {
		return graph;
	}

	public void tick() {

		for (Spawner spawner : spawners) {
			spawner.tick();
		}

		rounds.tick();
		entityManager.tick();

	}

	BufferedImage map;
	public void render(Graphics g) {
		
		entityManager.render(g);
		for (int i = 0; i < handler.getWorld().getEntityManager().getInteractables().size(); i++) {
			handler.getWorld().getEntityManager().getInteractables().get(i).render(g);

		}

	}

	private void loadWorld(String path) {
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		width = Utils.parseInt(tokens[0]);
		height = Utils.parseInt(tokens[1]);
		spawnX = Utils.parseInt(tokens[2]);
		spawnY = Utils.parseInt(tokens[3]);

	}

	private void buildGraph(int src) {
//		graph = new Graph(width * height);
//		Tile t;
//		boolean topLeft = true, top = true, topRight = true, right = true, bottomRight = true, bottom = true,
//				bottomLeft = true, left = true;
//
//		// add edges
//		for (int j = 0; j < height; j++) {
//			for (int i = 0; i < width; i++) {
//				// System.out.println(i + ", " + j);
//				t = getTile(i, j);
//				if (!t.isSolid() && !checkForStaticEntities(i, j)) {
//					// check if the tile is on edges
//					if (j == 0) {
//						topLeft = false;
//						top = false;
//						topRight = false;
//					}
//					if (j == height - 1) {
//						bottomLeft = false;
//						bottom = false;
//						bottomRight = false;
//					}
//					if (i == 0) {
//						topLeft = false;
//						left = false;
//						bottomLeft = false;
//					}
//					if (i == width - 1) {
//						topRight = false;
//						right = false;
//						bottomRight = false;
//					}
//
//					float hypotenuse = (float) Math.sqrt(2);
//					int pos = i + j * width;
//
//					// add vertices
//					if (top == true) {
//						t = getTile(i, j - 1);
//						if (!t.isSolid() && !checkForStaticEntities(i, j - 1)) {
//							graph.createEdge(pos, pos - width, 1);
//						} else {
//							top = false;
//							topLeft = false;
//							topRight = false;
//						}
//					}
//					if (bottom == true) {
//						t = getTile(i, j + 1);
//						if (!t.isSolid() && !checkForStaticEntities(i, j + 1)) {
//							graph.createEdge(pos, pos + width, 1);
//						} else {
//							bottom = false;
//							bottomLeft = false;
//							bottomRight = false;
//						}
//					}
//					if (left == true) {
//						t = getTile(i - 1, j);
//						if (!t.isSolid() && !checkForStaticEntities(i - 1, j)) {
//							graph.createEdge(pos, pos - 1, 1);
//						} else {
//							left = false;
//							topLeft = false;
//							bottomLeft = false;
//						}
//					}
//					if (right == true) {
//						t = getTile(i + 1, j);
//						if (!t.isSolid() && !checkForStaticEntities(i + 1, j)) {
//							graph.createEdge(pos, pos + 1, 1);
//						} else {
//							right = false;
//							bottomRight = false;
//							topRight = false;
//						}
//					}
//					if (topLeft == true) {
//						t = getTile(i - 1, j - 1);
//						if (!t.isSolid() && !checkForStaticEntities(i - 1, j - 1)) {
//							graph.createEdge(pos, pos - width - 1, hypotenuse);
//						}
//					}
//
//					if (bottomLeft == true) {
//						t = getTile(i - 1, j + 1);
//						if (!t.isSolid() && !checkForStaticEntities(i - 1, j + 1)) {
//							graph.createEdge(pos, pos + width - 1, hypotenuse);
//						}
//					}
//					if (topRight == true) {
//						t = getTile(i + 1, j - 1);
//						if (!t.isSolid() && !checkForStaticEntities(i + 1, j - 1)) {
//							graph.createEdge(pos, pos - width + 1, hypotenuse);
//						}
//					}
//
//					if (bottomRight == true) {
//						t = getTile(i + 1, j + 1);
//						if (!t.isSolid() && !checkForStaticEntities(i + 1, j + 1)) {
//							graph.createEdge(pos, pos + width + 1, hypotenuse);
//						}
//					}
//
//					// reset all booleans to true
//					topLeft = true;
//					top = true;
//					topRight = true;
//					right = true;
//					bottomRight = true;
//					bottom = true;
//					bottomLeft = true;
//					left = true;
//
//				}
//			}
//		}
//		nextStep[src] = graph.findPaths(src);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	} 

	public int getNextStep(int src, int dest) {
		System.out.println(src + "," + dest);
		return nextStep[src][dest];
	}

	public boolean checkForStaticEntities(int x, int y) {
		Rectangle tile = new Rectangle(x * 100, y * 100, 100, 100);
		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			if (tile.intersects(e.getCollisionBounds(0, 0))) {
				return true;
			}
		}

		return false;
	}

}
