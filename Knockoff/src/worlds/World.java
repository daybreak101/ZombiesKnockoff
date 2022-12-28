package worlds;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import entities.EntityManager;
import entities.creatures.Player;
import entities.statics.AmmoRefill;
import entities.statics.Barrier;
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
import utils.Graph;
import utils.Node;
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
	private ArrayList<Node> nodes;
	private int[][] nextStep; // [src][dest]
	// int i = 0;
	int ticker = 0, tickerLimit = 600;

	public World(Handler handler, String path, String entityPath, String nodesPath, String edgesPath) {
		this.handler = handler;
		nodes = new ArrayList<Node>();
		entityManager = new EntityManager(handler, new Player(handler, 100, 100));

		spawners = new ArrayList<Spawner>();

		rounds = new RoundLogic(handler, spawners);
		handler.setRoundLogic(rounds);

		loadWorld(path);
		handler.setWorld(this);
		createStaticEntities(entityPath);
		createNodes(nodesPath);

		for (Node n : nodes) {
			graph = new Graph(nodes.size());
			createEdges(edgesPath);
			buildGraph(n.getVertex());
		}
		writeToFile();

//		nextStep = new int[width * height][width * height];
//		for (int i = 0; i < width * height; i++) {
//			buildGraph(i);
//
//		}

		entityManager.getPlayer().setX(spawnX);
		entityManager.getPlayer().setY(spawnY);
	}
	
	public void createEdges(String edgesPath) {
		// read file
		String file = Utils.loadFileAsString(edgesPath);
		String[] tokens = file.split("\\s+");

		// get number of nodes
		int i = 0;
		
		// process edges
		while (i < tokens.length) {
			int n1= Utils.parseInt(tokens[i++]);
			int n2= Utils.parseInt(tokens[i++]);
			Node m = nodes.get(n1);
			Node n = nodes.get(n2);
			float distance = Utils.getEuclideanDistance(m.getX(), m.getY(), n.getX(), n.getY());
			if (!checkForStaticEntities(m.getX(), m.getY(), n.getX(), n.getY())) {
				graph.createEdge(m.getVertex(), n.getVertex(), distance);
				graph.createEdge(n.getVertex(), m.getVertex(), distance);
			}
		}
	}

	public void createNodes(String nodesPath) {
		// read file
		String file = Utils.loadFileAsString(nodesPath);
		String[] tokens = file.split("\\s+");

		// get number of nodes
		int i = 0;

		// process nodes
		int vertex, x, y;
		while(i < tokens.length) {
			vertex = Utils.parseInt(tokens[i++]);
			x = Utils.parseInt(tokens[i++]);
			y = Utils.parseInt(tokens[i++]);
			nodes.add(new Node(vertex, x, y));
		}
	}

	public void createStaticEntities(String entityPath) {
		String file = Utils.loadFileAsString(entityPath);
		String[] tokens = file.split("\\s+");
		int i = 0;
		int token = 0;
		int x, y;
		int vertex = 0;
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
			case 12:
				whatWall = Utils.parseInt(tokens[i++]);
				entityManager.addBarrier(new Barrier(handler, x, y, whatWall));
				break;
			default:
				break;

			}
		}

