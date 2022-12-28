package weapons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

import entities.creatures.Player;
import entities.creatures.Zombie;
import main.Handler;
import utils.Timer;
import utils.Utils;

public class Knife {

	Handler handler;
	Timer meleeCooldown;
	int damage;

	public Knife(Handler handler) {
		this.handler = handler;
		meleeCooldown = new Timer(60);
		damage = 150;
	}

	public void tick() {
		meleeCooldown.tick();
		postTick();
	}
	
	public void render(Graphics g) {
		if(ellipseBounds != null) {
			//g.fillRect((int) (ellipseBounds.x- handler.getGameCamera().getxOffset()), (int) (ellipseBounds.y- handler.getGameCamera().getyOffset()), (int)ellipseBounds.width, (int)ellipseBounds.height);
			g.setColor(Color.white);
			g.drawArc((int) (meleeArc.x- handler.getGameCamera().getxOffset()), (int) (meleeArc.y- handler.getGameCamera().getyOffset()), (int)meleeArc.width, (int)meleeArc.height, (int) meleeArc.start, (int) meleeArc.extent);
		}
	}

	// also get mouse
	Rectangle2D.Float ellipseBounds = null;
	Arc2D.Float meleeArc = null;
	public void damageNearbyZombie() {
		if (meleeCooldown.isReady()) {
			
			Player player = handler.getPlayer();
			float playerX = player.getX();
			float playerY = player.getY();
			float mouseX = playerX - handler.getGameCamera().getxOffset() - (int) handler.getMouseManager().getMouseX();
			float mouseY = playerY - handler.getGameCamera().getyOffset() - (int) handler.getMouseManager().getMouseY();
			float midAngle = (float) Math.atan2(mouseY, -mouseX);

			float startAngle = (float) (midAngle - Math.PI / 6);
			float xMove = (float) (Math.cos(startAngle) * 50) + playerX;
			float yMove = (float) (Math.sin(startAngle) * 50) + playerY;
			System.out.println("Player x: " + playerX + ", y: " + playerY);
			System.out.println("Rectangle x: " + xMove + ", y: " + yMove);
			ellipseBounds = new Rectangle2D.Float(playerX + player.getWidth()/2 - 100, playerY + player.getHeight()/2 - 100, 200, 200);
			// float angleWidth = (float) (Math.PI/2);

			meleeArc = new Arc2D.Float(ellipseBounds, (float) Math.toDegrees(startAngle), (float) Math.toDegrees(Math.PI / 3), Arc2D.PIE);
			
			Zombie closestZombie = null;
			float closestDist = 999999;
			float zDist;
			for (Zombie z : handler.getWorld().getEntityManager().getZombies()) {
				if (meleeArc.intersects(z.getHitBox(0, 0))) {
					zDist = Utils.getEuclideanDistance(playerX, playerY, z.getX(), z.getY());
					if (closestZombie == null) {
						closestZombie = z;
						closestDist = zDist;
					}
					if (zDist < closestDist) {
						closestZombie = z;
						closestDist = zDist;
					}
				}
			}
			if (closestZombie != null) {
				closestZombie.takeDamage(damage);
				System.out.println("HERE");
			}
				
			
			meleeCooldown.resetTimer();
		}
	}

	public void setDamage(int newDamage) {
		damage = newDamage;
	}

	public int getDamage() {
		return damage;
	}

	public void setMeleeCooldown(int newCooldown) {
		meleeCooldown = new Timer(newCooldown);
	}

	public int getMeleeCooldown() {
		return meleeCooldown.limit;
	}

	public void postTick() {
		// used for future implementations of different melees that would have different
		// effects and such
	}
}
