package entities.bullets;

import java.awt.Graphics;

import entities.blood.Blood;

import java.awt.Color;
import main.Handler;

public class Explosion extends Blood{
	
	Color color;

	public Explosion(Handler handler, float x, float y, int width, int height) {
		super(handler, x, y, -1);
		this.timer = 20;
		this.width = width;
		this.height = height;
		this.color = Color.orange;
	}
	
	public Explosion(Handler handler, float x, float y, int width, int height, Color color) {
		super(handler, x, y, -1);
		this.timer = 20;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	public void render(Graphics g) {
		g.setColor(color);
		g.fillOval((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height);
	}
	
	

}
