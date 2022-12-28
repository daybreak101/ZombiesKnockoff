package entities.bullets;

import java.awt.Graphics;
import java.awt.geom.Arc2D;

import entities.blood.Blood;

import java.awt.Color;
import main.Handler;

public class Explosion extends Blood {

	Color color;
	boolean isArc;
	float startAngle, arcAngle;

	public Explosion(Handler handler, float x, float y, int width, int height) {
		super(handler, x, y, -1);
		this.timer = 20;
		this.width = width;
		this.height = height;
		this.color = Color.orange;
		isArc = false;
	}

	public Explosion(Handler handler, float x, float y, int width, int height, Color color) {
		super(handler, x, y, -1);
		this.timer = 20;
		this.width = width;
		this.height = height;
		this.color = color;
		isArc = false;
	}

	public Explosion(Handler handler, Arc2D.Float arc) {
		super(handler, arc.x, arc.y, -1);
		this.timer = 20;
		this.width = (int) arc.width;
		this.height = (int) arc.height;
		this.color = Color.orange;
		arcAngle = (float) arc.getAngleExtent();
		startAngle = (float) arc.getAngleStart();
		isArc = true;
	}

	public void render(Graphics g) {
		g.setColor(color);
		if (isArc) {
			g.fillArc((int) (x - handler.getGameCamera().getxOffset()),
					(int) (y - handler.getGameCamera().getyOffset()), width, height,
					(int) Math.toDegrees(arcAngle), (int) Math.toDegrees(arcAngle));

		} else {
			g.fillOval((int) (x - handler.getGameCamera().getxOffset()),
					(int) (y - handler.getGameCamera().getyOffset()), width, height);

		}
	}

}
