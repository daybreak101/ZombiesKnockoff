package entities.areas;

import java.awt.Graphics;

import main.Handler;

public abstract class Areas {
	
	protected Handler handler;
	
	public Areas(Handler handler) {
		this.handler = handler;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
}
