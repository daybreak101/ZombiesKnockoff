package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import main.Handler;
import utils.Timer;

public class IceStorm extends Bullet {

	Ellipse2D.Float stormRadius;
	Rectangle[] particles;

	public IceStorm(Handler handler, float x, float y) {
		super(handler, x, y, 5);
		particles = new Rectangle[20];
		Random rand = new Random();
		int dx, dy;
		for (int i = 0; i < 20; i++) {
			dx = rand.nextInt(-130, 130);
			dy = rand.nextInt(-130, 130);
			particles[i] = new Rectangle((int) (x + dx), (int) (y + dy), 5, 5);
		}
	}

	public void postTick() {
		Random rand = new Random();
		int dx, dy;
		for (int i = 0; i < 20; i++) {
			dx = rand.nextInt(-130, 130);
			dy = rand.nextInt(-130, 130);
			particles[i] = new Rectangle((int) (x + dx), (int) (y + dy), 5, 5);
		}
	}

	public boolean checkForImpact() {
		cb = new Rectangle((int) (x + bounds.x - 1), (int) (y + bounds.y - 1), width + 1, height + 1);
		stormRadius = new Ellipse2D.Float((int) (x - 150), (int) (y - 150), 300, 300);

		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (stormRadius.intersects(e.getHitBox(0, 0))) {
				e.freeze();
			}
		}

		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			if (e.getCollisionBounds(0, 0).intersects(cb)) {
				handler.getWorld().getEntityManager().getEntities().remove(this);
				return true;
			}
		}
		return false;
	}

	Timer stormTimer = new Timer(600);

	public void die() {
		rangeCounter++;
		if (handler.getPlayer().getInv().isDeadshot()) {
			if (rangeCounter >= range * 1.5) {
				speed = 0;
				xMove = 0;
				yMove = 0;
				stormTimer.tick();
			}
		} else if (rangeCounter >= range) {
			speed = 0;
			xMove = 0;
			yMove = 0;
			stormTimer.tick();
		}
		if (stormTimer.isReady()) {
			handler.getWorld().getEntityManager().getEntities().remove(this);
		}
	}

	public void render(Graphics g) {
		g.setColor(new Color(0, 160, 240, 20));
		g.fillOval((int) (stormRadius.x - handler.getGameCamera().getxOffset()),
				(int) (stormRadius.y - handler.getGameCamera().getyOffset()), (int) stormRadius.width,
				(int) stormRadius.height);
		if (rangeCounter < range) {
			g.setColor(Color.BLUE);
			g.fillRect((int) (x - handler.getGameCamera().getxOffset()),
					(int) (y - handler.getGameCamera().getyOffset()), width * 2, height * 2);
		}

		g.setColor(new Color(255, 255, 255, 100));
		for (Rectangle r : particles) {
			g.fillRect(r.x - (int) handler.getGameCamera().getxOffset(),
					r.y - (int) handler.getGameCamera().getyOffset(), r.width, r.height);
		}

	}

}
