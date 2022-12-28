package entities.statics.traps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import entities.bullets.Explosion;
import entities.creatures.Zombie;
import main.Handler;
import utils.Timer;

public class MineFieldTrap extends Trap {

	Timer mineInterval = new Timer(18);
	public MineFieldTrap(Handler handler, float x, float y, float switchX, float switchY,
			int switchRotation) {
		super(handler, x, y, 500, 500, switchX, switchY, switchRotation, 45 * 60, 1500);
		cooldown = 15 * 60;
	}
	
	
	public void postTick() {
		if(cooldownTimer > cooldown) {
			activated = false;
		}
		else if(activated && cooldownTimer <= cooldown && mineInterval.isReady()) {
			killInArea();
			mineInterval.resetTimer();
		}
		else if(activated && cooldownTimer <= cooldown && !mineInterval.isReady()) {
			mineInterval.tick();
		}
	}
	
	public void render(Graphics g) {
			g.setColor(Color.gray);
			g.fillRect((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()),
				width, height);

	}

	Ellipse2D explosionRadius;
	public void killInArea() {
		Random rand = new Random();
		int dx = rand.nextInt(100, width - 100);
		int dy = rand.nextInt(100, height - 100);
		
		explosionRadius = new Ellipse2D.Float(x + dx - 100, y + dy - 100, 200, 200);
		handler.getWorld().getEntityManager().addBlood(new Explosion(handler, x + dx - 100, y + dy - 100, 200, 200, Color.orange));
		for(Zombie f: handler.getWorld().getEntityManager().getZombies()) {
			int damage = 10000;
			if(explosionRadius.intersects(f.getHitBox(0,0))) {
				f.takeDamage(damage);
				if(f.getHealth()/handler.getRoundLogic().getZombieHealth()  < (f.getHealth()* 3/10 )&& f.getZombieType() == 0) {
					f.turnToCrawler();
				}
			}
		}
		
		if(explosionRadius.intersects(handler.getPlayer().getCollisionBounds(0, 0))){
			if(!handler.getPlayer().getInv().isPhd())
				handler.getPlayer().takeDamage(60);
		}
	}

}
