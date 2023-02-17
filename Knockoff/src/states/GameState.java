package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import hud.GameplayElement;
import hud.HudManager;
import main.Cheats;
import main.Handler;
import sounds.Sounds;
import worlds.World;

public class GameState extends State {

	private World world;
	private HudManager hud;
	private Cheats cheats;


	public GameState(Handler handler) {
		super(handler);
		
		Sounds.init();
		
		world = new World(handler,
				"/worlds/test/world1.txt", 
				"/worlds/test/entities.txt",
				"/worlds/test/nodes.txt",
				"/worlds/test/edges.txt");
		//world = new World(handler, "/worlds/factory/world1.txt", "/worlds/factory/entities.txt");
		handler.setWorld(world);
		
		hud = new HudManager(handler);
		hud.addObject(new GameplayElement(handler));

		
		
		handler.setHud(hud);
		cheats = new Cheats(handler);
	}
	

	@Override
	public void tick() {
		world.tick();
		hud.tick();
		cheats.tick();
	}

	int transparency = 255;

	@Override
	public void render(Graphics g) {
		//double zoomLevel = handler.getSettings().getZoomLevel();
		//Graphics2D g2d = (Graphics2D) g;
		//AffineTransform old = g2d.getTransform();
		
		//old.scale(zoomLevel, zoomLevel);
		//g2d.setTransform(old);
		/////////////////////////////////////////////////////////////
		
		world.render(g);
		
		
		//old.scale(1/zoomLevel, 1/zoomLevel);
		//g2d.setTransform(old);
		hud.render(g);
		
	if(transparency > 0){
			
			Color color = new Color(0, 0, 0, transparency);
			transparency--;
			
			g.setColor(color);
			g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
			
		}
	}
	
	public World getWorld() {
		return world;
	}

	

}
