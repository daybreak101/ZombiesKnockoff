package entities.statics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import main.Handler;

public class Barrier extends InteractableStaticEntity {

	private int health;
	private boolean isBroken, cantAfford;
	private int length, whatWall;
	private Rectangle playerBarrier;

	public Barrier(Handler handler, float x, float y, int whatWall) {
		super(handler, x, y, 100, 100);
		isBroken = false;
		cantAfford = false;
		length = 100;
		health = 100;
		this.whatWall = whatWall;

		triggerText = "Press F to repair barricade";
		if (whatWall == 0) {
			bounds.x = 0;
			bounds.y = 75;
			bounds.width = length;
			bounds.height = 25;
		} else if (whatWall == 1) {
			bounds.x = 0;
			bounds.y = 0;
			bounds.width = length;
			bounds.height = 25;
		} else if (whatWall == 2) {
			bounds.x = 75;
			bounds.y = 0;
			bounds.width = 25;
			bounds.height = length;
		} else if (whatWall == 3) {
			bounds.x = 0;
			bounds.y = 0;
			bounds.width = 25;
			bounds.height = length;
		}
		playerBarrier = new Rectangle((int) (x + bounds.x), (int) (y + bounds.y), bounds.width, bounds.height);
		System.out.println("x: " + playerBarrier.x);
		System.out.println("y: " + playerBarrier.y);
		System.out.println("width: " + playerBarrier.width);
		System.out.println("height: " + playerBarrier.height);
	}

	public void takeDamage(int damage) {
		health -= damage;
	}

	@Override
	public void render(Graphics g) {
		if (isBroken) {
			g.setColor(new Color(153, 102, 51));
			g.fillRect((int) (x + bounds.x + playerBarrier.width/2 - handler.getGameCamera().getxOffset()),
					(int) (y +  bounds.y - handler.getGameCamera().getyOffset()), playerBarrier.width/3, playerBarrier.height/5);
			g.fillRect((int) (x + bounds.x + playerBarrier.width/4 - handler.getGameCamera().getxOffset()),
					(int) (y + 5 + bounds.y - handler.getGameCamera().getyOffset()), playerBarrier.width/3, playerBarrier.height/5);
			
			g.fillRect((int) (x + bounds.x + playerBarrier.width/2 - handler.getGameCamera().getxOffset()),
					(int) (y + 20 +  bounds.y - handler.getGameCamera().getyOffset()), playerBarrier.width/3, playerBarrier.height/5);
			g.fillRect((int) (x - 30 + bounds.x - handler.getGameCamera().getxOffset()),
					(int) (y + 20 +  bounds.y - handler.getGameCamera().getyOffset()), 2 * playerBarrier.width/3, playerBarrier.height/5);
		} else {
			g.setColor(new Color(153, 102, 51));
			g.fillRect((int) (x + bounds.x - handler.getGameCamera().getxOffset()),
					(int) (y + bounds.y - handler.getGameCamera().getyOffset()), bounds.width, bounds.height);
		}
	}

	public void fulfillInteraction() {
		if (cooldownTimer >= cooldown && (health < 100)) {
			cooldownTimer = 0;
			if (handler.getWorld().getEntityManager().getPlayer().purchase(50)) {
				health = 100;
				cantAfford = false;
			} else {
				cantAfford = true;
				cooldownTimer = 0;
			}

		}

	}

	@Override
	public void postTick() {

		if (cantAfford && cooldownTimer < cooldown) {
			triggerText = "              Not enough points!";
		} else if (health == 100 && cooldownTimer < cooldown) {
			triggerText = "                Already repaired!";
		} else if (cooldownTimer >= cooldown) {
			triggerText = "Press F to repair barricade: 50";
		} else {
			triggerText = "";
		}

		if (health <= 0) {
			isBroken = true;
			bounds.x = 0;
			bounds.y = 0;
			bounds.width = 0;
			bounds.height = 0;
		} else {
			isBroken = false;
			if (whatWall == 0) {
				bounds.x = 0;
				bounds.y = 75;
				bounds.width = length;
				bounds.height = 25;
			} else if (whatWall == 1) {
				bounds.x = 0;
				bounds.y = 0;
				bounds.width = length;
				bounds.height = 25;
			} else if (whatWall == 2) {
				bounds.x = 75;
				bounds.y = 0;
				bounds.width = 25;
				bounds.height = length;
			} else if (whatWall == 3) {
				bounds.x = 0;
				bounds.y = 0;
				bounds.width = 25;
				bounds.height = length;
			}
		}
	}

	public boolean getIsBroken() {
		return isBroken;
	}

	public Rectangle getPlayerBarrier() {
		return playerBarrier;
	}
}
