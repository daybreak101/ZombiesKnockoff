package main;

import java.awt.Color;

public class Settings {
	
	private Handler handler;
	private double zoomLevel;
	private boolean gore, zombieCounter;
	private Color laserColor;
	private Color hudColor;
	
	public Settings(Handler handler) {
		this.handler = handler;
		//default settings
		zoomLevel = 1;
		gore = true;
		laserColor = Color.red;
		hudColor = Color.green
				;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public double getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public boolean isGore() {
		return gore;
	}

	public void setGore(boolean gore) {
		this.gore = gore;
	}

	public Color getLaserColor() {
		return laserColor;
	}

	public void setLaserColor(Color laserColor) {
		this.laserColor = laserColor;
	}

	public Color getHudColor() {
		return hudColor;
	}

	public void setHudColor(Color hudColor) {
		this.hudColor = hudColor;
	}
}