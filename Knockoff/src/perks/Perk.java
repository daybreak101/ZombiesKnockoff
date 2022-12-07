package perks;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Handler;

public abstract class Perk {
	
	Handler handler;
	String name;
	BufferedImage icon;
	int cooldown, maxCooldown;
	
	public Perk(Handler handler) {
		this.handler = handler;
	}
	
	public void tick() {}
	
	public void render(Graphics g) {}
	
	public String getName() {
		return name;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void buff() {}
	
	public void debuff() {}
}
