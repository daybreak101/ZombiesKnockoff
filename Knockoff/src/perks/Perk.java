package perks;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Handler;

public abstract class Perk {
	
	Handler handler;
	String name;
	BufferedImage icon;
	int cooldown, maxCooldown;
	int level;
	
	public Perk(Handler handler, int level) {
		this.handler = handler;
		this.level = level;
	}
	
	public void tick() {}
	
	public void render(Graphics g) {}
	
	public String getName() {
		return name;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void buff() {}
	
	public void debuff() {}
}
