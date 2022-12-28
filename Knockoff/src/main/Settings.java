package main;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import utils.Utils;

public class Settings {

	private Handler handler;
	private double zoomLevel;
	private boolean gore, zombieCounter, toggleCrits, toggleDamage, healthBar;
	private Color laserColor;
	private Color hudColor;

	public Settings(Handler handler) {
		this.handler = handler;
		// default settings
		zoomLevel = 1;
		gore = true;
		zombieCounter = false;
		toggleCrits = false;
		toggleDamage = false;
		healthBar = false;
		laserColor = Color.red;
		hudColor = Color.green;
		useSavedSettings();
	}

	public void useSavedSettings() {
		String file = Utils.loadFileAsString("/info/settings.txt");
		String[] tokens = file.split("\\s+");
		int i = 0;
		int token = 0;

		if (tokens.length == 0) {
			return;
		}
		int zoomLevelToken = Utils.parseInt(tokens[i++]);
		int hudColorToken = Utils.parseInt(tokens[i++]);
		int laserColorToken = Utils.parseInt(tokens[i++]);
		int zombieCounterToken = Utils.parseInt(tokens[i++]);
		int toggleCritsToken = Utils.parseInt(tokens[i++]);
		int toggleDamageToken = Utils.parseInt(tokens[i++]);
		int healthBarToken = Utils.parseInt(tokens[i++]);
		
		switch (zoomLevelToken) {
		case 0:
			zoomLevel = 1.0;
			break;
		case 1:
			zoomLevel = 1.1;
			break;
		case 2:
			zoomLevel = 1.2;
			break;
		case 3:
			zoomLevel = 1.3;
			break;
		case 4:
			zoomLevel = 1.4;
			break;
		case 5:
			zoomLevel = 1.5;
			break;
		default:
			zoomLevel = 1.0;
			break;
		}

		switch (hudColorToken) {
		case 0:
			hudColor = Color.green;
			break;
		case 1:
			hudColor = Color.yellow;
			break;
		case 2:
			hudColor = Color.red;
			break;
		case 3:
			hudColor = Color.blue;
			break;
		case 4:
			hudColor = Color.magenta;
			break;
		case 5:
			hudColor = Color.cyan;
			break;
		case 6:
			hudColor = Color.orange;
			break;
		case 7:
			hudColor = Color.white;
			break;
		default:
			hudColor = Color.green;
			break;
		}

		switch (laserColorToken) {
		case 0:
			laserColor = Color.green;
			break;
		case 1:
			laserColor = Color.yellow;
			break;
		case 2:
			laserColor = Color.red;
			break;
		case 3:
			laserColor = Color.blue;
			break;
		case 4:
			laserColor = Color.magenta;
			break;
		case 5:
			laserColor = Color.cyan;
			break;
		case 6:
			laserColor = Color.orange;
			break;
		case 7:
			laserColor = Color.white;
			break;
		default:
			laserColor = Color.red;
			break;
		}

		switch (zombieCounterToken) {
		case 0:
			zombieCounter = false;
			break;
		case 1:
			zombieCounter = true;
			break;
		default:
			zombieCounter = false;
			break;
		}

		switch (toggleCritsToken) {
		case 0:
			toggleCrits = false;
			break;
		case 1:
			toggleCrits = true;
			break;
		default:
			toggleCrits = false;
			break;
		}

		switch (toggleDamageToken) {
		case 0:
			toggleDamage = false;
			break;
		case 1:
			toggleDamage = true;
			break;
		default:
			toggleDamage = false;
			break;
		}
		
		switch (healthBarToken) {
		case 0:
			healthBar = false;
			break;
		case 1:
			healthBar = true;
			break;
		default:
			healthBar = false;
			break;
		}

	}

	public void writeToFile() {
		try {
			FileWriter writer = new FileWriter("res/info/settings.txt");
			BufferedWriter buffer = new BufferedWriter(writer);

			if (zoomLevel == 1.0) {
				buffer.write("0");
			} else if (zoomLevel == 1.1) {
				buffer.write("1");
			} else if (zoomLevel == 1.2) {
				buffer.write("2");
			} else if (zoomLevel == 1.3) {
				buffer.write("3");
			} else if (zoomLevel == 1.4) {
				buffer.write("4");
			} else if (zoomLevel == 1.5) {
				buffer.write("5");
			}

			buffer.newLine();

			if (hudColor == Color.green)
				buffer.write("0");
			else if (hudColor == Color.yellow)
				buffer.write("1");
			else if (hudColor == Color.red)
				buffer.write("2");
			else if (hudColor == Color.blue)
				buffer.write("3");
			else if (hudColor == Color.magenta)
				buffer.write("4");
			else if (hudColor == Color.cyan)
				buffer.write("5");
			else if (hudColor == Color.orange)
				buffer.write("6");
			else if (hudColor == Color.white)
				buffer.write("7");
			else
				buffer.write("0");

			buffer.newLine();

			if (laserColor == Color.green)
				buffer.write("0");
			else if (laserColor == Color.yellow)
				buffer.write("1");
			else if (laserColor == Color.red)
				buffer.write("2");
			else if (laserColor == Color.blue)
				buffer.write("3");
			else if (laserColor == Color.magenta)
				buffer.write("4");
			else if (laserColor == Color.cyan)
				buffer.write("5");
			else if (laserColor == Color.orange)
				buffer.write("6");
			else if (laserColor == Color.white)
				buffer.write("7");
			else
				buffer.write("0");
			buffer.newLine();

			if (zombieCounter == true) {
				buffer.write("1");
			} else {
				buffer.write("0");
			}
			buffer.newLine();

			if (toggleCrits == true) {
				buffer.write("1");
			} else {
				buffer.write("0");
			}
			buffer.newLine();

			if (toggleDamage == true) {
				buffer.write("1");
			} else {
				buffer.write("0");
			}
			buffer.newLine();
			
			if (healthBar == true) {
				buffer.write("1");
			} else {
				buffer.write("0");
			}

			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public boolean isZombieCounter() {
		return zombieCounter;
	}

	public void setZombieCounter(boolean zombieCounter) {
		this.zombieCounter = zombieCounter;
	}

	public boolean isToggleCrits() {
		return toggleCrits;
	}

	public void setToggleCrits(boolean toggleCrits) {
		this.toggleCrits = toggleCrits;
	}
	
	public boolean isToggleDamage() {
		return toggleDamage;
	}

	public void setToggleDamage(boolean toggleDamage) {
		this.toggleDamage = toggleDamage;
	}
	
	public boolean isHealthBar() {
		return healthBar;
	}

	public void setHealthBar(boolean healthBar) {
		this.healthBar = healthBar;
	}
}
