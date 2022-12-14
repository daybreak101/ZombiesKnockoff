package graphics;

import entities.Entity;
import main.Handler;

public class GameCamera {
	private Handler handler;
	private float xOffset, yOffset;


	public GameCamera(Handler handler, float xOffset, float yOffset) {
		this.handler = handler;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
	}
	
	public void checkBlankSpace() {
		/*
		 * if(xOffset < 0) { xOffset = 0; } else if(xOffset >
		 * handler.getWorld().getWidth() * 100 - handler.getWidth()) { xOffset =
		 * handler.getWorld().getWidth() * 100 - handler.getWidth(); } if(yOffset < 0) {
		 * yOffset = 0; } else if(yOffset > handler.getWorld().getHeight() * 100 -
		 * handler.getHeight()) { yOffset = handler.getWorld().getHeight() * 100 -
		 * handler.getHeight(); }
		 */
	}
	
	public void centerOnEntity(Entity e) {
		xOffset = (float) (e.getX() - handler.getWidth()/2/handler.getSettings().getZoomLevel() + e.getWidth()/2  / handler.getSettings().getZoomLevel());
		yOffset = (float) (e.getY() - handler.getHeight()/2/ handler.getSettings().getZoomLevel() + e.getHeight()/2 / handler.getSettings().getZoomLevel());
		checkBlankSpace();
	}
	
	public void move(float xAmt, float yAmt) {
		xOffset += xAmt * handler.getSettings().getZoomLevel();
		yOffset += yAmt * handler.getSettings().getZoomLevel();
		checkBlankSpace();
	}
	
	public float getxOffset() {
		return xOffset;
	}

	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getyOffset() {
		return yOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}
}
