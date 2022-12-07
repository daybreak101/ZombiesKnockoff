package entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import graphics.Animation;
import graphics.Assets;
import main.Handler;

public class Licker extends Zombie {

	public Licker(Handler handler, float x, float y, float dspeed, int health) {
		super(handler, x, y, dspeed, health);
		zombieType = LICKER;
		zombieAnim = new Animation(300, Assets.lickerAnim);
		this.speed =6.0f +  dspeed + 0.5f;
		this.health = health - 200;
		this.width = 90;
		this.height = 90;
		bounds.width = 20;
		bounds.height = 20;
		hitbox = new Rectangle(0, 0, width, height);
	}


	public void postTick() {
		if (!justAttacked)
			zombieAnim.tick();
	}
	
	public void followPlayer() {
		xMove = 0;
		yMove = 0;

		if (justAttacked == true) {
			timer++;
			if (timer == 100) {
				justAttacked = false;
				timer = 0;
			}
		} else {
			float moveToX = handler.getPlayer().getX() - x;
			float moveToY = handler.getPlayer().getY() - y;

			float angle = (float) Math.atan2(moveToY, moveToX);
			xMove = (float) (speed * Math.cos(angle));
			yMove = (float) (speed * Math.sin(angle));

			if (!checkEntityCollisions(xMove, 0f)) {
				moveX();
			}

			if (!checkEntityCollisions(0f, yMove)) {

				moveY();
			}
		}
	}

	@Override
	public void render(Graphics g) {
		float moveToX = handler.getPlayer().getX() - handler.getGameCamera().getxOffset()
				+ handler.getPlayer().getWidth() / 2;
		float moveToY = handler.getPlayer().getY() - handler.getGameCamera().getyOffset()
				+ handler.getPlayer().getHeight() / 2;
		float angle = (float) Math
				.toDegrees(Math.atan2(-(x - handler.getGameCamera().getxOffset() - moveToX + width / 2),
						y - handler.getGameCamera().getyOffset() - moveToY + height / 2));
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		g2d.rotate(Math.toRadians(angle), x - handler.getGameCamera().getxOffset() + width / 2,
				y - handler.getGameCamera().getyOffset() + height / 2);	
		g2d.drawImage(Assets.shadow, (int) (x + 10 - handler.getGameCamera().getxOffset()),
				(int) (y + 10 - handler.getGameCamera().getyOffset()), 70, 70, null);

		if (isBurning) {
			g2d.setColor(Color.orange);
			g2d.fillOval((int) (x + 10 - handler.getGameCamera().getxOffset()),
					(int) (y + 10 - handler.getGameCamera().getyOffset()), 70, 70);
		}
		g2d.drawImage(getCurrentAnimationFrame(), (int) (x - handler.getGameCamera().getxOffset()),
				(int) (y - handler.getGameCamera().getyOffset()), width, height, null);
	
		g2d.setTransform(old);
	}


}
