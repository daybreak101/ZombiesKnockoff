package hud;
import java.awt.Graphics;
import java.util.ArrayList;

import main.Handler;

public class HudManager {

	private Handler handler;
	private ArrayList<HudElement> elements;

	
	public HudManager(Handler handler) {
		this.handler = handler;
		elements = new ArrayList<HudElement>();
	}
	
	
	public void tick() {
		for(HudElement o: elements)
			o.tick();
	}
	
	public void render(Graphics g) {
		for(HudElement o: elements)
			o.render(g);
	}
	
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public ArrayList<HudElement> getObjects() {
		return elements;
	}


	public void addObject(HudElement o) {
		elements.add(o);
	}
	
	public void removeObject(HudElement o) {
		elements.remove(o);
	}
	
}
