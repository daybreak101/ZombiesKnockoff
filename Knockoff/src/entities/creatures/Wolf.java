package entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import graphics.Assets;
import main.Handler;
import utils.Utils;

public class Wolf extends Creature {

	protected boolean justAttacked = false;
	protected long timer = 0;
	protected Rectangle hitbox;
	protected int attackTicker = 0, attackTimer = 100;

	public Wolf(Handler handler, float x, float y) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
		speed = 4.0f;
		hitbox = new Rectangle(0, 0, width, height);
		bounds.x = 65 / 2;
		bounds.y = 65 / 2;
		bounds.width = 10;
		bounds.height = 10;
	}

	public void followClosestZombie() {
		xMove = 0;
		yMove = 0;

		if (justAttacked == true) {
			timer++;
			if (timer == 100) {
				justAttacked = false;
				timer = 0;
			}
		} else {
			// find closest zombie
			Zombie closestZombie = null;
			float closestDist = 2000000;
			float eDist;
			for (Zombie e : handler.getWorld().getEntityManager().getZombies().getObjects()) {
				eDist = Utils.getEuclideanDistance(x, y, e.getX(), e.getY());
				if (closestZombie == null) {
					closestZombie = e;
					closestDist = eDist;
				}
				if (eDist < closestDist) {
					closestZombie = e;
					closestDist = eDist;
				}
			}

			// move towards closest zombie
			if (closestZombie != null) {
				float moveToX = closestZombie.getX() - x;
				float moveToY = closestZombie.getY() - y;
				float angle = (float) Math.atan2(moveToY, moveToX);
				xMove = (float) (speed * Math.cos(angle));
				yMove = (float) (speed * Math.sin(angle));

				if (!checkEntityCollisions(xMove, 0f)) {
					moveX();
				}

				if (!checkEntityCollisions(0f, yMove)) {

					moveY();
				}
				
				if (this.getCollisionBounds(0, 0).intersects(closestZombie.getHitBox(0, 0))) {
					if (justAttacked() == false) {
						closestZombie.dieByTrap();
						justAttacked = true;
					}
				}
			}

		}
	}

	public void tick() {
		followClosestZombie();
	}

	public void render(Graphics g) {

		float moveToX = handler.getPlayer().getX() - handler.getGameCamera().getxOffset()
				+ handler.getPlayer().getWidth() / 2;
		float moveToY = handler.getPlayer().getY() - handler.getGameCamera().getyOffset()
				+ handler.getPlayer().getHeight() / 2;
		float angle = (float) Math
				.toDegrees(Math.atan2(-(x - handler.getGameCamera().getxOffset() - moveToX + width / 2),
						y - handler.getGameCamera().getyOffset() - moveToY + height / 2));

		g.drawImage(Assets.shadow, (int) (x - handler.getGameCamera().getxOffset()),
				(int) (y - handler.getGameCamera().getyOffset()), width, height, null);

		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		g2d.rotate(Math.toRadians(angle), x - handler.getGameCamera().getxOffset() + width / 2,
				y - handler.getGameCamera().getyOffset() + height / 2);

		g2d.drawImage(Assets.lickerAnim[1], (int) (x - handler.getGameCamera().getxOffset()),
				(int) (y - handler.getGameCamera().getyOffset()), width, height, null);

		g2d.setTransform(old);

	}

	public boolean justAttacked() {
		return justAttacked;
	}
}
