package states;

import java.awt.Color;
import java.awt.Graphics;

import hud.GameplayElement;
import hud.HudManager;
import main.Handler;
import sounds.Sounds;
import worlds.World;

public class GameState extends State {

	private World world;
	private HudManager hud;


	public GameState(Handler handler) {
		super(handler);
		
		Sounds.init();
		
		world = new World(handler, "/worlds/test/world1.txt", "/worlds/test/entities.txt");
		//world = new World(handler, "/worlds/factory/world1.txt", "/worlds/factory/entities.txt");
		handler.setWorld(world);
		
		hud = new HudManager(handler);
		hud.addObject(new GameplayElement(handler));

		
		
		handler.setHud(hud);
		Cheats cheats = new Cheats(handler);
	}
	

	@Override
	public void tick() {
		world.tick();
		hud.tick();
		
	}

	int transparency = 255;
	@Override
	public void render(Graphics g) {
		world.render(g);
		
		//crosshair
		g.setColor(Color.RED);
		g.fillOval(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), 10, 10);
		hud.render(g);
		
	if(transparency > 0){
			
			Color color = new Color(0, 0, 0, transparency);
			transparency--;
			
			g.setColor(color);
			g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
			
		}
	}
	

}