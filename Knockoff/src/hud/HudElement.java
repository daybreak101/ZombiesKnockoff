package hud;

import java.awt.Graphics;
import java.awt.Rectangle;

import main.Handler;

public abstract class HudElement {
	protected float x, y;
	protected int width, height;
	protected Rectangle bounds;
	protected Handler handler;
	
	public HudElement(float x, float y, int width, int height, Handler handler) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.handler = handler;
		bounds = new Rectangle((int) x, (int) y, width, height);
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}
}