//		for (int j = 0; j < height * 2; j++) {
//			for (int k = 0; k < width * 2; k++) {
//				if (!checkWithinStaticEntities(k * 50, j * 50)) {
//					nodes.add(new Node(vertex, k * 50, j * 50));
//					vertex++;
//				}
//			}
//		}

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

	// BufferedImage map;
	public void render(Graphics g) {
		double zoomLevel = handler.getSettings().getZoomLevel();
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();

		old.scale(zoomLevel, zoomLevel);
		g2d.setTransform(old);
		entityManager.render(g);

		g.setColor(Color.red);
		for (Node n : nodes) {
			g.fillOval((int) (n.getX() - handler.getGameCamera().getxOffset()),
					(int) (n.getY() - handler.getGameCamera().getyOffset()), 5, 5);
			g.drawString(Integer.toString(n.getVertex()), (int) (n.getX() - handler.getGameCamera().getxOffset()),
					(int) (n.getY() - handler.getGameCamera().getyOffset()));
			g.drawString(Integer.toString(n.getX()) + ", " + Integer.toString(n.getY()),
					(int) (n.getX() - handler.getGameCamera().getxOffset()),
					(int) (n.getY() + 10 - handler.getGameCamera().getyOffset()));
			for(Node m : nodes) {
				Node nextStep = n.getNextNode(m.getVertex());
				if(nextStep != null)
					g.drawLine((int) (n.getX()- handler.getGameCamera().getxOffset()), (int) (n.getY()- handler.getGameCamera().getyOffset()), 
							(int) (nextStep.getX()- handler.getGameCamera().getxOffset()), (int) (nextStep.getY()- handler.getGameCamera().getyOffset()));
			}
		}

		old.scale(1 / zoomLevel, 1 / zoomLevel);
		g2d.setTransform(old);
		/////////////////////////////////////////////////////////////

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
		System.out.println(src);
		//graph = new Graph(nodes.size());

		// float distance = 0;
		// for (Node n : nodes) {
		// for (Node m : nodes) {
		// distance = Utils.getEuclideanDistance(m.getX(), m.getY(), n.getX(),
		// n.getY());
		// if (distance < 200) {
		// if (!checkForStaticEntities(m.getX(), m.getY(), n.getX(), n.getY())) {
		// graph.createEdge(m.getVertex(), n.getVertex(), distance);
		// }
		// }
		// }
		// }
		int[] path = graph.findPaths(src);
		for (int i = 0; i < path.length; i++) {
			//System.out.println("Source Node: " + src + ", Dest node: " + i + ", Next Step: " + path[i]);
			if (i == src) {
				nodes.get(src).setNextNodes(null);
			} else if (path[i] == -1)
				nodes.get(src).setNextNodes(null);
			else
				nodes.get(src).setNextNodes(nodes.get(path[i]));
		}
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

	public Node getNextStep(int src, int dest) {
		// System.out.println("Source Node: " + src + ", Dest node: " + dest + ", Next
		// Step: " + nodes.get(src).getNextNode(dest).getVertex());

		return nodes.get(src).getNextNode(dest);

	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void writeToFile() {
		try {
			FileWriter writer = new FileWriter("res/paths.txt");
			BufferedWriter buffer = new BufferedWriter(writer);

			for (Node n : nodes) {
				for (Node m : nodes) {
					if (n.getNextNode(m.getVertex()) == null) {
						buffer.write("Source: " + n.getVertex() + ", Dest: " + m.getVertex() + ", Next Node: -1");
					} else
						buffer.write("Source: " + n.getVertex() + ", Dest: " + m.getVertex() + ", Next Node: "
								+ n.getNextNode(m.getVertex()).getVertex());

					buffer.newLine();
				}
			}

			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// make sure no static entities in between
	public int getClosestNode(float x, float y) {
		Node closestNode = null;
		float closestDistance = 2000000;
		float currentDistance = 0;
		for (Node n : nodes) {
			if (!checkForStaticEntities((int) x, (int) y, n.getX(), n.getY())) {
				currentDistance = Utils.getEuclideanDistance(x, y, n.getX(), n.getY());
				if (closestNode == null || currentDistance < closestDistance) {
					closestNode = n;
					closestDistance = currentDistance;
				}
			}
		}

		if (closestNode == null) {
			closestNode = nodes.get(0);
		}
		return closestNode.getVertex();
	}

	public boolean checkForStaticEntities(int x1, int y1, int x2, int y2) {
		Line2D.Float line = new Line2D.Float(x1, y1, x2, y2);
		for (InteractableStaticEntity e : entityManager.getInteractables()) {
			if (line.intersects(e.getCollisionBounds(0, 0))) {
				if (!handler.getWorld().getEntityManager().getBarriers().contains(e)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean checkWithinStaticEntities(int x, int y) {
		Ellipse2D.Float point = new Ellipse2D.Float(x, y, 20, 20);
		for (InteractableStaticEntity e : entityManager.getInteractables()) {
			if (point.intersects(e.getCollisionBounds(0, 0))) {
				if (!handler.getWorld().getEntityManager().getBarriers().contains(e)) {
					return true;
				}
			}
		}
		return false;
	}

}
