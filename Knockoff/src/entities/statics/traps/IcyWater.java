package entities.statics.traps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import entities.areas.Areas;
import entities.creatures.Zombie;
import main.Handler;

public class IcyWater extends Areas {

	private Handler handler;
	private float x, y;
	private int width, height;
	Rectangle bounds;

	public IcyWater(Handler handler, float x, float y) {
		super(handler);
		this.handler = handler;
		this.x = x;
		this.y = y;
		width = 500;
		height = 500;
		bounds = new Rectangle((int) x, (int) y, width, height);
	}

	public void tick() {

	}

	public void render(Graphics g) {
		g.setColor(new Color(0, 20, 200, 100));
		g.fillRect((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()),
				width, height);

	}
	
	public boolean checkIfEntityIsContained(Rectangle hitbox) {
		if(bounds.intersects(hitbox)) {
			return true;
		}
		return false;
	}


}
