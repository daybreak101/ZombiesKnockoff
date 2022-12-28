package entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import entities.blood.Blood;
import entities.bullets.Grenade;
import entities.bullets.ToxenBullet;
import graphics.Assets;
import main.Handler;
import utils.Timer;

public class Toxen extends Zombie {
	
	private Timer shootTimer;

	public Toxen(Handler handler, float x, float y, float dspeed, int health) {
		super(handler, x, y, dspeed, health);
		zombieType = TOXEN;
		speed = 2.0f + dspeed - 1f;
		this.health = health * 3;
		shootTimer = new Timer(500);
	}
	
	public void postTick() {
		shootTimer.tick();
		if(shootTimer.isReady()) {
			System.out.println("SHOOT");
			shootTimer.resetTimer();
			justAttacked = true;
			handler.getWorld().getEntityManager().addEntity(new ToxenBullet(handler, 
					x + width/2,
					y + height/2,
					300));	
		}
	}
	
	@Override
	public void render(Graphics g) {
		
		g.drawImage(Assets.shadow, (int) (x - handler.getGameCamera().getxOffset()),
				(int) (y - handler.getGameCamera().getyOffset()), width, height, null);

		if(isBurning) {
			g.setColor(Color.orange);
			g.fillOval((int) (x - handler.getGameCamera().getxOffset()),
				(int) (y - handler.getGameCamera().getyOffset()), width, height);
		}
		
		float moveToX = handler.getPlayer().getX() - handler.getGameCamera().getxOffset()  + handler.getPlayer().getWidth()/2 ;
		float moveToY = handler.getPlayer().getY() - handler.getGameCamera().getyOffset() +  handler.getPlayer().getHeight()/2;
		float angle = (float) Math.toDegrees(Math.atan2(-(x  - handler.getGameCamera().getxOffset() - moveToX + width/2), y - handler.getGameCamera().getyOffset() - moveToY + height/2 ));
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform old = g2d.getTransform();
		g2d.rotate(Math.toRadians(angle), x - handler.getGameCamera().getxOffset() + width/2, y  - handler.getGameCamera().getyOffset() + height/2);
		g2d.drawImage(Assets.toxen, (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null);
		g2d.setTransform(old);
		
	
	}

	@Override
	public void die() {
		if(handler.getPlayer().getInv().isVamp())
			handler.getPlayer().incrementHealth();
		
		if(isBurning) {
			new Grenade(handler, x + width/2, y + height/2, new Color(144, 238, 144)).findEntitiesInRadius();
		}
		else
			handler.getWorld().getEntityManager().addBlood(new Blood(handler, x, y, ZombieType.TOXEN));
	}
}
